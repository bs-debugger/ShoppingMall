package com.xq.live.model;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品SKU 实体entity
 */
public class Sku {
    /**
     * sku_type 1 平台券  2 特色菜 3 活动券 4砍价券 5商家套餐 6砍价菜   7抢购菜  8抢购券 9兑换菜 10兑换券
     */
    public final static int SKU_TYPE_XQQ = 1;   //享七券

    public final static int SKU_TYPE_TSC = 2;   //特色菜

    public final static int SKU_TYPE_HDQ = 3;   //食典券

    public final static int SKU_TYPE_KJQ = 4;   //砍价券

    public final static int SKU_TYPE_SJTC = 5;   //商家套餐

    public final static int SKU_TYPE_KJC = 6;   //砍价菜

    public final static int SKU_TYPE_QGC = 7;   //抢购菜

    public final static int SKU_TYPE_QGQ = 8;   //抢购券

    public final static int SKU_TYPE_DHC = 9;   //兑换菜

    public final static int SKU_TYPE_DHQ = 10;   //兑换券

    /**
     * 是否删除
     */
    public final static int SKU_NO_DELETED = 0; //未删除(相当于上架)

    public final static int SKU_IS_DELETED = 1;//已删除(相当于已下架)

    public final static int SKU_PX_NUM = 15;//螃蟹的库存


    /**
     * 享七产品类型 1大闸蟹
     */
    public final static int TYPE_DZX = 1; //大闸蟹

    private Long id;
    private String skuCode;
    @NotNull(message = "skuName必填")
    private String skuName;
    @NotNull(message = "skuType必填")
    private Integer skuType;   //sku类型 1 券  2 其他

    private BigDecimal sellPrice;//相当于原价

    private BigDecimal inPrice;//相当于进价

    private BigDecimal agioPrice;//折扣价

    @NotNull(message = "stockNum必填")
    private Integer stockNum;

    private Integer sellNum;

    private Date createTime;

    private Date updateTime;

    @NotNull(message = "opreatorId必填")
    private Long opreatorId;//操作人的用户id

    @NotNull(message = "opreatorName必填")
    private String opreatorName;//操作人的用户名称

    private Integer isDeleted;

    private String picUrl;

    private String smallPicUrl; //商品图片地址_裁剪图

    private String skuInfo;

    private Long shopId;//关联的shopId

    private Integer couponSkuType;//加入的票券在coupon_sku表里面的类型

    private Integer type;//享七自营产品类型 1大闸蟹

    public String getSmallPicUrl() {
        return smallPicUrl;
    }

    public void setSmallPicUrl(String smallPicUrl) {
        this.smallPicUrl = smallPicUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode == null ? null : skuCode.trim();
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName == null ? null : skuName.trim();
    }

    public Integer getSkuType() {
        return skuType;
    }

    public void setSkuType(Integer skuType) {
        this.skuType = skuType;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public BigDecimal getInPrice() {
        return inPrice;
    }

    public void setInPrice(BigDecimal inPrice) {
        this.inPrice = inPrice;
    }

    public BigDecimal getAgioPrice() {
        return agioPrice;
    }

    public void setAgioPrice(BigDecimal agioPrice) {
        this.agioPrice = agioPrice;
    }

    public Integer getStockNum() {
        return stockNum;
    }

    public void setStockNum(Integer stockNum) {
        this.stockNum = stockNum;
    }

    public Integer getSellNum() {
        return sellNum;
    }

    public void setSellNum(Integer sellNum) {
        this.sellNum = sellNum;
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

    public Long getOpreatorId() {
        return opreatorId;
    }

    public void setOpreatorId(Long opreatorId) {
        this.opreatorId = opreatorId;
    }

    public String getOpreatorName() {
        return opreatorName;
    }

    public void setOpreatorName(String opreatorName) {
        this.opreatorName = opreatorName == null ? null : opreatorName.trim();
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }

    public String getSkuInfo() {
        return skuInfo;
    }

    public void setSkuInfo(String skuInfo) {
        this.skuInfo = skuInfo;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Integer getCouponSkuType() {
        return couponSkuType;
    }

    public void setCouponSkuType(Integer couponSkuType) {
        this.couponSkuType = couponSkuType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
