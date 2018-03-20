package org.sheng.trade.pay.api;

import org.sheng.trade.common.facade.PayFacade;
import org.sheng.trade.common.protocol.Result;
import org.sheng.trade.common.protocol.pay.PaymentCallbackReq;
import org.sheng.trade.common.protocol.pay.PaymentCallbackRes;
import org.sheng.trade.common.protocol.pay.PaymentCreateReq;
import org.sheng.trade.common.protocol.pay.PaymentCreateRes;
import org.sheng.trade.pay.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shengxingyue, created on 2018/3/20
 */
@RestController
public class PayApi implements PayFacade {

    @Autowired
    private PayService payService;

    public Result<PaymentCreateRes> createPayment(PaymentCreateReq paymentCreateReq) {
        return null;
    }

    public Result<PaymentCallbackRes> callbackPayment(PaymentCallbackReq paymentCallbackReq) {
        return null;
    }
}
