package com.xq.live.vo.out;

import java.util.Date;

public class OrderCouponSendOut {
    private Long id;

    private String orderCouponCode;

    private Long sendUserId;

    private Long receiveUserId;

    private Date createTime;

    private Date updateTime;

    private Integer versionNo;//版本号

    private String sendUserName;//赠送人用户名字

    private String sendIconUrl;//赠送人用户头像

    private String sendNickName;

    private String receiveUserName;//接收人用户名字

    private String receiveIconUrl;//接收人用户头像

    private String receiveNickName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderCouponCode() {
        return orderCouponCode;
    }

    public void setOrderCouponCode(String orderCouponCode) {
        this.orderCouponCode = orderCouponCode;
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

    public Integer getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public String getSendIconUrl() {
        return sendIconUrl;
    }

    public void setSendIconUrl(String sendIconUrl) {
        this.sendIconUrl = sendIconUrl;
    }

    public String getReceiveUserName() {
        return receiveUserName;
    }

    public void setReceiveUserName(String receiveUserName) {
        this.receiveUserName = receiveUserName;
    }

    public String getReceiveIconUrl() {
        return receiveIconUrl;
    }

    public void setReceiveIconUrl(String receiveIconUrl) {
        this.receiveIconUrl = receiveIconUrl;
    }

    public String getSendNickName() {
        return sendNickName;
    }

    public void setSendNickName(String sendNickName) {
        this.sendNickName = sendNickName;
    }

    public String getReceiveNickName() {
        return receiveNickName;
    }

    public void setReceiveNickName(String receiveNickName) {
        this.receiveNickName = receiveNickName;
    }
}
