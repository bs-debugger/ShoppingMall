package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xq.live.model.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商城系统Sku的出参
 * Created by lipeng on 2018/8/29.
 */
public class GoodsSkuOut implements Serializable {

    private static final long serialVersionUID = -6214231388432092429L;

    //用于记录首页查询的条数
    private Integer homeTotle;

    private Long id;

    private String skuCode;

    private String skuName;

    private String skuPic;

    private BigDecimal costPrice;

    private BigDecimal marketPrice;

    private BigDecimal sellPrice;

    private BigDecimal otherMarkerPrice;//别人的市场价

    private Integer stockNum;

    private Integer waringStock;

    private Long spuId;

    private Integer isDeleted;

    private Integer status;

    private Long shopId;

    private Shop shop;

    private ShopOut shopOut;

    private Integer sellNum;//已售销量

    private String unit;//单位

    private Integer miniNum;//最小出售量

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private BigDecimal realWeight;//实际重量

    private Long deliveryTemplateId;//运费模版id

    private String spuName;//产品名字

    private String spuCode;//产品编码

    private Integer spuType;//产品类型

    private Long categoryId;//类目id

    private Long brandId;//品牌id

    private String categoryName;//类目名字

    private String brandName;//品牌名字

    private Integer sendType;//商品配送类型 0均支持 1平台邮购 2门店自提

    private String smallSkuPic;//商品图片_裁剪图

    private String remark;//备注

    private Integer singleType;//分单类型 0均支持 1分单 2整单

    private Integer expiryDate;//商品生成券的过期时间

    private String goodsSkuPics;//商品图片组

    private Integer sortNum;//排序

    private Integer auditStatus;//审核状态

    private Integer useSkuDetail;//设置商品明细(0,不设置,1,设置)

    private GoodsSkuRejectLog goodsSkuRejectLog;//商品驳回原因详情

    private List<GoodsSkuSpecValueOut> goodsSkuSpecValues;//商品规格的集合

    private List<GoodsPromotionRules> goodsPromotionRules;//商品的优惠规则集合

    private List<SalePointOut> salePointOuts;//商品销售点信息

    private List<ActGoodsSkuOut> actGoodsSkuOuts;//商品参与的所有活动列表

    private ActGoodsSkuOut actGoodsSkuOut;//商品参与的活动价格和类目

    private GoodsSpuOut goodsSpuOut;//商品对应的spu的详细信息和spu对应的文描信息

    private GoodsCategory goodsCategory;//商品类目信息

    private ActInfo actInfo;//活动

    private Integer distance; //距离

    private List<ActOrder> actOrder;//活动订单

    private String shopName;//商家名称

    private List<Attachment> attachments;//商品图片组详情

    private Integer canUpdate;//是否能够更改商品信息  0能更改  1不能更改

    private Integer piece; //件数
    private BigDecimal bulk;//体积

    private List<ActRankingOut> actRankingOut;//商品获奖列表

    private Integer goodsPayType;// 1原价购买   2砍价购买    3原价与砍价购买

    private BigDecimal singlyPrice;//单独购买价格

    private Integer autoAddStock;//是否自动补充库存 0 否 1是

    private Integer showType; //商品展示类型0，商品列表，1，会员优惠券，2会员套餐

    private List<GoodsSkuDetail> goodsSkuDetails;//商品明细

    public Integer getHomeTotle() {
        return homeTotle;
    }

    public void setHomeTotle(Integer homeTotle) {
        this.homeTotle = homeTotle;
    }

    public ActGoodsSkuOut getActGoodsSkuOut() {
        return actGoodsSkuOut;
    }

    public void setActGoodsSkuOut(ActGoodsSkuOut actGoodsSkuOut) {
        this.actGoodsSkuOut = actGoodsSkuOut;
    }

    public ActInfo getActInfo() {
        return actInfo;
    }

    public void setActInfo(ActInfo actInfo) {
        this.actInfo = actInfo;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

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

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public String getSpuCode() {
        return spuCode;
    }

    public void setSpuCode(String spuCode) {
        this.spuCode = spuCode;
    }

    public Integer getSpuType() {
        return spuType;
    }

    public void setSpuType(Integer spuType) {
        this.spuType = spuType;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public List<GoodsSkuSpecValueOut> getGoodsSkuSpecValues() {
        return goodsSkuSpecValues;
    }

    public void setGoodsSkuSpecValues(List<GoodsSkuSpecValueOut> goodsSkuSpecValues) {
        this.goodsSkuSpecValues = goodsSkuSpecValues;
    }

    public List<GoodsPromotionRules> getGoodsPromotionRules() {
        return goodsPromotionRules;
    }

    public void setGoodsPromotionRules(List<GoodsPromotionRules> goodsPromotionRules) {
        this.goodsPromotionRules = goodsPromotionRules;
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

    public List<SalePointOut> getSalePointOuts() {
        return salePointOuts;
    }

    public void setSalePointOuts(List<SalePointOut> salePointOuts) {
        this.salePointOuts = salePointOuts;
    }

    public Integer getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Integer expiryDate) {
        this.expiryDate = expiryDate;
    }

    public List<ActGoodsSkuOut> getActGoodsSkuOuts() {
        return actGoodsSkuOuts;
    }

    public void setActGoodsSkuOuts(List<ActGoodsSkuOut> actGoodsSkuOuts) {
        this.actGoodsSkuOuts = actGoodsSkuOuts;
    }

    public GoodsSpuOut getGoodsSpuOut() {
        return goodsSpuOut;
    }

    public void setGoodsSpuOut(GoodsSpuOut goodsSpuOut) {
        this.goodsSpuOut = goodsSpuOut;
    }

    public List<ActOrder> getActOrder() {
        return actOrder;
    }

    public void setActOrder(List<ActOrder> actOrder) {
        this.actOrder = actOrder;
    }

    public GoodsCategory getGoodsCategory() {
        return goodsCategory;
    }

    public void setGoodsCategory(GoodsCategory goodsCategory) {
        this.goodsCategory = goodsCategory;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public String getGoodsSkuPics() {
        return goodsSkuPics;
    }

    public void setGoodsSkuPics(String goodsSkuPics) {
        this.goodsSkuPics = goodsSkuPics;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Integer getCanUpdate() {
        return canUpdate;
    }

    public void setCanUpdate(Integer canUpdate) {
        this.canUpdate = canUpdate;
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

    public List<ActRankingOut> getActRankingOut() {
        return actRankingOut;
    }

    public void setActRankingOut(List<ActRankingOut> actRankingOut) {
        this.actRankingOut = actRankingOut;
    }

    public Integer getGoodsPayType() {
        return goodsPayType;
    }

    public void setGoodsPayType(Integer goodsPayType) {
        this.goodsPayType = goodsPayType;
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

    public GoodsSkuRejectLog getGoodsSkuRejectLog() {
        return goodsSkuRejectLog;
    }

    public void setGoodsSkuRejectLog(GoodsSkuRejectLog goodsSkuRejectLog) {
        this.goodsSkuRejectLog = goodsSkuRejectLog;
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

    public List<GoodsSkuDetail> getGoodsSkuDetails() {
        return goodsSkuDetails;
    }

    public void setGoodsSkuDetails(List<GoodsSkuDetail> goodsSkuDetails) {
        this.goodsSkuDetails = goodsSkuDetails;
    }

    public ShopOut getShopOut() {
        return shopOut;
    }

    public void setShopOut(ShopOut shopOut) {
        this.shopOut = shopOut;
    }
}
