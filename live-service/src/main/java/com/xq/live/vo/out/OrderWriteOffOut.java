package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xq.live.model.GoodsSku;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商城系统订单票券使用情况表出参
 */
public class OrderWriteOffOut {
    private Long id;

    private Long orderId;

    private Long shopId;

    private String shopName;

    private Long orderCouponId;

    private String orderCouponCode;

    private Long orderAddressId;

    private Long goodsSkuId;

    private BigDecimal couponAmount;

    private BigDecimal sendAmount;

    private Long userId;

    private String userName;

    private Long changerId;

    private String changerName;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private String sendTime;//期望配送日期

    private String remark;//备注

    private BigDecimal serviceAmount;//服务费

    private BigDecimal shopServiceAmount;//商家服务费

    private BigDecimal userServiceAmount;//用户服务费

    private BigDecimal realUnitPrice;//票券实际支付单价

    private BigDecimal realShopUnitPrice;//真实商家营业额单价(realUnitPrice - serviceAmount)

    private Integer isBill;//是否结算

    private Integer isDui;//是否对账

    private GoodsSku goodsSku;//商品信息

    private OrderCouponOut orderCouponOut;//票券详情

    private BigDecimal totalPrice;//总营业额

    private BigDecimal totalService;//总服务费

    private BigDecimal totalNoService;//未结算的总服务费

    private BigDecimal totalShopPrice;//商家端用户订单总营业额

    private Integer totalItem;//核销数目

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDoTime;//开始时间

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDoTime;//结束时间

    private Long salepointId;

    private String showCode;//展示的票券编号

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
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

    public Long getOrderAddressId() {
        return orderAddressId;
    }

    public void setOrderAddressId(Long orderAddressId) {
        this.orderAddressId = orderAddressId;
    }

    public Long getGoodsSkuId() {
        return goodsSkuId;
    }

    public void setGoodsSkuId(Long goodsSkuId) {
        this.goodsSkuId = goodsSkuId;
    }

    public BigDecimal getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public Long getChangerId() {
        return changerId;
    }

    public void setChangerId(Long changerId) {
        this.changerId = changerId;
    }

    public String getChangerName() {
        return changerName;
    }

    public void setChangerName(String changerName) {
        this.changerName = changerName == null ? null : changerName.trim();
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

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getServiceAmount() {
        return serviceAmount;
    }

    public void setServiceAmount(BigDecimal serviceAmount) {
        this.serviceAmount = serviceAmount;
    }

    public BigDecimal getRealUnitPrice() {
        return realUnitPrice;
    }

    public void setRealUnitPrice(BigDecimal realUnitPrice) {
        this.realUnitPrice = realUnitPrice;
    }

    public Integer getIsBill() {
        return isBill;
    }

    public void setIsBill(Integer isBill) {
        this.isBill = isBill;
    }

    public Integer getIsDui() {
        return isDui;
    }

    public void setIsDui(Integer isDui) {
        this.isDui = isDui;
    }

    public GoodsSku getGoodsSku() {
        return goodsSku;
    }

    public void setGoodsSku(GoodsSku goodsSku) {
        this.goodsSku = goodsSku;
    }

    public OrderCouponOut getOrderCouponOut() {
        return orderCouponOut;
    }

    public void setOrderCouponOut(OrderCouponOut orderCouponOut) {
        this.orderCouponOut = orderCouponOut;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalService() {
        return totalService;
    }

    public void setTotalService(BigDecimal totalService) {
        this.totalService = totalService;
    }

    public BigDecimal getTotalNoService() {
        return totalNoService;
    }

    public void setTotalNoService(BigDecimal totalNoService) {
        this.totalNoService = totalNoService;
    }

    public Date getStartDoTime() {
        return startDoTime;
    }

    public void setStartDoTime(Date startDoTime) {
        this.startDoTime = startDoTime;
    }

    public Date getEndDoTime() {
        return endDoTime;
    }

    public void setEndDoTime(Date endDoTime) {
        this.endDoTime = endDoTime;
    }

    public Long getSalepointId() {
        return salepointId;
    }

    public void setSalepointId(Long salepointId) {
        this.salepointId = salepointId;
    }

    public BigDecimal getRealShopUnitPrice() {
        return realShopUnitPrice;
    }

    public void setRealShopUnitPrice(BigDecimal realShopUnitPrice) {
        this.realShopUnitPrice = realShopUnitPrice;
    }

    public String getShowCode() {
        return showCode;
    }

    public void setShowCode(String showCode) {
        this.showCode = showCode;
    }

    public BigDecimal getShopServiceAmount() {
        return shopServiceAmount;
    }

    public void setShopServiceAmount(BigDecimal shopServiceAmount) {
        this.shopServiceAmount = shopServiceAmount;
    }

    public BigDecimal getUserServiceAmount() {
        return userServiceAmount;
    }

    public void setUserServiceAmount(BigDecimal userServiceAmount) {
        this.userServiceAmount = userServiceAmount;
    }

    public BigDecimal getTotalShopPrice() {
        return totalShopPrice;
    }

    public void setTotalShopPrice(BigDecimal totalShopPrice) {
        this.totalShopPrice = totalShopPrice;
    }

    public Integer getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(Integer totalItem) {
        this.totalItem = totalItem;
    }
}
