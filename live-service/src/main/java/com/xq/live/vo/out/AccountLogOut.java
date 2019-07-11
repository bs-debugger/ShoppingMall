package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xq.live.model.User;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by admin on 2018/11/23.
 */
public class AccountLogOut {
    private Long id;

    private Long userId;

    private String userName;

    private Long accountId;

    private String accountName;

    private BigDecimal preAmount;

    private BigDecimal afterAmount;

    private BigDecimal operateAmount;

    private Integer operateType;

    private String remark;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private Integer preGold;

    private Integer afterGold;

    private Integer operateGold;

    private Integer type;//日志类型：1，商户余额 ，2 操作金币，3,用户余额,4，用户已获得的奖励金，5，用户审核中的奖励金，6，用户获取失败的奖励金

    private Long cashApplyId;//提现申请的ID

    private Byte applyStatus;//提现申请状态

    private Date paidTime;

    private  Integer applyType;//申请类型：1,商家提现，2,用户提现

    private Long orderId;//奖励金来源订单id

    private Integer actGoodsSkuState;//团的状态

    private Integer actOrderState;//参团状态

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private User user;//奖励金来源用户信息

    private String orderCouponCode;//票券编号

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
        this.accountName = accountName;
    }

    public BigDecimal getPreAmount() {
        return preAmount;
    }

    public void setPreAmount(BigDecimal preAmount) {
        this.preAmount = preAmount;
    }

    public BigDecimal getAfterAmount() {
        return afterAmount;
    }

    public void setAfterAmount(BigDecimal afterAmount) {
        this.afterAmount = afterAmount;
    }

    public BigDecimal getOperateAmount() {
        return operateAmount;
    }

    public void setOperateAmount(BigDecimal operateAmount) {
        this.operateAmount = operateAmount;
    }

    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getPreGold() {
        return preGold;
    }

    public void setPreGold(Integer preGold) {
        this.preGold = preGold;
    }

    public Integer getAfterGold() {
        return afterGold;
    }

    public void setAfterGold(Integer afterGold) {
        this.afterGold = afterGold;
    }

    public Integer getOperateGold() {
        return operateGold;
    }

    public void setOperateGold(Integer operateGold) {
        this.operateGold = operateGold;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getCashApplyId() {
        return cashApplyId;
    }

    public void setCashApplyId(Long cashApplyId) {
        this.cashApplyId = cashApplyId;
    }

    public Byte getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Byte applyStatus) {
        this.applyStatus = applyStatus;
    }

    public Date getPaidTime() {
        return paidTime;
    }

    public void setPaidTime(Date paidTime) {
        this.paidTime = paidTime;
    }

    public Integer getApplyType() {
        return applyType;
    }

    public void setApplyType(Integer applyType) {
        this.applyType = applyType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getActGoodsSkuState() {
        return actGoodsSkuState;
    }

    public void setActGoodsSkuState(Integer actGoodsSkuState) {
        this.actGoodsSkuState = actGoodsSkuState;
    }

    public Integer getActOrderState() {
        return actOrderState;
    }

    public void setActOrderState(Integer actOrderState) {
        this.actOrderState = actOrderState;
    }

    public String getOrderCouponCode() {
        return orderCouponCode;
    }

    public void setOrderCouponCode(String orderCouponCode) {
        this.orderCouponCode = orderCouponCode;
    }
}
