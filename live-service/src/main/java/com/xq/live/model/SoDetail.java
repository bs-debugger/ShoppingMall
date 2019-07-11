package com.xq.live.model;

import java.math.BigDecimal;
import java.util.Date;

public class SoDetail {
    private Long id;

    private Long soId;

    private Long skuId;

    private String skuCode;

    private String skuName;

    private Integer skuNum;

    private BigDecimal unitPrice;

    private Date createTime;

    private Date updateTime;

    private Long dishSkuId;

    private String dishSkuName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSoId() {
        return soId;
    }

    public void setSoId(Long soId) {
        this.soId = soId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode == null ? null : skuCode.trim();
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName == null ? null : skuName.trim();
    }

    public Integer getSkuNum() {
        return skuNum;
    }

    public void setSkuNum(Integer skuNum) {
        this.skuNum = skuNum;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
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

    public Long getDishSkuId() {
        return dishSkuId;
    }

    public void setDishSkuId(Long dishSkuId) {
        this.dishSkuId = dishSkuId;
    }

    public String getDishSkuName() {
        return dishSkuName;
    }

    public void setDishSkuName(String dishSkuName) {
        this.dishSkuName = dishSkuName;
    }
}
