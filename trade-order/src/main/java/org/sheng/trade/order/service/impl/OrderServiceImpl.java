package org.sheng.trade.order.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.sheng.trade.common.constant.MQEnums;
import org.sheng.trade.common.constant.TradeEnums;
import org.sheng.trade.common.constant.TradeEnums.OrderStatusEnum;
import org.sheng.trade.common.constant.TradeEnums.UserMoneyLogTypeEnum;
import org.sheng.trade.common.constant.TradeEnums.YesOrNoEnum;
import org.sheng.trade.common.exception.TradeMqException;
import org.sheng.trade.common.exception.TradeOrderException;
import org.sheng.trade.common.facade.CouponFacade;
import org.sheng.trade.common.facade.GoodsFacade;
import org.sheng.trade.common.facade.UserFacade;
import org.sheng.trade.common.protocol.Result;
import org.sheng.trade.common.protocol.coupon.CouponChangeStatusReq;
import org.sheng.trade.common.protocol.coupon.CouponQueryReq;
import org.sheng.trade.common.protocol.coupon.CouponQueryRes;
import org.sheng.trade.common.protocol.goods.GoodsQueryReq;
import org.sheng.trade.common.protocol.goods.GoodsQueryRes;
import org.sheng.trade.common.protocol.goods.GoodsReduceReq;
import org.sheng.trade.common.protocol.order.OrderConfirmReq;
import org.sheng.trade.common.protocol.user.UserChangeMoneyReq;
import org.sheng.trade.common.protocol.user.UserQueryReq;
import org.sheng.trade.common.protocol.user.UserQueryRes;
import org.sheng.trade.common.rocketmq.OrderCancelMessage;
import org.sheng.trade.common.rocketmq.TradeMqProducer;
import org.sheng.trade.common.util.BigDecimalUtil;
import org.sheng.trade.common.util.UUIDUtil;
import org.sheng.trade.dao.entity.TradeOrder;
import org.sheng.trade.dao.mapper.TradeOrderMapper;
import org.sheng.trade.order.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

import static org.sheng.trade.common.constant.TradeEnums.PayStatusEnum;
import static org.sheng.trade.common.constant.TradeEnums.ShippingStatusEnum;

