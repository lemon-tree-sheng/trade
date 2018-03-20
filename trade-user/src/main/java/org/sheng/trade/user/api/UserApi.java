package org.sheng.trade.user.api;

import org.apache.commons.lang3.StringUtils;
import org.sheng.trade.common.constant.TradeEnums.ResponseCode;
import org.sheng.trade.common.facade.UserFacade;
import org.sheng.trade.common.protocol.Result;
import org.sheng.trade.common.protocol.user.UserChangeMoneyReq;
import org.sheng.trade.common.protocol.user.UserQueryReq;
import org.sheng.trade.common.protocol.user.UserQueryRes;
import org.sheng.trade.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author shengxingyue, created on 2018/3/17
 */
@RestController
public class UserApi implements UserFacade {

    @Autowired
    private UserService userService;

    @PostMapping("/queryUserById")
    public Result<UserQueryRes> queryUserById(@RequestBody UserQueryReq userQueryReq) {
        Result<UserQueryRes> result;
        // 参数校验
        boolean isReqValid = (userQueryReq != null) && (userQueryReq.getUserId() != null);
        if (!isReqValid) {
            String msg = String.format("用户 id 为空！！！");
            result = Result.fail(ResponseCode.FAIL.getCode(), msg);
        } else {
            try {
                result = userService.queryUserById(userQueryReq);
            } catch (Exception e) {
                result = Result.fail(ResponseCode.INNER_ERROR.getCode());
            }
        }
        return result;
    }

    @PostMapping("/changeUserMoney")
    public Result changeUserMoney(@RequestBody UserChangeMoneyReq userChangeMoneyReq) {
        Result result;
        boolean isReqValid = userChangeMoneyReq != null && userChangeMoneyReq.getUserId() != null
                && userChangeMoneyReq.getUserMoney() != null && (userChangeMoneyReq.getUserMoney().compareTo(BigDecimal.ZERO) > 0)
                && StringUtils.isNotEmpty(userChangeMoneyReq.getMoneyLogType().toString()) && StringUtils.isNotEmpty(userChangeMoneyReq.getOrderId());
        if (!isReqValid) {
            result = Result.fail(ResponseCode.FAIL.getCode());
        } else {
            result = userService.changeUserMoney(userChangeMoneyReq);
        }
        return result;
    }
}
