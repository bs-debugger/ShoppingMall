package com.xq.live.vo.in;

import io.swagger.annotations.ApiModelProperty;

public class ShopAccountInVo extends BaseInVo {

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "用户编号")
    private Integer userId;

    @ApiModelProperty(value = "商铺编号")
    private Integer shopId;

    @ApiModelProperty(value = "商铺名称")
    private String shopName;

    @ApiModelProperty(value = "提现编号")
    private Integer cashId;

    @ApiModelProperty(value = "审批状态")
    private Integer applyStatus;

    @ApiModelProperty(value = "是否发送短信: 1 是 0 否")
    private Integer isSend;

    @ApiModelProperty(value = "驳回原因")
    private String content;

    @ApiModelProperty(value = "图片路径")
    private String imageUrl;

    @ApiModelProperty(value = "用户类型(1.平台用户 2.商户)")
    private Integer userType;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getCashId() {
        return cashId;
    }

    public void setCashId(Integer cashId) {
        this.cashId = cashId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Integer getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Integer applyStatus) {
        this.applyStatus = applyStatus;
    }

    public Integer getIsSend() {
        return isSend;
    }

    public void setIsSend(Integer isSend) {
        this.isSend = isSend;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }
}
