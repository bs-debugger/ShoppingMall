package com.xq.live.model;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class ActivityRecommend {

    private Integer id;

    @NotNull(message = "活动布局ID不能为空")
    @ApiModelProperty("活动布局ID")
    private Integer activityLayoutId; // 活动布局ID

    @ApiModelProperty("活动地址")
    private String linkUrl; // 活动地址

    @ApiModelProperty("活动封面图地址")
    private String imgUrl; // 图片地址

    private String shopCate; // 活动经营品类，为空表示适用所有

    @ApiModelProperty("商品ID")
    private Long goodsSkuId; // 商品ID

    @ApiModelProperty("商家ID")
    private Long shopId; // 商家ID

    @ApiModelProperty("act ID")
    private Long actId; // 活动ID

    @NotNull(message = "城市名不能为空")
    @ApiModelProperty("城市名")
    private String city; // 城市名

    @ApiModelProperty("备注")
    private String remark; // 备注

    @ApiModelProperty("排序")
    private Integer sort; // 排序

    private Boolean status; // 状态（0：不显示，1：显示）

    @ApiModelProperty("是否跳转")
    private Boolean isJump; // 是否跳转（0：否，1：是）

    private Boolean isDeleted; // 是否删除（0：否，1：是）

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActivityLayoutId() {
        return activityLayoutId;
    }

    public void setActivityLayoutId(Integer activityLayoutId) {
        this.activityLayoutId = activityLayoutId;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl == null ? null : linkUrl.trim();
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }

    public String getShopCate() {
        return shopCate;
    }

    public void setShopCate(String shopCate) {
        this.shopCate = shopCate == null ? null : shopCate.trim();
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
        this.city = city == null ? null : city.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getIsJump() {
        return isJump;
    }

    public void setIsJump(Boolean isJump) {
        this.isJump = isJump;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
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