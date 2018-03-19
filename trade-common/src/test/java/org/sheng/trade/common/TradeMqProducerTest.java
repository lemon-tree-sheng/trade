package org.sheng.trade.common;

import org.junit.Test;
import org.sheng.trade.common.rocketmq.TradeMqProducer;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author shengxingyue, created on 2018/3/16
 */
public class TradeMqProducerTest extends BaseTest {

    @Autowired
    TradeMqProducer tradeMqProducer;

    @Test
    public void sendMsg() throws Exception {
        tradeMqProducer.sendMsg("test", "test", "test", "hello trade msg");
    }
}