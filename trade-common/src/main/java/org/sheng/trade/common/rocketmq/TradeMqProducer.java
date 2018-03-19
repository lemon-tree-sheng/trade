package org.sheng.trade.common.rocketmq;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.sheng.trade.common.constant.MQEnums;
import org.sheng.trade.common.constant.MQEnums.TopicEnum;
import org.sheng.trade.common.exception.TradeMqException;

/**
 * @author shengxingyue, created on 2018/3/14
 */
@Slf4j
public class TradeMqProducer {

    private DefaultMQProducer mqProducer;

    @Setter
    private String producerGroup;

    @Setter
    private String namesrvAddr = "localhost:9876";

    @Setter
    private Integer maxMessageSize = 1024 * 1024 * 4;

    @Setter
    private Integer sendMsgTimeout = 3000;

    public void init() throws TradeMqException {
        mqProducer = new DefaultMQProducer(producerGroup);
        mqProducer.setNamesrvAddr(namesrvAddr);
        mqProducer.setMaxMessageSize(maxMessageSize);
        mqProducer.setSendMsgTimeout(sendMsgTimeout);
        try {
            mqProducer.start();
            log.info("producer started...");
        } catch (MQClientException e) {
            throw new TradeMqException(e);
        }
    }

    public SendResult sendMsg(String topic, String tag, String keys, String msg) throws TradeMqException {
        Message message = new Message(topic, tag, keys, msg.getBytes());
        SendResult sendResult;
        try {
            sendResult = mqProducer.send(message);
            log.debug("msg send success!!!");
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            throw new TradeMqException(e);
        }
        return sendResult;
    }

    public SendResult sendMsg(TopicEnum topicEnum, String keys, String msg) throws TradeMqException {
        return sendMsg(topicEnum.getTopic(), topicEnum.getTag(), keys, msg);
    }
}
