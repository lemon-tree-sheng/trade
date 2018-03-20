package org.sheng.trade.pay.service;

import org.sheng.trade.common.protocol.Result;
import org.sheng.trade.common.protocol.pay.PaymentCallbackReq;
import org.sheng.trade.common.protocol.pay.PaymentCallbackRes;
import org.sheng.trade.common.protocol.pay.PaymentCreateReq;
import org.sheng.trade.common.protocol.pay.PaymentCreateRes;

/**
 * @author shengxingyue, created on 2018/3/20
 */
public interface PayService {
    Result<PaymentCreateRes> createPayment(PaymentCreateReq paymentCreateReq);

    Result<PaymentCallbackRes> callbackPayment(PaymentCallbackReq paymentCallbackReq);
}
