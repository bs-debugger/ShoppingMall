package com.xq.live.vo.in;

import com.xq.live.model.GoodsSku;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class BusinessListingsInVo extends BaseInVo {

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "用户ID")
    private String CashierId;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "入驻手机号")
    private String mobile;

    @ApiModelProperty(value = "所在城市")
    private String address;

    @ApiModelProperty(value = "管理员手机号")
    private String adminMobile;

    @ApiModelProperty(value = "核销员")
    private List<String> writeOffClerk;

    @ApiModelProperty(value = "店铺主图")
    private String logoUrl;

    @ApiModelProperty(value = "开始时间")
    private String beginTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;//结束时间

    @ApiModelProperty(value = "查询条件")
    private String keywords;

    @ApiModelProperty(value = "所属专区")
    private String position;

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

    @ApiModelProperty(value = "详情图片")
    private List<GoodsSku> goodsSkuPicsList;

    @ApiModelProperty(value = "1.删除2.新增")
    private Integer type;

    @ApiModelProperty(value = "缩略图url")
    private String smallPidcUrl;

    @ApiModelProperty(value = "原图url")
    private String picUrl;

    @ApiModelProperty(value = "attachementId")
    private Integer attachementId;

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<GoodsSku> getGoodsSkuPicsList() {
        return goodsSkuPicsList;
    }

    public void setGoodsSkuPicsList(List<GoodsSku> goodsSkuPicsList) {
        this.goodsSkuPicsList = goodsSkuPicsList;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAdminMobile() {
        return adminMobile;
    }

    public void setAdminMobile(String adminMobile) {
        this.adminMobile = adminMobile;
    }

    public List<String> getWriteOffClerk() {
        return writeOffClerk;
    }

    public void setWriteOffClerk(List<String> writeOffClerk) {
        this.writeOffClerk = writeOffClerk;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCashierId() {
        return CashierId;
    }

    public void setCashierId(String cashierId) {
        CashierId = cashierId;
    }

    public String getSmallPidcUrl() {
        return smallPidcUrl;
    }

    public void setSmallPidcUrl(String smallPidcUrl) {
        this.smallPidcUrl = smallPidcUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Integer getAttachementId() {
        return attachementId;
    }

    public void setAttachementId(Integer attachementId) {
        this.attachementId = attachementId;
    }
}
