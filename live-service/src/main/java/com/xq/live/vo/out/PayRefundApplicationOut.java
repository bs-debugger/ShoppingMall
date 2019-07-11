package com.xq.live.vo.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.xq.live.model.OrderItem;
import com.xq.live.model.Shop;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Table(name = "pay_refund_application")
public class PayRefundApplicationOut {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "订单号")
    @Excel(name="订单号",orderNum = "0",width=15)
    private String outTradeNo;//订单号

    @ApiModelProperty(value = "退款单号")
    private String outRefundNo;//退款单号

    @Excel(name="订单类型",orderNum = "2",replace = { "订单_1", "商城订单_2", "商城券_3" })
    @ApiModelProperty(value = "订单类型")
    private Integer orderType;

    @Excel(name="订单支付金额",orderNum = "3")
    @ApiModelProperty(value = "订单付款金额")
    private BigDecimal totalFee;//订单付款金额

    @ApiModelProperty(value = "申请退款金额")
    private BigDecimal refundFee;//申请退款金额

    @ApiModelProperty(value = "支付方式")
    @Excel(name="支付类型",orderNum = "8",replace = { "免费赠送_0", "享七金币支付_1", "微信支付_2","支付宝支付_3","余额支付_4"})
    private Integer payType;

    @ApiModelProperty(value = "申请原因")
    @Excel(name="申请原因",orderNum = "9")
    private String applyReason;//申请原因

    @Excel(name="审核状态",orderNum = "5",replace = { "待审批_0", "审批通过_1", "审批不通过_2"})
    @ApiModelProperty(value = "审核状态 0 待审批 1 审批通过 2审批不通过")
    private Integer status;//审核状态 0 待审批 1 审批通过 2审批不通过

    @ApiModelProperty(value = "审核备注")
    private String remarks;//审核备注

    @ApiModelProperty(value = "退款状态0 退款成功 1 退款异常 2 退款关闭")
    private Integer refundStatus;//退款状态0 退款成功 1 退款异常 2 退款关闭

    @Excel(name="提交申请时间",orderNum = "1",format = "yyyy-mm-dd hh:mm:ss")
    private Date createTime;

    private Date updateTime;

    @ApiModelProperty(value = "实际退款金额")
    private BigDecimal settlementRefundFee;//实际退款金额

    private Date refundTime;

    @Excel(name="申请来源",orderNum = "6",replace = { "小程序_1", "app_2"})
    private Integer type;

    private Long userId;

    @Excel(name="其他原因",orderNum = "10")
    @ApiModelProperty(value = "其他原因")
    private String otherReason;//其他原因

    @Excel(name="用户账号",orderNum = "4")
    @ApiModelProperty(value = "用户名")
    @Transient
    private String userName;//用户名

    @Transient
    @ApiModelProperty(value = "商铺名对象")
    private Shop shop;

    @Excel(name="所属门店",orderNum = "7")
    private String shopNameTo;

    public String getShopNameTo() {
        return shopNameTo;
    }

    public void setShopNameTo(String shopNameTo) {
        this.shopNameTo = shopNameTo;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Transient
    private List<OrderItem> orderItems;

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public BigDecimal getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(BigDecimal refundFee) {
        this.refundFee = refundFee;
    }

    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
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

    public BigDecimal getSettlementRefundFee() {
        return settlementRefundFee;
    }

    public void setSettlementRefundFee(BigDecimal settlementRefundFee) {
        this.settlementRefundFee = settlementRefundFee;
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOtherReason() {
        return otherReason;
    }

    public void setOtherReason(String otherReason) {
        this.otherReason = otherReason;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }
}
