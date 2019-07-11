package com.xq.live.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class PromotionRules {

    public PromotionRules( Long skuId, String skuCode, String skuName, Integer ruleType, String ruleDesc, BigDecimal manAmount, BigDecimal jianAmount) {
        this.skuId = skuId;
        this.skuCode = skuCode;
        this.skuName = skuName;
        this.ruleType = ruleType;
        this.ruleDesc = ruleDesc;
        this.manAmount = manAmount;
        this.jianAmount = jianAmount;
    }

    @Override
    public String toString() {
        return "PromotionRules{" +
                "id=" + id +
                ", skuId=" + skuId +
                ", skuCode='" + skuCode + '\'' +
                ", skuName='" + skuName + '\'' +
                ", ruleType=" + ruleType +
                ", ruleDesc='" + ruleDesc + '\'' +
                ", endTime=" + endTime +
                ", shopId=" + shopId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", manAmount=" + manAmount +
                ", jianAmount=" + jianAmount +
                '}';
    }

    public PromotionRules() {}

    /**
     * 促销类型 0 免费 1 满减 2 满赠
     */
    public static final int RULE_TYPE_FREE = 0;

    public static final int RULE_TYPE_DELETE = 1;

    public static final int RULE_TYPE_ADD = 2;

    public static final int RULE_TYPE_ACT=3;

    private Long id;

    @NotNull(message = "skuId必填")
    private Long skuId;

    @NotNull(message = "skuCode必填")
    private String skuCode;

    @NotNull(message = "skuName必填")
    private String skuName;

    @NotNull(message = "ruleType必填")
    private Integer ruleType;

    @NotNull(message = "ruleDesc必填")
    private String ruleDesc;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @NotNull(message = "shopId必填")
    private Integer shopId;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private BigDecimal manAmount;

    private BigDecimal jianAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getRuleType() {
        return ruleType;
    }

    public void setRuleType(Integer ruleType) {
        this.ruleType = ruleType;
    }

    public String getRuleDesc() {
        return ruleDesc;
    }

    public void setRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc == null ? null : ruleDesc.trim();
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
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

    public BigDecimal getManAmount() {
        return manAmount;
    }

    public void setManAmount(BigDecimal manAmount) {
        this.manAmount = manAmount;
    }

    public BigDecimal getJianAmount() {
        return jianAmount;
    }

    public void setJianAmount(BigDecimal jianAmount) {
        this.jianAmount = jianAmount;
    }
}
