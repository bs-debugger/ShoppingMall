package com.xq.live.poientity;

import java.math.BigDecimal;

/**
 * Created by admin on 2019/1/28.
 */
public class OrderCouponEntity {

    private String index;

    private String goodsSkuName;

    private String flagTypeName;//标记类型(普通型  砍价型  秒杀型)

    private String usedTime;

    private BigDecimal realUnitPrice;

    private BigDecimal serviceAmount;//服务费

    private BigDecimal realAmount;//实收款 realUnitPrice减去serviceAmount

    private String couponCode;

    private BigDecimal shopServiceAmount;//商家服务费

    private BigDecimal userServiceAmount;//用户服务费

    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getGoodsSkuName() {
        return goodsSkuName;
    }

    public void setGoodsSkuName(String goodsSkuName) {
        this.goodsSkuName = goodsSkuName;
    }

    public String getFlagTypeName() {
        return flagTypeName;
    }

    public void setFlagTypeName(String flagTypeName) {
        this.flagTypeName = flagTypeName;
    }

    public String getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(String usedTime) {
        this.usedTime = usedTime;
    }

    public BigDecimal getRealUnitPrice() {
        return realUnitPrice;
    }

    public void setRealUnitPrice(BigDecimal realUnitPrice) {
        this.realUnitPrice = realUnitPrice;
    }

    public BigDecimal getServiceAmount() {
        return serviceAmount;
    }

    public void setServiceAmount(BigDecimal serviceAmount) {
        this.serviceAmount = serviceAmount;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
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
