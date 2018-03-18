package org.sheng.trade.common.facade;

import org.sheng.trade.common.protocol.Result;
import org.sheng.trade.common.protocol.user.UserChangeMoneyReq;
import org.sheng.trade.common.protocol.user.UserQueryReq;
import org.sheng.trade.common.protocol.user.UserQueryRes;

/**
 * @author shengxingyue, created on 2018/3/17
 */
public interface UserFacade {
    Result<UserQueryRes> queryUserById(UserQueryReq userQueryReq);

    Result changeUserMoney(UserChangeMoneyReq userChangeMoneyReq);
}
