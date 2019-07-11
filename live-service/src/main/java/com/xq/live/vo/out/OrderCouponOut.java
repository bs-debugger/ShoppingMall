package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xq.live.model.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商城系统订单票券出参
 */
public class OrderCouponOut {
    private Long id;

    private Long orderId;

    private String couponCode;

    private Long goodsSkuId;

    private String goodsSkuCode;

    private String goodsSkuName;

    private BigDecimal couponAmount;

    private BigDecimal realUnitPrice;

    private BigDecimal realShopUnitPrice;//真实商家营业额单价(realUnitPrice - serviceAmount)

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

    private Long changerId;//使用人的用户id

    private String ownName;//拥有人的姓名

    private String changerName;//使用人的姓名

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private String skuPic;//商品图片

    private Long spuId;//产品id

    private String smallSkuPic;//裁剪图

    private OrderAddressOut orderAddress;//用户兑换的收货地址信息

    private String sendTime;//期望配送日期

    private String remark;//备注

    private Long couponSalepointId;//销售点id

    private BigDecimal serviceAmount;//服务费

    private BigDecimal shopServiceAmount;//商家服务费

    private BigDecimal userServiceAmount;//用户服务费

    private GoodsSku goodsSku;//票券所对应的商品信息

    private String expressCode;//快递单号

    private BigDecimal otherMarkerPrice;//别人的市场价

    private Long salepointId;//销售点Id

    private Long sendUserId;//赠送人的userId

    private String sendUserName;//赠送人名字

    private Long receiveUserId;//收取人的userId

    private String receiveUserName;//收取人名字

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ocsCreateTime;//赠送或领取的时间

    private Long ocsId;//票券赠送记录的id

    private Integer versionNo;//版本号(作为乐观锁的判断依据)

    private Integer singleType;//分单类型 1分单 2整单

    private SalePoint salePoint;//销售点信息 ------券被使用之后对应的销售点的信息

    private List<SalePointOut> salePointOuts;//券所对应的销售点列表,可能为空

    private ShopOut shopOut;

    private OrderAddressOut orderAddressOut;//地址信息

    private List<OrderItemOut> orderItemOuts;//券里面包含的详情

    private GoodsSpu goodsSpu;//类目

    private ActOrderOut actOrderOut;//活动订单相关

    private String maleWeight;  //公的重量

    private String femaleWeight; //母的重量

    private DeliveryTemplate deliveryTemplate;//计算方式

    private String styleName;   //款式

    private Integer flagType;//标记类型(1普通型  4砍价型  5秒杀型)

    private Integer status;//票卷状态：0 默认状态 4 退款申请中 5 已退款

    private Integer isShow;//票卷是否展示给用户，0和null展示，1不展示

    private Integer getTheStatus;//是否赠送

    private String showCode;//展示的票券编号

    private Integer expressState;//快递状态：1，待发货 2，已发货  3，已完成

    private BigDecimal realAmount;//实收款 realUnitPrice减去serviceAmount

    private String flagTypeName;//标记类型(普通型  砍价型  秒杀型)

    private List<GoodsCategory> goodsCategories;//商品的类目信息

    private List<OrderCouponSendOut> orderCouponSendOuts;//票券赠送和收取的信息

    private String girlsDayResult;//3.7,3.8女神节送奖励金结果

    private Integer isXiangGou;//是否为享购商品，类目中status为1和3的为享购 0不是享购，1是享购

    private ActGoodsSkuOut actGoodsSkuOut;

    private Long actId;

    public String getMaleWeight() {
        String skuName = this.getGoodsSkuName();
        if(skuName.indexOf("公") >= 3){
            return skuName.substring(skuName.indexOf("公") - 3, skuName.indexOf("公"));
        }
        return null;
    }

    public String getFemaleWeight() {
        String skuName = this.getGoodsSkuName();
        if(skuName.indexOf("母") >= 3){
            return skuName.substring(skuName.indexOf("母")-3, skuName.indexOf("母"));
        }
        return null;
    }

    public String getStyleName() {
        String skuName = this.getGoodsSkuName();
        if(skuName.indexOf("提蟹券") > 0){
            return skuName.substring(0, skuName.indexOf("提蟹券")).trim();
        }
        return null;
    }

    public GoodsSpu getGoodsSpu() {
        return goodsSpu;
    }

    public void setGoodsSpu(GoodsSpu goodsSpu) {
        this.goodsSpu = goodsSpu;
    }

    public BigDecimal getOtherMarkerPrice() {
        return otherMarkerPrice;
    }

    public void setOtherMarkerPrice(BigDecimal otherMarkerPrice) {
        this.otherMarkerPrice = otherMarkerPrice;
    }

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

