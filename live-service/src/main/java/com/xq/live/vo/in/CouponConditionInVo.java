package com.xq.live.vo.in;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class CouponConditionInVo {
    /**
     * 券类型 1 平台券 2 商家券 3 活动券 4砍价券  5抢购券 6兑换券
     */
    public final static int COUPON_TYPE_PLAT = 1;

    public final static int OUNPON_TYPE_ACT= 3;

    public final static int OUNPON_TYPE_KJQ= 4;//4砍价券

    public final static int OUNPON_TYPE_QGQ= 5;//5抢购券

    public final static int OUNPON_TYPE_DHQ= 6;//6兑换券

    /**
     * 抵用券是否使用
     */
    public final static int COUPON_IS_USED_YES = 1;

    public final static int COUPON_IS_USED_NO = 0;

    /**
     * 票卷状态：0 默认状态 4 退款申请中 5 已退款
     */
    public final static int STATUS_DEFAULT = 0;

    public final static int STATUS_REFUND_APPLICATION = 4;

    public final static int STATUS_REFUND = 5;

    private Long id;

    private Long soId;

    private String couponCode;

    private Long skuId;

    private String skuCode;

    private String skuName;

    private BigDecimal couponAmount;

    private Integer type;

    private String qrcodeUrl;

    private Long userId;

    private String userName;

    private Integer isUsed;

    private Integer isDeleted;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date usedTime;

    private Long shopId;

    private Long shopCashierId;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date expiryDate;

    private Long dishSkuId;

    private Integer status;//票卷状态：0 默认状态 4 退款申请中 5 已退款

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

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode == null ? null : couponCode.trim();
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

    public BigDecimal getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl == null ? null : qrcodeUrl.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
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

    public Date getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Date usedTime) {
        this.usedTime = usedTime;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getShopCashierId() {
        return shopCashierId;
    }

    public void setShopCashierId(Long shopCashierId) {
        this.shopCashierId = shopCashierId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Long getDishSkuId() {
        return dishSkuId;
    }

    public void setDishSkuId(Long dishSkuId) {
        this.dishSkuId = dishSkuId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
