package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xq.live.model.MessageText;

import java.util.Date;

/**
 * @package: com.xq.live.vo.out
 * @description: 站内信出参
 * @author: zhangpeng32
 * @date: 2018/3/28 19:10
 * @version: 1.0
 */
public class MessageOut {
    private Long id;

    private Long senderId;

    private Long receiverId;

    private Long messageTextId;

    private Integer status;

    private Integer isDeleted;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private MessageText messageText;

    private Integer total;

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

    public MessageText getMessageText() {
        return messageText;
    }

    public void setMessageText(MessageText messageText) {
        this.messageText = messageText;
    }

    public Long getMessageTextId() {
        return messageTextId;
    }

    public void setMessageTextId(Long messageTextId) {
        this.messageTextId = messageTextId;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
