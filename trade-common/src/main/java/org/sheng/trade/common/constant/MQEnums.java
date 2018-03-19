package org.sheng.trade.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shengxingyue, created on 2018/3/19
 */
public interface MQEnums {

    @AllArgsConstructor
    @Getter
    enum TopicEnum {
        ORDER_CONFIRM("orderTopic", "confirm"),
        ORDER_CANCEL("orderTopic", "cancel"),
        PAY_PAID("payTopic", "paid");

        private String topic;
        private String tag;
    }
}
