package org.sheng.trade.common.protocol.user;

import com.sun.istack.internal.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author shengxingyue, created on 2018/3/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserQueryReq implements Serializable {

    private Integer userId;
}
