package org.sheng.trade.common.facade;

import org.sheng.trade.common.protocol.Result;
import org.sheng.trade.common.protocol.pay.PaymentCallbackReq;
import org.sheng.trade.common.protocol.pay.PaymentCallbackRes;
import org.sheng.trade.common.protocol.pay.PaymentCreateReq;
import org.sheng.trade.common.protocol.pay.PaymentCreateRes;

/**
 * @author shengxingyue, created on 2018/3/18
 */
public interface PayFacade {
    Result<PaymentCreateRes> createPayment(PaymentCreateReq paymentCreateReq);

    Result<PaymentCallbackRes> callbackPayment(PaymentCallbackReq paymentCallbackReq);
}
