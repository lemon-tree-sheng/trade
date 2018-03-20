package org.sheng.trade.coupon.service;

import org.sheng.trade.common.protocol.Result;
import org.sheng.trade.common.protocol.coupon.CouponChangeStatusReq;
import org.sheng.trade.common.protocol.coupon.CouponQueryReq;
import org.sheng.trade.common.protocol.coupon.CouponQueryRes;

/**
 * @author shengxingyue, created on 2018/3/20
 */
public interface CouponService {
    Result<CouponQueryRes> queryCoupon(CouponQueryReq couponQueryReq);

    Result changeCouponStatus(CouponChangeStatusReq couponChangeStatusReq);
}
