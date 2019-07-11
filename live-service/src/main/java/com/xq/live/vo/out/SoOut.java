package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单出参Vo
 *
 * @author zhangpeng32
 * @date 2018-02-09 14:43
 * @copyright:hbxq
 **/
public class SoOut implements Comparable<SoOut>{

    private Long id;

    private BigDecimal soAmount;

    private Long userId;

    private String userName;

    private Integer payType;

    private Integer soStatus;

    private Integer soType;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private Long soId;

    private Long skuId;

    private String skuCode;

    private String skuName;

    private Integer skuNum;

    @NumberFormat
    private BigDecimal unitPrice;

    private String ruleDesc;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paidTime;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private  Date hxTime;

    private Long shopId;//商家订单绑定的shopId

    private String shopName;//商家名称

    private String logoUrl;//商家的主图

    private BigDecimal inPrice;//商家订单消费享七券的面值

    private String mobile;//下单用户的手机号

    private String userIconUrl;//下单人的头像

    private Integer isDui;//是否对账

    private Integer skuType;//产品分类

    private BigDecimal servicePrice;//单个服务费

    private BigDecimal accountAmount;//应收款

    private String coupon;//优惠券

    private BigDecimal realAmount;//实收款

    private Long dishSkuId;//订单所对菜的sku_id

    private String dishSkuName;//订单所对菜的sku_name

    public Integer getSkuType() {
        return skuType;
    }

    public void setSkuType(Integer skuType) {
        this.skuType = skuType;
    }

    public Integer getIsDui() {
        return isDui;
    }

    public void setIsDui(Integer isDui) {
        this.isDui = isDui;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getSoAmount() {
        return soAmount;
    }

    public void setSoAmount(BigDecimal soAmount) {
        this.soAmount = soAmount;
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
        this.userName = userName;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getSoStatus() {
        return soStatus;
    }

    public void setSoStatus(Integer soStatus) {
        this.soStatus = soStatus;
    }

    public Integer getSoType() {
        return soType;
    }

    public void setSoType(Integer soType) {
        this.soType = soType;
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

    public Long getSoId() {
        return soId;
    }

    public void setSoId(Long soId) {
        this.soId = soId;
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
        this.skuCode = skuCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Integer getSkuNum() {
        return skuNum;
    }

    public void setSkuNum(Integer skuNum) {
        this.skuNum = skuNum;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getRuleDesc() {
        return ruleDesc;
    }

    public void setRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc;
    }

    public Date getPaidTime() {
        return paidTime;
    }

    public void setPaidTime(Date paidTime) {
        this.paidTime = paidTime;
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
        this.shopName = shopName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public BigDecimal getInPrice() {
        return inPrice;
    }

    public void setInPrice(BigDecimal inPrice) {
        this.inPrice = inPrice;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserIconUrl() {
        return userIconUrl;
    }

    public void setUserIconUrl(String userIconUrl) {
        this.userIconUrl = userIconUrl;
    }

    public Date getHxTime() {
        return hxTime;
    }

    public void setHxTime(Date hxTime) {
        this.hxTime = hxTime;
    }

    public BigDecimal getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(BigDecimal servicePrice) {
        this.servicePrice = servicePrice;
    }

    public BigDecimal getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(BigDecimal accountAmount) {
        this.accountAmount = accountAmount;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }

    public Long getDishSkuId() {
        return dishSkuId;
    }

    public void setDishSkuId(Long dishSkuId) {
        this.dishSkuId = dishSkuId;
    }

    public String getDishSkuName() {
        return dishSkuName;
    }

    public void setDishSkuName(String dishSkuName) {
        this.dishSkuName = dishSkuName;
    }

    //由大到小排序
    @Override
    public int compareTo(SoOut o) {
        Date a = this.getPaidTime();
        Date b = o.getPaidTime();
        if(a.getTime()>b.getTime()){
            return -1;
        }
        return 1;
    }
}
