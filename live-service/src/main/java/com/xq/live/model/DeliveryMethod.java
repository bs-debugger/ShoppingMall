package com.xq.live.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class DeliveryMethod {
    private Long id;

    private Long deliveryTemplateId;

    private String region;

    private String regionDesc;

    private Integer firstPiece;

    private BigDecimal firstWeight;

    private BigDecimal firstBulk;

    private BigDecimal firstAmount;

    private Integer secondPiece;

    private BigDecimal secondWeight;

    private BigDecimal secondBulk;

    private BigDecimal secondAmount;

    private Integer deliveryMethodType;

    private Integer isDefault;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeliveryTemplateId() {
        return deliveryTemplateId;
    }

    public void setDeliveryTemplateId(Long deliveryTemplateId) {
        this.deliveryTemplateId = deliveryTemplateId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region == null ? null : region.trim();
    }

    public String getRegionDesc() {
        return regionDesc;
    }

    public void setRegionDesc(String regionDesc) {
        this.regionDesc = regionDesc;
    }

    public Integer getFirstPiece() {
        return firstPiece;
    }

    public void setFirstPiece(Integer firstPiece) {
        this.firstPiece = firstPiece;
    }

    public BigDecimal getFirstWeight() {
        return firstWeight;
    }

    public void setFirstWeight(BigDecimal firstWeight) {
        this.firstWeight = firstWeight;
    }

    public BigDecimal getFirstBulk() {
        return firstBulk;
    }

    public void setFirstBulk(BigDecimal firstBulk) {
        this.firstBulk = firstBulk;
    }

    public BigDecimal getFirstAmount() {
        return firstAmount;
    }

    public void setFirstAmount(BigDecimal firstAmount) {
        this.firstAmount = firstAmount;
    }

    public Integer getSecondPiece() {
        return secondPiece;
    }

    public void setSecondPiece(Integer secondPiece) {
        this.secondPiece = secondPiece;
    }

    public BigDecimal getSecondWeight() {
        return secondWeight;
    }

    public void setSecondWeight(BigDecimal secondWeight) {
        this.secondWeight = secondWeight;
    }

    public BigDecimal getSecondBulk() {
        return secondBulk;
    }

    public void setSecondBulk(BigDecimal secondBulk) {
        this.secondBulk = secondBulk;
    }

    public BigDecimal getSecondAmount() {
        return secondAmount;
    }

    public void setSecondAmount(BigDecimal secondAmount) {
        this.secondAmount = secondAmount;
    }

    public Integer getDeliveryMethodType() {
        return deliveryMethodType;
    }

    public void setDeliveryMethodType(Integer deliveryMethodType) {
        this.deliveryMethodType = deliveryMethodType;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
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
