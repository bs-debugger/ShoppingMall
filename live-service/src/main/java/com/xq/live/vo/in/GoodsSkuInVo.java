package com.xq.live.vo.in;

import com.xq.live.model.GoodsSkuDetail;
import com.xq.live.model.GoodsSkuSpecValue;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商城系统Sku的入参
 * Created by lipeng on 2018/8/29.
 */
public class GoodsSkuInVo extends BaseInVo{
    @ApiModelProperty(value = "商品id")
    private Long id;
    @ApiModelProperty(value = "商品编码")
    private String skuCode;

    @NotNull(message = "skuName不能为空")
    @ApiModelProperty(value = "商品名称")
    private String skuName;

    @NotNull(message = "skuPic不能为空")
    @ApiModelProperty(value = "商品图片")
    private String skuPic;
    @ApiModelProperty(value = "成本价(进货价)")
    private BigDecimal costPrice;//成本价（进货价）
    @ApiModelProperty(value = "市场价")
    private BigDecimal marketPrice;//市场价

    @NotNull(message = "sellPrice不能为空")
    @ApiModelProperty(value = "售价")
    private BigDecimal sellPrice;//售价
    @ApiModelProperty(value = "别人的市场价")
    private BigDecimal otherMarkerPrice;//别人的市场价

    @NotNull(message = "stockNum不能为空")
    @ApiModelProperty(value = "原价库存")
    private Integer stockNum;//实际库存
    @ApiModelProperty(value = "预警库存")
    private Integer waringStock;//预警库存
    @ApiModelProperty(value = "spuId")
    private Long spuId;//产品id
    @ApiModelProperty(value = "运费模板id")
    private Long deliveryTemplateId;//运费模板id
    @ApiModelProperty(value = "是否删除 0 否 1 是")
    private Integer isDeleted;//是否删除 0 否 1 是
    @ApiModelProperty(value = "商品状态  1 上架,2 下架,3 预售")
    private Integer status;//商品状态  1 上架,2 下架,3 预售

    @NotNull(message = "shopId不能为空")
    @ApiModelProperty(value = "商铺id,为0表示自营")
    private Long shopId;//商铺id,为0表示自营
    @ApiModelProperty(value = "专区id")
    private Long shopZoneItemId;//专区
    @ApiModelProperty(value = "已售销量")
    private Integer sellNum;//已售销量
    @ApiModelProperty(value = "单位")
    private String unit;//单位

    @NotNull(message = "miniNum不能为空")
    @ApiModelProperty(value = "最小出售量")
    private Integer miniNum;//最小出售量

    private Date createTime;

    private Date updateTime;

    @ApiModelProperty(value = "产品类型")
    private Integer spuType;//产品类型

    @NotNull(message = "sendType不能为空")
    @ApiModelProperty(value = "商品配送类型 0均支持 1平台邮购 2门店自提")
    private Integer sendType;//商品配送类型 0均支持 1平台邮购 2门店自提

    private String smallSkuPic;//商品图片_裁剪图
    @ApiModelProperty(value = "类目id")
    private Long categoryId;//类目id

    @ApiModelProperty(value = "排除类目id")
    private Long notCategoryId;//排除类目id

    @NotNull(message = "singleType不能为空")
    @ApiModelProperty(value = "分单类型 1分单 2整单")
    private Integer singleType;//分单类型 1分单 2整单

    @ApiModelProperty(value = "活动id")
    private Long actId;//活动id

    @ApiModelProperty(value = "商家所在经度")
    private BigDecimal locationX;
    @ApiModelProperty(value = "商家所在纬度")
    private BigDecimal locationY;

    private Long userId;
    @ApiModelProperty(value = "商家所在城市")
    private String city;//城市
    @ApiModelProperty(value = "拼团状态1,进行中 2,成功 3,失败")
    private Integer groupState;//拼团状态1,进行中 2,成功 3,失败
    @ApiModelProperty(value = "排序规则 0附近 1销量 2价格")
    private Integer browSort;//排序规则 0附近 1销量 2价格
    @ApiModelProperty(value = "商品实际重量")
    private BigDecimal realWeight;//实际重量
    @ApiModelProperty(value = "商品实际件数")
    private Integer piece; //件数
    @ApiModelProperty(value = "商品实际体积")
    private BigDecimal bulk;//体积

    @NotNull(message = "expiryDate不能为空")
    @ApiModelProperty(value = "商品生成券的过期时间")
    private Integer expiryDate;//商品生成券的过期时间
    @ApiModelProperty(value = "商品规格值关联详情")
    private GoodsSkuSpecValue goodsSkuSpecValue;//商品规格值关联详情

    private GoodsSpuInVo goodsSpuInVo;//spu详情+spu文描

    private List<ActGoodsSkuInVo> actGoodsSkuInVos;//商品参与活动的参数列表 + 商品活动促销规则

    private DeliveryTemplateInVo deliveryTemplateInVo;//运费模板详情

    private  String searchKey;//模糊搜索字段

    private String regionName;//专区名称

    private String shopZoneCity;//专区城市

    private String shopZoneItem;//专区详细名称

    private Integer rankingByVoteNum;//是否通过投票数目排序

    private String goodsSkuPics;//商品图片组

    private SalePointInVo salePointInVo;//销售点入参详情

