package com.xq.live.vo.in;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 专区详情入参
 * Created by lipeng on 2019/1/5.
 */
public class ShopZoneItemInVo extends BaseInVo{

    private Long id;

    private Long shopZoneId;

    private String name;

    private String picUrl;

    private String address;

    private String phone;

    private BigDecimal locationX;

    private BigDecimal locationY;

    private String zoneInfo;

    private Integer isDeleted;

    private Date createTime;

    private Date updateTime;

    private String city;

    private String regionName;

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
}
