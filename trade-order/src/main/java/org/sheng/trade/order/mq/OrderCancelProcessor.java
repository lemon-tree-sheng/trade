package org.sheng.trade.order.mq;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.rocketmq.common.message.MessageExt;
import org.sheng.trade.common.constant.TradeEnums.OrderStatusEnum;
import org.sheng.trade.common.rocketmq.OrderCancelMessage;
import org.sheng.trade.common.rocketmq.TradeMessageProcessor;
import org.sheng.trade.dao.entity.TradeOrder;
import org.sheng.trade.dao.mapper.TradeOrderMapper;

/**
 * @author shengxingyue, created on 2018/3/20
 */
@Slf4j
public class OrderCancelProcessor implements TradeMessageProcessor {

    private TradeOrderMapper tradeOrderMapper;

    public boolean handleMessage(MessageExt meg) {
        boolean result = false;
        String body = new String(meg.getBody());
        OrderCancelMessage orderCancelMessage = JSON.parseObject(body, OrderCancelMessage.class);
        // 消息参数校验
        if (StringUtils.isAnyEmpty(orderCancelMessage.getOrderId(), orderCancelMessage.getUserId())) {
            log.error("消息参数错误 req:{}", orderCancelMessage);
            return true;
        }

        // 取消订单
        TradeOrder tradeOrder = new TradeOrder();
        tradeOrder.setOrderId(orderCancelMessage.getOrderId());
        tradeOrder.setOrderStatus(OrderStatusEnum.CANCEL.getStatusCode());
        int affectedRows = tradeOrderMapper.updateByPrimaryKeySelective(tradeOrder);
        log.info("订单取消 req:{} | res:{}", tradeOrder, affectedRows);
        if (NumberUtils.INTEGER_ONE == affectedRows) {
            result = true;
        }
        return result;
    }
}
