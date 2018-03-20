package org.sheng.trade.coupon.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.sheng.trade.common.constant.TradeEnums;
import org.sheng.trade.common.constant.TradeEnums.UseOrUnUseEnum;
import org.sheng.trade.common.protocol.Result;
import org.sheng.trade.common.protocol.coupon.CouponChangeStatusReq;
import org.sheng.trade.common.protocol.coupon.CouponQueryReq;
import org.sheng.trade.common.protocol.coupon.CouponQueryRes;
import org.sheng.trade.coupon.service.CouponService;
import org.sheng.trade.dao.entity.TradeCoupon;
import org.sheng.trade.dao.mapper.TradeCouponMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shengxingyue, created on 2018/3/20
 */
@Service
@Slf4j
public class CouponServiceImpl implements CouponService {
    @Autowired
    private TradeCouponMapper tradeCouponMapper;

    public Result<CouponQueryRes> queryCoupon(CouponQueryReq couponQueryReq) {
        Result<CouponQueryRes> result;

        TradeCoupon tradeCoupon = tradeCouponMapper.selectByPrimaryKey(couponQueryReq.getCouponId());
        if (tradeCoupon == null) {
            result = Result.ok("优惠券信息不存在 couponId:" + couponQueryReq.getCouponId());
        } else {
            CouponQueryRes couponQueryRes = new CouponQueryRes();
            BeanUtils.copyProperties(tradeCoupon, couponQueryRes);
            result = Result.ok(couponQueryRes);
        }
        return result;
    }

    public Result changeCouponStatus(CouponChangeStatusReq couponChangeStatusReq) {
        Result result = Result.ok();
        // 幂等处理 如果请求状态与系统中的状态一致则不予处理
        TradeCoupon tradeCoupon = tradeCouponMapper.selectByPrimaryKey(couponChangeStatusReq.getCouponId());
        boolean isRepeatReq = couponChangeStatusReq.getIsUsed().equals(tradeCoupon.getIsUsed());
        if (isRepeatReq) {
            log.warn("变更优惠券状态请求重复 req:{} | res:{}", couponChangeStatusReq.getIsUsed(), tradeCoupon.getIsUsed());
            return Result.ok("变更优惠券状态请求重复");
        }

        boolean toUse = couponChangeStatusReq.getIsUsed().equals(UseOrUnUseEnum.USED.getCode());
        TradeCoupon tradeCoupon1 = new TradeCoupon();
        BeanUtils.copyProperties(couponChangeStatusReq, tradeCoupon1);
        int affectedRows;
        if (toUse) {
            // 使用优惠券
            affectedRows = tradeCouponMapper.useCoupon(tradeCoupon1);
        } else {
            // 返还优惠券
            affectedRows = tradeCouponMapper.returnCoupon(tradeCoupon1);
        }
        if (affectedRows != NumberUtils.INTEGER_ONE) {
            log.error("变更优惠券状态请求失败 req:{} | res:{}", tradeCoupon1, affectedRows);
            result = Result.fail("变更优惠券状态请求失败");
        }
        return result;
    }
}
