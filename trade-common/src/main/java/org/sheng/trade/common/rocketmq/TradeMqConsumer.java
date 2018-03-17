package org.sheng.trade.common.rocketmq;

import com.google.common.collect.Lists;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.sheng.trade.common.exception.TradeMqException;

/**
 * @author shengxingyue, created on 2018/3/16
 */
@Slf4j
public class TradeMqConsumer {
    @Setter
    private String groupName;
    @Setter
    private String namesrvAddr = "localhost:9876";
    @Setter
    private String topic;

    /**
     * 订阅多个 tag 以双竖线分割
     */
    @Setter
    private String tag = "*";

    @Setter
    private TradeMessageProcessor tradeMessageProcessor;

    private DefaultMQPushConsumer mqConsumer;

    public void init() throws TradeMqException {
        // 参数校验
        if (StringUtils.isAnyEmpty(groupName, topic, tag)) {
            throw new TradeMqException(String.format("rocketmq init error,参数有误。groupName:%s, topic:%s, tag:%s", groupName, topic, tag));
        }

        mqConsumer = new DefaultMQPushConsumer(groupName);
        mqConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        mqConsumer.setNamesrvAddr(namesrvAddr);
        try {
            mqConsumer.subscribe(topic, tag);

            // 接收消息的回调
            TradeMessageListener tradeMessageListener = new TradeMessageListener(tradeMessageProcessor);
            mqConsumer.registerMessageListener(tradeMessageListener);
            mqConsumer.start();
            log.debug("rocketmq started...");

            Lists.newArrayList();
        } catch (MQClientException e) {
            throw new TradeMqException("rocketmq init error", e);
        }
    }
}
