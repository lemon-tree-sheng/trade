package org.sheng.trade.common.rocketmq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author shengxingyue, created on 2018/3/19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCancelMessage {
    String orderId;
    String userId;
    Integer goodsNumber;
    String goodsId;
    String couponId;
    BigDecimal userMoney;
}
