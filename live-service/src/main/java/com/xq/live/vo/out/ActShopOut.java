package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xq.live.model.ActTimeRules;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 活动商家出参
 *
 * @author zhangpeng32
 * @date 2018-03-06 21:12
 * @copyright:hbxq
 **/
public class ActShopOut implements Comparable<ActShopOut>{

    private Long id;

    private Long actId;

    private Long shopId;

    private String shopCode;

    private Integer applyStatus;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private String logoUrl;

    private String shopName;

    private String shopInfo;    //商家简介

    private int voteNum;

    private int isVote;//是否已投

    private Integer isLuoxuan;//是否落选

    private Long userId;//分组中与商家绑定的选手用户id

    private String groupCode;//分组编号

    private String groupName;//分组名称

    private Integer groupVoteNum;//分组的投票数

    private String iconUrl;//分组中与商家绑定选手的用户头像

    private String actUserName;//分组中与商家绑定选手的用户填写名称

    private Long skuId;//推荐菜的id

    private String skuName;//推荐菜的名字

    private String picUrl;//推荐菜的图片

    private BigDecimal sellPrice;//票券的售价


    private BigDecimal discount;//折扣

    private Integer amountLimit;//是否限制优惠金额(0,不限制,1,限制)

    private BigDecimal maxDiscountAmount;//最高优惠金额

    private Integer weekendUsable;//是否周末通用(0,不通用,1 通用)

    private Integer timeUsable;//是否限制时段(0,不限制,1 限制)

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expiryDate;//有效期时间

    private String ruleDesc;//商品活动规则描述

    private List<ActTimeRules> actTimeRules;//参与活动商品活动时间规则

    private Integer isDeleted;//是否删除 0 否 1 是

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

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(String shopInfo) {
        this.shopInfo = shopInfo;
    }

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }

    public int getIsVote() {
        return isVote;
    }

    public void setIsVote(int isVote) {
        this.isVote = isVote;
    }

    public Integer getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Integer applyStatus) {
        this.applyStatus = applyStatus;
    }

    public Integer getIsLuoxuan() {
        return isLuoxuan;
    }

    public void setIsLuoxuan(Integer isLuoxuan) {
        this.isLuoxuan = isLuoxuan;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getGroupVoteNum() {
        return groupVoteNum;
    }

    public void setGroupVoteNum(Integer groupVoteNum) {
        this.groupVoteNum = groupVoteNum;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getActUserName() {
        return actUserName;
    }

    public void setActUserName(String actUserName) {
        this.actUserName = actUserName;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Integer getAmountLimit() {
        return amountLimit;
    }

    public void setAmountLimit(Integer amountLimit) {
        this.amountLimit = amountLimit;
    }

    public BigDecimal getMaxDiscountAmount() {
        return maxDiscountAmount;
    }

    public void setMaxDiscountAmount(BigDecimal maxDiscountAmount) {
        this.maxDiscountAmount = maxDiscountAmount;
    }

    public Integer getWeekendUsable() {
        return weekendUsable;
    }

    public void setWeekendUsable(Integer weekendUsable) {
        this.weekendUsable = weekendUsable;
    }

    public Integer getTimeUsable() {
        return timeUsable;
    }

    public void setTimeUsable(Integer timeUsable) {
        this.timeUsable = timeUsable;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getRuleDesc() {
        return ruleDesc;
    }

    public void setRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc;
    }

    public List<ActTimeRules> getActTimeRules() {
        return actTimeRules;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setActTimeRules(List<ActTimeRules> actTimeRules) {
        this.actTimeRules = actTimeRules;
    }

    @Override
    public int compareTo(ActShopOut o) {
        int a = this.getVoteNum();
        int b = o.getVoteNum();
        if(a>b){
            return -1;
        }else {
            return 1;
        }

    }
}
