package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by lipeng on 2018/12/24.
 */
public class OrderInvoiceOut {
    private Long id;

    private String orderCode;

    private Long userId;

    private String mobile;

    private Integer sourceType;

    private Integer initialType;

    private Integer applyStatus;

    private BigDecimal invoiceAmount;

    private String email;

    private String invoiceOpen;

    private String dutyParagraph;

    private Long orderAddressId;

    private Integer goodsSkuDutyType;

    private Integer isDeleted;

    @Transient
    private String chatName;//联系人

    @Transient
    private String addressMobile;//手机号

    @Transient
    private String detailAddress;//详细地址

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getAddressMobile() {
        return addressMobile;
    }

    public void setAddressMobile(String addressMobile) {
        this.addressMobile = addressMobile;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    private OrderAddressOut orderAddressOut;//发票开票接收地址信息

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public Integer getInitialType() {
        return initialType;
    }

    public void setInitialType(Integer initialType) {
        this.initialType = initialType;
    }

    public Integer getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Integer applyStatus) {
        this.applyStatus = applyStatus;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getInvoiceOpen() {
        return invoiceOpen;
    }

    public void setInvoiceOpen(String invoiceOpen) {
        this.invoiceOpen = invoiceOpen == null ? null : invoiceOpen.trim();
    }

    public String getDutyParagraph() {
        return dutyParagraph;
    }

    public void setDutyParagraph(String dutyParagraph) {
        this.dutyParagraph = dutyParagraph == null ? null : dutyParagraph.trim();
    }

    public Long getOrderAddressId() {
        return orderAddressId;
    }

    public void setOrderAddressId(Long orderAddressId) {
        this.orderAddressId = orderAddressId;
    }

    public Integer getGoodsSkuDutyType() {
        return goodsSkuDutyType;
    }

    public void setGoodsSkuDutyType(Integer goodsSkuDutyType) {
        this.goodsSkuDutyType = goodsSkuDutyType;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
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

    public OrderAddressOut getOrderAddressOut() {
        return orderAddressOut;
    }

    public void setOrderAddressOut(OrderAddressOut orderAddressOut) {
        this.orderAddressOut = orderAddressOut;
    }
}
