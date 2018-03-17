package org.sheng.trade.user.service;

import org.sheng.trade.common.protocol.Result;
import org.sheng.trade.common.protocol.user.UserQueryReq;
import org.sheng.trade.common.protocol.user.UserQueryRes;

/**
 * @author shengxingyue, created on 2018/3/17
 */
public interface UserService {
    Result<UserQueryRes> queryUserById(UserQueryReq userQueryReq);
}
