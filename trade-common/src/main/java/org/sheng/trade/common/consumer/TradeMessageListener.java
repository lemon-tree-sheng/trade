package org.sheng.trade.common.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author shengxingyue, created on 2018/3/16
 */
@Slf4j
public class TradeMessageListener implements MessageListenerConcurrently {
    private TradeMessageProcessor tradeMessageProcessor;

    TradeMessageListener(TradeMessageProcessor tradeMessageProcessor) {
        this.tradeMessageProcessor = tradeMessageProcessor;
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        for (MessageExt msg : msgs) {
            if (!tradeMessageProcessor.handleMessage(msg)) {
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
