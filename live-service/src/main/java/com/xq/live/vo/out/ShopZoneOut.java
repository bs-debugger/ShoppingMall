package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2019/1/9.
 */
public class ShopZoneOut {
    @ApiModelProperty(value = "大专区id")
    private Long id;
    @ApiModelProperty(value = "大专区城市")
    private String city;
    @ApiModelProperty(value = "大专区名字")
    private String regionName;
    @ApiModelProperty(value = "大专区图片")
    private String picUrl;
    @ApiModelProperty(value = "是否删除 0未删除 1删除")
    private Integer isDeleted;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @ApiModelProperty(value = "大专区所对应的专区列表")
    private List<ShopZoneItemOut> ShopZoneItem;

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
        this.city = city;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
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

    public List<ShopZoneItemOut> getShopZoneItem() {
        return ShopZoneItem;
    }

    public void setShopZoneItem(List<ShopZoneItemOut> shopZoneItem) {
        ShopZoneItem = shopZoneItem;
    }
}
