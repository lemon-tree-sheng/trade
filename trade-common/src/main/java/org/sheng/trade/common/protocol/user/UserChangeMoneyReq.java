package org.sheng.trade.common.protocol.user;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author shengxingyue, created on 2018/3/18
 */
@Data
public class UserChangeMoneyReq {
    private Integer userId;
    private BigDecimal userMoney;
    private String moneyLogType;
    private String orderId;
}