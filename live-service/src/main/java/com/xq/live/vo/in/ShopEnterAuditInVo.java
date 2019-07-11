package com.xq.live.vo.in;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class ShopEnterAuditInVo {

    @NotNull(message = "商家入驻ID不能为空")
    @ApiModelProperty("商家入驻ID")
    private Long shopEnterId;

    @ApiModelProperty("审核说明")
    private String memo;

    @ApiModelProperty("审核附加的图片组（多个用“,”隔开）")
    private String pictures;

    @ApiModelProperty("是否发送短信（0：不发送， 1：发送短信）")
    private Boolean sendMsg;

    @NotNull(message = "审核通过或失败？")
    @ApiModelProperty("审核（0：驳回，1：通过）")
    private Boolean status;

    public Long getShopEnterId() {
        return shopEnterId;
    }

    public void setShopEnterId(Long shopEnterId) {
        this.shopEnterId = shopEnterId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public Boolean getSendMsg() {
        return sendMsg;
    }

    public void setSendMsg(Boolean sendMsg) {
        this.sendMsg = sendMsg;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
