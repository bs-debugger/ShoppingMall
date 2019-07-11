package com.xq.live.vo.out;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class ShopRelationOut {

    @ApiModelProperty("商户ID")
    private Long id;

    @ApiModelProperty("商户CODE")
    private String shopCode;

    @ApiModelProperty("商户名")
    private String shopName;

    @ApiModelProperty("商户城市")
    private String city;

    @ApiModelProperty("商户地址")
    private String address;

    @ApiModelProperty("商户电话")
    private String mobile;

    @ApiModelProperty("商户主页")
    private String indexUrl;

    @ApiModelProperty("商户主页裁剪图")
    private String smallIndexUrl;

    @ApiModelProperty("商户logo或者主图")
    private String logoUrl;

    @ApiModelProperty("商户logo或者主图的裁剪图")
    private String smallLogoUrl;

    @ApiModelProperty("经度")
    private BigDecimal locationX;

    @ApiModelProperty("纬度")
    private BigDecimal locationY;

    @ApiModelProperty("商户经营品类")
    private String shopCate;

    @ApiModelProperty("商户创建时间")
    private String createTime;

    @ApiModelProperty("认领的用户ID")
    private Long userId;

    @ApiModelProperty("认领的用户名")
    private String userName;

    @ApiModelProperty("认领的时间")
    private String relationCreateTime;

    @ApiModelProperty("距离")
    private Integer distance;

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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }

    public String getSmallIndexUrl() {
        return smallIndexUrl;
    }

    public void setSmallIndexUrl(String smallIndexUrl) {
        this.smallIndexUrl = smallIndexUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getSmallLogoUrl() {
        return smallLogoUrl;
    }

    public void setSmallLogoUrl(String smallLogoUrl) {
        this.smallLogoUrl = smallLogoUrl;
    }

    public BigDecimal getLocationX() {
        return locationX;
    }

    public void setLocationX(BigDecimal locationX) {
        this.locationX = locationX;
    }

    public BigDecimal getLocationY() {
        return locationY;
    }

    public void setLocationY(BigDecimal locationY) {
        this.locationY = locationY;
    }

    public String getShopCate() {
        return shopCate;
    }

    public void setShopCate(String shopCate) {
        this.shopCate = shopCate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getRelationCreateTime() {
        return relationCreateTime;
    }

    public void setRelationCreateTime(String relationCreateTime) {
        this.relationCreateTime = relationCreateTime;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }
}
