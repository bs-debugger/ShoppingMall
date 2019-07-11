package com.xq.live.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

public class OrderItem {
    private Long id;

    private String orderCode;

    private Long goodsSpuId;

    private Long goodsSkuId;

    private Long shopId;

    @ApiModelProperty(value = "商品名称")
    private String goodsSkuName;

    private Integer goodsNum;

    private Integer giftNum;

    @ApiModelProperty(value = "商品金额")
    private BigDecimal goodsPrice;

    private BigDecimal packingPrice;

    private BigDecimal realUnitPrice;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private String remark;

    @ApiModelProperty(value = "订单状态 0否1是")
    private String usageState;//0否1是

    @ApiModelProperty(value = "核销时间")
    private Date verificationTime;//核销时间

    /**
     * 具体商品对应商家的shopId,因为orderInfo里面也有个shopId表示列表显示的shopId，为了不与orderItem里的shopId重复，
     * 所以这里取一个别名
     *
     * 实际上好像可以不用，只要前端的数据是json格式，可以不用取别名，但是没测试，懒得管
     */
    private Long orderItemShopId;

    private Integer sendType;

    private String sendTime;

    private Long orderAddressId;

    private Long salepointId;//销售点id

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

    public String getUsageState() {
        return usageState;
    }

    public void setUsageState(String usageState) {
        this.usageState = usageState;
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

    public Long getOrderItemShopId() {
        return orderItemShopId;
    }

    public void setOrderItemShopId(Long orderItemShopId) {
        this.orderItemShopId = orderItemShopId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public Long getOrderAddressId() {
        return orderAddressId;
    }

    public void setOrderAddressId(Long orderAddressId) {
        this.orderAddressId = orderAddressId;
    }

    public Long getSalepointId() {
        return salepointId;
    }

    public void setSalepointId(Long salepointId) {
        this.salepointId = salepointId;
    }

    public Date getVerificationTime() {
        return verificationTime;
    }

    public void setVerificationTime(Date verificationTime) {
        this.verificationTime = verificationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem)) return false;

        OrderItem item = (OrderItem) o;

        if (!getOrderItemShopId().equals(item.getOrderItemShopId())) return false;
        if (!getSendType().equals(item.getSendType())) return false;
        return !(getSalepointId() != null ? !getSalepointId().equals(item.getSalepointId()) : item.getSalepointId() != null);

    }

    @Override
    public int hashCode() {
        int result = getOrderItemShopId().hashCode();
        result = 31 * result + getSendType().hashCode();
        result = 31 * result + (getSalepointId() != null ? getSalepointId().hashCode() : 0);
        return result;
    }
}
