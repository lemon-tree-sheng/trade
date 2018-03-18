package org.sheng.trade.common.protocol.goods;

import lombok.Data;

/**
 * @author shengxingyue, created on 2018/3/18
 */
@Data
public class GoodsReduceReq {
    private Integer goodsId;
    private Integer goodsNumber;
    private String orderId;
}
