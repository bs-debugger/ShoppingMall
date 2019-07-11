package com.xq.live.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

public class ShopZoneItem {
    @ApiModelProperty(value = "专区id")
    private Long id;
    @ApiModelProperty(value = "大专区id")
    private Long shopZoneId;
    @ApiModelProperty(value = "专区详细名字")
    private String name;
    @ApiModelProperty(value = "专区详细图片")
    private String picUrl;
    @ApiModelProperty(value = "专区详细地址")
    private String address;
    @ApiModelProperty(value = "专区详细手机号")
    private String phone;
    @ApiModelProperty(value = "专区详细经度")
    private BigDecimal locationX;
    @ApiModelProperty(value = "专区详细纬度")
    private BigDecimal locationY;
    @ApiModelProperty(value = "专区详细简介描述")
    private String zoneInfo;
    @ApiModelProperty(value = "是否删除 0未删除  1已删除")
    private Integer isDeleted;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShopZoneId() {
        return shopZoneId;
    }

    public void setShopZoneId(Long shopZoneId) {
        this.shopZoneId = shopZoneId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
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

    public String getZoneInfo() {
        return zoneInfo;
    }

    public void setZoneInfo(String zoneInfo) {
        this.zoneInfo = zoneInfo == null ? null : zoneInfo.trim();
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
