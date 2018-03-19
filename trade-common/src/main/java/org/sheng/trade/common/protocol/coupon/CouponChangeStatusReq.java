package org.sheng.trade.common.protocol.coupon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shengxingyue, created on 2018/3/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponChangeStatusReq {
    String couponId;
    String orderId;
    String isUsed;
}
