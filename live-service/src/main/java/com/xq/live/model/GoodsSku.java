package com.xq.live.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商城系统Sku的实体类
 */
public class GoodsSku {
    /**
     * is_deleted 0 未删除  1 已删除
     */
    public final static int GOODS_SKU_NO_DELETED = 0;   //未删除

    public final static int GOODS_SKU_IS_DELETED = 1;   //已删除


    /**
     * // 1原价购买   2砍价购买    3原价与砍价购买
     */
    public final static int GOODS_SKU_PAY_TYPE_YJ = 1;   //1原价购买
    public final static int GOODS_SKU_PAY_TYPE_KJ = 2;//2砍价购买
    public final static int GOODS_SKU_PAY_TYPE_ALL = 3;//3原价与砍价购买



    /**
     * status 1上架  2下架 3预售
     */
    public final static int STATUS_SJ = 1;   //上架

    public final static int STATUS_XJ = 2;   //下架

    public final static int STATUS_YS = 3;   //预售

    /**
     * 配送类型  0均支持 1平台邮购 2门店自提
     */
    public final static int SEND_TYPE_ALL = 0;   //均支持

    public final static int SEND_TYPE_PTYG = 1;   //平台邮购

    public final static int SEND_TYPE_MDZT = 2;   //门店自提

    /**
     * 分单类型  0均支持 1分单  2整单
     */
    public final static int SINGLE_TYPE_ALL = 0;   //均支持

    public final static int SINGLE_TYPE_FD = 1;    //分单

    public final static int SINGLE_TYPE_ZD = 2;    //整单

    /**
     * 是否自动补充库存 0 否 1是
     */
    public final static int AUTO_ADD_STOCK_NO = 0;

    public final static int AUTO_ADD_STOCK_YES = 1;

    private Long id;

    private String skuCode;

    @NotNull(message = "skuName不能为空")
    private String skuName;

    @NotNull(message = "skuPic不能为空")
    private String skuPic;

    private String smallSkuPic;//商品图片_裁剪图

    @NotNull(message = "costPrice不能为空")
    private BigDecimal costPrice;//成本价（进货价）

    @NotNull(message = "marketPrice不能为空")
    private BigDecimal marketPrice;//市场价

    @NotNull(message = "sellPrice不能为空")
    private BigDecimal sellPrice;//售价

    private Integer stockNum;//实际库存

    @NotNull(message = "waringStock不能为空")
    private Integer waringStock;//预警库存

    @NotNull(message = "spuId不能为空")
    private Long spuId;//产品id

    private Integer isDeleted;//是否删除 0 否 1 是

    private Integer status;//商品状态  1 上架,2 下架,3 预售

    private Long shopId;//商铺id,为0表示自营

    private Integer sellNum;//已售销量

    private String unit;//单位

    private Integer miniNum;//最小出售量

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private BigDecimal realWeight;//实际重量

    private Long deliveryTemplateId;//运费模版id

    private BigDecimal otherMarkerPrice;//别人的市场价

    private Integer sendType;//商品配送类型 0均支持 1平台邮购 2门店自提

    private String remark;//备注

    private Integer singleType;//分单类型 0均支持 1分单 2整单

    private Integer expiryDate;//商品生成券的过期时间

    private Integer piece; //件数
    private BigDecimal bulk;//体积

    private String goodsSkuPics;//商品图片组

    private Integer sortNum;//排序

    private BigDecimal singlyPrice;//单独购买价格

    private Integer autoAddStock;//是否自动补充库存 0 否 1是

    private Integer auditStatus;//审核状态  0 待审核  1 审核通过  2 驳回

    private Integer useSkuDetail;//设置商品明细(0,不设置,1,设置)

    private Integer showType;//商品展示类型0，商品列表，1，会员优惠券，2会员套餐

    public String getSmallSkuPic() {
        return smallSkuPic;
    }

    public void setSmallSkuPic(String smallSkuPic) {
        this.smallSkuPic = smallSkuPic;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
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

    public String getSkuPic() {
        return skuPic;
    }

    public void setSkuPic(String skuPic) {
        this.skuPic = skuPic;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Integer getStockNum() {
        return stockNum;
    }

    public void setStockNum(Integer stockNum) {
        this.stockNum = stockNum;
    }

    public Integer getWaringStock() {
        return waringStock;
    }

    public void setWaringStock(Integer waringStock) {
        this.waringStock = waringStock;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Integer getSellNum() {
        return sellNum;
    }

    public void setSellNum(Integer sellNum) {
        this.sellNum = sellNum;
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

    public BigDecimal getRealWeight() {
        return realWeight;
    }

    public void setRealWeight(BigDecimal realWeight) {
        this.realWeight = realWeight;
    }

    public Long getDeliveryTemplateId() {
        return deliveryTemplateId;
    }

    public void setDeliveryTemplateId(Long deliveryTemplateId) {
        this.deliveryTemplateId = deliveryTemplateId;
    }

    public BigDecimal getOtherMarkerPrice() {
        return otherMarkerPrice;
    }

    public void setOtherMarkerPrice(BigDecimal otherMarkerPrice) {
        this.otherMarkerPrice = otherMarkerPrice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSingleType() {
        return singleType;
    }

    public void setSingleType(Integer singleType) {
        this.singleType = singleType;
    }

    public Integer getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Integer expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getPiece() {
        return piece;
    }

    public void setPiece(Integer piece) {
        this.piece = piece;
    }

    public BigDecimal getBulk() {
        return bulk;
    }

    public void setBulk(BigDecimal bulk) {
        this.bulk = bulk;
    }

    public String getGoodsSkuPics() {
        return goodsSkuPics;
    }

    public void setGoodsSkuPics(String goodsSkuPics) {
        this.goodsSkuPics = goodsSkuPics;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public BigDecimal getSinglyPrice() {
        return singlyPrice;
    }

    public void setSinglyPrice(BigDecimal singlyPrice) {
        this.singlyPrice = singlyPrice;
    }

    public Integer getAutoAddStock() {
        return autoAddStock;
    }

    public void setAutoAddStock(Integer autoAddStock) {
        this.autoAddStock = autoAddStock;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Integer getUseSkuDetail() {
        return useSkuDetail;
    }

    public void setUseSkuDetail(Integer useSkuDetail) {
        this.useSkuDetail = useSkuDetail;
    }

    public Integer getShowType() {
        return showType;
    }

    public void setShowType(Integer showType) {
        this.showType = showType;
    }
}
