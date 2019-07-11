package com.xq.live.model;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class OrderInvoice {
    /**
     * applyStatus 1 待审批 2 审批通过 3 审批不通过
     */
    public final static int APPLY_STATUS_WAIT = 1;   //待审批

    public final static int APPLY_STATUS_SUCCESS = 2;   //审批通过

    public final static int APPLY_STATUS_FAIL = 3;   //审批不通过


    private Long id;

    @NotNull(message = "orderCode不能为空")
    private String orderCode;

    private Long userId;

    private String mobile;

    @NotNull(message = "sourceType不能为空")
    private Integer sourceType;

    @NotNull(message = "initialType不能为空")
    private Integer initialType;


    private Integer applyStatus;

    @NotNull(message = "invoiceAmount不能为空")
    private BigDecimal invoiceAmount;

    private String email;

    @NotNull(message = "invoiceOpen不能为空")
    private String invoiceOpen;

    private String dutyParagraph;

    private Long orderAddressId;

    private Integer goodsSkuDutyType;

    private Integer isDeleted;

    private Date createTime;

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
}
