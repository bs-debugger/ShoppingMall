package com.xq.live.model;

import java.math.BigDecimal;
import java.util.Date;

public class ServiceConfig {
    /**
     * 类型  1活动   2类目  3商品
     */
    public final static int TYPE_ACT = 1;   //活动

    public final static int TYPE_CATEGORY = 2;   //类目

    public final static int TYPE_GOODS_SKU = 3;   //商品

    private Long id;

    private Long refId;

    private Integer type;

    private BigDecimal serviceRate;

    private BigDecimal userServiceRate;

    private BigDecimal serviceWeight;

    private BigDecimal userServiceWeight;

    private Integer isDeleted;

    private Date createTime;

    private Date updateTime;

    private Integer sortNum;

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

    public BigDecimal getServiceRate() {
        return serviceRate;
    }

    public void setServiceRate(BigDecimal serviceRate) {
        this.serviceRate = serviceRate;
    }

    public BigDecimal getServiceWeight() {
        return serviceWeight;
    }

    public void setServiceWeight(BigDecimal serviceWeight) {
        this.serviceWeight = serviceWeight;
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

    public BigDecimal getUserServiceRate() {
        return userServiceRate;
    }

    public void setUserServiceRate(BigDecimal userServiceRate) {
        this.userServiceRate = userServiceRate;
    }

    public BigDecimal getUserServiceWeight() {
        return userServiceWeight;
    }

    public void setUserServiceWeight(BigDecimal userServiceWeight) {
        this.userServiceWeight = userServiceWeight;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }
}
