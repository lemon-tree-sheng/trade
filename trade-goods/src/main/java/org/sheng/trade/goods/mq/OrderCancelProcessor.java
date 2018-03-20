package org.sheng.trade.goods.mq;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.MessageExt;
import org.sheng.trade.common.protocol.Result;
import org.sheng.trade.common.protocol.goods.GoodsReturnReq;
import org.sheng.trade.common.rocketmq.OrderCancelMessage;
import org.sheng.trade.common.rocketmq.TradeMessageProcessor;
import org.sheng.trade.goods.service.GoodsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author shengxingyue, created on 2018/3/20
 */
@Slf4j
public class OrderCancelProcessor implements TradeMessageProcessor {

    @Autowired
    private GoodsService goodsService;

    public boolean handleMessage(MessageExt meg) {
        boolean result = false;
        String body = new String(meg.getBody());
        OrderCancelMessage orderCancelMessage = JSON.parseObject(body, OrderCancelMessage.class);
        // 消息参数校验
        if (StringUtils.isAnyEmpty(orderCancelMessage.getOrderId(), orderCancelMessage.getUserId())) {
            log.error("消息参数错误 req:{}", orderCancelMessage);
            return true;
        }

        // 库存返还
        GoodsReturnReq goodsReturnReq = new GoodsReturnReq();
        BeanUtils.copyProperties(orderCancelMessage, goodsReturnReq);
        Result result1 = goodsService.returnGoods(goodsReturnReq);
        log.info("库存返还 req:{} | res:{}", goodsReturnReq, result1);
        if (result1.getSuccess() == true) {
            result = true;
        }
        return result;
    }
}
