package com.xq.live.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class MessageText {
    /**
     * 站内信类型 1 系统消息 2 公共消息 3 个人消息
     */
    public final static int MESSAGE_TEXT_TYPE_SYSTEM = 1;

    public final static int MESSAGE_TEXT_TYPE_COMMON = 2;

    public final static int MESSAGE_TEXT_TYPE_PRIVATE = 3;


    private Long id;

    private String title;

    private String content;

    private Integer type;

    private Integer isDeleted;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

    /**
     * 关联的活动id
     */
    private  Long actId;

    private String url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
