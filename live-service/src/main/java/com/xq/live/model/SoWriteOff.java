package com.xq.live.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class SoWriteOff {
    /**
     * is_bill 0 未对账  1 对账
     */
    public final static int SO_WRITE_OFF_NO_BILL = 0;   //未结算

    public final static int SO_WRITE_OFF_IS_BILL = 1;   //已结算



    private Long id;

    @NotNull(message = "soId必填")
    private Long soId;

    private Long shopId;

    private String shopName;

    @NotNull(message = "shopAmount必填")
    private BigDecimal shopAmount;

    @NotNull(message = "couponId必填")
    private Long couponId;

    @NotNull(message = "couponCode必填")
    private String couponCode;

    @NotNull(message = "skuId必填")
    private Long skuId;

    @NotNull(message = "couponAmount必填")
    private BigDecimal couponAmount;

    @NotNull(message = "userId必填")
    private Long userId;//卷所属订单的userId

    @NotNull(message = "userName必填")
    private String userName;//卷所属订单的userId

    //@NotNull(message = "cashierId必填")
    private Long cashierId;//收银员的id---当前操作的用户id

    //@NotNull(message = "cashierName必填")
    private String cashierName;

    private BigDecimal paidAmount;

    private Date createTime;

    private Date updateTime;

    private Integer isBill;//是否对账  0 未对账  1对账

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

    public BigDecimal getShopAmount() {
        return shopAmount;
    }

    public void setShopAmount(BigDecimal shopAmount) {
        this.shopAmount = shopAmount;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
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

    public BigDecimal getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
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

    public Long getCashierId() {
        return cashierId;
    }

    public void setCashierId(Long cashierId) {
        this.cashierId = cashierId;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName == null ? null : cashierName.trim();
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
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

    public Integer getIsBill() {
        return isBill;
    }

    public void setIsBill(Integer isBill) {
        this.isBill = isBill;
    }
}
