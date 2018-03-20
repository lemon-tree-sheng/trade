package org.sheng.trade.user.mq;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.MessageExt;
import org.sheng.trade.common.constant.TradeEnums;
import org.sheng.trade.common.protocol.Result;
import org.sheng.trade.common.protocol.user.UserChangeMoneyReq;
import org.sheng.trade.common.rocketmq.OrderCancelMessage;
import org.sheng.trade.common.rocketmq.TradeMessageProcessor;
import org.sheng.trade.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author shengxingyue, created on 2018/3/20
 */
@Slf4j
public class OrderCancelProcessor implements TradeMessageProcessor {

    @Autowired
    private UserService userService;

    public boolean handleMessage(MessageExt meg) {
        boolean result = false;
        String body = new String(meg.getBody());
        OrderCancelMessage orderCancelMessage = JSON.parseObject(body, OrderCancelMessage.class);
        // 消息参数校验
        if (StringUtils.isAnyEmpty(orderCancelMessage.getOrderId(), orderCancelMessage.getUserId())) {
            log.error("消息参数错误 req:{}", orderCancelMessage);
            return true;
        }

        // 发起退款
        UserChangeMoneyReq userChangeMoneyReq = new UserChangeMoneyReq();
        BeanUtils.copyProperties(orderCancelMessage, userChangeMoneyReq);
        userChangeMoneyReq.setMoneyLogType(Integer.valueOf(TradeEnums.UserMoneyLogTypeEnum.REFUND.getCode()));
        Result userChangeMoneyRes = userService.changeUserMoney(userChangeMoneyReq);
        log.info("订单退款 req:{} | res:{}", userChangeMoneyReq, userChangeMoneyRes);
        if (userChangeMoneyRes.getSuccess() == true) {
            result = true;
        }
        return result;
    }
}
