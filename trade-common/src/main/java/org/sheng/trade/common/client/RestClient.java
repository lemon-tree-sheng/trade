package org.sheng.trade.common.client;

import lombok.extern.slf4j.Slf4j;
import org.sheng.trade.common.constant.TradeEnums.ServerConfigEnum;
import org.sheng.trade.common.protocol.Result;
import org.sheng.trade.common.protocol.user.UserQueryReq;
import org.springframework.web.client.RestTemplate;

/**
 * @author shengxingyue, created on 2018/3/17
 */
@Slf4j
public class RestClient {
    private static final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        UserQueryReq userQueryReq = new UserQueryReq();
        userQueryReq.setUserId(1);
        Result result = restTemplate.postForObject(ServerConfigEnum.USER.getServerPath() + "queryUserById", userQueryReq, Result.class);
        System.out.println(result);
    }
}
