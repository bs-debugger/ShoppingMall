package com.xq.live.vo.in;

import com.fasterxml.jackson.annotation.JsonFormat;
import groovy.transform.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ss on 2018/9/17.
 * 商品订单
 */
@EqualsAndHashCode(callSuper = false)
public class OrderInfoConditionInVO {
    private Long id;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private String orderCode;//订单编号

    private Long userId;//下单人的user_id

    private Long shopId;//商家id(0代表自营，如果为null的话那就显示享七商城，如果为具体某个值的话为具体某个店)

    private Long orderAddressId;//订单收货地址的id

    private Integer payType;//支付方式 1享七金币支付 2微信支付 3支付宝支付

    private Integer sendType;//配送方式

    private String sendTime;//期望的配送时间(开始时间到结束时间)

    private BigDecimal skuAmount;//商品总额

    private BigDecimal sendAmount;//运费(现在该阶段目前可以不用)

    private BigDecimal realAmount;//实际付款金额

    private Integer status;//订单状态  1待付款  2待收货  3已完成 10取消

    private Integer soStatus;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paidTime;//支付时间

    private Integer stype;//查询条件 1按下单时间查询 2按购买时间查询

    private Integer goodsSpuId;//类型

    private Long categoryId ;//二级类目id



    public Integer getGoodsSpuId() {
        return goodsSpuId;
    }

    public void setGoodsSpuId(Integer goodsSpuId) {
        this.goodsSpuId = goodsSpuId;
    }

    public Integer getStype() {
        return stype;
    }

    public void setStype(Integer stype) {
        this.stype = stype;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
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

    public Long getOrderAddressId() {
        return orderAddressId;
    }

    public void setOrderAddressId(Long orderAddressId) {
        this.orderAddressId = orderAddressId;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public BigDecimal getSkuAmount() {
        return skuAmount;
    }

    public void setSkuAmount(BigDecimal skuAmount) {
        this.skuAmount = skuAmount;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public BigDecimal getSendAmount() {
        return sendAmount;
    }

    public void setSendAmount(BigDecimal sendAmount) {
        this.sendAmount = sendAmount;
    }

    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getPaidTime() {
        return paidTime;
    }

    public void setPaidTime(Date paidTime) {
        this.paidTime = paidTime;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getSoStatus() {
        return soStatus;
    }

    public void setSoStatus(Integer soStatus) {
        this.soStatus = soStatus;
    }

}
