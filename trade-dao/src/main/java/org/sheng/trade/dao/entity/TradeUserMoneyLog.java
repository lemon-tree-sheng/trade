package org.sheng.trade.dao.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TradeUserMoneyLog extends TradeUserMoneyLogKey implements Serializable {
    private BigDecimal userMonny;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public BigDecimal getUserMonny() {
        return userMonny;
    }

    public void setUserMonny(BigDecimal userMonny) {
        this.userMonny = userMonny;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userMonny=").append(userMonny);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TradeUserMoneyLog other = (TradeUserMoneyLog) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
            && (this.getMoneyLogType() == null ? other.getMoneyLogType() == null : this.getMoneyLogType().equals(other.getMoneyLogType()))
            && (this.getUserMonny() == null ? other.getUserMonny() == null : this.getUserMonny().equals(other.getUserMonny()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getMoneyLogType() == null) ? 0 : getMoneyLogType().hashCode());
        result = prime * result + ((getUserMonny() == null) ? 0 : getUserMonny().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        return result;
    }
}