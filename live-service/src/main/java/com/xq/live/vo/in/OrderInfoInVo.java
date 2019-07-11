    package com.xq.live.vo.in;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xq.live.model.OrderItem;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商城系统订单相关入参
 * Created by lipeng on 2018/9/4.
 */
public class OrderInfoInVo extends BaseInVo{
    private Long id;

    private String orderCode;

    /*@NotNull(message = "userId不能为空")*/
    private Long userId;

    private String userName;//用户的名称

    private Long shopId;

    /*@NotNull(message = "orderAddressId不能为空")*/
    private Long orderAddressId;

    @NotNull(message = "payType不能为空")
    private Integer payType;

    private Integer sendType;

    private String sendTime;

    private BigDecimal skuAmount;//商品费用

    private BigDecimal sendAmount;//运费

    private BigDecimal realAmount;//实付款

    private BigDecimal packingAmount;//包装费

    private String qrcodeUrl;//订单二维码

    private Long parentOrderId;//父订单编码(如果本身就是父订单 就为0)

    private Integer isParent;//是否是父订单 0子订单 1父订单

    private Integer orderType;//订单类型  1实物订单  2虚拟订单

    private Integer status;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date expiryDate;//有效期

    private Date createTime;

    private Date paidTime;

    private Date updateTime;

    private String userIp;//用户ip

    private Long salepointId;//销售点id

    private Integer singleType;//分单类型 1分单 2整单

    private Long hxUserId;//核销人的用户id

    private Integer flagType;//标记类型(1普通型  2砍价型  3秒杀型)

    private Long actId;//活动id

    private String otherStatus;//status的扩展，查询状态为"4,5"的退款）

    private Long categoryId;//类目id

    private Long groupId;//砍价活动,分组的id

    private String totalKey;//活动缓存主键key

    private String valueKey;//活动缓存item的Key

    private Date beginTime;//查询商家订单中的开始时间

    private Date endTime;//查询商家订单中的结束时间

    private BatchOrderInVo batchOrderInVo;//批量制造数据入参

    private List<OrderItem> orderItemList;//各个商品的详细信息

    private Integer sourceType;//1平台订单 2商家订单

    private Integer isDui;//0未对账1对账

    private  Integer createType;

    private BigDecimal accountAmount;//钱包支付金额

    private Integer useAccount;//使用钱包余额支付0 不使用 ，1 使用

    private Integer defaultUsed;//是否默认使用  1是 2否 目前是用户抽到红包自动余额

    private Long goodsSkuId;//商品id

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode == null ? null : orderCode.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getOrderAddressId() {
        return orderAddressId;
    }

    public void setOrderAddressId(Long orderAddressId) {
        this.orderAddressId = orderAddressId;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime == null ? null : sendTime.trim();
    }

    public BigDecimal getSkuAmount() {
        return skuAmount;
    }

    public void setSkuAmount(BigDecimal skuAmount) {
        this.skuAmount = skuAmount;
    }

    public BigDecimal getSendAmount() {
        return sendAmount;
    }

    public void setSendAmount(BigDecimal sendAmount) {
        this.sendAmount = sendAmount;
    }

    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }

    public BigDecimal getPackingAmount() {
        return packingAmount;
    }

    public void setPackingAmount(BigDecimal packingAmount) {
        this.packingAmount = packingAmount;
    }

    public Long getParentOrderId() {
        return parentOrderId;
    }

    public void setParentOrderId(Long parentOrderId) {
        this.parentOrderId = parentOrderId;
    }

    public Integer getIsParent() {
        return isParent;
    }

    public void setIsParent(Integer isParent) {
        this.isParent = isParent;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPaidTime() {
        return paidTime;
    }

    public void setPaidTime(Date paidTime) {
        this.paidTime = paidTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getSalepointId() {
        return salepointId;
    }

    public void setSalepointId(Long salepointId) {
        this.salepointId = salepointId;
    }

    public Integer getSingleType() {
        return singleType;
    }

    public void setSingleType(Integer singleType) {
        this.singleType = singleType;
    }

    public Long getHxUserId() {
        return hxUserId;
    }

    public void setHxUserId(Long hxUserId) {
        this.hxUserId = hxUserId;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public Integer getFlagType() {
        return flagType;
    }

    public void setFlagType(Integer flagType) {
        this.flagType = flagType;
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public String getOtherStatus() {
        return otherStatus;
    }

    public void setOtherStatus(String otherStatus) {
        this.otherStatus = otherStatus;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getTotalKey() {
        return totalKey;
    }

    public void setTotalKey(String totalKey) {
        this.totalKey = totalKey;
    }

    public String getValueKey() {
        return valueKey;
    }

    public void setValueKey(String valueKey) {
        this.valueKey = valueKey;
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

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public Integer getIsDui() {
        return isDui;
    }

    public void setIsDui(Integer isDui) {
        this.isDui = isDui;
    }

    public BatchOrderInVo getBatchOrderInVo() {
        return batchOrderInVo;
    }

    public void setBatchOrderInVo(BatchOrderInVo batchOrderInVo) {
        this.batchOrderInVo = batchOrderInVo;
    }

    public Integer getCreateType() {
        return createType;
    }

    public void setCreateType(Integer createType) {
        this.createType = createType;
    }

    public BigDecimal getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(BigDecimal accountAmount) {
        this.accountAmount = accountAmount;
    }

    public Integer getUseAccount() {
        return useAccount;
    }

    public void setUseAccount(Integer useAccount) {
        this.useAccount = useAccount;
    }

    public Integer getDefaultUsed() {
        return defaultUsed;
    }

    public void setDefaultUsed(Integer defaultUsed) {
        this.defaultUsed = defaultUsed;
    }

    public Long getGoodsSkuId() {
        return goodsSkuId;
    }

    public void setGoodsSkuId(Long goodsSkuId) {
        this.goodsSkuId = goodsSkuId;
    }
}
