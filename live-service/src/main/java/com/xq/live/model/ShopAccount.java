package com.xq.live.model;

import java.math.BigDecimal;

/**
 * 商户对账
 * @author zhangmm
 * @date 2019-4-8
 */
public class ShopAccount {

    /************商铺对账统计***************/
    private BigDecimal wxPayMoney; //微信收款金额

    private Integer wxPayTotal; //微信收款笔数

    private BigDecimal wxRefundTatalMoney; //微信退款金额

    private Integer wxRefundTatal; //微信退款笔数

    private BigDecimal yuePayMoney; //余额收款金额

    private Integer yuePayTatal; //余额收款笔数

    private BigDecimal yueRefundTatalMoney; //余额退款金额

    private Integer yueRefundTatal; //余额退款笔数

    private  BigDecimal toDayRefundTatalMoney; //今日提款总金额

    private Integer toDayRefundTatal;  //今日退款笔数

    private  BigDecimal hisRefundTatalMoney; //历史退款总金额

    private Integer hisRefundTatal; //历史退款总笔数

    /**************搜索**************/
    private String startTime; //开始时间

    private String endTime; //结束时间

    /**************提现******************/

    private Integer cashId; //提现主键编号

    private Integer shopId; // 店铺编号

    private Integer userId; //用户编号

    private String mobile; //手机号码

    private String accountName; //银行卡

    private String accountCardholderName; //持卡人姓名

    private String bankCardName; //所属银行

    private String beginTime; //提现发起时间

    private BigDecimal cashAmount; //提现金额

    private String shopName;    //商铺名字

    private Integer applyStatus; //审批状态

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCashId() {
        return cashId;
    }

    public void setCashId(Integer cashId) {
        this.cashId = cashId;
    }

    public BigDecimal getWxPayMoney() {
        return wxPayMoney;
    }

    public void setWxPayMoney(BigDecimal wxPayMoney) {
        this.wxPayMoney = wxPayMoney;
    }

    public Integer getWxPayTotal() {
        return wxPayTotal;
    }

    public void setWxPayTotal(Integer wxPayTotal) {
        this.wxPayTotal = wxPayTotal;
    }

    public BigDecimal getWxRefundTatalMoney() {
        return wxRefundTatalMoney;
    }

    public void setWxRefundTatalMoney(BigDecimal wxRefundTatalMoney) {
        this.wxRefundTatalMoney = wxRefundTatalMoney;
    }

    public Integer getWxRefundTatal() {
        return wxRefundTatal;
    }

    public void setWxRefundTatal(Integer wxRefundTatal) {
        this.wxRefundTatal = wxRefundTatal;
    }

    public BigDecimal getYuePayMoney() {
        return yuePayMoney;
    }

    public void setYuePayMoney(BigDecimal yuePayMoney) {
        this.yuePayMoney = yuePayMoney;
    }

    public Integer getYuePayTatal() {
        return yuePayTatal;
    }

    public void setYuePayTatal(Integer yuePayTatal) {
        this.yuePayTatal = yuePayTatal;
    }

    public BigDecimal getYueRefundTatalMoney() {
        return yueRefundTatalMoney;
    }

    public void setYueRefundTatalMoney(BigDecimal yueRefundTatalMoney) {
        this.yueRefundTatalMoney = yueRefundTatalMoney;
    }

    public Integer getYueRefundTatal() {
        return yueRefundTatal;
    }

    public void setYueRefundTatal(Integer yueRefundTatal) {
        this.yueRefundTatal = yueRefundTatal;
    }

    public BigDecimal getToDayRefundTatalMoney() {
        return toDayRefundTatalMoney;
    }

    public void setToDayRefundTatalMoney(BigDecimal toDayRefundTatalMoney) {
        this.toDayRefundTatalMoney = toDayRefundTatalMoney;
    }

    public Integer getToDayRefundTatal() {
        return toDayRefundTatal;
    }

    public void setToDayRefundTatal(Integer toDayRefundTatal) {
        this.toDayRefundTatal = toDayRefundTatal;
    }

    public BigDecimal getHisRefundTatalMoney() {
        return hisRefundTatalMoney;
    }

    public void setHisRefundTatalMoney(BigDecimal hisRefundTatalMoney) {
        this.hisRefundTatalMoney = hisRefundTatalMoney;
    }

    public Integer getHisRefundTatal() {
        return hisRefundTatal;
    }

    public void setHisRefundTatal(Integer hisRefundTatal) {
        this.hisRefundTatal = hisRefundTatal;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getMobile() {
        return mobile;
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

    public Integer getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Integer applyStatus) {
        this.applyStatus = applyStatus;
    }
}
