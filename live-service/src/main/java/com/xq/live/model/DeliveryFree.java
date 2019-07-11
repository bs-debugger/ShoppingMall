package com.xq.live.model;

import java.math.BigDecimal;
import java.util.Date;

public class DeliveryFree {
    private Long id;

    private Long deliveryTemplateId;

    private String region;

    private Integer pieceNum;

    private BigDecimal weightNum;

    private BigDecimal bulkNum;

    private BigDecimal amount;

    private Date createTime;

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

    public Integer getPieceNum() {
        return pieceNum;
    }

    public void setPieceNum(Integer pieceNum) {
        this.pieceNum = pieceNum;
    }

    public BigDecimal getWeightNum() {
        return weightNum;
    }

    public void setWeightNum(BigDecimal weightNum) {
        this.weightNum = weightNum;
    }

    public BigDecimal getBulkNum() {
        return bulkNum;
    }

    public void setBulkNum(BigDecimal bulkNum) {
        this.bulkNum = bulkNum;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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