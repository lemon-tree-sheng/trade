package org.sheng.trade.user.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.sheng.trade.common.constant.TradeEnums.UserMoneyLogTypeEnum;
import org.sheng.trade.common.protocol.Result;
import org.sheng.trade.common.protocol.user.UserChangeMoneyReq;
import org.sheng.trade.common.protocol.user.UserQueryReq;
import org.sheng.trade.common.protocol.user.UserQueryRes;
import org.sheng.trade.dao.entity.TradeUser;
import org.sheng.trade.dao.entity.TradeUserMoneyLog;
import org.sheng.trade.dao.entity.TradeUserMoneyLogExample;
import org.sheng.trade.dao.mapper.TradeUserMapper;
import org.sheng.trade.dao.mapper.TradeUserMoneyLogMapper;
import org.sheng.trade.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author shengxingyue, created on 2018/3/17
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private TradeUserMapper tradeUserMapper;

    @Autowired
    private TradeUserMoneyLogMapper tradeUserMoneyLogMapper;

    public Result<UserQueryRes> queryUserById(UserQueryReq userQueryReq) {
        Result<UserQueryRes> result;
        TradeUser tradeUser = tradeUserMapper.selectByPrimaryKey(userQueryReq.getUserId());
        if (tradeUser == null) {
            String msg = String.format("该用户不存在 id:%d", userQueryReq.getUserId());
            result = Result.ok(msg);
        } else {
            UserQueryRes userQueryRes = new UserQueryRes();
            BeanUtils.copyProperties(tradeUser, userQueryRes);
            result = Result.ok(userQueryRes);
        }
        return result;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public Result changeUserMoney(UserChangeMoneyReq userChangeMoneyReq) {
        Result result = Result.ok();

        // 订单扣款 逻辑去重幂等处理
        boolean isLogExist = isMoneyLogExist(userChangeMoneyReq);
        if (isLogExist) {
            return Result.ok("该扣款动作已进行处理");
        }

        // 记录付款日志
        TradeUserMoneyLog tradeUserMoneyLog = new TradeUserMoneyLog();
        BeanUtils.copyProperties(userChangeMoneyReq, tradeUserMoneyLog);
        tradeUserMoneyLog.setCreateTime(new Date());
        int affectedRows = tradeUserMoneyLogMapper.insert(tradeUserMoneyLog);
        if (affectedRows != NumberUtils.INTEGER_ONE) {
            log.error("记录款项日志失败 req:{}", userChangeMoneyReq);
            return Result.fail("记录款项日志失败");
        }

        // 订单付款 & 退款
        TradeUser tradeUser = new TradeUser();
        BeanUtils.copyProperties(userChangeMoneyReq, tradeUser);
        boolean isPay = UserMoneyLogTypeEnum.PAID.getCode().equals(userChangeMoneyReq.getMoneyLogType());
        if (isPay) {
            // 订单付款
            int affectedRows1 = tradeUserMapper.reduceUserMoney(tradeUser);
            if (affectedRows1 != NumberUtils.INTEGER_ONE) {
                result = Result.fail("订单付款失败");
                log.error("订单付款失败 req:{}", userChangeMoneyReq);
            }
        } else {
            // 订单退款
            int affectedRows1 = tradeUserMapper.addUserMoney(tradeUser);
            if (affectedRows1 != NumberUtils.INTEGER_ONE) {
                result = Result.fail("订单退款失败");
                log.error("订单退款失败 req:{}", userChangeMoneyReq);
            }
        }
        return result;
    }

    /**
     * 查询款项日志是否存在
     *
     * @param userChangeMoneyReq
     * @return
     */
    private boolean isMoneyLogExist(UserChangeMoneyReq userChangeMoneyReq) {
        boolean result = false;
        TradeUserMoneyLogExample tradeUserMoneyLogExample = new TradeUserMoneyLogExample();
        tradeUserMoneyLogExample.createCriteria().andUserIdEqualTo(userChangeMoneyReq.getUserId())
                .andOrderIdEqualTo(userChangeMoneyReq.getOrderId())
                .andMoneyLogTypeEqualTo(userChangeMoneyReq.getMoneyLogType());
        List<TradeUserMoneyLog> tradeUserMoneyLogList = tradeUserMoneyLogMapper.selectByExample(tradeUserMoneyLogExample);
        if (tradeUserMoneyLogList.size() > 0) {
            result = true;
        }
        return result;
    }
}
