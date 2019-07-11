package com.xq.live.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户提现
 * @author zhangmm
 * @date 2019-4-10
 */
public class UserAdvance {

    private Integer cashId; //提现编号

    private String nickName; //昵称

    private String userName; //用户

    private String mobile;  //手机号

    private String beginTime; //提现时间

    private String accountName; //银行卡账号

    private String accountCardholderName; //持卡人

    private String bankCardName; //所属银行

    private BigDecimal cashAmount; //提现金额

    private BigDecimal serviceAmount; //服务费

    private Integer applyStatus; //审批状态

    private String paidUserName; //打款人

    private String payType; // 打款方式

    private Date createTime; // 提现申请时间

    public Integer getCashId() {
        return cashId;
    }

    public void setCashId(Integer cashId) {
        this.cashId = cashId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountCardholderName() {
        return accountCardholderName;
    }

    public void setAccountCardholderName(String accountCardholderName) {
        this.accountCardholderName = accountCardholderName;
    }

    public String getBankCardName() {
        return bankCardName;
    }

    public void setBankCardName(String bankCardName) {
        this.bankCardName = bankCardName;
    }

    public BigDecimal getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(BigDecimal cashAmount) {
        this.cashAmount = cashAmount;
    }

    public BigDecimal getServiceAmount() {
        return serviceAmount;
    }

    public void setServiceAmount(BigDecimal serviceAmount) {
        this.serviceAmount = serviceAmount;
    }

    public Integer getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Integer applyStatus) {
        this.applyStatus = applyStatus;
    }

    public String getPaidUserName() {
        return paidUserName;
    }

    public void setPaidUserName(String paidUserName) {
        this.paidUserName = paidUserName;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
