package org.sheng.trade.common.protocol.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author shengxingyue, created on 2018/3/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserQueryReq implements Serializable {

    @NotNull(message = "88888888888不能为空")
    private Integer userId;
}
