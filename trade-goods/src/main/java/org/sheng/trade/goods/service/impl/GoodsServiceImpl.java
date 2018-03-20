package org.sheng.trade.goods.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.sheng.trade.common.exception.TradeOrderException;
import org.sheng.trade.common.protocol.Result;
import org.sheng.trade.common.protocol.goods.GoodsQueryReq;
import org.sheng.trade.common.protocol.goods.GoodsQueryRes;
import org.sheng.trade.common.protocol.goods.GoodsReduceReq;
import org.sheng.trade.common.protocol.goods.GoodsReturnReq;
import org.sheng.trade.dao.entity.TradeGoods;
import org.sheng.trade.dao.entity.TradeGoodsNumberLog;
import org.sheng.trade.dao.entity.TradeGoodsNumberLogExample;
import org.sheng.trade.dao.mapper.TradeGoodsMapper;
import org.sheng.trade.dao.mapper.TradeGoodsNumberLogMapper;
import org.sheng.trade.goods.service.GoodsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author shengxingyue, created on 2018/3/20
 */
@Service
@Slf4j
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private TradeGoodsMapper tradeGoodsMapper;

    @Autowired
    private TradeGoodsNumberLogMapper tradeGoodsNumberLogMapper;

    public Result<GoodsQueryRes> queryGoods(GoodsQueryReq goodsQueryReq) {
        Result<GoodsQueryRes> result;
        TradeGoods tradeGoods = tradeGoodsMapper.selectByPrimaryKey(goodsQueryReq.getGoodsId());
        if (tradeGoods == null) {
            log.warn("该商品不存在 req:{}", goodsQueryReq.getGoodsId());
            result = Result.ok("该商品不存在");
        } else {
            GoodsQueryRes goodsQueryRes = new GoodsQueryRes();
            BeanUtils.copyProperties(tradeGoods, goodsQueryRes);
            result = Result.ok(goodsQueryRes);
        }
        return result;
    }

    @Transactional
    public Result reduceGoods(GoodsReduceReq goodsReduceReq) {
        Result result = Result.ok();
        // 1. 幂等控制 todo 利用日志表去重
        // 2. 记录减库存日志
        TradeGoodsNumberLog tradeGoodsNumberLog = new TradeGoodsNumberLog();
        BeanUtils.copyProperties(goodsReduceReq, tradeGoodsNumberLog);
        tradeGoodsNumberLog.setLogTime(new Date());
        int affectedRows = tradeGoodsNumberLogMapper.insert(tradeGoodsNumberLog);
        if (NumberUtils.INTEGER_ONE != affectedRows) {
            log.error("记录库存扣减日志失败 req:{} | res:{}", tradeGoodsNumberLog, affectedRows);
            return Result.fail("记录库存扣减日志失败");
        }

        // 3. 减库存
        TradeGoods tradeGoods = new TradeGoods();
        BeanUtils.copyProperties(goodsReduceReq, tradeGoods);
        int affectedRows1 = tradeGoodsMapper.reduceGoods(tradeGoods);
        if (NumberUtils.INTEGER_ONE != affectedRows1) {
            log.error("库存扣减失败 req:{} | res:{}", tradeGoods, affectedRows);
            throw new TradeOrderException("库存扣减失败");
        }
        return result;
    }

    public Result returnGoods(GoodsReturnReq goodsReturnReq) {
        Result result = Result.ok();
        // 1. 查询是否有库存扣减记录 todo 库存返还也应该加入日志，用于消息去重
        TradeGoodsNumberLogExample tradeGoodsNumberLogExample = new TradeGoodsNumberLogExample();
        tradeGoodsNumberLogExample.createCriteria().andOrderIdEqualTo(goodsReturnReq.getOrderId())
                .andGoodsIdEqualTo(goodsReturnReq.getGoodsId())
                .andGoodsNumberEqualTo(goodsReturnReq.getGoodsNumber());
        List<TradeGoodsNumberLog> tradeGoodsNumberLog = tradeGoodsNumberLogMapper.selectByExample(tradeGoodsNumberLogExample);
        if (tradeGoodsNumberLog.size() <= 0) {
            log.error("库存扣减记录不存在 req:{}", tradeGoodsNumberLogExample);
            result = Result.fail("库存扣减记录不存在");
        } else {
            TradeGoods tradeGoods = new TradeGoods();
            BeanUtils.copyProperties(goodsReturnReq, tradeGoods);
            int affectedRows = tradeGoodsMapper.returnGoods(tradeGoods);
            if (affectedRows != NumberUtils.INTEGER_ONE) {
                log.error("库存返还失败 req:{}", tradeGoods);
                result = Result.fail("库存返还失败");
            }
        }
        return result;
    }
}
