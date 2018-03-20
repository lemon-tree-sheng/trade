package org.sheng.trade.pay.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.sheng.trade.common.constant.MQEnums;
import org.sheng.trade.common.constant.MQEnums.TopicEnum;
import org.sheng.trade.common.constant.TradeEnums;
import org.sheng.trade.common.constant.TradeEnums.YesOrNoEnum;
import org.sheng.trade.common.exception.TradeOrderException;
import org.sheng.trade.common.protocol.Result;
import org.sheng.trade.common.protocol.pay.PaymentCallbackReq;
import org.sheng.trade.common.protocol.pay.PaymentCallbackRes;
import org.sheng.trade.common.protocol.pay.PaymentCreateReq;
import org.sheng.trade.common.protocol.pay.PaymentCreateRes;
import org.sheng.trade.common.rocketmq.PaySuccessMessage;
import org.sheng.trade.common.rocketmq.TradeMqProducer;
import org.sheng.trade.common.util.UUIDUtil;
import org.sheng.trade.dao.entity.TradePay;
import org.sheng.trade.dao.entity.TradePayExample;
import org.sheng.trade.dao.entity.TradePayMessageTemp;
import org.sheng.trade.dao.mapper.TradePayMapper;
import org.sheng.trade.dao.mapper.TradePayMessageTempMapper;
import org.sheng.trade.pay.service.PayService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author shengxingyue, created on 2018/3/20
 */
@Service
@Slf4j
public class PayServiceImpl implements PayService {

    @Autowired
    TradePayMapper tradePayMapper;

    @Autowired
    TradePayMessageTempMapper tradePayMessageTempMapper;

    @Autowired
    TradeMqProducer tradeMqProducer;

    public Result<PaymentCreateRes> createPayment(PaymentCreateReq paymentCreateReq) {
        Result<PaymentCreateRes> result = Result.ok();
        // 1. 支付记录存在则不重复创建
        TradePayExample tradePayExample = new TradePayExample();
        tradePayExample.createCriteria().andOrderIdEqualTo(paymentCreateReq.getOrderId())
                .andIsPaidEqualTo(YesOrNoEnum.YES.getCode());
        List<TradePay> tradePay = tradePayMapper.selectByExample(tradePayExample);
        if (tradePay.size() > 0) {
            log.error("该订单已支付 req:{}", paymentCreateReq);
            result = Result.fail("该订单已支付");
        } else {
            TradePay tradePay1 = new TradePay();
            BeanUtils.copyProperties(paymentCreateReq, tradePay1);
            tradePay1.setPayId(UUIDUtil.generateUUID());
            tradePay1.setIsPaid(Integer.valueOf(YesOrNoEnum.NO.getCode()));
            int affectedRows = tradePayMapper.insert(tradePay1);
            if (NumberUtils.INTEGER_ONE != affectedRows) {
                log.error("创建支付记录失败 req:{} | res:{}", tradePay1, affectedRows);
                result = Result.fail("创建支付记录失败");
            }
        }
        return result;
    }

    @Transactional
    public Result<PaymentCallbackRes> callbackPayment(PaymentCallbackReq paymentCallbackReq) {
        /**
         * 发送可靠消息
         * 原理：将 mq 与 数据库事务相绑定
         * 做法：回调成功之后向临时消息表插入临时消息记录，启动一个异步线程进行消息的发送，如果消息发送成功则删除临时消息记录，如果消息发送失败则由定时任务去扫描临时消息表，不断进行重试，以达到可靠消息的目的
         */
        Result<PaymentCallbackRes> result = Result.ok();
        boolean isPaid = paymentCallbackReq.getIsPaid().equals(YesOrNoEnum.YES.getCode());
        if (isPaid) {
            TradePay tradePay = tradePayMapper.selectByPrimaryKey(paymentCallbackReq.getPayId());
            if (tradePay == null) {
                log.error("找不到支付记录 req:{}", paymentCallbackReq.getPayId());
                result = Result.fail("找不到支付记录");
            }
            boolean alreadyPaid = YesOrNoEnum.YES.getCode().equals(tradePay.getIsPaid());
            if (alreadyPaid) {
                log.warn("该订单已支付 req:{}", paymentCallbackReq.getPayId());
                result = Result.ok("该订单已支付");
            } else {
                log.info("更新支付状态 req:{}", paymentCallbackReq);
                TradePay tradePay1 = new TradePay();
                BeanUtils.copyProperties(paymentCallbackReq, tradePay1);
                int affectedRows = tradePayMapper.updateByPrimaryKeySelective(tradePay1);
                if (NumberUtils.INTEGER_ONE != affectedRows) {
                    log.error("更新支付状态失败 req:{} | res:{}", paymentCallbackReq.getPayId());
                    result = Result.fail("更新支付状态失败 ");
                }

                // 插入临时消息
                PaySuccessMessage paySuccessMessage = new PaySuccessMessage();
                BeanUtils.copyProperties(tradePay1, paySuccessMessage);
                paySuccessMessage.setIsPaid(Integer.valueOf(YesOrNoEnum.YES.getCode()));
                TradePayMessageTemp tradePayMessageTemp = new TradePayMessageTemp("pay-success-group", TopicEnum.PAY_PAID.getTag(), paymentCallbackReq.getPayId(), paySuccessMessage.toString(), new Date());
                int affectedRows1 = tradePayMessageTempMapper.insert(tradePayMessageTemp);
                if (NumberUtils.INTEGER_ONE != affectedRows1) {
                    log.error("插入临时消息失败 req:{} | res:{}", tradePayMessageTemp, affectedRows1);
                    throw new TradeOrderException("插入临时消息失败");
                }

                // 异步发送消息 todo
            }


        } else {
            log.error("支付回调参数错误 req:{} | expect:{}", paymentCallbackReq, YesOrNoEnum.YES.getCode());
            result = Result.fail(TradeEnums.ResponseCode.FAIL.getCode(), "支付回调参数错误");
        }
        return result;
    }
}
