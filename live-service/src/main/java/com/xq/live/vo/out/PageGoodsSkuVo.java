package com.xq.live.vo.out;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author xuzhen
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2019/4/289:29
 */
public class PageGoodsSkuVo implements Serializable {

    private Long id;

    private String shopName;

    private String shopType;

    private String  skuName;

    private String skuType;

    private BigDecimal marketPrice;

    private BigDecimal sellPrice;//门市价

    private BigDecimal singlyPrice;//单买价

    private BigDecimal spikePrice;//秒杀价

    private BigDecimal actPrice;//低价

    private String updateTime;//提交审核时间

    private String skuPic;

    private String content;

    private String goodsSkuPics;

    private List<String> goodsSkuPicsList;

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
        this.shopName = shopName;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
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

    public String getSkuType() {
        return skuType;
    }

    public void setSkuType(String skuType) {
        this.skuType = skuType;
    }

    public BigDecimal getSinglyPrice() {
        return singlyPrice;
    }

    public void setSinglyPrice(BigDecimal singlyPrice) {
        this.singlyPrice = singlyPrice;
    }

    public BigDecimal getSpikePrice() {
        return spikePrice;
    }

    public void setSpikePrice(BigDecimal spikePrice) {
        this.spikePrice = spikePrice;
    }

    public BigDecimal getActPrice() {
        return actPrice;
    }

    public void setActPrice(BigDecimal actPrice) {
        this.actPrice = actPrice;
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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "PageGoodsSkuVo{" +
                "id=" + id +
                ", shopName='" + shopName + '\'' +
                ", shopType='" + shopType + '\'' +
                ", skuName='" + skuName + '\'' +
                ", skuType='" + skuType + '\'' +
                ", marketPrice=" + marketPrice +
                ", sellPrice=" + sellPrice +
                ", singlyPrice=" + singlyPrice +
                ", spikePrice=" + spikePrice +
                ", actPrice=" + actPrice +
                ", updateTime='" + updateTime + '\'' +
                ", skuPic='" + skuPic + '\'' +
                ", content='" + content + '\'' +
                ", goodsSkuPics='" + goodsSkuPics + '\'' +
                ", goodsSkuPicsList=" + goodsSkuPicsList +
                '}';
    }
}
