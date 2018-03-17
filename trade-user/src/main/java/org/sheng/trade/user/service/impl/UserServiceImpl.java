package org.sheng.trade.user.service.impl;

import org.sheng.trade.common.protocol.Result;
import org.sheng.trade.common.protocol.user.UserQueryReq;
import org.sheng.trade.common.protocol.user.UserQueryRes;
import org.sheng.trade.dao.entity.TradeUser;
import org.sheng.trade.dao.mapper.TradeUserMapper;
import org.sheng.trade.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shengxingyue, created on 2018/3/17
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TradeUserMapper tradeUserMapper;

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
}
