package com.xq.live.vo.out;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Table;

@Table(name = "pay_refund_application")
public class RefundTablesOut {

    @ApiModelProperty(value = "今日退款订单总数")
    private String todayFefundTotal;

    @ApiModelProperty(value = "今日退款订单总金额")
    private String todayFefundAmount;

    @ApiModelProperty(value = "历史退款订单总数")
    private String historicalFefundTotal;

    @ApiModelProperty(value = "历史退款订单总金额")
    private String historicalFefundAmount;

    public String getTodayFefundTotal() {
        return todayFefundTotal;
    }

    public void setTodayFefundTotal(String todayFefundTotal) {
        this.todayFefundTotal = todayFefundTotal;
    }

    public String getTodayFefundAmount() {
        return todayFefundAmount;
    }

    public void setTodayFefundAmount(String todayFefundAmount) {
        this.todayFefundAmount = todayFefundAmount;
    }

    public String getHistoricalFefundTotal() {
        return historicalFefundTotal;
    }

    public void setHistoricalFefundTotal(String historicalFefundTotal) {
        this.historicalFefundTotal = historicalFefundTotal;
    }

    public String getHistoricalFefundAmount() {
        return historicalFefundAmount;
    }

    public void setHistoricalFefundAmount(String historicalFefundAmount) {
        this.historicalFefundAmount = historicalFefundAmount;
    }
}
