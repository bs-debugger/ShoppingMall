package com.xq.live.vo.in;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * com.xq.live.vo.in
 *
 * @author zhangpeng32
 * Created on 2018/5/6 下午5:05
 * @Description:
 */
public class CashApplyInVo extends BaseInVo {
    private Long id;
    private Long userId;
    private String userName;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    private Date endTime;

    private  Integer applyType;//申请类型：1,商家提现，2,用户提现

    private BigDecimal cashAmount;  //提现金额

    private BigDecimal serviceAmount;  //提现服务费（用户提现收取服务费）

    private BigDecimal serviceRatio;    //提现服务费收取比例

    @ApiModelProperty(value = "持卡人姓名 ")
    private String accountCardholderName;//持卡人姓名

    @ApiModelProperty(value = "银行卡银行(所属银行)")
    private String bankCardName;//银行卡银行

    @ApiModelProperty(value = "申请人openId")
    private String applyOpenId;//申请人openId

    @ApiModelProperty(value = "打款方式 1 银行卡  2 微信")
    private Byte payType;//打款方式 1 银行卡  2 微信

    private long shopId;

    private Long accountId;//账户ID

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
        this.userName = userName;
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

    public BigDecimal getServiceRatio() {
        return serviceRatio;
    }

    public void setServiceRatio(BigDecimal serviceRatio) {
        this.serviceRatio = serviceRatio;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
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

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
