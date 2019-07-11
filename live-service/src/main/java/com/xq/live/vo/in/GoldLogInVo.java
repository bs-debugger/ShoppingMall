package com.xq.live.vo.in;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户发起砍菜领取金币
 * Created by ss on 2018/8/6.
 */
public class GoldLogInVo extends BaseInVo{

    private Long id;

    private Long shopId;

    private Long userId;

    private Long refId;

    private Byte type;

    private Long parentId;

    private Integer goldAmount;

    private Byte stateType;

    private Byte isDelete;

    private Date paidTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;//开始时间

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;//截止时间

    private Integer groupId;//小组id

    private BigDecimal amount;//砍菜价格范围

    private BigDecimal skuMoneyNow;//菜的现价

    private BigDecimal skuMoneyOut;//菜的原价

    private BigDecimal skuMoneyMin;//菜的底价

    public BigDecimal getSkuMoneyMin() {
        return skuMoneyMin;
    }

    public void setSkuMoneyMin(BigDecimal skuMoneyMin) {
        this.skuMoneyMin = skuMoneyMin;
    }

    public BigDecimal getSkuMoneyOut() {
        return skuMoneyOut;
    }

    public void setSkuMoneyOut(BigDecimal skuMoneyOut) {
        this.skuMoneyOut = skuMoneyOut;
    }

    public BigDecimal getSkuMoneyNow() {
        return skuMoneyNow;
    }

    public void setSkuMoneyNow(BigDecimal skuMoneyNow) {
        this.skuMoneyNow = skuMoneyNow;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getGoldAmount() {
        return goldAmount;
    }

    public void setGoldAmount(Integer goldAmount) {
        this.goldAmount = goldAmount;
    }

    public Byte getStateType() {
        return stateType;
    }

    public void setStateType(Byte stateType) {
        this.stateType = stateType;
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    public Date getPaidTime() {
        return paidTime;
    }

    public void setPaidTime(Date paidTime) {
        this.paidTime = paidTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
