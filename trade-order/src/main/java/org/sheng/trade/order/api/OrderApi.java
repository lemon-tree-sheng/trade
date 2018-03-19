package org.sheng.trade.order.api;

import org.sheng.trade.common.facade.OrderFacade;
import org.sheng.trade.common.protocol.Result;
import org.sheng.trade.common.protocol.order.OrderConfirmReq;
import org.sheng.trade.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shengxingyue, created on 2018/3/18
 */
@RestController
public class OrderApi implements OrderFacade {

    @Autowired
    private OrderService orderService;

    @PostMapping("/confirmOrder")
    public Result confirmOrder(@RequestBody OrderConfirmReq orderConfirmReq) {
        Result result = null;
        // todo 简单的入参校验
        return result;
    }
}
