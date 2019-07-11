package com.xq.live.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class OrderCoupon {
    /**
     * type 1礼品券 2门店自提券
     */
    public final static int TYPE_LPQ = 1;   //礼品券(平台邮购)

    public final static int TYPE_MDZTQ = 2;   //门店自提券(门店自提)

    public final static int TYPE_RED_PACKET = 3;//红包券
    /**
     * 抵用券是否使用   0未使用  1已使用
     */
    public final static int ORDER_COUPON_IS_USED_YES = 1;

    public final static int ORDER_COUPON_IS_USED_NO = 0;

    /**
     * 兑换券是否赠送   0未赠送  1已赠送
     */
    public final static int ORDER_COUPON_GET_STATUS_HAVE = 2;

    public final static int ORDER_COUPON_GET_STATUS_NOT = 1;

    /**
     * 分单类型   1分单  2整单
     */
    public final static int SINGLE_TYPE_FD = 1;

    public final static int SINGLE_TYPE_ZD = 2;

    /**
     * 票卷状态：0 默认状态 4 退款申请中 5 已退款
     */
    public final static int STATUS_DEFAULT = 0;

    public final static int STATUS_REFUND_APPLICATION = 4;

    public final static int STATUS_REFUND = 5;

    /**
     * 标记类型(1普通型  4砍价型  5秒杀型)
     */
    public final static int FLAG_TYPE_PT = 1;//普通型

    public final static int FLAG_TYPE_KJ = 4;//砍价型

    public final static int FLAG_TYPE_MS = 5;//秒杀型


    /*
    * 票卷是否展示给用户，0和null展示，1不展示
    * */
    public final static Integer IS_NOT_SHOW = 1;//不展示

    public final static Integer IS_SHOW = 0;//展示

    /**
     * 快递状态：1，待发货 2，已发货  3，已完成
     */
    public final static  Integer EXPRESSSTATE_WAIT=1;//待发货

    public final static  Integer  EXPRESSSTATE_ENDING=2;//已发货

    public final static  Integer  EXPRESSSTATE_FINISH=3;//已完成

    /**
     * 票券赠送或领取 1赠送  2领取
     */
    public final static  Integer ORDER_COUPON_SEND_SEND=1;//赠送

    public final static  Integer  ORDER_COUPON_SEND_RECEIVE=2;//领取



    private Long id;

    private Long orderId;

    private String couponCode;

    private Long goodsSkuId;

    private String goodsSkuCode;

    private String goodsSkuName;

    private BigDecimal couponAmount;

    private BigDecimal realUnitPrice;

    private Integer type;

    private String qrcodeUrl;

    private Long userId;

    private String userName;

    private Integer isUsed;

    private Integer isDeleted;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date expiryDate;

    private Long couponAddressId;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date usedTime;

    private Long shopId;

    private Long ownId;//拥有人的id

    private Long changerId;//使用人的id

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private String sendTime;//期望配送日期

    private String remark;//备注

    private String expressCode;//快递单号

    private Integer versionNo;//版本号(作为乐观锁的判断依据)

    private Integer singleType;//分单类型 1分单 2整单

    private Integer flagType;//标记类型(1普通型  4砍价型  5秒杀型)

    private Integer status;//票卷状态：0 默认状态 4 退款申请中 5 已退款

    private Long couponSalepointId;//销售点id

    private BigDecimal serviceAmount;//服务费

    private BigDecimal shopServiceAmount;//商家服务费

    private BigDecimal userServiceAmount;//用户服务费

    private Integer isShow;//票卷是否展示给用户，0和null展示，1不展示

    private Integer getTheStatus;//是否赠送

    private String showCode;//展示的票券编号

    private Integer expressState;//快递状态：1，待发货 2，已发货  3，已完成

    private BigDecimal realShopUnitPrice;//真实商家营业额单价(realUnitPrice - serviceAmount)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode == null ? null : couponCode.trim();
    }

    public Long getGoodsSkuId() {
        return goodsSkuId;
    }

    public void setGoodsSkuId(Long goodsSkuId) {
        this.goodsSkuId = goodsSkuId;
    }

    public String getGoodsSkuCode() {
        return goodsSkuCode;
    }

    public void setGoodsSkuCode(String goodsSkuCode) {
        this.goodsSkuCode = goodsSkuCode == null ? null : goodsSkuCode.trim();
    }

    public String getGoodsSkuName() {
        return goodsSkuName;
    }

    public void setGoodsSkuName(String goodsSkuName) {
        this.goodsSkuName = goodsSkuName == null ? null : goodsSkuName.trim();
    }

    public BigDecimal getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
    }

    public BigDecimal getRealUnitPrice() {
        return realUnitPrice;
    }

    public void setRealUnitPrice(BigDecimal realUnitPrice) {
        this.realUnitPrice = realUnitPrice;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl == null ? null : qrcodeUrl.trim();
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

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Long getCouponAddressId() {
        return couponAddressId;
    }

    public void setCouponAddressId(Long couponAddressId) {
        this.couponAddressId = couponAddressId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Date usedTime) {
        this.usedTime = usedTime;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getOwnId() {
        return ownId;
    }

    public void setOwnId(Long ownId) {
        this.ownId = ownId;
    }

    public Long getChangerId() {
        return changerId;
    }

    public void setChangerId(Long changerId) {
        this.changerId = changerId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getExpressCode() {
        return expressCode;
    }

    public void setExpressCode(String expressCode) {
        this.expressCode = expressCode;
    }

    public Integer getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    public Integer getSingleType() {
        return singleType;
    }

    public void setSingleType(Integer singleType) {
        this.singleType = singleType;
    }

    public Integer getFlagType() {
        return flagType;
    }

    public void setFlagType(Integer flagType) {
        this.flagType = flagType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCouponSalepointId() {
        return couponSalepointId;
    }

    public void setCouponSalepointId(Long couponSalepointId) {
        this.couponSalepointId = couponSalepointId;
    }

    public BigDecimal getServiceAmount() {
        return serviceAmount;
    }

    public void setServiceAmount(BigDecimal serviceAmount) {
        this.serviceAmount = serviceAmount;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getGetTheStatus() {
        return getTheStatus;
    }

    public void setGetTheStatus(Integer getTheStatus) {
        this.getTheStatus = getTheStatus;
    }

    public String getShowCode() {
        return showCode;
    }

    public void setShowCode(String showCode) {
        this.showCode = showCode;
    }

    public Integer getExpressState() {
        return expressState;
    }

    public void setExpressState(Integer expressState) {
        this.expressState = expressState;
    }

    public BigDecimal getRealShopUnitPrice() {
        return realShopUnitPrice;
    }

    public void setRealShopUnitPrice(BigDecimal realShopUnitPrice) {
        this.realShopUnitPrice = realShopUnitPrice;
    }

    public BigDecimal getShopServiceAmount() {
        return shopServiceAmount;
    }

    public void setShopServiceAmount(BigDecimal shopServiceAmount) {
        this.shopServiceAmount = shopServiceAmount;
    }

    public BigDecimal getUserServiceAmount() {
        return userServiceAmount;
    }

    public void setUserServiceAmount(BigDecimal userServiceAmount) {
        this.userServiceAmount = userServiceAmount;
    }
}
