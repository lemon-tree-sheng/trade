package org.sheng.trade.common.protocol.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author shengxingyue, created on 2018/3/17
 */
@Data
public class UserQueryReq implements Serializable {
    private Integer userId;
}
