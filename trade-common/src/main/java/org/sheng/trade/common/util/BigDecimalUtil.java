package org.sheng.trade.common.util;

import java.math.BigDecimal;

/**
 * @author shengxingyue, created on 2018/3/18
 */
public class BigDecimalUtil {
    public static final int TWO = 2;

    /**
     * 保留两位小数 向上取整
     *
     * @param target
     * @return
     */
    public static BigDecimal KeepTwoDecimalPlaces(BigDecimal target) {
        return target.setScale(TWO, BigDecimal.ROUND_HALF_UP);
    }
}
