package com.xq.live.model;

import java.util.Date;

public class PaidLog {
    /**
     * paidType 1 微信支付 2 支付宝 3 银行卡  4享七金币
     */
    public final static int PAID_TYPE_WX = 1;   //微信支付

    public final static int PAID_TYPE_ZFB = 2;   //支付宝

    public final static int PAID_TYPE_YHK = 3;   //银行卡

    public final static int PAID_TYPE_XQJB = 4;   //享七金币


    /**
     * operatorType 1支付 2退款 3支付运费
     */
    public final static int OPERATOR_TYPE_ZF = 1;   //支付

    public final static int OPERATOR_TYPE_TK = 2;   //退款

    public final static int OPERATOR_TYPE_ZFYF = 3;   //支付运费


    private Long id;

    private Long soId;

    private String orderCode;

    private String orderCouponCode;

    private Integer operatorType;//1支付 2退款 3支付运费

    private Integer paidType;//1 微信支付 2 支付宝 3 银行卡  4享七金币

    private String paidNo;

    private Long userId;

    private String userName;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSoId() {
        return soId;
    }

    public void setSoId(Long soId) {
        this.soId = soId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderCouponCode() {
        return orderCouponCode;
    }

    public void setOrderCouponCode(String orderCouponCode) {
        this.orderCouponCode = orderCouponCode;
    }

    public Integer getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(Integer operatorType) {
        this.operatorType = operatorType;
    }

    public Integer getPaidType() {
        return paidType;
    }

    public void setPaidType(Integer paidType) {
        this.paidType = paidType;
    }

    public String getPaidNo() {
        return paidNo;
    }

    public void setPaidNo(String paidNo) {
        this.paidNo = paidNo == null ? null : paidNo.trim();
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
