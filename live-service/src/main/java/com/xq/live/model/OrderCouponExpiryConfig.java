package com.xq.live.model;

import java.util.Date;

public class OrderCouponExpiryConfig {
    /**
     * 类型  1活动   2销售点
     */
    public final static int TYPE_ACT = 1;   //活动

    public final static int TYPE_SALEPOINT = 2;   //销售点
    private Long id;

    private Long refId;

    private Integer type;

    private Date expiryDate;

    private Integer expiryDateDays;

    private Integer isDeleted;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getExpiryDateDays() {
        return expiryDateDays;
    }

    public void setExpiryDateDays(Integer expiryDateDays) {
        this.expiryDateDays = expiryDateDays;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
