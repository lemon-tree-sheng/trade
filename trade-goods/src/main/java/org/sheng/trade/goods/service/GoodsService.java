package org.sheng.trade.goods.service;

import org.sheng.trade.common.protocol.Result;
import org.sheng.trade.common.protocol.goods.GoodsQueryReq;
import org.sheng.trade.common.protocol.goods.GoodsQueryRes;
import org.sheng.trade.common.protocol.goods.GoodsReduceReq;
import org.sheng.trade.common.protocol.goods.GoodsReturnReq;

/**
 * @author shengxingyue, created on 2018/3/20
 */
public interface GoodsService {

    Result<GoodsQueryRes> queryGoods(GoodsQueryReq goodsQueryReq);

    Result reduceGoods(GoodsReduceReq goodsReduceReq);

    Result returnGoods(GoodsReturnReq goodsReturnReq);
}
