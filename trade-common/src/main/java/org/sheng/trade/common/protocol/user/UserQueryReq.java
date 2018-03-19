package org.sheng.trade.common.protocol.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author shengxingyue, created on 2018/3/17
 */
@Data
@NoArgsConstructor
public class UserQueryReq implements Serializable {
    private Integer userId;

    public UserQueryReq(Integer userId) {
        this.userId = userId;
    }
}
