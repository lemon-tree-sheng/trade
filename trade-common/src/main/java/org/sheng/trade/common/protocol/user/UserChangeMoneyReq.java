package org.sheng.trade.common.protocol.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author shengxingyue, created on 2018/3/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserChangeMoneyReq {
    private Integer userId;
    private BigDecimal userMoney;
    private Integer moneyLogType;
    private String orderId;
}
