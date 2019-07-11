package com.xq.live.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name="account_log")
public class AccountLog {

    public final static int OPERATE_TYPE_INCOME = 2;    //收入

    public final static int OPERATE_TYPE_PAYOUT = 1;    //支出

    /**
     * 日志类型：1，商户余额，2，操作金币 3,用户余额,4，用户已获得的奖励金，5，用户审核中的奖励金，6，用户获取失败的奖励金
     */
    public final static int TYPE_SHOP = 1;    //商户余额

    public final static int TYPE_GOLD = 2;    //操作金币

    public final static int TYPE_USER = 3;    //操作用户余额

    public final static int TYPE_USER_PASSED = 4;    //用户已获得的奖励金

    public final static int TYPE_USER_REVIEW = 5;    //用户审核中的奖励金

    public final static int TYPE_USER_FAIL = 6;    //用户获取失败的奖励金

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

    private Long orderId;//奖励金来源订单id

    private String orderCouponCode;//票券编号

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
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

    public String getOrderCouponCode() {
        return orderCouponCode;
    }

    public void setOrderCouponCode(String orderCouponCode) {
        this.orderCouponCode = orderCouponCode;
    }
}
