package org.sheng.trade.dao.mapper;

import org.junit.Test;
import org.sheng.trade.dao.BaseTest;
import org.sheng.trade.dao.entity.TradeUser;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.util.Assert.isTrue;

/**
 * @author shengxingyue, created on 2018/3/17
 */
public class TradeUserMapperTest extends BaseTest {

    @Autowired
    TradeUserMapper tradeUserMapper;

    @Test
    public void insert() {
        TradeUser record = new TradeUser();
        record.setUserName("sheng");
        record.setUserPassword("12345678");
        int result = tradeUserMapper.insert(record);
        isTrue(1 == result);
        isTrue(2 == record.getUserId());
    }
}