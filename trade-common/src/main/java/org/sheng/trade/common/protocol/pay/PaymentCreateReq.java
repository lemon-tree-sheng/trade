package org.sheng.trade.common.protocol.pay;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author shengxingyue, created on 2018/3/20
 */
@Data
public class PaymentCreateReq {
    private String orderId;
    private BigDecimal payAmount;
}
