package com.xq.live.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class OrderInfo {
    /**
     * payType 0免费赠送 1享七金币支付 2微信支付 3支付宝支付
     */
    public final static int PAY_TYPE_MFZS = 0;   //免费赠送

    public final static int PAY_TYPE_XQJB = 1;   //享七金币支付

    public final static int PAY_TYPE_WX = 2;   //微信支付

    public final static int PAY_TYPE_ZFB = 3;   //支付宝支付

    public final static int PAY_TYPE_XQYE=4;//余额支付

    /**
     * status 1待付款 2已付款 3待收货  4已完成 10取消
     */
    public final static int STATUS_WAIT_PAID = 1;   //待付款

    public final static int STATUS_WAIT_SH = 2;   //待收货

    public final static int STATUS_IS_SUCCESS = 3;   //已完成

    public final static int STATUS_QX = 10;   //取消

    public final static int STATUS_REFUND_APPICATION = 4;   //退款申请中

    public final static int STATUS_REFUND = 5;   //已退款

    /**
     *是否默认使用  1是 2否 目前是用户抽到红包自动余额
     */
    public final static int SDEFAULT_USED_YES = 1;   //1是

    public final static int SDEFAULT_USED_NO = 2;   //2否

    /**
     * sendType 平台邮购 门店自提
     */
    public final static int SEND_TYPE_PTYG = 1;   //平台邮购

    public final static int SEND_TYPE_MDZT = 2;   //门店自提

    /**
     * 是否是父订单 0子订单 1父订单
     */
    public final static int IS_PARENT_NO = 0;   //子订单

    public final static int IS_PARENT_YES = 1;   //父订单

    /**
     * 订单类型  1实物订单   2虚拟订单
     */
    public final static int ORDER_TYPE_RO = 1;   //实物订单

    public final static int ORDER_TYPE_VO = 2;   //虚拟订单

    /**
     * 分单类型   1分单  2整单
     */
    public final static int SINGLE_TYPE_FD = 1;

    public final static int SINGLE_TYPE_ZD = 2;

    /**
     * 标记类型(1普通型  4砍价型  5秒杀型 6抽奖型 7团购型)
     */
    public final static int FLAG_TYPE_PT = 1;//普通型

    public final static int FLAG_TYPE_KJ = 4;//砍价型

    public final static int FLAG_TYPE_MS = 5;//秒杀型

    public final static int FLAG_TYPE_CG = 6;//抽奖型

    public final static int FLAG_TYPE_TG = 7;//团购型

    public final static int FLAG_TYPE_YHQ =9;//会员优惠券

    public final static int FLAG_TYPE_ZKQ=10;//会员折扣

    /**
     * 订单来源类型  1平台订单  2商家订单
     */
    public final static int SOURCE_TYPE_PT = 1;//平台订单

    public final static int SOURCE_TYPE_SJ = 2;//商家订单

    /**
     * 使用钱包余额支付0 不使用 ，1 使用
     */
    public  final static int USE_ACCOUNT_NO=0;

    public  final static int USE_ACCOUNT_YES=1;

    private Long id;

    private String orderCode;

    @NotNull(message = "userId不能为空")
    private Long userId;

    private String userName;//用户名称

    private Long shopId;

    @NotNull(message = "orderAddressId不能为空")
    private Long orderAddressId;

    @NotNull(message = "payType不能为空")
    private Integer payType;

    private Integer sendType;

    private String sendTime;

    private BigDecimal skuAmount;

    private BigDecimal sendAmount;

    private BigDecimal realAmount;

    private String qrcodeUrl;//订单二维码

    private Long parentOrderId;//父订单编码(如果本身就是父订单 就为0)

    private Integer isParent;//是否是父订单 0子订单 1父订单

    private Integer orderType;//订单类型  1实物订单  2虚拟订单

    private Integer status;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date expiryDate;//有效期

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paidTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private Long salepointId;//销售点

    private Integer singleType;//分单类型 1分单 2整单

    private Integer flagType;//标记类型(1普通型  4砍价型  5秒杀型)

    private Long actId;//活动id

    private Integer sourceType;//1平台订单 2商家订单

    private Integer createType;//

    private Integer isDui;//0未对账1对账

    private BigDecimal accountAmount;//钱包支付金额

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
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
        this.sendTime = sendTime == null ? null : sendTime.trim();
    }

    public BigDecimal getSkuAmount() {
        return skuAmount;
    }

    public void setSkuAmount(BigDecimal skuAmount) {
        this.skuAmount = skuAmount;
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

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl;
    }

    public Long getParentOrderId() {
        return parentOrderId;
    }

    public void setParentOrderId(Long parentOrderId) {
        this.parentOrderId = parentOrderId;
    }

    public Integer getIsParent() {
        return isParent;
    }

    public void setIsParent(Integer isParent) {
        this.isParent = isParent;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPaidTime() {
        return paidTime;
    }

    public void setPaidTime(Date paidTime) {
        this.paidTime = paidTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getSalepointId() {
        return salepointId;
    }

    public void setSalepointId(Long salepointId) {
        this.salepointId = salepointId;
    }

    public Integer getSingleType() {
        return singleType;
    }

    public void setSingleType(Integer singleType) {
        this.singleType = singleType;
    }

    public Integer getFlagType() {
        return flagType;
    }

    public void setFlagType(Integer flagType) {
        this.flagType = flagType;
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public Integer getIsDui() {
        return isDui;
    }

    public void setIsDui(Integer isDui) {
        this.isDui = isDui;
    }

    public Integer getCreateType() {
        return createType;
    }

    public void setCreateType(Integer createType) {
        this.createType = createType;
    }

    public BigDecimal getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(BigDecimal accountAmount) {
        this.accountAmount = accountAmount;
    }
}
