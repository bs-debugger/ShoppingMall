package com.xq.live.model;

import java.math.BigDecimal;

public class ShopAccountDetail {

    private String orderCode; //订单编号

    private String userName; //下单人

    private BigDecimal serviceAmount; //服务费

    private Integer flagType; //订单类型

    private Integer isBill; //结算状态

    private BigDecimal realUnitPrice; //支付金额

    private BigDecimal realShopUnitPirce;//商家实收金额

    private String createTime; //核销时间

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getServiceAmount() {
        return serviceAmount;
    }

    public void setServiceAmount(BigDecimal serviceAmount) {
        this.serviceAmount = serviceAmount;
    }

    public Integer getFlagType() {
        return flagType;
    }

    public void setFlagType(Integer flagType) {
        this.flagType = flagType;
    }

    public Integer getIsBill() {
        return isBill;
    }

    public void setIsBill(Integer isBill) {
        this.isBill = isBill;
    }

    public BigDecimal getRealUnitPrice() {
        return realUnitPrice;
    }

    public void setRealUnitPrice(BigDecimal realUnitPrice) {
        this.realUnitPrice = realUnitPrice;
    }

    public BigDecimal getRealShopUnitPirce() {
        return realShopUnitPirce;
    }

    public void setRealShopUnitPirce(BigDecimal realShopUnitPirce) {
        this.realShopUnitPirce = realShopUnitPirce;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
