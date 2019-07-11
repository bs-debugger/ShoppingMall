package com.xq.live.vo.in;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class ShopRelationInVo extends BaseInVo {

    @ApiModelProperty("是否查询所有商家，false时只查询自己认领的商家")
    private Boolean allRelation;

    @ApiModelProperty("用户ID，前端不需要传")
    private Long userId;

    @ApiModelProperty("商户ID")
    private Long shopId;

    @ApiModelProperty("经度")
    private BigDecimal locationX;

    @ApiModelProperty("纬度")
    private BigDecimal locationY;

    @ApiModelProperty("认领/解除")
    private Boolean status;

    public Boolean getAllRelation() {
        return allRelation;
    }

    public void setAllRelation(Boolean allRelation) {
        this.allRelation = allRelation;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
