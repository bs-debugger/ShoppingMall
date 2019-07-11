package com.xq.live.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class CashApply {
    /**
     * 审批状态 1 待审批 2 审批通过 3 审批不通过 4 取消 5 终止
     */
    public final static int CASH_APPLY_STATUS_WAIT = 1;

    public final static int CASH_APPLY_STATUS_TG= 2;

    public final static int CASH_APPLY_STATUS_BTG= 3;

    public final static int CASH_APPLY_STATUS_QX= 4;

    public final static int CASH_APPLY_STATUS_ZZ= 5;

    /**
     *申请类型：1,商家提现，2,用户提现,3，商家缴纳服务费
     */
    public final static int CASH_APPLY_TYPE_SHOP= 1;

    public final static int CASH_APPLY_TYPE_USER= 2;

    public final static int CASH_APPLY_TYPE_SHOP_SERVICE= 3;

    /**
     * 打款方式 1 银行卡  2 微信
     */
    public final static int CASH_APPLY_PAYTYPE_BANK= 1;

    public final static int CASH_APPLY_PAYTYPE_WEIXIN= 2;


    private Long id;
    //@NotNull(message = "userId必填")
    private Long userId;
    //@NotNull(message = "userName必填")
    private String userName;

    private Long accountId;

    private String accountName;

    @NotNull(message = "cashAmount必填")
    private BigDecimal cashAmount;  //提现金额

    private Byte applyStatus;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private Date paidTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private Long paidUserId;

    private String paidUserName;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private  Integer applyType;//申请类型：1,商家提现，2,用户提现

    private BigDecimal serviceAmount;  //提现服务费（用户提现收取服务费）

    @ApiModelProperty(value = "持卡人姓名 ")
    private String accountCardholderName;//持卡人姓名

    @ApiModelProperty(value = "银行卡银行(所属银行)")
    private String bankCardName;//银行卡银行

    @ApiModelProperty(value = "申请人openId")
    private String applyOpenId;//申请人openId

    @ApiModelProperty(value = "打款方式 1 银行卡  2 微信")
    private Byte payType;//打款方式 1 银行卡  2 微信

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName == null ? null : accountName.trim();
    }

    public BigDecimal getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(BigDecimal cashAmount) {
        this.cashAmount = cashAmount;
    }

    public Byte getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Byte applyStatus) {
        this.applyStatus = applyStatus;
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

    public Long getPaidUserId() {
        return paidUserId;
    }

    public void setPaidUserId(Long paidUserId) {
        this.paidUserId = paidUserId;
    }

    public String getPaidUserName() {
        return paidUserName;
    }

    public void setPaidUserName(String paidUserName) {
        this.paidUserName = paidUserName == null ? null : paidUserName.trim();
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getApplyType() {
        return applyType;
    }

    public void setApplyType(Integer applyType) {
        this.applyType = applyType;
    }

    public BigDecimal getServiceAmount() {
        return serviceAmount;
    }

    public void setServiceAmount(BigDecimal serviceAmount) {
        this.serviceAmount = serviceAmount;
    }

    public String getBankCardName() {
        return bankCardName;
    }

    public void setBankCardName(String bankCardName) {
        this.bankCardName = bankCardName;
    }

    public String getAccountCardholderName() {
        return accountCardholderName;
    }

    public void setAccountCardholderName(String accountCardholderName) {
        this.accountCardholderName = accountCardholderName;
    }

    public String getApplyOpenId() {
        return applyOpenId;
    }

    public void setApplyOpenId(String applyOpenId) {
        this.applyOpenId = applyOpenId;
    }

    public Byte getPayType() {
        return payType;
    }

    public void setPayType(Byte payType) {
        this.payType = payType;
    }
}
