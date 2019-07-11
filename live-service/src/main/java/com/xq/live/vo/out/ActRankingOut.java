package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xq.live.model.*;

import java.util.Date;
import java.util.List;

/**
 * Created by ss on 2019/2/28.
 */
public class ActRankingOut {

    private Long id;

    private Long skuId;

    private Long actGoodsSkuId;

    private Long actId;

    private Long userId;

    private Long shopId;

    private Integer sortNum;

    private Integer type;

    private Integer isDeleted;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private GoodsCategory goodsCategory;//类目详情

    private ActGoodsSku actGoodsSku;//活动商品关联

    private ActInfo actInfo;//活动

    private GoodsPromotionRules goodsPromotionRules;//活动商品对应的促销规则

    private List<GoodsSkuOut> goodsSkuOut;//商品

    private Shop shop;

    private User user;

    private Integer distance;//距离

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getActGoodsSkuId() {
        return actGoodsSkuId;
    }

    public void setActGoodsSkuId(Long actGoodsSkuId) {
        this.actGoodsSkuId = actGoodsSkuId;
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
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

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public GoodsCategory getGoodsCategory() {
        return goodsCategory;
    }

    public void setGoodsCategory(GoodsCategory goodsCategory) {
        this.goodsCategory = goodsCategory;
    }

    public ActInfo getActInfo() {
        return actInfo;
    }

    public void setActInfo(ActInfo actInfo) {
        this.actInfo = actInfo;
    }

    public GoodsPromotionRules getGoodsPromotionRules() {
        return goodsPromotionRules;
    }

    public void setGoodsPromotionRules(GoodsPromotionRules goodsPromotionRules) {
        this.goodsPromotionRules = goodsPromotionRules;
    }

    public List<GoodsSkuOut> getGoodsSkuOut() {
        return goodsSkuOut;
    }

    public void setGoodsSkuOut(List<GoodsSkuOut> goodsSkuOut) {
        this.goodsSkuOut = goodsSkuOut;
    }

    public ActGoodsSku getActGoodsSku() {
        return actGoodsSku;
    }

    public void setActGoodsSku(ActGoodsSku actGoodsSku) {
        this.actGoodsSku = actGoodsSku;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }
}
