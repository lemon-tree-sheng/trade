package org.sheng.trade.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shengxingyue, created on 2018/3/17
 */
public interface TradeEnums {
    @AllArgsConstructor
    @Getter
    enum ServerConfigEnum {
        PAY("localhost", "pay", 8080),
        ORDER("localhost", "order", 8081),
        COUPON("localhost", "coupon", 8082),
        USER("localhost", "user", 8083);
        private String serverHost;
        private String contextPath;
        private Integer port;

        public String getServerPath() {
            String result = String.format("http://%s:%d/%s/", serverHost, port, contextPath);
            return result;
        }
    }

    @AllArgsConstructor
    @Getter
    enum ResponseCode {
        SUCCESS(200, "请求成功"),
        FAIL(400, "参数错误"),
        INNER_ERROR(500, "内部处理错误");
        private Integer code;
        private String desc;

        public static String getDesc(Integer code) {
            String result = null;
            for (ResponseCode responseCode : values()) {
                if (code.equals(responseCode.getCode())) {
                    result = responseCode.getDesc();
                }
            }
            return result;
        }
    }
}