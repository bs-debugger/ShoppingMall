package com.xq.live.vo.in;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-03-09 14:07
 * @copyright:hbxq
 **/
public class WeixinInVo {
    /**
     * type 1 原价购买  2 砍价购买  砍价购买(商城系统的砍价)
     */
    public final static int WEI_XIN_TYPE_YJ = 1;   //原价购买

    public final static int WEI_XIN_TYPE_KJ = 2;   //砍价购买(小程序原来的砍价)

    public final static int WEI_XIN_TYPE_SHOP_KJ = 4;   //砍价购买(商城系统的砍价),到时候会慢慢的把小程序里面的迁移到商城系统里面

    public final static int WEI_XIN_TYPE_SHOP_MS = 5; // 秒杀购买

    public final static int WEI_XIN_TYPE_SHOP_CJ = 6; // 抽奖购买

    public final static int WEI_XIN_TYPE_SHOP_TG = 7; // 团购购买

    private String appid;//小程序ID
    private String mch_id;//商户号
    private String device_info;//设备号
    private String nonce_str;//随机字符串
    private String sign;//签名
    private String body;//商品描述
    private String detail;//商品详情
    private String attach;//附加数据
    private String out_trade_no;//商户订单号
    private String fee_type;//货币类型
    private String spbill_create_ip;//终端IP
    private String time_start;//交易起始时间
    private String time_expire;//交易结束时间
    private String goods_tag;//商品标记
    private String total_fee;//总金额
    private String notify_url;//通知地址
    private String trade_type;//交易类型
    private String limit_pay;//指定支付方式
    //@NotNull(message = "openId必填")
    private String openId;//用户标识
    //@NotNull(message = "soId必填")
    private Long soId;//支付服务费的时候没有订单id

    private Long couponId;//为了完成之后的对账操作，加入的票卷的id,主要是商家订单中的平台代收，直接把服务费扣掉

    private Long shopId;//1.商家单中对应的shopId 2.商家支付服务费的shopId

    private String subMchId;//特约商户的mchId

    private Long actId;//活动中购买券中的活动id

    private Long skuId;//活动中购买的对应的推荐菜id(也可以当作购买的时候所对应的砍价菜订单)

    private BigDecimal servicePrice;//支付的服务费

    private Date beginTime;//服务费计算的开始时间

    private Date endTime;//服务费计算的结束时间

    private Long userId;//支付人的用户id

    private Integer type;//购买类型  1.原价购买 2.砍价购买

    private Integer groupId;//砍价分组的组id

    private Long orderId;//商城系统的订单id

    private Long orderCouponId;//礼品券id

    private BigDecimal realWeight;//实际重量

    private Long orderAddressId;//发货地址

    private Long templateId;//运费模版id

    private Integer piece;//件数
    private BigDecimal bulk;//体积
    private Integer formulaMode;//计算方式 0或则null为重量 1 为件数 2为体积

    private Long orderCouponPostageId;//运费订单id

    private  Long cashApplyId;//提现申请表的ID，商家缴纳服务费视为负提现

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }

    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_expire() {
        return time_expire;
    }

    public void setTime_expire(String time_expire) {
        this.time_expire = time_expire;
    }

    public String getGoods_tag() {
        return goods_tag;
    }

    public void setGoods_tag(String goods_tag) {
        this.goods_tag = goods_tag;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getLimit_pay() {
        return limit_pay;
    }

    public void setLimit_pay(String limit_pay) {
        this.limit_pay = limit_pay;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Long getSoId() {
        return soId;
    }

    public void setSoId(Long soId) {
        this.soId = soId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getSubMchId() {
        return subMchId;
    }

    public void setSubMchId(String subMchId) {
        this.subMchId = subMchId;
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public BigDecimal getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(BigDecimal servicePrice) {
        this.servicePrice = servicePrice;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderCouponId() {
        return orderCouponId;
    }

    public void setOrderCouponId(Long orderCouponId) {
        this.orderCouponId = orderCouponId;
    }

    public BigDecimal getRealWeight() {
        return realWeight;
    }

    public void setRealWeight(BigDecimal realWeight) {
        this.realWeight = realWeight;
    }

    public Long getOrderAddressId() {
        return orderAddressId;
    }

    public void setOrderAddressId(Long orderAddressId) {
        this.orderAddressId = orderAddressId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Integer getPiece() {
        return piece;
    }

    public void setPiece(Integer piece) {
        this.piece = piece;
    }

    public BigDecimal getBulk() {
        return bulk;
    }

    public void setBulk(BigDecimal bulk) {
        this.bulk = bulk;
    }

    public Integer getFormulaMode() {
        return formulaMode;
    }

    public void setFormulaMode(Integer formulaMode) {
        this.formulaMode = formulaMode;
    }

    public Long getOrderCouponPostageId() {
        return orderCouponPostageId;
    }

    public void setOrderCouponPostageId(Long orderCouponPostageId) {
        this.orderCouponPostageId = orderCouponPostageId;
    }

    public Long getCashApplyId() {
        return cashApplyId;
    }

    public void setCashApplyId(Long cashApplyId) {
        this.cashApplyId = cashApplyId;
    }
}
