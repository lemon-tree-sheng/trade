package org.sheng.trade.common.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shengxingyue, created on 2018/3/15
 */
@Slf4j
public class TradeMqException extends Exception {

    public TradeMqException(Throwable e) {
        super(e);
        log.error("trade mq error:{}", e);
    }

    public TradeMqException(String msg) {
        super(msg);
        log.error("trade mq error:{}", msg);
    }

    public TradeMqException(String msg, Throwable e) {
        super(msg, e);
        log.error("trade mq error:{} | {}", msg, e);
    }
}
