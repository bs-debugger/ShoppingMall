package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xq.live.model.OrderCoupon;
import com.xq.live.model.SalePoint;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商城系统订单相关出参
 * Created by lipeng on 2018/9/4.
 */
public class OrderInfoOut {
    private Long id;

    private String orderCode;

    private Long userId;

    private String userName;//下单人

    private Long shopId;

    private Long orderAddressId;

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

    private BigDecimal marketPrice;//市场价

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

    private Integer itemTotal;//商品的总数

    private List<OrderItemOut> orderItemOuts;//订单里面包含的详情

    private String shopName;//商家名字

    private String expressCode;//快递单号

    private BigDecimal duiPrice;//对账的所有实际营业额

    private BigDecimal noDuiPrice;//未对账的所有实际营业额

    private BigDecimal allPrice;//所有实际营业额

    private OrderAddressOut orderAddressOut;//收货地址

    private BigDecimal deliveryFee; //兑换支付的运费

    private SalePoint salePoint;//销售点

    private Integer flagType;//标记类型(1普通型  4砍价型  5秒杀型)

    private Long actId;//活动id

    private List<OrderInfoOut> orderInfoOuts;//子订单列表

    private ActOrderOut actOrderOut;

    private Integer sourceType;//1平台订单 2商家订单

    private Integer isDui;//0未对账1对账

    private Integer createType;

    private Integer isOrderInvoice;//是否有开发票的权限

    private  Integer isPayRefund;//订单是否能退款0 可以退款，1不能退款

    private BigDecimal accountAmount;//钱包支付金额

    private List<OrderCoupon> orderCoupons;//订单所对应的票券列表

    private BigDecimal allShopOrderPrice;//商家端商家订单总营业额

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

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

    public Integer getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(Integer itemTotal) {
        this.itemTotal = itemTotal;
    }

    public List<OrderItemOut> getOrderItemOuts() {
        return orderItemOuts;
    }

    public void setOrderItemOuts(List<OrderItemOut> orderItemOuts) {
        this.orderItemOuts = orderItemOuts;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getExpressCode() {
        return expressCode;
    }

    public void setExpressCode(String expressCode) {
        this.expressCode = expressCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public OrderAddressOut getOrderAddressOut() {
        return orderAddressOut;
    }

    public void setOrderAddressOut(OrderAddressOut orderAddressOut) {
        this.orderAddressOut = orderAddressOut;
    }

    public SalePoint getSalePoint() {
        return salePoint;
    }

    public void setSalePoint(SalePoint salePoint) {
        this.salePoint = salePoint;
    }

    public List<OrderInfoOut> getOrderInfoOuts() {
        return orderInfoOuts;
    }

    public void setOrderInfoOuts(List<OrderInfoOut> orderInfoOuts) {
        this.orderInfoOuts = orderInfoOuts;
    }

    public ActOrderOut getActOrderOut() {
        return actOrderOut;
    }

    public void setActOrderOut(ActOrderOut actOrderOut) {
        this.actOrderOut = actOrderOut;
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

    public BigDecimal getDuiPrice() {
        return duiPrice;
    }

    public void setDuiPrice(BigDecimal duiPrice) {
        this.duiPrice = duiPrice;
    }

    public BigDecimal getNoDuiPrice() {
        return noDuiPrice;
    }

    public void setNoDuiPrice(BigDecimal noDuiPrice) {
        this.noDuiPrice = noDuiPrice;
    }

    public BigDecimal getAllPrice() {
        return allPrice;
    }

    public void setAllPrice(BigDecimal allPrice) {
        this.allPrice = allPrice;
    }

    public Integer getCreateType() {
        return createType;
    }

    public void setCreateType(Integer createType) {
        this.createType = createType;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Integer getIsOrderInvoice() {
        return isOrderInvoice;
    }

    public void setIsOrderInvoice(Integer isOrderInvoice) {
        this.isOrderInvoice = isOrderInvoice;
    }

    public Integer getIsPayRefund() {
        return isPayRefund;
    }

    public void setIsPayRefund(Integer isPayRefund) {
        this.isPayRefund = isPayRefund;
    }

    public BigDecimal getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(BigDecimal accountAmount) {
        this.accountAmount = accountAmount;
    }

    public List<OrderCoupon> getOrderCoupons() {
        return orderCoupons;
    }

    public void setOrderCoupons(List<OrderCoupon> orderCoupons) {
        this.orderCoupons = orderCoupons;
    }

    public BigDecimal getAllShopOrderPrice() {
        return allShopOrderPrice;
    }

    public void setAllShopOrderPrice(BigDecimal allShopOrderPrice) {
        this.allShopOrderPrice = allShopOrderPrice;
    }
}
