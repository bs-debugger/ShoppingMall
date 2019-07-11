package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 购物车项出参
 * Created by lipeng on 2018/10/4.
 */
public class OrderCartOut {
    /**
     * 是否选中   0未选中  1已选中
     */
    public final static int ORDER_CART_NO_CHECKED = 0;

    public final static int ORDER_CART_IS_CHECKED = 1;

    private Long id;

    private Long goodsSkuId;

    private Long goodsSpuId;

    private Long userId;

    private Long shopId;

    private String goodsSkuName;

    private Integer num;//购买的数量,不含有赠送的数量,赠送数量要通过具体的业务逻辑来计算

    private BigDecimal price;

    private Integer isDeleted;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private Integer isChecked;//是否被选中

    private GoodsSkuOut goodsSkuOut;//商品详情

    private String errorMessage;//商品查询的过程中出现的报错信息

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodsSkuId() {
        return goodsSkuId;
    }

    public void setGoodsSkuId(Long goodsSkuId) {
        this.goodsSkuId = goodsSkuId;
    }

    public Long getGoodsSpuId() {
        return goodsSpuId;
    }

    public void setGoodsSpuId(Long goodsSpuId) {
        this.goodsSpuId = goodsSpuId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
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

    public GoodsSkuOut getGoodsSkuOut() {
        return goodsSkuOut;
    }

    public void setGoodsSkuOut(GoodsSkuOut goodsSkuOut) {
        this.goodsSkuOut = goodsSkuOut;
    }

    public Integer getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Integer isChecked) {
        this.isChecked = isChecked;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderCartOut)) return false;

        OrderCartOut that = (OrderCartOut) o;

        return getGoodsSkuId().equals(that.getGoodsSkuId());
    }

    @Override
    public int hashCode() {
        return getGoodsSkuId().hashCode();
    }
}
