package org.sheng.trade.dao.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.sheng.trade.dao.entity.TradeGoods;
import org.sheng.trade.dao.entity.TradeGoodsExample;

public interface TradeGoodsMapper {
    int countByExample(TradeGoodsExample example);

    int deleteByExample(TradeGoodsExample example);

    int deleteByPrimaryKey(Integer goodsId);

    int insert(TradeGoods record);

    int insertSelective(TradeGoods record);

    List<TradeGoods> selectByExample(TradeGoodsExample example);

    TradeGoods selectByPrimaryKey(Integer goodsId);

    int updateByExampleSelective(@Param("record") TradeGoods record, @Param("example") TradeGoodsExample example);

    int updateByExample(@Param("record") TradeGoods record, @Param("example") TradeGoodsExample example);

    int updateByPrimaryKeySelective(TradeGoods record);

    int updateByPrimaryKey(TradeGoods record);

    int reduceGoods(TradeGoods record);

    int returnGoods(TradeGoods record);
}