package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by lipeng on 2018/4/19.
 */
public class SoWriteOffOut {

    private Long id;

    @NotNull(message = "soId必填")
    private Long soId;

    private Long shopId;

    private String shopName;

    @NotNull(message = "shopAmount必填")
    private BigDecimal shopAmount;

    @NotNull(message = "couponId必填")
    private Long couponId;

    @NotNull(message = "couponCode必填")
    private String couponCode;

    @NotNull(message = "skuId必填")
    private Long skuId;

    @NotNull(message = "couponAmount必填")
    private BigDecimal couponAmount;//卷面值

    @NotNull(message = "userId必填")
    private Long userId;//卷所属订单的userId

    @NotNull(message = "userName必填")
    private String userName;//卷所属订单的userId

    @NotNull(message = "cashierId必填")
    private Long cashierId;//收银员的id---当前操作的用户id

    @NotNull(message = "cashierName必填")
    private String cashierName;

    private BigDecimal paidAmount;//支付金额(已经减去了卷面值的)

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//等同于核销时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private BigDecimal totalPrice;//总营业额

    private BigDecimal totalService;//总服务费

    private BigDecimal totalNoService;//未结算的总服务费

    private BigDecimal servicePrice;//单个服务费

    private Integer isBill;//是否对账 0未对账 1对账

    private String skuName;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDoTime;//开始时间

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDoTime;//结束时间

    public Date getStartDoTime() {
        return startDoTime;
    }

    public void setStartDoTime(Date startDoTime) {
        this.startDoTime = startDoTime;
    }

    public Date getEndDoTime() {
        return endDoTime;
    }

    public void setEndDoTime(Date endDoTime) {
        this.endDoTime = endDoTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSoId() {
        return soId;
    }

    public void setSoId(Long soId) {
        this.soId = soId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    public BigDecimal getShopAmount() {
        return shopAmount;
    }

    public void setShopAmount(BigDecimal shopAmount) {
        this.shopAmount = shopAmount;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode == null ? null : couponCode.trim();
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public BigDecimal getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
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

    public Long getCashierId() {
        return cashierId;
    }

    public void setCashierId(Long cashierId) {
        this.cashierId = cashierId;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName == null ? null : cashierName.trim();
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalService() {
        return totalService;
    }

    public void setTotalService(BigDecimal totalService) {
        this.totalService = totalService;
    }

    public Integer getIsBill() {
        return isBill;
    }

    public void setIsBill(Integer isBill) {
        this.isBill = isBill;
    }

    public BigDecimal getTotalNoService() {
        return totalNoService;
    }

    public void setTotalNoService(BigDecimal totalNoService) {
        this.totalNoService = totalNoService;
    }

    public BigDecimal getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(BigDecimal servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }
}
