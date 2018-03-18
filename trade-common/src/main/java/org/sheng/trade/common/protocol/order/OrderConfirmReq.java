package org.sheng.trade.common.protocol.order;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author shengxingyue, created on 2018/3/18
 */
@Data
public class OrderConfirmReq {
    private Integer userId;
    private String address;
    private String consignee;
    private Integer goodsId;
    private Integer goodsNumber;
    private String couponId;
    private BigDecimal moneyPaid;
    private BigDecimal goodsPrice;
    private BigDecimal shippingFee;
    private BigDecimal orderAmount;
}
