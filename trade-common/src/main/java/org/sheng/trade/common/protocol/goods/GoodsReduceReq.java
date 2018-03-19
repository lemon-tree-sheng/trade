package org.sheng.trade.common.protocol.goods;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shengxingyue, created on 2018/3/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsReduceReq {
    private Integer goodsId;
    private Integer goodsNumber;
    private String orderId;
}
