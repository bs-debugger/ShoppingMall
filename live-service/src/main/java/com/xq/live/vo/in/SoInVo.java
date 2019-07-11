package com.xq.live.vo.in;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-09 14:26
 * @copyright:hbxq
 **/
public class SoInVo extends BaseInVo {
    private Long id;
    //@NotNull(message = "userId必填")
    private Long userId;

    //@NotNull(message = "userName必填")
    private String userName;

    private Integer soStatus;

    private Integer soType;

    @NotNull(message = "payType必填")
    private Integer payType;

    @NotNull(message = "skuId必填")
    private Long skuId;

    @NotNull(message = "skuNum必填")
    private Integer skuNum;

    private BigDecimal soAmount;//如果是商家订单创建的时候，这个值必填

    private Long actId;//生成的订单对应的活动

    private String userIp;//用户下单的ip地址

    private Long shopId;//商家订单对应的shopId,在商家订单创建的时候必填

    private Long couponId;//商家订单中,核销票券的couponId

    private Date beginTime;//查询商家订单中的开始时间

    private Date endTime;//查询商家订单中的结束时间

    private Integer isDui;//是否对账

    private Long dishSkuId;//订单所对菜的sku_id

    private String dishSkuName;//订单所对菜的sku_name

    private Integer type;//购买类型  1.原价购买 2.砍价购买

    private Integer groupId;//砍价分组的组id

    private String otherStatus;//status的扩展，查询状态为"4,5"的退款）

    public Integer getIsDui() {
        return isDui;
    }

    public void setIsDui(Integer isDui) {
        this.isDui = isDui;
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
        this.userName = userName;
    }

    public Integer getSoStatus() {
        return soStatus;
    }

    public void setSoStatus(Integer soStatus) {
        this.soStatus = soStatus;
    }

    public Integer getSoType() {
        return soType;
    }

    public void setSoType(Integer soType) {
        this.soType = soType;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getSkuNum() {
        return skuNum;
    }

    public void setSkuNum(Integer skuNum) {
        this.skuNum = skuNum;
    }

    public BigDecimal getSoAmount() {
        return soAmount;
    }

    public void setSoAmount(BigDecimal soAmount) {
        this.soAmount = soAmount;
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
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

    public Long getDishSkuId() {
        return dishSkuId;
    }

    public void setDishSkuId(Long dishSkuId) {
        this.dishSkuId = dishSkuId;
    }

    public String getDishSkuName() {
        return dishSkuName;
    }

    public void setDishSkuName(String dishSkuName) {
        this.dishSkuName = dishSkuName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getOtherStatus() {
        return otherStatus;
    }

    public void setOtherStatus(String otherStatus) {
        this.otherStatus = otherStatus;
    }
}
