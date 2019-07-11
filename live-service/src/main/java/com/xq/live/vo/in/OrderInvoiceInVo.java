package com.xq.live.vo.in;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by lipeng on 2018/12/24.
 */
public class OrderInvoiceInVo extends BaseInVo{

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "订单编号")
    private String orderCode;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "开票手机")
    private String mobile;

    @ApiModelProperty(value = "开票手机 1电子发票  2纸质发票")
    private Integer sourceType;

    @ApiModelProperty(value = "发票类型 1个人  2企业")
    private Integer initialType;

    @ApiModelProperty(value = "审批状态 1 待审批 2 审批通过 3 审批不通过")
    private Integer applyStatus;

    @ApiModelProperty(value = "发票金额")
    private BigDecimal invoiceAmount;

    @ApiModelProperty(value = "邮箱地址")
    private String email;

    @ApiModelProperty(value = "发票抬头")
    private String invoiceOpen;

    @ApiModelProperty(value = "税号")
    private String dutyParagraph;

    @ApiModelProperty(value = "收件地址")
    private Long orderAddressId;

    @ApiModelProperty(value = "商品纳税类型")
    private Integer goodsSkuDutyType;

    @ApiModelProperty(value = "是否删除(0未删除  1删除)")
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "开始时间")
    private String beginTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "查询条件")
    private String keywords;

    @ApiModelProperty(value = "是否发送短信 1发 2不发")
    private int isSend;

    @ApiModelProperty(value = "驳回原因")
    private String content;

    public String getKeywords() {
        return keywords;
    }

    @ApiModelProperty(value = "id数组")
    private String ids;

    @ApiModelProperty(value = "id数组长度")
    private int idsLength;

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public int getIsSend() {
        return isSend;
    }

    public void setIsSend(int isSend) {
        this.isSend = isSend;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String remake) {
        this.content = remake;
    }

    public int getIdsLength() {
        return idsLength;
    }

    public void setIdsLength(int idsLength) {
        this.idsLength = idsLength;
    }
}
