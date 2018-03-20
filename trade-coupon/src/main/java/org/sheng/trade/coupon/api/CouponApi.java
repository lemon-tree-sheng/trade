package org.sheng.trade.coupon.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.sheng.trade.common.constant.TradeEnums;
import org.sheng.trade.common.facade.CouponFacade;
import org.sheng.trade.common.protocol.Result;
import org.sheng.trade.common.protocol.coupon.CouponChangeStatusReq;
import org.sheng.trade.common.protocol.coupon.CouponQueryReq;
import org.sheng.trade.common.protocol.coupon.CouponQueryRes;
import org.sheng.trade.coupon.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shengxingyue, created on 2018/3/20
 */
@Slf4j
@RestController
public class CouponApi implements CouponFacade {

    @Autowired
    private CouponService couponService;

    @PostMapping("/queryCoupon")
    public Result<CouponQueryRes> queryCoupon(@RequestBody CouponQueryReq couponQueryReq) {
        Result<CouponQueryRes> result;
        // 参数校验
        boolean isReqValid = couponQueryReq != null && StringUtils.isNotEmpty(couponQueryReq.getCouponId());
        if (!isReqValid) {
            result = Result.fail(TradeEnums.ResponseCode.FAIL.getCode());
        } else {
            result = couponService.queryCoupon(couponQueryReq);
        }
        return result;
    }

    @PostMapping("/changeCouponStatus")
    public Result changeCouponStatus(@RequestBody CouponChangeStatusReq couponChangeStatusReq) {
        Result result;
        // 1. 参数校验
        boolean isReqValid = couponChangeStatusReq != null && !StringUtils.isAnyEmpty(couponChangeStatusReq.getCouponId(), couponChangeStatusReq.getOrderId(), couponChangeStatusReq.getIsUsed());
        if (!isReqValid) {
            result = Result.fail(TradeEnums.ResponseCode.FAIL.getCode());
        } else {
            result = couponService.changeCouponStatus(couponChangeStatusReq);
        }
        return result;
    }
}