/**
 * @author shengxingyue, created on 2018/3/18
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private GoodsFacade goodsClient;

    @Autowired
    private CouponFacade couponClient;

    @Autowired
    private UserFacade userClient;

    @Autowired
    private TradeOrderMapper tradeOrderMapper;

    @Autowired
    private TradeMqProducer tradeMqProducer;

    public Result confirmOrder(OrderConfirmReq orderConfirmReq) {
        Result result = Result.ok();
        try {
            // 1. 订单参数校验
            checkOrderConfirmReq(orderConfirmReq);
            // 2.创建不可见订单
            String orderId = saveNoConfirmOrder(orderConfirmReq);
            // 3.调用远程服务，扣库存、减余额、使用优惠券 -> 全部成功则更改订单状态 | 失败则发送退单消息
            callRemoteService(orderConfirmReq, orderId);
        } catch (Exception e) {
            result = Result.fail(e.getMessage());
        }
        return result;
    }

    /**
     * 调用远程服务，扣库存、减余额、使用优惠券 -> 全部成功则更改订单状态 | 失败则发送退单消息
     *
     * @param orderConfirmReq
     */
    private void callRemoteService(OrderConfirmReq orderConfirmReq, String orderId) throws TradeMqException {
        try {
            // 1. 使用优惠券
            boolean needCouponService = StringUtils.isNoneEmpty(orderConfirmReq.getCouponId());
            if (needCouponService) {
                CouponChangeStatusReq couponChangeStatusReq = new CouponChangeStatusReq(orderConfirmReq.getCouponId(), orderId, YesOrNoEnum.YES.getCode());
                Result couponChangeStatusRes = couponClient.changeCouponStatus(couponChangeStatusReq);
                log.info("更新优惠券状态 orderId:{} | couponId:{} | req:{} | res:{}", orderId, orderConfirmReq.getCouponId(), couponChangeStatusReq, couponChangeStatusRes);
                if (couponChangeStatusRes.getSuccess() == false) {
                    throw new TradeOrderException("优惠券使用失败");
                }
            }

            // 2. 扣余额
            boolean needUserMoneyService = orderConfirmReq.getMoneyPaid().compareTo(BigDecimal.ZERO) > BigDecimal.ZERO.intValue();
            if (needUserMoneyService) {
                UserChangeMoneyReq userChangeMoneyReq = new UserChangeMoneyReq(orderConfirmReq.getUserId(), orderConfirmReq.getMoneyPaid(), UserMoneyLogTypeEnum.PAID.getCode(), orderId);
                Result userChangeMoneyRes = userClient.changeUserMoney(userChangeMoneyReq);
                log.info("扣减用户余额 orderId:{} | req:{} | res:{}", orderId, userChangeMoneyReq, userChangeMoneyRes);
                if (userChangeMoneyRes.getSuccess() == false) {
                    throw new TradeOrderException("扣减用户余额失败");
                }
            }

            // 3. 扣减库存
            GoodsReduceReq goodsReduceReq = new GoodsReduceReq(orderConfirmReq.getGoodsId(), orderConfirmReq.getGoodsNumber(), orderId);
            Result goodsReduceRes = goodsClient.reduceGoods(goodsReduceReq);
            log.info("扣减库存 orderId:{} | req:{} | res:{}", orderId, goodsReduceReq, goodsReduceRes);
            if (goodsReduceRes.getSuccess() == false) {
                throw new TradeOrderException("扣减库存失败");
            }

            // 4. 更改订单状态为"已确认"
            TradeOrder tradeOrder1 = new TradeOrder();
            tradeOrder1.setOrderId(orderId);
            tradeOrder1.setOrderStatus(OrderStatusEnum.CONFIRM.getStatusCode());
            tradeOrder1.setConfirmTime(new Date());
            int affectedRows = tradeOrderMapper.updateByPrimaryKeySelective(tradeOrder1);
            if (affectedRows != 1) {
                log.error("更新订单状态失败 orderId:{} | req:{} | res:{}", orderId, tradeOrder1, affectedRows);
                throw new TradeOrderException("更新订单状态失败");
            }
        } catch (Exception e) {
            // 发送 mq 消息退单
            OrderCancelMessage orderCancelMessage = new OrderCancelMessage();
            BeanUtils.copyProperties(orderConfirmReq, orderCancelMessage);
            orderCancelMessage.setOrderId(orderId);
            try {
                SendResult sendResult = tradeMqProducer.sendMsg(MQEnums.TopicEnum.ORDER_CANCEL, orderId, orderCancelMessage.toString());
                log.info("发送退单消息 orderId:{} | msg:{}", orderId, orderCancelMessage);
            } catch (TradeMqException e1) {
                log.error("发送退单消息失败 orderId:{} | msg:{}", orderId, orderCancelMessage);
                throw new TradeMqException("发送退单消息失败", e1);
            }
        }
    }

    /**
     * 创建不可见订单
     *
     * @param orderConfirmReq
     */
    private String saveNoConfirmOrder(OrderConfirmReq orderConfirmReq) {
        String result;
        TradeOrder tradeOrder = assembleTradeOrder(orderConfirmReq);
        int rows = tradeOrderMapper.insert(tradeOrder);
        if (1 != rows) {
            log.error("创建不可见订单失败 req:{} | res:{}", orderConfirmReq, rows);
            throw new TradeOrderException("创建不可见订单失败");
        } else {
            result = tradeOrder.getOrderId();
        }
        return result;
    }

    /**
     * 组装订单保存对象
     *
     * @param orderConfirmReq
     * @return
     */
    private TradeOrder assembleTradeOrder(OrderConfirmReq orderConfirmReq) {
        TradeOrder tradeOrder = new TradeOrder();
        BeanUtils.copyProperties(orderConfirmReq, tradeOrder);
        /**
         * orderId
         */
        String orderId = UUIDUtil.generateUUID();
        log.info("新订单号生成 userId:{} | goodsId:{} | orderId:{}", orderConfirmReq.getUserId(), orderConfirmReq.getGoodsId(), orderId);
        tradeOrder.setOrderId(orderId);
        tradeOrder.setOrderStatus(OrderStatusEnum.NO_CONFIRM.getStatusCode());
        tradeOrder.setPayStatus(PayStatusEnum.NO_PAY.getStatusCode());
        tradeOrder.setShippingStatus(ShippingStatusEnum.NO_SHIP.getStatusCode());
        /**
         * goodsAmount
         */
        BigDecimal goodsAmount = orderConfirmReq.getGoodsPrice().multiply(BigDecimal.valueOf(orderConfirmReq.getGoodsNumber()));
        tradeOrder.setGoodsAmount(goodsAmount);
        /**
         * shippingFee
         */
        BigDecimal shippingFee = calculateShippingFee(goodsAmount);
        BigDecimal reqShippingFee = BigDecimalUtil.KeepTwoDecimalPlaces(orderConfirmReq.getShippingFee());
        BigDecimal resShippingFee = BigDecimalUtil.KeepTwoDecimalPlaces(shippingFee);
        boolean isShippingFeeValid = reqShippingFee.equals(resShippingFee);
        if (!isShippingFeeValid) {
            log.error("快递费用不合法 orderId:{} | req:{} | res:{}", orderId, orderConfirmReq.getShippingFee(), shippingFee);
            throw new TradeOrderException("快递费用不合法");
        }
        /**
         * orderAmount
         */
        BigDecimal orderAmount = goodsAmount.add(shippingFee);
        BigDecimal orderAmountReq = BigDecimalUtil.KeepTwoDecimalPlaces(orderConfirmReq.getOrderAmount());
        BigDecimal orderAmountRes = BigDecimalUtil.KeepTwoDecimalPlaces(orderAmount);
        if (!orderAmountReq.equals(orderAmountRes)) {
            log.error("订单总价异常 orderId:{} | req:{} | res:{}", orderId, orderAmountReq, orderAmountRes);
            throw new TradeOrderException("订单总价异常");
        }
        /**
         * couponId & couponPaid
         */
        if (StringUtils.isNoneEmpty(orderConfirmReq.getCouponId())) {
            CouponQueryReq couponQueryReq = new CouponQueryReq(orderConfirmReq.getCouponId());
            Result<CouponQueryRes> couponQueryRes = couponClient.queryCoupon(couponQueryReq);
            log.info("优惠券信息查询 orderId:{} | req:{} | res:{}", orderId, orderConfirmReq.getCouponId(), couponQueryRes);
            boolean isCouponValid = couponQueryRes.getSuccess() && couponQueryRes.getData() != null && couponQueryRes.getData().getIsUsed().equals(YesOrNoEnum.NO.getCode());
            if (!isCouponValid) {
                log.error("优惠券信息异常 orderId:{} | req:{} | res:{}", orderId, orderAmountReq, orderAmountRes);
                throw new TradeOrderException("优惠券信息异常");
            }
            tradeOrder.setCouponPaid(couponQueryRes.getData().getCouponPrice());
        } else {
            tradeOrder.setCouponPaid(BigDecimal.ZERO);
        }
        /**
         * moneyPaid
         */
        if (orderConfirmReq.getMoneyPaid() != null) {
            int result = orderConfirmReq.getMoneyPaid().compareTo(BigDecimal.ZERO);
            if (result == -1) {
                log.error("支付金额异常 orderId:{} | req:{}", orderId, orderConfirmReq.getMoneyPaid());
                throw new TradeOrderException("支付金额异常");
            } else {
                UserQueryReq userQueryReq = new UserQueryReq(orderConfirmReq.getUserId());
                Result<UserQueryRes> userQueryRes = userClient.queryUserById(userQueryReq);
                log.error("用户信息查询 orderId:{} | req:{} | res:{}", orderId, userQueryReq, userQueryRes);
                boolean isUserValid = userQueryRes.getSuccess() == true && userQueryRes.getData() != null;
                if (!isUserValid) {
                    throw new TradeOrderException("用户信息异常");
                }
                // 查询余额是否足够
                BigDecimal userMoney = BigDecimalUtil.KeepTwoDecimalPlaces(userQueryRes.getData().getUserMoney());
                BigDecimal moneyPaidReq = BigDecimalUtil.KeepTwoDecimalPlaces(orderConfirmReq.getMoneyPaid());
                boolean isMoneyEnough = userMoney.compareTo(moneyPaidReq) > BigDecimal.ZERO.intValue();
                if (!isMoneyEnough) {
                    log.error("用户余额不足 userId:{} | req:{} | res:{}", orderConfirmReq.getUserId(), moneyPaidReq, userMoney);
                    throw new TradeOrderException("用户余额不足");
                }
            }
        } else {
            tradeOrder.setMoneyPaid(BigDecimal.ZERO);
        }
        /**
         * payAmount
         */
        BigDecimal payAmount = tradeOrder.getOrderAmount().subtract(tradeOrder.getMoneyPaid()).subtract(tradeOrder.getCouponPaid());
        tradeOrder.setPayAmount(payAmount);
        /**
         * addTime
         */
        tradeOrder.setAddTime(new Date());
        return tradeOrder;
    }

    /**
     * 计算快递费用
     *
     * @param goosAmount
     * @return
     */
    private BigDecimal calculateShippingFee(BigDecimal goosAmount) {
        if (goosAmount.doubleValue() > 100.00) {
            return BigDecimal.ZERO;
        } else {
            return new BigDecimal(10.0);
        }
    }

    /**
     * 订单参数校验
     *
     * @param orderConfirmReq
     */
    private void checkOrderConfirmReq(OrderConfirmReq orderConfirmReq) {
        /**
         * userId
         */
        if ((orderConfirmReq.getUserId() == null)) {
            throw new TradeOrderException("用户 id 为空！！！");
        }
        /**
         * address
         */
        if (StringUtils.isEmpty(orderConfirmReq.getAddress())) {
            throw new TradeOrderException("收货地址为空！！！");
        }
        /**
         * consignee
         */
        if (StringUtils.isEmpty(orderConfirmReq.getConsignee())) {
            throw new TradeOrderException("收件人为空！！！");
        }
        /**
         * goodsId & goodsNumber & goodsPrice
         */
        if (orderConfirmReq.getGoodsId() == null) {
            throw new TradeOrderException("商品 id 为空");
        }
        GoodsQueryReq goodsQueryReq = new GoodsQueryReq();
        goodsQueryReq.setGoodsId(orderConfirmReq.getGoodsId());
        Result<GoodsQueryRes> goodsQueryRes = goodsClient.queryGoods(goodsQueryReq);
        log.info("商品信息查询 req:{} | res:{}", goodsQueryReq, goodsQueryRes);
        if (goodsQueryRes.getData() == null) {
            throw new TradeOrderException("商品信息查询失败 goodsId:" + goodsQueryReq.getGoodsId());
        } else {
            // 库存是否超出
            boolean isExceeds = orderConfirmReq.getGoodsNumber() > goodsQueryRes.getData().getGoodsNumber();
            if (isExceeds) {
                String errorMsg = String.format("库存不足 goodsId:%d | req:%d | res:%d", goodsQueryReq.getGoodsId(), orderConfirmReq.getGoodsNumber(), goodsQueryRes.getData().getGoodsNumber());
                throw new TradeOrderException(errorMsg);
            }
            // 价格是否发生变化
            BigDecimal reqPrice = BigDecimalUtil.KeepTwoDecimalPlaces(orderConfirmReq.getGoodsPrice());
            BigDecimal resPrice = BigDecimalUtil.KeepTwoDecimalPlaces(goodsQueryRes.getData().getGoodsPrice());
            boolean isPriceConsistant = reqPrice.equals(resPrice);
            if (!isPriceConsistant) {
                String errorMsg = String.format("商品价格发生变化 goodsId:%d | req:%d | res:%d", goodsQueryReq.getGoodsId(), orderConfirmReq.getGoodsPrice(), goodsQueryRes.getData().getGoodsPrice());
                throw new TradeOrderException(errorMsg);
            }
        }
        /**
         * moneyPaid
         */
        orderConfirmReq.setMoneyPaid(new BigDecimal(0));
        /**
         * shippingFee
         */
        orderConfirmReq.setShippingFee(new BigDecimal(0));
        /**
         * orderAmount
         */
        if (orderConfirmReq.getOrderAmount() == null) {
            throw new TradeOrderException("订单金额错误 req:" + orderConfirmReq.getOrderAmount());
        }
    }
}
