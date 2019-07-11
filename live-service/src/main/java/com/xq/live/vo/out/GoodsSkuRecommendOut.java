package com.xq.live.vo.out;

import com.xq.live.model.GoodsSku;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class GoodsSkuRecommendOut extends GoodsSku {

    @ApiModelProperty("城市地址")
    private String shopCity;

    @ApiModelProperty("分类名")
    private String categoryName;

    @ApiModelProperty("活动售价")
    private BigDecimal actAmount;

    @ApiModelProperty("活动折扣")
    private BigDecimal discount;

    @ApiModelProperty("与商店的距离")
    private BigDecimal distance;

    public String getShopCity() {
        return shopCity;
    }

    public void setShopCity(String shopCity) {
        this.shopCity = shopCity;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getActAmount() {
        return actAmount;
    }

    public void setActAmount(BigDecimal actAmount) {
        this.actAmount = actAmount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }
}
