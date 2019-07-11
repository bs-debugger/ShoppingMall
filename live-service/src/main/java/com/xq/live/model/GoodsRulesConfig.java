package com.xq.live.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class GoodsRulesConfig {
    private Long id;

    private Integer refType;//关联类型：1,商品类目 2商家类目

    private Long refId;//关联id

    private BigDecimal userServiceAmount;//用户服务费

    private BigDecimal shopServiceAmount;//商户服务费

    private BigDecimal minMarketPrice;//门市最低价要求

    private BigDecimal kjMinPrice;//砍价底价最低要求

    private Integer minStore;//原价商品最低库存

    private Integer kjMinStore;//砍价最低库存

    private Integer kjMaxStore;//砍价最高库存

    private Integer msMinStore;//秒杀最低库存

    private BigDecimal singleDiscount;//独立购买最低折扣数

    private BigDecimal discount;//砍价底价最低折扣数

    private Integer kjMinNum;//饮砍价最低人数

    private Integer kjMaxNum;//砍价最高人数

    private Byte isDeleted;//是否删除 0否 1是

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//创建时间

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;//更新时间

    private String kjServiceAmountRules;//砍价服务费规则描述

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRefType() {
        return refType;
    }

    public void setRefType(Integer refType) {
        this.refType = refType;
    }

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    }

    public BigDecimal getUserServiceAmount() {
        return userServiceAmount;
    }

    public void setUserServiceAmount(BigDecimal userServiceAmount) {
        this.userServiceAmount = userServiceAmount;
    }

    public BigDecimal getShopServiceAmount() {
        return shopServiceAmount;
    }

    public void setShopServiceAmount(BigDecimal shopServiceAmount) {
        this.shopServiceAmount = shopServiceAmount;
    }

    public BigDecimal getMinMarketPrice() {
        return minMarketPrice;
    }

    public void setMinMarketPrice(BigDecimal minMarketPrice) {
        this.minMarketPrice = minMarketPrice;
    }

    public BigDecimal getKjMinPrice() {
        return kjMinPrice;
    }

    public void setKjMinPrice(BigDecimal kjMinPrice) {
        this.kjMinPrice = kjMinPrice;
    }

    public Integer getMinStore() {
        return minStore;
    }

    public void setMinStore(Integer minStore) {
        this.minStore = minStore;
    }

    public Integer getKjMinStore() {
        return kjMinStore;
    }

    public void setKjMinStore(Integer kjMinStore) {
        this.kjMinStore = kjMinStore;
    }

    public Integer getKjMaxStore() {
        return kjMaxStore;
    }

    public void setKjMaxStore(Integer kjMaxStore) {
        this.kjMaxStore = kjMaxStore;
    }

    public Integer getMsMinStore() {
        return msMinStore;
    }

    public void setMsMinStore(Integer msMinStore) {
        this.msMinStore = msMinStore;
    }

    public BigDecimal getSingleDiscount() {
        return singleDiscount;
    }

    public void setSingleDiscount(BigDecimal singleDiscount) {
        this.singleDiscount = singleDiscount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Integer getKjMinNum() {
        return kjMinNum;
    }

    public void setKjMinNum(Integer kjMinNum) {
        this.kjMinNum = kjMinNum;
    }

    public Integer getKjMaxNum() {
        return kjMaxNum;
    }

    public void setKjMaxNum(Integer kjMaxNum) {
        this.kjMaxNum = kjMaxNum;
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
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

    public String getKjServiceAmountRules() {
        return kjServiceAmountRules;
    }

    public void setKjServiceAmountRules(String kjServiceAmountRules) {
        this.kjServiceAmountRules = kjServiceAmountRules;
    }
}
