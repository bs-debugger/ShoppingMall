package com.xq.live.vo.in;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xq.live.model.ActTimeRules;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 活动商家入参
 *
 * @author zhangpeng32
 * @date 2018-03-06 21:10
 * @copyright:hbxq
 **/
public class ActShopInVo extends BaseInVo{
    private Long id;

    private String shopCode;

    private Long actId;

    private Long shopId;

    private Long voteUserId;//投票的用户id

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;//开始时间

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;//截止时间

    private Integer type;//查询类型  null 不分组，查询单个list   2 分组 ，查询分组后的信息

    private Long skuId;//查询跟活动绑定的商家中用到的skuId

    private String city;//活动报名的商家划分的城市


    private BigDecimal discount;//折扣

    private Integer amountLimit;//是否限制优惠金额(0,不限制,1,限制)

    private BigDecimal maxDiscountAmount;//最高优惠金额

    private Integer weekendUsable;//是否周末通用(0,不通用,1 通用)

    private Integer timeUsable;//是否限制时段(0,不限制,1 限制)

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expiryDate;//有效期时间

    private String ruleDesc;//商品活动规则描述

    private Integer status;//状态0 进行中 1 已失效

    @ApiModelProperty(value = "参与活动商品活动时间规则")
    private List<ActTimeRules> actTimeRules;//参与活动商品活动时间规则

    private Integer isDeleted;//是否删除 0 否 1 是

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
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

    public Long getVoteUserId() {
        return voteUserId;
    }

    public void setVoteUserId(Long voteUserId) {
        this.voteUserId = voteUserId;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public void setActTimeRules(List<ActTimeRules> actTimeRules) {
        this.actTimeRules = actTimeRules;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
