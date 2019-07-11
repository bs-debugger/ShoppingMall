package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xq.live.model.GoodsSku;
import com.xq.live.model.ShopCategory;
import com.xq.live.model.ShopZoneItem;
import org.javatuples.Pair;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**商家返回列表包含推荐菜实体类
 * Created by lipeng on 2018/3/14.
 */
public class ShopOut implements Comparable<ShopOut>, Serializable {

    private static final long serialVersionUID = 7385757746267444500L;

    //用于记录首页查询的条数
    private Integer homeTotle;

    private Long id;
    @NotNull(message = "shopName必填")
    private String shopName;

    private String address;

    private String mobile;

    private String phone;

    private String indexUrl;

    private String logoUrl;

    private String shopInfo;

    private BigDecimal locationX;

    private BigDecimal locationY;

    private Integer isDeleted;

    private Integer popNum;//人气

    private String remark;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @NotNull(message = "userId必填")
    private Long userId;       //店铺关联的账号id

    private String skuName;//推荐菜

    private Integer distance;    //距离

    private List<Pair<String, String>> shopTopPics;

    private Integer shopStatus;//店铺状态

    private Integer applyStatus;//审批状态

    private String businessCate;//经营品类

    private List<String> ruleDescs;//商家规则列

    private String shopCode;//商家编码

    private String shopHours;//商家营业时间

    private String otherService;//其他服务

    private String city;

    private Long parentId;//总店id，总店没有

    private Integer isSub;//是否是分店

    private List<SkuForTscOut> skuForTscOuts;

    private String smallIndexUrl;

    private String smallLogoUrl;

    private String shopCate;//商家经营品类(取关联的id)

    private GoodsSku goodsSku;

    private List<ShopCategory> shopCategories;//商家经营品类详情列表

    private Long shopZoneItemId;

    private ShopZoneItem shopZoneItem;

    private ShopAllocationOut shopAllocationOut;//收款方式

    private Boolean isCollected;//否已经收藏

    private GoodsSkuOut goodsSkuOut;

    private String shopCateName;//经营品类

    private String position; // 活动布局

    private Integer activityLayoutId;  // 活动布局ID

    private List<GoodsSkuOut> goodsSkuList;//商家商品列表主要信息

    public Integer getHomeTotle() {
        return homeTotle;
    }

    public void setHomeTotle(Integer homeTotle) {
        this.homeTotle = homeTotle;
    }

    public String getSmallIndexUrl() {
        return smallIndexUrl;
    }

    public void setSmallIndexUrl(String smallIndexUrl) {
        this.smallIndexUrl = smallIndexUrl;
    }

    public String getSmallLogoUrl() {
        return smallLogoUrl;
    }

    public void setSmallLogoUrl(String smallLogoUrl) {
        this.smallLogoUrl = smallLogoUrl;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getIsSub() {
        return isSub;
    }

    public void setIsSub(Integer isSub) {
        this.isSub = isSub;
    }


    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl == null ? null : indexUrl.trim();
    }

    public String getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(String shopInfo) {
        this.shopInfo = shopInfo == null ? null : shopInfo.trim();
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


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getPopNum() {
        return popNum;
    }

    public void setPopNum(Integer popNum) {
        this.popNum = popNum;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    @Override
    public int compareTo(ShopOut o) {
        int a = this.getPopNum();
        int b = o.getPopNum();
        if(a>b){
            return 1;
        }
        return -1;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public List<Pair<String, String>> getShopTopPics() {
        return shopTopPics;
    }

    public void setShopTopPics(List<Pair<String, String>> shopTopPics) {
        this.shopTopPics = shopTopPics;
    }

    public Integer getShopStatus() {
        return shopStatus;
    }

    public void setShopStatus(Integer shopStatus) {
        this.shopStatus = shopStatus;
    }

    public Integer getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Integer applyStatus) {
        this.applyStatus = applyStatus;
    }

    public String getBusinessCate() {
        return businessCate;
    }

    public void setBusinessCate(String businessCate) {
        this.businessCate = businessCate;
    }

    public List<String> getRuleDescs() {
        return ruleDescs;
    }

    public void setRuleDescs(List<String> ruleDescs) {
        this.ruleDescs = ruleDescs;
    }

    public String getShopHours() {
        return shopHours;
    }

    public void setShopHours(String shopHours) {
        this.shopHours = shopHours;
    }

    public String getOtherService() {
        return otherService;
    }

    public void setOtherService(String otherService) {
        this.otherService = otherService;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<SkuForTscOut> getSkuForTscOuts() {
        return skuForTscOuts;
    }

    public void setSkuForTscOuts(List<SkuForTscOut> skuForTscOuts) {
        this.skuForTscOuts = skuForTscOuts;
    }

    public String getShopCate() {
        return shopCate;
    }

    public void setShopCate(String shopCate) {
        this.shopCate = shopCate;
    }

    public List<ShopCategory> getShopCategories() {
        return shopCategories;
    }

    public void setShopCategories(List<ShopCategory> shopCategories) {
        this.shopCategories = shopCategories;
    }

    public GoodsSku getGoodsSku() {
        return goodsSku;
    }

    public void setGoodsSku(GoodsSku goodsSku) {
        this.goodsSku = goodsSku;
    }

    public Long getShopZoneItemId() {
        return shopZoneItemId;
    }

    public void setShopZoneItemId(Long shopZoneItemId) {
        this.shopZoneItemId = shopZoneItemId;
    }

    public ShopZoneItem getShopZoneItem() {
        return shopZoneItem;
    }

    public void setShopZoneItem(ShopZoneItem shopZoneItem) {
        this.shopZoneItem = shopZoneItem;
    }

    public Boolean getCollected() {
        return isCollected;
    }

    public void setCollected(Boolean collected) {
        isCollected = collected;
    }

    public ShopAllocationOut getShopAllocationOut() {
        return shopAllocationOut;
    }

    public void setShopAllocationOut(ShopAllocationOut shopAllocationOut) {
        this.shopAllocationOut = shopAllocationOut;
    }

    public GoodsSkuOut getGoodsSkuOut() {
        return goodsSkuOut;
    }

    public void setGoodsSkuOut(GoodsSkuOut goodsSkuOut) {
        this.goodsSkuOut = goodsSkuOut;
    }

    public String getShopCateName() {
        return shopCateName;
    }

    public void setShopCateName(String shopCateName) {
        this.shopCateName = shopCateName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getActivityLayoutId() {
        return activityLayoutId;
    }

    public void setActivityLayoutId(Integer activityLayoutId) {
        this.activityLayoutId = activityLayoutId;
    }

    public List<GoodsSkuOut> getGoodsSkuList() {
        return goodsSkuList;
    }

    public void setGoodsSkuList(List<GoodsSkuOut> goodsSkuList) {
        this.goodsSkuList = goodsSkuList;
    }
}
