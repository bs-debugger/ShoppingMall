package com.xq.live.vo.in;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class ActivityLayoutInVo {

    @ApiModelProperty("活动类型")
    private String type;

    @ApiModelProperty("布局位置（xcx-home：小程序首页，xcx-category-1：正餐，xcx-category-48：轻餐饮）")
    private String position;

    @ApiModelProperty("城市名")
    private String city;

    @ApiModelProperty("经度")
    private BigDecimal locationX;

    @ApiModelProperty("纬度")
    private BigDecimal locationY;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

}
