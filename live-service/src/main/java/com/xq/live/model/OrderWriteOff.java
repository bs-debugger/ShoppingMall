package com.xq.live.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class OrderWriteOff {
    /**
     * is_bill 0 未结算  1 结算
     */
    public final static int ORDER_WRITE_OFF_NO_BILL = 0;   //未结算

    public final static int ORDER_WRITE_OFF_IS_BILL = 1;   //已结算


    /**
     * is_Dui 0 未对账  1 对账
     */
    public final static int ORDER_WRITE_OFF_NO_DUI = 0;   //未对账

    public final static int ORDER_WRITE_OFF_IS_DUI = 1;   //已对账

    private Long id;

    private Long orderId;

    private Long shopId;

    private String shopName;

    private Long orderCouponId;

    private String orderCouponCode;

    private Long orderAddressId;

    private Long goodsSkuId;

    private BigDecimal couponAmount;

    private BigDecimal sendAmount;

    private Long userId;//购买人

    private String userName;

    private Long changerId;//核销人

    private String changerName;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private String sendTime;//期望配送日期

    private String remark;//备注

    private Long salepointId;

    private BigDecimal serviceAmount;//服务费

    private BigDecimal shopServiceAmount;//商家服务费

    private BigDecimal userServiceAmount;//用户服务费

    private BigDecimal realUnitPrice;//票券实际支付单价

    private BigDecimal realShopUnitPrice;//真实商家营业额单价(realUnitPrice - serviceAmount)

    private Integer isBill;//是否结算

    private Integer isDui;//是否对账

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    public Long getOrderCouponId() {
        return orderCouponId;
    }

    public void setOrderCouponId(Long orderCouponId) {
        this.orderCouponId = orderCouponId;
    }

    public String getOrderCouponCode() {
        return orderCouponCode;
    }

    public void setOrderCouponCode(String orderCouponCode) {
        this.orderCouponCode = orderCouponCode == null ? null : orderCouponCode.trim();
    }

    public Long getOrderAddressId() {
        return orderAddressId;
    }

    public void setOrderAddressId(Long orderAddressId) {
        this.orderAddressId = orderAddressId;
    }

    public Long getGoodsSkuId() {
        return goodsSkuId;
    }

    public void setGoodsSkuId(Long goodsSkuId) {
        this.goodsSkuId = goodsSkuId;
    }

    public BigDecimal getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
    }

    public BigDecimal getSendAmount() {
        return sendAmount;
    }

    public void setSendAmount(BigDecimal sendAmount) {
        this.sendAmount = sendAmount;
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

    public Long getChangerId() {
        return changerId;
    }

    public void setChangerId(Long changerId) {
        this.changerId = changerId;
    }

    public String getChangerName() {
        return changerName;
    }

    public void setChangerName(String changerName) {
        this.changerName = changerName == null ? null : changerName.trim();
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

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getSalepointId() {
        return salepointId;
    }

    public void setSalepointId(Long salepointId) {
        this.salepointId = salepointId;
    }

    public BigDecimal getServiceAmount() {
        return serviceAmount;
    }

    public void setServiceAmount(BigDecimal serviceAmount) {
        this.serviceAmount = serviceAmount;
    }

    public BigDecimal getRealUnitPrice() {
        return realUnitPrice;
    }

    public void setRealUnitPrice(BigDecimal realUnitPrice) {
        this.realUnitPrice = realUnitPrice;
    }

    public Integer getIsBill() {
        return isBill;
    }

    public void setIsBill(Integer isBill) {
        this.isBill = isBill;
    }

    public Integer getIsDui() {
        return isDui;
    }

    public void setIsDui(Integer isDui) {
        this.isDui = isDui;
    }

    public BigDecimal getRealShopUnitPrice() {
        return realShopUnitPrice;
    }

    public void setRealShopUnitPrice(BigDecimal realShopUnitPrice) {
        this.realShopUnitPrice = realShopUnitPrice;
    }

    public BigDecimal getShopServiceAmount() {
        return shopServiceAmount;
    }

    public void setShopServiceAmount(BigDecimal shopServiceAmount) {
        this.shopServiceAmount = shopServiceAmount;
    }

    public BigDecimal getUserServiceAmount() {
        return userServiceAmount;
    }

    public void setUserServiceAmount(BigDecimal userServiceAmount) {
        this.userServiceAmount = userServiceAmount;
    }
}
