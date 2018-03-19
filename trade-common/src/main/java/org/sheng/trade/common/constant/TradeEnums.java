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

    /**
     * 返回状态码枚举
     */
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

    @AllArgsConstructor
    @Getter
    enum OrderStatusEnum {
        NO_CONFIRM("0", "未确认"),
        CONFIRM("1", "已确认"),
        CANCEL("2", "已取消"),
        INVALID("3", "无效"),
        RETURNED("4", "退货");

        private String statusCode;
        private String desc;
    }

    @AllArgsConstructor
    @Getter
    enum PayStatusEnum {
        NO_PAY("0", "未支付"),
        PAYING("1", "支付中"),
        PAID("2", "已支付");

        private String statusCode;
        private String desc;

    }

    @AllArgsConstructor
    @Getter
    enum ShippingStatusEnum {
        NO_SHIP("0", "未发货"),
        SHIPPED("1", "已发货"),
        RECEIVED("2", "已收货");
        private String statusCode;
        private String desc;
    }

    @AllArgsConstructor
    @Getter
    enum YesOrNoEnum {
        NO("0", "否"),
        YES("1", "是");
        private String code;
        private String desc;
    }

    @AllArgsConstructor
    @Getter
    enum UserMoneyLogTypeEnum {
        PAID("1", "订单付款"),
        REFUND("2", "订单退款");
        private String code;
        private String desc;

    }

}