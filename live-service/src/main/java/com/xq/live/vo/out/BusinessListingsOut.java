package com.xq.live.vo.out;

import com.xq.live.model.ShopCashier;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

public class BusinessListingsOut {

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "入驻手机号")
    private String mobile;

    @ApiModelProperty(value = "入驻时间")
    private String settlingTime;

    @ApiModelProperty(value = "今日订单量")
    private String orderToday;

    @ApiModelProperty(value = "历史订单量")
    private String orderHistory;

    @ApiModelProperty(value = "商品名称")
    private String skuName;

    @ApiModelProperty(value = "商品价格")
    private String sellPrice;

    @ApiModelProperty(value = "商品主图")
    private String skuPic;

    @ApiModelProperty(value = "商品描述")
    private String content;

    @ApiModelProperty(value = "详情id逗号拼接")
    private String goodsSkuPics;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "详情图片")
    private List<String> goodsSkuPicsList;

    @ApiModelProperty(value = "管理员手机号")
    private String adminMobile;

    @ApiModelProperty(value = "店铺主图")
    private String logoUrl;

    @ApiModelProperty(value = "所属城市")
    private String city;

    @ApiModelProperty(value = "核销员")
    private List<ShopCashier> shopCashier;

    private List<Map<String,Object>> shopOwnerMap;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSettlingTime() {
        return settlingTime;
    }

    public void setSettlingTime(String settlingTime) {
        this.settlingTime = settlingTime;
    }

    public String getOrderToday() {
        return orderToday;
    }

    public void setOrderToday(String orderToday) {
        this.orderToday = orderToday;
    }

    public String getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(String orderHistory) {
        this.orderHistory = orderHistory;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getSkuPic() {
        return skuPic;
    }

    public void setSkuPic(String skuPic) {
        this.skuPic = skuPic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGoodsSkuPics() {
        return goodsSkuPics;
    }

    public void setGoodsSkuPics(String goodsSkuPics) {
        this.goodsSkuPics = goodsSkuPics;
    }

    public List<String> getGoodsSkuPicsList() {
        return goodsSkuPicsList;
    }

    public void setGoodsSkuPicsList(List<String> goodsSkuPicsList) {
        this.goodsSkuPicsList = goodsSkuPicsList;
    }

    public String getAdminMobile() {
        return adminMobile;
    }

    public void setAdminMobile(String adminMobile) {
        this.adminMobile = adminMobile;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<ShopCashier> getShopCashier() {
        return shopCashier;
    }

    public void setShopCashier(List<ShopCashier> shopCashier) {
        this.shopCashier = shopCashier;
    }

    public List<Map<String, Object>> getShopOwnerMap() {
        return shopOwnerMap;
    }

    public void setShopOwnerMap(List<Map<String, Object>> shopOwnerMap) {
        this.shopOwnerMap = shopOwnerMap;
    }
}
