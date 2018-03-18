package org.sheng.trade.common.facade;

import org.sheng.trade.common.protocol.order.OrderConfirmReq;
import org.sheng.trade.common.protocol.Result;

/**
 * @author shengxingyue, created on 2018/3/18
 */
public interface OrderFacade {
    Result confirmOrder(OrderConfirmReq orderConfirmReq);
}