    private Integer sortNum;//排序

    @ApiModelProperty(value = "单独购买价格")
    private BigDecimal singlyPrice;//单独购买价格

    @ApiModelProperty(value = "是否自动补充库存 0 否 1是")
    private Integer autoAddStock;//是否自动补充库存 0 否 1是

    @ApiModelProperty(value = "商家经营品类(取关联的id)")
    private String shopCate;//商家经营品类(取关联的id)

    private Integer auditStatus;//审核状态

    private Integer useSkuDetail;//设置商品明细(0,不设置,1,设置)

    private Integer showType;//商品展示类型0，商品列表，1，会员优惠券，2会员套餐

    private List<GoodsSkuDetail> goodsSkuDetails;//商品明细

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

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
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


    @Override
    public String toString() {
        return "GoodsSkuInVo{" +
                "id=" + id +
                ", skuCode='" + skuCode + '\'' +
                ", skuName='" + skuName + '\'' +
                ", skuPic='" + skuPic + '\'' +
                ", costPrice=" + costPrice +
                ", marketPrice=" + marketPrice +
                ", sellPrice=" + sellPrice +
                ", stockNum=" + stockNum +
                ", waringStock=" + waringStock +
                ", spuId=" + spuId +
                ", isDeleted=" + isDeleted +
                ", status=" + status +
                ", shopId=" + shopId +
                ", sellNum=" + sellNum +
                ", unit='" + unit + '\'' +
                ", miniNum=" + miniNum +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", spuType=" + spuType +
                ", sendType=" + sendType +
                ", singlyPrice=" + singlyPrice +
                '}';
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

    public GoodsSkuSpecValue getGoodsSkuSpecValue() {
        return goodsSkuSpecValue;
    }

    public void setGoodsSkuSpecValue(GoodsSkuSpecValue goodsSkuSpecValue) {
        this.goodsSkuSpecValue = goodsSkuSpecValue;
    }

    public GoodsSpuInVo getGoodsSpuInVo() {
        return goodsSpuInVo;
    }

    public void setGoodsSpuInVo(GoodsSpuInVo goodsSpuInVo) {
        this.goodsSpuInVo = goodsSpuInVo;
    }

    public List<ActGoodsSkuInVo> getActGoodsSkuInVos() {
        return actGoodsSkuInVos;
    }

    public void setActGoodsSkuInVos(List<ActGoodsSkuInVo> actGoodsSkuInVos) {
        this.actGoodsSkuInVos = actGoodsSkuInVos;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getBrowSort() {
        return browSort;
    }

    public void setBrowSort(Integer browSort) {
        this.browSort = browSort;
    }

    public Integer getGroupState() {
        return groupState;
    }

    public void setGroupState(Integer groupState) {
        this.groupState = groupState;
    }

    public DeliveryTemplateInVo getDeliveryTemplateInVo() {
        return deliveryTemplateInVo;
    }

    public void setDeliveryTemplateInVo(DeliveryTemplateInVo deliveryTemplateInVo) {
        this.deliveryTemplateInVo = deliveryTemplateInVo;
    }

    public BigDecimal getRealWeight() {
        return realWeight;
    }

    public void setRealWeight(BigDecimal realWeight) {
        this.realWeight = realWeight;
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

    @Override
    public String getSearchKey() {
        return searchKey;
    }

    @Override
    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getShopZoneCity() {
        return shopZoneCity;
    }

    public void setShopZoneCity(String shopZoneCity) {
        this.shopZoneCity = shopZoneCity;
    }

    public String getShopZoneItem() {
        return shopZoneItem;
    }

    public void setShopZoneItem(String shopZoneItem) {
        this.shopZoneItem = shopZoneItem;
    }

    public Long getShopZoneItemId() {
        return shopZoneItemId;
    }

    public void setShopZoneItemId(Long shopZoneItemId) {
        this.shopZoneItemId = shopZoneItemId;
    }

    public Integer getRankingByVoteNum() {
        return rankingByVoteNum;
    }

    public void setRankingByVoteNum(Integer rankingByVoteNum) {
        this.rankingByVoteNum = rankingByVoteNum;
    }

    public String getGoodsSkuPics() {
        return goodsSkuPics;
    }

    public void setGoodsSkuPics(String goodsSkuPics) {
        this.goodsSkuPics = goodsSkuPics;
    }

    public SalePointInVo getSalePointInVo() {
        return salePointInVo;
    }

    public void setSalePointInVo(SalePointInVo salePointInVo) {
        this.salePointInVo = salePointInVo;
    }

    public Long getNotCategoryId() {
        return notCategoryId;
    }

    public void setNotCategoryId(Long notCategoryId) {
        this.notCategoryId = notCategoryId;
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

    public String getShopCate() {
        return shopCate;
    }

    public void setShopCate(String shopCate) {
        this.shopCate = shopCate;
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

    public List<GoodsSkuDetail> getGoodsSkuDetails() {
        return goodsSkuDetails;
    }

    public void setGoodsSkuDetails(List<GoodsSkuDetail> goodsSkuDetails) {
        this.goodsSkuDetails = goodsSkuDetails;
    }

    public Integer getShowType() {
        return showType;
    }

    public void setShowType(Integer showType) {
        this.showType = showType;
    }
}
