package com.xq.live.vo.out;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class ActivityRecommendOut implements Serializable {

    private static final long serialVersionUID = 7022858561503476766L;

    @ApiModelProperty("活动布局ID")
    private Integer activityLayoutId;

    @ApiModelProperty("推荐ID")
    private Integer recommendId;

    @ApiModelProperty("活动类型")
    private Integer type;

    @ApiModelProperty("布局位置（xcx-home：小程序首页，xcx-category-1：正餐，xcx-category-48：轻餐饮）")
    private String position;

    @ApiModelProperty("活动地址")
    private String linkUrl;

    @ApiModelProperty("活动封面图")
    private String imgUrl;

    @ApiModelProperty("商品ID")
    private Long goodsSkuId;

    @ApiModelProperty("商家ID")
    private Long shopId;

    @ApiModelProperty("活动ID")
    private Long actId;

    @ApiModelProperty("城市名")
    private String city;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("排序")
    private Boolean isJump;

    @ApiModelProperty("活动内容")
    private String content;

    public Integer getActivityLayoutId() {
        return activityLayoutId;
    }

    public void setActivityLayoutId(Integer activityLayoutId) {
        this.activityLayoutId = activityLayoutId;
    }

    public Integer getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(Integer recommendId) {
        this.recommendId = recommendId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Long getGoodsSkuId() {
        return goodsSkuId;
    }

    public void setGoodsSkuId(Long goodsSkuId) {
        this.goodsSkuId = goodsSkuId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Boolean getIsJump() {
        return isJump;
    }

    public void setIsJump(Boolean isJump) {
        this.isJump = isJump;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