    public String getSkuPic() {
        return skuPic;
    }

    public void setSkuPic(String skuPic) {
        this.skuPic = skuPic;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public OrderAddressOut getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(OrderAddressOut orderAddress) {
        this.orderAddress = orderAddress;
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

    public GoodsSku getGoodsSku() {
        return goodsSku;
    }

    public void setGoodsSku(GoodsSku goodsSku) {
        this.goodsSku = goodsSku;
    }

    public String getExpressCode() {
        return expressCode;
    }

    public void setExpressCode(String expressCode) {
        this.expressCode = expressCode;
    }

    public Long getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(Long sendUserId) {
        this.sendUserId = sendUserId;
    }

    public Long getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(Long receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public Date getOcsCreateTime() {
        return ocsCreateTime;
    }

    public void setOcsCreateTime(Date ocsCreateTime) {
        this.ocsCreateTime = ocsCreateTime;
    }

    public Long getOcsId() {
        return ocsId;
    }

    public void setOcsId(Long ocsId) {
        this.ocsId = ocsId;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public String getReceiveUserName() {
        return receiveUserName;
    }

    public void setReceiveUserName(String receiveUserName) {
        this.receiveUserName = receiveUserName;
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

    public List<OrderItemOut> getOrderItemOuts() {
        return orderItemOuts;
    }

    public void setOrderItemOuts(List<OrderItemOut> orderItemOuts) {
        this.orderItemOuts = orderItemOuts;
    }

    public SalePoint getSalePoint() {
        return salePoint;
    }

    public void setSalePoint(SalePoint salePoint) {
        this.salePoint = salePoint;
    }

    public Long getSalepointId() {
        return salepointId;
    }

    public void setSalepointId(Long salepointId) {
        this.salepointId = salepointId;
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

    public OrderAddressOut getOrderAddressOut() {
        return orderAddressOut;
    }

    public void setOrderAddressOut(OrderAddressOut orderAddressOut) {
        this.orderAddressOut = orderAddressOut;
    }

    public List<SalePointOut> getSalePointOuts() {
        return salePointOuts;
    }

    public void setSalePointOuts(List<SalePointOut> salePointOuts) {
        this.salePointOuts = salePointOuts;
    }

    public ShopOut getShopOut() {
        return shopOut;
    }

    public void setShopOut(ShopOut shopOut) {
        this.shopOut = shopOut;
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

    public DeliveryTemplate getDeliveryTemplate() {
        return deliveryTemplate;
    }

    public void setDeliveryTemplate(DeliveryTemplate deliveryTemplate) {
        this.deliveryTemplate = deliveryTemplate;
    }

    public String getOwnName() {
        return ownName;
    }

    public void setOwnName(String ownName) {
        this.ownName = ownName;
    }

    public String getChangerName() {
        return changerName;
    }

    public void setChangerName(String changerName) {
        this.changerName = changerName;
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

    public ActOrderOut getActOrderOut() {
        return actOrderOut;
    }

    public void setActOrderOut(ActOrderOut actOrderOut) {
        this.actOrderOut = actOrderOut;
    }

    public String getSmallSkuPic() {
        return smallSkuPic;
    }

    public void setSmallSkuPic(String smallSkuPic) {
        this.smallSkuPic = smallSkuPic;
    }

    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }

    public String getFlagTypeName() {
        return flagTypeName;
    }

    public void setFlagTypeName(String flagTypeName) {
        this.flagTypeName = flagTypeName;
    }

    public List<GoodsCategory> getGoodsCategories() {
        return goodsCategories;
    }

    public void setGoodsCategories(List<GoodsCategory> goodsCategories) {
        this.goodsCategories = goodsCategories;
    }

    public List<OrderCouponSendOut> getOrderCouponSendOuts() {
        return orderCouponSendOuts;
    }

    public void setOrderCouponSendOuts(List<OrderCouponSendOut> orderCouponSendOuts) {
        this.orderCouponSendOuts = orderCouponSendOuts;
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

    public String getGirlsDayResult() {
        return girlsDayResult;
    }

    public void setGirlsDayResult(String girlsDayResult) {
        this.girlsDayResult = girlsDayResult;
    }

    public Integer getIsXiangGou() {
        return isXiangGou;
    }

    public void setIsXiangGou(Integer isXiangGou) {
        this.isXiangGou = isXiangGou;
    }

    public ActGoodsSkuOut getActGoodsSkuOut() {
        return actGoodsSkuOut;
    }

    public void setActGoodsSkuOut(ActGoodsSkuOut actGoodsSkuOut) {
        this.actGoodsSkuOut = actGoodsSkuOut;
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }
}
