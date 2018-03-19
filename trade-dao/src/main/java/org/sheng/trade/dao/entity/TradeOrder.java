package org.sheng.trade.dao.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author shengxingyue
 */
@Data
public class TradeOrder implements Serializable {
    private String orderId;

    private Integer userId;

    private String orderStatus;

    private String payStatus;

    private String shippingStatus;

    private String address;

    private String consignee;

    private Integer goodsId;

    private Integer goodsNumber;

    private BigDecimal goodsPrice;

    private BigDecimal goodsAmount;

    private BigDecimal shippingFee;

    private BigDecimal orderAmount;

    private String couponId;

    private BigDecimal couponPaid;

    private BigDecimal moneyPaid;

    private BigDecimal payAmount;

    private Date addTime;

    private Date confirmTime;

    private Date payTime;
}