package org.sheng.trade.common.protocol.user;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author shengxingyue, created on 2018/3/17
 */
@Data
public class UserQueryRes implements Serializable {
    private Integer userId;

    private String userMobile;

    private Integer userScore;

    private Date userRegTime;

    private BigDecimal userMoney;

    private String userName;
}
