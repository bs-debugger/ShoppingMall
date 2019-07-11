package com.xq.live.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

public class OrderCouponPostage {
    /**
     * 订单状态
     */
    public final static int POSTAGE_STATUS_NOT_PAY = 0;   //未支付
    public final static int POSTAGE_STATUS_HAD_PAY = 1;   //已支付



    /**
     * 是否包邮
     */
    public final static int PASTAGE_FREE_NOT_FREE = 0;   //不包邮
    public final static int PASTAGE_FREE_HAD_FREE = 1;   //包邮

    @ApiModelProperty(value = "运费订单id")
    private Long id;

    @ApiModelProperty(value = "票券id")
    private Long orderCouponId;

    @ApiModelProperty(value = "票券code")
    private String orderCouponCode;

    @ApiModelProperty(value = "商家id")
    private Long shopId;

    @ApiModelProperty(value = "运费")
    private BigDecimal sendAmount;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "票券地址id")
    private Long couponAddressId;

    @ApiModelProperty(value = "订单状态 0未支付 1已支付")
    private Integer status;

    @ApiModelProperty(value = "支付时间")
    private Date paidTime;

    @ApiModelProperty(value = "商户订单号")
    private String outTradeNo;

    @ApiModelProperty(value = "重量")
    private BigDecimal realWeight;

    @ApiModelProperty(value = "体积")
    private BigDecimal bulk;

    @ApiModelProperty(value = "件数")
    private Integer piece;

    @ApiModelProperty(value = "运费模板id")
    private Long templateId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否包邮 0不包邮 1包邮")
    private Integer postFree;

    @ApiModelProperty(value = "期望配送时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderCouponId() {
        return orderCouponId;
    }

    public void setOrderCouponId(Long orderCouponId) {
        this.orderCouponId = orderCouponId;
    }

    public String getOrderCouponCode() {
        return orderCouponCode;
    }

    public void setOrderCouponCode(String orderCouponCode) {
        this.orderCouponCode = orderCouponCode == null ? null : orderCouponCode.trim();
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public BigDecimal getSendAmount() {
        return sendAmount;
    }

    public void setSendAmount(BigDecimal sendAmount) {
        this.sendAmount = sendAmount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCouponAddressId() {
        return couponAddressId;
    }

    public void setCouponAddressId(Long couponAddressId) {
        this.couponAddressId = couponAddressId;
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

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo == null ? null : outTradeNo.trim();
    }

    public BigDecimal getRealWeight() {
        return realWeight;
    }

    public void setRealWeight(BigDecimal realWeight) {
        this.realWeight = realWeight;
    }

    public BigDecimal getBulk() {
        return bulk;
    }

    public void setBulk(BigDecimal bulk) {
        this.bulk = bulk;
    }

    public Integer getPiece() {
        return piece;
    }

    public void setPiece(Integer piece) {
        this.piece = piece;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getPostFree() {
        return postFree;
    }

    public void setPostFree(Integer postFree) {
        this.postFree = postFree;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
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
}