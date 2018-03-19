package org.sheng.trade.common.util;

import java.util.UUID;

/**
 * @author shengxingyue, created on 2018/3/19
 */
public class UUIDUtil {
    public static String generateUUID() {
        String result = UUID.randomUUID().toString().replace("-", "");
        return result;
    }
}
