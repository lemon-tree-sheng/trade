package org.sheng.trade.common.protocol.coupon;

import lombok.Data;

/**
 * @author shengxingyue, created on 2018/3/18
 */
@Data
public class CouponChangeStatusReq {
    String couponId;
    String orderId;
    Integer isUsed;
}
