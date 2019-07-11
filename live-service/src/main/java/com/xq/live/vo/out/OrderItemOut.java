package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商城系统订单详情出参
 * Created by lipeng on 2018/9/5.
 */
public class OrderItemOut {
    private Long id;

    private String orderCode;

    private Long goodsSpuId;

    private Long goodsSkuId;

    private Long shopId;

    private String goodsSkuName;

    private String goodsSkuPic;

    private String smallSkuPic;//裁剪图

    private String unit;//单位

    private Integer miniNum;//最小出售数量

    private Integer goodsNum;

    private Integer giftNum;//优惠数目

    private BigDecimal goodsPrice;

    private BigDecimal packingPrice;

    private BigDecimal realUnitPrice;

    private Integer goodsIsDeleted;//是否删除 0 否 1 是

    private Integer goodsStatus;//商品状态  1 上架,2 下架,3 预售

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private String remark;

    private List<GoodsSkuSpecValueOut> goodsSkuSpecValues;//商品规格的集合

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode == null ? null : orderCode.trim();
    }

    public Long getGoodsSpuId() {
        return goodsSpuId;
    }

    public void setGoodsSpuId(Long goodsSpuId) {
        this.goodsSpuId = goodsSpuId;
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

    public String getGoodsSkuName() {
        return goodsSkuName;
    }

    public void setGoodsSkuName(String goodsSkuName) {
        this.goodsSkuName = goodsSkuName == null ? null : goodsSkuName.trim();
    }

    public String getGoodsSkuPic() {
        return goodsSkuPic;
    }

    public void setGoodsSkuPic(String goodsSkuPic) {
        this.goodsSkuPic = goodsSkuPic;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getMiniNum() {
        return miniNum;
    }

    public void setMiniNum(Integer miniNum) {
        this.miniNum = miniNum;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public Integer getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(Integer giftNum) {
        this.giftNum = giftNum;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public BigDecimal getPackingPrice() {
        return packingPrice;
    }

    public void setPackingPrice(BigDecimal packingPrice) {
        this.packingPrice = packingPrice;
    }

    public BigDecimal getRealUnitPrice() {
        return realUnitPrice;
    }

    public void setRealUnitPrice(BigDecimal realUnitPrice) {
        this.realUnitPrice = realUnitPrice;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<GoodsSkuSpecValueOut> getGoodsSkuSpecValues() {
        return goodsSkuSpecValues;
    }

    public void setGoodsSkuSpecValues(List<GoodsSkuSpecValueOut> goodsSkuSpecValues) {
        this.goodsSkuSpecValues = goodsSkuSpecValues;
    }

    public Integer getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(Integer goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public Integer getGoodsIsDeleted() {
        return goodsIsDeleted;
    }

    public void setGoodsIsDeleted(Integer goodsIsDeleted) {
        this.goodsIsDeleted = goodsIsDeleted;
    }

    public String getSmallSkuPic() {
        return smallSkuPic;
    }

    public void setSmallSkuPic(String smallSkuPic) {
        this.smallSkuPic = smallSkuPic;
    }
}
