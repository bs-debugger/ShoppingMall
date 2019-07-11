package com.xq.live.vo.in;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 专区相关入参
 * Created by lipeng on 2019/1/4.
 */
public class ShopZoneInVo extends BaseInVo{
    @ApiModelProperty(value = "大专区id",dataType = "Long")
    private Long id;
    @ApiModelProperty(value = "大专区城市",example = "武汉市",dataType = "string")
    private String city;
    @ApiModelProperty(value = "大专区名字",example = "万达",dataType = "string")
    private String regionName;
    @ApiModelProperty(value = "大专区图片",dataType = "string")
    private String picUrl;
    @ApiModelProperty(value = "是否删除 0未删除 1已删除",dataType = "int")
    private Integer isDeleted;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName == null ? null : regionName.trim();
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
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
