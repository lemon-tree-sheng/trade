package org.sheng.trade.common.exception;

/**
 * @author shengxingyue, created on 2018/3/18
 */
public class TradeOrderException extends RuntimeException {
    public TradeOrderException() {
    }

    public TradeOrderException(String message) {
        super(message);
    }

    public TradeOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public TradeOrderException(Throwable cause) {
        super(cause);
    }

    public TradeOrderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
