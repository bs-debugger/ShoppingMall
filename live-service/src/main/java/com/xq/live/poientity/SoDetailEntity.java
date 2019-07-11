package com.xq.live.poientity;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.util.Date;

/**
 * 订单明细导出
 * 注:经过测试，如果使用模版的话，注解属性没有用
 * Created by lipeng on 2018/7/24.
 */
public class SoDetailEntity{
    @Excel(name = "序号")
    private String index;

    @Excel(name = "交易日期")
    private String paidTime;

    @Excel(name = "订单号")
    private Long soId;

    @Excel(name = "类型")
    private String type;

    @Excel(name = "应收款")
    private String accountAmount;

    @Excel(name = "优惠券")
    private String coupon;

    @Excel(name = "服务费")
    private String servicePrice;

    @Excel(name = "实收款",suffix = "元")
    private String realAmount;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public Long getSoId() {
        return soId;
    }

    public void setSoId(Long soId) {
        this.soId = soId;
    }

    public String getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(String accountAmount) {
        this.accountAmount = accountAmount;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(String realAmount) {
        this.realAmount = realAmount;
    }

    public String getPaidTime() {
        return paidTime;
    }

    public void setPaidTime(String paidTime) {
        this.paidTime = paidTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
