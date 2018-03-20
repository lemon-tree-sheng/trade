package org.sheng.trade.coupon.mq;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.MessageExt;
import org.sheng.trade.common.constant.TradeEnums.UseOrUnUseEnum;
import org.sheng.trade.common.protocol.Result;
import org.sheng.trade.common.protocol.coupon.CouponChangeStatusReq;
import org.sheng.trade.common.rocketmq.OrderCancelMessage;
import org.sheng.trade.common.rocketmq.TradeMessageProcessor;
import org.sheng.trade.coupon.service.CouponService;
import org.sheng.trade.dao.mapper.TradeCouponMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author shengxingyue, created on 2018/3/20
 */
@Slf4j
public class OrderCancelProcessor implements TradeMessageProcessor {

    @Autowired
    private TradeCouponMapper tradeCouponMapper;

    @Autowired
    private CouponService couponService;

    public boolean handleMessage(MessageExt meg) {
        boolean result = true;
        String body = new String(meg.getBody());
        OrderCancelMessage orderCancelMessage = JSON.parseObject(body, OrderCancelMessage.class);
        // 消息参数校验
        if (StringUtils.isAnyEmpty(orderCancelMessage.getOrderId(), orderCancelMessage.getUserId())) {
            log.error("消息参数错误 req:{}", orderCancelMessage);
            return true;
        }

        // 返还优惠券
        boolean needReturnCoupon = StringUtils.isNotEmpty(orderCancelMessage.getCouponId());
        if (needReturnCoupon) {
            CouponChangeStatusReq couponChangeStatusReq = new CouponChangeStatusReq();
            BeanUtils.copyProperties(orderCancelMessage, couponChangeStatusReq);
            couponChangeStatusReq.setIsUsed(UseOrUnUseEnum.UN_USED.getCode());
            Result result1 = couponService.changeCouponStatus(couponChangeStatusReq);
            log.info("返还优惠券 req:{} | res:{}", couponChangeStatusReq, result1);
            if (result1.getSuccess() == false) {
                return false;
            }
        }
        return result;
    }
}
