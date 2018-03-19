package org.sheng.trade.order.service;

import org.sheng.trade.common.protocol.Result;
import org.sheng.trade.common.protocol.order.OrderConfirmReq;

/**
 * @author shengxingyue, created on 2018/3/18
 */
public interface OrderService {
    Result confirmOrder(OrderConfirmReq orderConfirmReq);
}
