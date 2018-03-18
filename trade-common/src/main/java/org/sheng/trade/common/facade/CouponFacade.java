package org.sheng.trade.common.facade;

import org.sheng.trade.common.protocol.Result;
import org.sheng.trade.common.protocol.coupon.CouponChangeStatusReq;
import org.sheng.trade.common.protocol.coupon.CouponQueryReq;
import org.sheng.trade.common.protocol.coupon.CouponQueryRes;

/**
 * @author shengxingyue, created on 2018/3/18
 */
public interface CouponFacade {
    Result<CouponQueryRes> queryCoupon(CouponQueryReq couponQueryReq);

    Result changeCouponStatus(CouponChangeStatusReq couponChangeStatusReq);
}
