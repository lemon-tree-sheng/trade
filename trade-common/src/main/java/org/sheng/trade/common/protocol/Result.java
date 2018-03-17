package org.sheng.trade.common.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sheng.trade.common.constant.TradeEnums;

import static org.sheng.trade.common.constant.TradeEnums.*;

/**
 * @author shengxingyue, created on 2018/3/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private String msg = ResponseCode.SUCCESS.getDesc();
    private Integer code = ResponseCode.SUCCESS.getCode();
    private Boolean success = true;
    private T data;

    public Result(String msg, Integer code, Boolean success) {
        this.msg = msg;
        this.code = code;
        this.success = success;
    }

    public static Result ok() {
        Result result = new Result();
        return result;
    }

    public static Result ok(String msg) {
        Result result = new Result(msg, ResponseCode.SUCCESS.getCode(), true);
        return result;
    }

    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<>();
        result.setData(data);
        return result;
    }

    public static Result fail(Integer code) {
        Result result = new Result(ResponseCode.getDesc(code), code, false);
        return result;
    }

    public static Result fail(Integer code, String msg) {
        Result result = new Result(msg, code, false);
        return result;
    }

    public static Result fail(String msg) {
        Result result = new Result(msg, ResponseCode.INNER_ERROR.getCode(), false);
        return result;
    }

}
