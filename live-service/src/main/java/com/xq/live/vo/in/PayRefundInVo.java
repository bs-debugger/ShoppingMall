package com.xq.live.vo.in;

import javax.validation.constraints.NotNull;

/**
 * 退款申请入参
 * Created by admin on 2018/10/19.
 */
public class PayRefundInVo extends BaseInVo{

    private String outTradeNo;

    private Integer orderType;//订单类型:1,so订单 2 order商城订单 3 order_coupon商城卷

    private String applyReason;

    private Integer type;//申请来源类型:1,小程序 2 app

    private Integer refundStatus;//退款状态0 退款成功 1 退款异常 2 退款关闭

    private Integer status;//审核状态 0 待审批 1 审批通过 2审批不通过

    @NotNull(message = "userId不能为空")
    private Long userId;

    private String otherReason;//其他详细原因

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
