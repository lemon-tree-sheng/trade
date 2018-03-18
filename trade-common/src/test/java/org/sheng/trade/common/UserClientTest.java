package org.sheng.trade.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.sheng.trade.common.facade.UserFacade;
import org.sheng.trade.common.protocol.Result;
import org.sheng.trade.common.protocol.user.UserQueryReq;
import org.sheng.trade.common.protocol.user.UserQueryRes;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author shengxingyue, created on 2018/3/18
 */
@Slf4j
public class UserClientTest extends BaseTest {

    @Autowired
    private UserFacade userClient;

    @Test
    public void test() {
        UserQueryReq userQueryReq = new UserQueryReq();
        userQueryReq.setUserId(1);
        Result<UserQueryRes> result = userClient.queryUserById(userQueryReq);
        log.info("{}", result);
    }
}
