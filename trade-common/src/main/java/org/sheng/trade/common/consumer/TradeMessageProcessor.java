package org.sheng.trade.common.consumer;

import org.apache.rocketmq.common.message.MessageExt;

/**
 * @author shengxingyue, created on 2018/3/16
 */
public interface TradeMessageProcessor {

    /**
     * 自定义消息处理器
     *
     * @param meg
     * @return
     */
    boolean handleMessage(MessageExt meg);
}
