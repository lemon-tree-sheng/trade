package org.sheng.trade.dao.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author shengxingyue
 */
@Data
public class TradeCoupon implements Serializable {
    private String couponId;

    private BigDecimal couponPrice;

    private Integer userId;

    private String orderId;

    private Integer isUsed;

    private Date usedTime;
}