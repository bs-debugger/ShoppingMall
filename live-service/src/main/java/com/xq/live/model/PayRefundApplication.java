package com.xq.live.model;

import java.math.BigDecimal;
import java.util.Date;

public class PayRefundApplication {

    /**
     * order_type 订单类型 1,so订单 2 order商城订单 3 order_coupon商城卷
     */
    public final  static int PAY_REFUND_ORDER_TYPE_SO=1;//so订单

    public final  static int PAY_REFUND_ORDER_TYPE_ORDER=2;//order商城订单

    public final  static int PAY_REFUND_ORDER_TYPE_ORDER_COUPON=3;//order_coupon商城卷

    /**
     * status 审核状态 0 待审批 1 审批通过 2审批不通过
     */
    public final  static int PAY_REFUND_STATUS_DSP=0;//待审批

    public final  static int PAY_REFUND_STATUS_SPTG=1;//审批通过

    public final  static int PAY_REFUND_STATUS_SPBTG=2;//审批不通过

    /**
     * refund_status 退款状态0 退款成功 1 退款异常 2 退款关闭
     */
    public final  static int PAY_REFUND__REFUND_STATUS_SUCCESS=0;//退款成功

    public final  static int PAY_REFUND__REFUND_STATUS_CHANGE=1;//退款异常

    public final  static int PAY_REFUND__REFUND_STATUS_REFUNDCLOSE=2;//退款关闭

    /**
     *申请来源类型:1,小程序 2 app
     */
    public final  static int PAY_REFUND_TYPE_MINI=1;//小程序

    public final  static int PAY_REFUND_TYPE_APP=2;//app
    private Long id;

    private String outTradeNo;//订单号

    private String outRefundNo;//退款单号

    private Integer orderType;

    private BigDecimal totalFee;//订单付款金额

    private BigDecimal refundFee;//申请退款金额

    private String applyReason;//申请原因

        private Integer status;//审核状态 0 待审批 1 审批通过 2审批不通过

    private String remarks;//审核备注

    private Integer refundStatus;//退款状态0 退款成功 1 退款异常 2 退款关闭

    private Date createTime;

    private Date updateTime;

    private BigDecimal settlementRefundFee;//实际退款金额

    private Date refundTime;

    private Integer type;

    private Long userId;

    private String otherReason;//其他原因

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
        this.outTradeNo = outTradeNo == null ? null : outTradeNo.trim();
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo == null ? null : outRefundNo.trim();
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
        this.applyReason = applyReason == null ? null : applyReason.trim();
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
        this.remarks = remarks == null ? null : remarks.trim();
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
}
