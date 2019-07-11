package com.xq.live.vo.out;

import com.xq.live.model.OrderItem;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

public class OrderListOut {

    @ApiModelProperty(value = "主键ID")
    private Integer id;

    @ApiModelProperty(value = "订单编号")
    private String orderCode;

    @ApiModelProperty(value = "电话号码")
    private String mobile;

    @ApiModelProperty(value = "支付方式 0免费赠送  1享七金币支付 2微信支付 3支付宝支付 4余额支付")
    private Integer payType;

    @ApiModelProperty(value = "订单金额")
    private Integer skuAmount;

    @ApiModelProperty(value = "下单人")
    private String userName;

    @ApiModelProperty(value = "商家店铺")
    private String shopName;

    @ApiModelProperty(value = "订单状态")
    private Integer orderType;

    @ApiModelProperty(value = "下单时间")
    private Date createTime;

    @Transient
    private List<OrderItem> orderItems;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getSkuAmount() {
        return skuAmount;
    }

    public void setSkuAmount(Integer skuAmount) {
        this.skuAmount = skuAmount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
