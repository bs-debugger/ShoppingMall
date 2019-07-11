package com.xq.live.model;

import java.io.Serializable;
import java.util.Date;

public class ShopEnterAuditLog implements Serializable {

    private static final long serialVersionUID = -1665904919470736508L;

    private Long id;

    private Long shopEnterId;

    private String memo;

    private String pictures;

    private Boolean sendMsg;

    private Boolean status;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
        this.memo = memo == null ? null : memo.trim();
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures == null ? null : pictures.trim();
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