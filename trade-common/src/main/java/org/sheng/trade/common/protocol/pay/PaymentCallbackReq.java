package org.sheng.trade.common.protocol.pay;

import lombok.Data;

/**
 * @author shengxingyue, created on 2018/3/20
 */
@Data
public class PaymentCallbackReq {
    private String payId;
    private String isPaid;
}
