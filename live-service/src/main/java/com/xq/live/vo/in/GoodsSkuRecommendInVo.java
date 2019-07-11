package com.xq.live.vo.in;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class GoodsSkuRecommendInVo {

    @ApiModelProperty("城市名")
    private String city;

    @ApiModelProperty("纬度")
    private BigDecimal locationX;

    @ApiModelProperty("经度")
    private BigDecimal locationY;

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
