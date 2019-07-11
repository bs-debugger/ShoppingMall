package com.xq.live.vo.out;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 活动推荐菜出参
 * Created by lipeng on 2018/6/13.
 */
public class ActSkuOut {
    private Long id;

    private Long actId;

    private Long skuId;

    private String skuCode;//推荐菜在活动报名中所对应的编号

    private Long prId;//推荐菜对应的使用规则id

    private Byte applyStatus;

    private Byte isLuoxuan;

    private Integer voteNum;

    private Date createTime;

    private Date updateTime;

    private Long prSkuId;//推荐菜使用的规则对应的sku_id(要用券的sku_id)(比如:对应的16元的食典券)

    private String prSkuCode;//推荐菜使用的规则对应的sku_code(要用券的sku_code)

    private String prSkuName;//推荐菜使用的规则对应的sku_name(要用券的sku_name)

    private Integer ruleType;//推荐菜使用的规则对应的rule_type(要用券的rule_type)

    private String ruleDesc;//推荐菜使用的规则对应的rule_desc(要用券的rule_desc)

    private Long shopId;//推荐菜所绑定的shopId

    private BigDecimal manAmount;//推荐菜的原价

    private BigDecimal jianAmount;//推荐菜的现价+1元的服务费(比如:15+1=16元)

    private String actSkuCode;//推荐菜在sku表中所对应的唯一的sku_code

    private String actSkuName;//推荐菜在sku表中所对应的sku_name

    private BigDecimal sellPrice;//推荐菜所卖的原价

    private BigDecimal inPrice;//推荐菜所对应的进价

    private BigDecimal agioPrice;//推荐菜折扣之后的现价(比如:15元)

    private String picUrl;//推荐菜的封面图片

    private String skuInfo;//推荐菜的简介

    private Integer isVote;//判断用户是否投票的标记 0未投 1已投

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
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

    public Long getPrId() {
        return prId;
    }

    public void setPrId(Long prId) {
        this.prId = prId;
    }

    public Byte getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Byte applyStatus) {
        this.applyStatus = applyStatus;
    }

    public Byte getIsLuoxuan() {
        return isLuoxuan;
    }

    public void setIsLuoxuan(Byte isLuoxuan) {
        this.isLuoxuan = isLuoxuan;
    }

    public Integer getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(Integer voteNum) {
        this.voteNum = voteNum;
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

    public Long getPrSkuId() {
        return prSkuId;
    }

    public void setPrSkuId(Long prSkuId) {
        this.prSkuId = prSkuId;
    }

    public String getPrSkuCode() {
        return prSkuCode;
    }

    public void setPrSkuCode(String prSkuCode) {
        this.prSkuCode = prSkuCode;
    }

    public String getPrSkuName() {
        return prSkuName;
    }

    public void setPrSkuName(String prSkuName) {
        this.prSkuName = prSkuName;
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
        this.ruleDesc = ruleDesc;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public BigDecimal getManAmount() {
        return manAmount;
    }

    public void setManAmount(BigDecimal manAmount) {
        this.manAmount = manAmount;
    }

    public String getActSkuCode() {
        return actSkuCode;
    }

    public void setActSkuCode(String actSkuCode) {
        this.actSkuCode = actSkuCode;
    }

    public BigDecimal getJianAmount() {
        return jianAmount;
    }

    public void setJianAmount(BigDecimal jianAmount) {
        this.jianAmount = jianAmount;
    }

    public String getActSkuName() {
        return actSkuName;
    }

    public void setActSkuName(String actSkuName) {
        this.actSkuName = actSkuName;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public BigDecimal getInPrice() {
        return inPrice;
    }

    public void setInPrice(BigDecimal inPrice) {
        this.inPrice = inPrice;
    }

    public BigDecimal getAgioPrice() {
        return agioPrice;
    }

    public void setAgioPrice(BigDecimal agioPrice) {
        this.agioPrice = agioPrice;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getSkuInfo() {
        return skuInfo;
    }

    public void setSkuInfo(String skuInfo) {
        this.skuInfo = skuInfo;
    }

    public Integer getIsVote() {
        return isVote;
    }

    public void setIsVote(Integer isVote) {
        this.isVote = isVote;
    }
}
