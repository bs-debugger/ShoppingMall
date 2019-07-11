package com.xq.live.vo.out;

public class ActGoodsSkuRecommendOut {

    private Long actId;

    private Long skuId;

    private String goodsSkuName;

    private String goodsSkuPic;

    private String goodsSmallSkuPic;

    private String sellPrice;

    private Integer stockNum;

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getGoodsSkuName() {
        return goodsSkuName;
    }

    public void setGoodsSkuName(String goodsSkuName) {
        this.goodsSkuName = goodsSkuName;
    }

    public String getGoodsSkuPic() {
        return goodsSkuPic;
    }

    public void setGoodsSkuPic(String goodsSkuPic) {
        this.goodsSkuPic = goodsSkuPic;
    }

    public String getGoodsSmallSkuPic() {
        return goodsSmallSkuPic;
    }

    public void setGoodsSmallSkuPic(String goodsSmallSkuPic) {
        this.goodsSmallSkuPic = goodsSmallSkuPic;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Integer getStockNum() {
        return stockNum;
    }

    public void setStockNum(Integer stockNum) {
        this.stockNum = stockNum;
    }
}
