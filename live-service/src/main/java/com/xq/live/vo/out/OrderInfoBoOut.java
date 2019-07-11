package com.xq.live.vo.out;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xq.live.model.OrderInfo;


import java.math.BigDecimal;
import java.util.Date;

public class OrderInfoBoOut {
    private OrderInfo orderInfo;

    private Long id;

    private Integer payType;

    private BigDecimal accountAmount;//钱包支付金额

    private Long userId;

    private String orderCode;//订单编号

    public OrderInfoBoOut() {
        this.orderInfo = new OrderInfo();
    }

    public OrderInfoBoOut(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    @JsonIgnore
    public OrderInfo getOrderInfo() {
        return this.orderInfo;
    }

    public Long getId() {
        return this.orderInfo.getId();
    }

    public void setId(Long id) {
        this.orderInfo.setId(id);
    }

    public Date getCreateTime() {
        return this.orderInfo.getCreateTime();
    }

    public void setCreateTime(Date createTime) {
        this.orderInfo.setCreateTime(createTime);
    }

    public Date getUpdateTime() {
        return this.orderInfo.getUpdateTime();
    }

    public void setUpdateTime(Date updateTime) {
        this.orderInfo.setUpdateTime(updateTime);
    }

    public String getOrderCode() {
        return this.orderInfo.getOrderCode();
    }

    public void setOrderCode(String orderCode) {
        this.orderInfo.setOrderCode(orderCode);
    }

    public Long getUserId() {
        return this.orderInfo.getUserId();
    }

    public void setUserId(Long userId) {
        this.orderInfo.setUserId(userId);
    }

    public Long getShopId() {
        return this.orderInfo.getShopId();
    }

    public void setShopId(Long shopId) {
        this.orderInfo.setShopId(shopId);
    }

    public Long getOrderAddressId() {
        return this.orderInfo.getOrderAddressId();
    }

    public void setOrderAddressId(Long orderAddressId) {
        this.orderInfo.setOrderAddressId(orderAddressId);
    }

    public Integer getSendType() {
        return this.orderInfo.getSendType();
    }

    public void setSendType(Integer sendType) {
        this.orderInfo.setSendType(sendType);
    }

    public Integer getPayType() {
        return this.orderInfo.getPayType();
    }

    public void setPayType(Integer payType) {
        this.orderInfo.setPayType(payType);
    }

    public BigDecimal getSkuAmount() {
        return this.orderInfo.getSkuAmount();
    }

    public void setSkuAmount(BigDecimal skuAmount) {
        this.orderInfo.setSkuAmount(skuAmount);
    }

    public String getSendTime() {
        return this.orderInfo.getSendTime();
    }

    public void setSendTime(String sendTime) {
        this.orderInfo.setSendTime(sendTime);
    }

    public BigDecimal getSendAmount() {
        return this.orderInfo.getSendAmount();
    }

    public void setSendAmount(BigDecimal sendAmount) {
        this.orderInfo.setSendAmount(sendAmount);
    }

    public BigDecimal getRealAmount() {
        return this.orderInfo.getRealAmount();
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.orderInfo.setRealAmount(realAmount);
    }

    public Integer getStatus() {
        return this.orderInfo.getStatus();
    }

    public void setStatus(Integer status) {
        this.orderInfo.setStatus(status);
    }

    public Date getPaidTime() {
        return this.orderInfo.getPaidTime();
    }

    public void setPaidTime(Date paidTime) {
        this.orderInfo.setPaidTime(paidTime);
    }

/*    public Integer getStype() {
        return this.orderInfo.getStype();
    }

    public void setStype(Integer stype) {
        this.orderInfo.setStype(stype);
    }

    public OrderItem getOrderItem() {
        return this.orderInfo.getOrderItem();
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderInfo.setOrderItem(orderItem);
    }

    public GoodsSku getGoodsSku() {
        return this.orderInfo.getGoodsSku();
    }

    public void setGoodsSku(GoodsSku goodsSku) {
        this.orderInfo.setGoodsSku(goodsSku);
    }

    public Long getCategoryId() {
        return this.orderInfo.getCategoryId();
    }

    public void setCategoryId(Long categoryId) {
        this.orderInfo.setCategoryId(categoryId);
    }*/

    public BigDecimal getAccountAmount() {
        return this.orderInfo.getAccountAmount();
    }

    public void setAccountAmount(BigDecimal accountAmount){
        this.orderInfo.setAccountAmount(accountAmount);
    }

/*    public SalePoint getSalePoint() {
        return this.orderInfo.getSalePoint();
    }

    public void setSalePoint(SalePoint salePoint) {
        this.orderInfo.setSalePoint(salePoint);
    }*/

}
