package com.xq.live.model;

import java.util.Date;

public class Message {
    /**
     * 消息状态 0 未读 1 已读
     */
    public final static int MESSAGE_STATUS_READ = 1;

    public final static int MESSAGE_STATUS_UNREAD = 0;

    /**
     * 消息发送人 id  0 系统消息  1 公共消息 - others
     */
    public final static long MESSAGE_SEND_ID_SYSTEM = 0l;

    public final static long MESSAGE_SEND_ID_COMMON = 1l;

    public final static long MESSAGE_SEND_ID_OTHER = -1l;

    private Long id;

    private Long senderId;

    private Long receiverId;

    private Long messageTextId;

    private Integer status;

    private Integer isDeleted;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Long getMessageTextId() {
        return messageTextId;
    }

    public void setMessageTextId(Long messageTextId) {
        this.messageTextId = messageTextId;
    }
}