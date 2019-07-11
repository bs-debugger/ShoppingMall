package com.xq.live.vo.out;

import java.math.BigDecimal;

public class ShopAccountOut {

    private Integer cashId;

    private String mobile; //手机号码

    private String accountName; //银行卡

    private String accountCardholderName; //持卡人姓名

    private String bankCardName; //所属银行

    private String beginTime; //提现发起时间

    private String endTime;//提现结束时间

    private BigDecimal cashAmount; //提现金额

    private String shopName;    //商铺名字

    private Integer applyStatus; //审批状态

    public String getMobile() {
        return mobile;
    }

    public Integer getCashId() {
        return cashId;
    }

    public void setCashId(Integer cashId) {
        this.cashId = cashId;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public BigDecimal getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(BigDecimal cashAmount) {
        this.cashAmount = cashAmount;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Integer getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Integer applyStatus) {
        this.applyStatus = applyStatus;
    }
}
