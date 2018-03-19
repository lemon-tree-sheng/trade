package org.sheng.trade.common.protocol.coupon;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shengxingyue, created on 2018/3/18
 */
@Data
@NoArgsConstructor
public class CouponQueryReq {
    private String couponId;

    public CouponQueryReq(String couponId) {
        this.couponId = couponId;
    }
}
