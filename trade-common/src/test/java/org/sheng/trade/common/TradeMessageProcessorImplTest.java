package org.sheng.trade.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;
import org.sheng.trade.common.consumer.TradeMessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author shengxingyue, created on 2018/3/16
 */
@Slf4j
public class TradeMessageProcessorImplTest extends BaseTest implements TradeMessageProcessor {

    @Autowired
    private TradeMqProducer tradeMqProducer;

    @Test
    public void handleMessage() throws Exception {
        tradeMqProducer.sendMsg("test", "test", "hello world!!!");
    }

    @Override
    public boolean handleMessage(MessageExt meg) {
        log.info("{} received msg, msg:{}", Thread.currentThread().getName(), new String(meg.getBody()));
        return true;
    }
}