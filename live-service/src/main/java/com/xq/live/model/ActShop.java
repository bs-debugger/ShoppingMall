package com.xq.live.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class ActShop {
    /**
     * 参与活动申请审批状态 0 待审批 1 审批通过 2 审批不通过
     */
    public final static int ACT_SHOP_APPLY_STATUS_WAIT_APPLIED = 0;

    public final static int ACT_SHOP_APPLY_STATUS_APPLIED = 1;

    public final static int ACT_SHOP_APPLY_STATUS_REFUSED = 2;

    /**
     * 判断商家是否已经参加该活动 0 未报名 1已报名
     */
    public final static int ACT_SHOP_IS_SIGN = 1;

    public final static int ACT_SHOP_NO_SIGN = 0;


    /**
     * 查询类型    2 分组 ，查询分组后的信息
     */
    public final static int ACT_SHOP_GROUP = 2;

    private Long id;
    @NotNull(message = "actId必填")
    private Long actId;
    @NotNull(message = "shopId必填")
    private Long shopId;

    private String shopCode;

    private Integer applyStatus;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private Integer voteNum;

    private Integer isLuoxuan;//是否落选

    private BigDecimal discount;//折扣

    private Integer amountLimit;//是否限制优惠金额(0,不限制,1,限制)

    private BigDecimal maxDiscountAmount;//最高优惠金额

    private Integer weekendUsable;//是否周末通用(0,不通用,1 通用)

    private Integer timeUsable;//是否限制时段(0,不限制,1 限制)

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expiryDate;//有效期时间

    private String ruleDesc;//商品活动规则描述

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

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public Integer getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Integer applyStatus) {
        this.applyStatus = applyStatus;
    }

    public Integer getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(Integer voteNum) {
        this.voteNum = voteNum;
    }

    public Integer getIsLuoxuan() {
        return isLuoxuan;
    }

    public void setIsLuoxuan(Integer isLuoxuan) {
        this.isLuoxuan = isLuoxuan;
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

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
