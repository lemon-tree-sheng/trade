package org.sheng.trade.goods.api;

import lombok.extern.slf4j.Slf4j;
import org.sheng.trade.common.facade.GoodsFacade;
import org.sheng.trade.common.protocol.Result;
import org.sheng.trade.common.protocol.goods.GoodsQueryReq;
import org.sheng.trade.common.protocol.goods.GoodsQueryRes;
import org.sheng.trade.common.protocol.goods.GoodsReduceReq;
import org.sheng.trade.common.protocol.goods.GoodsReturnReq;
import org.sheng.trade.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shengxingyue, created on 2018/3/20
 */
@RestController
@Slf4j
public class GoodsApi implements GoodsFacade {

    @Autowired
    private GoodsService goodsService;

    public Result<GoodsQueryRes> queryGoods(GoodsQueryReq goodsQueryReq) {
        Result<GoodsQueryRes> result;
        // todo 参数校验
        result = goodsService.queryGoods(goodsQueryReq);
        return result;
    }

    public Result reduceGoods(GoodsReduceReq goodsReduceReq) {
        Result result;
        // todo 参数校验
        result = goodsService.reduceGoods(goodsReduceReq);
        return result;
    }

    public Result returnGoods(GoodsReturnReq goodsReturnReq) {
        Result result;
        // todo 参数校验
        result = goodsService.returnGoods(goodsReturnReq);
        return result;
    }
}
