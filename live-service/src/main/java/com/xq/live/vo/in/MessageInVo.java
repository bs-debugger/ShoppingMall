package com.xq.live.vo.in;

import javax.validation.constraints.NotNull;

/**
 * @package: com.xq.live.vo.in
 * @description: 站内信入参
 * @author: zhangpeng32
 * @date: 2018/3/28 18:58
 * @version: 1.0
 */
public class MessageInVo extends BaseInVo {
    private Long id;       //主键id
    @NotNull(message = "receiverId必填")
    private Long receiverId;    //消息接收人userId
    @NotNull(message = "type必填")
    private Integer type;       //消息类型 1 系统消息 2 公共消息 3 个人消息

    private Long messageTextId;     //消息内容id

    private Integer isDeleted;

    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getMessageTextId() {
        return messageTextId;
    }

    public void setMessageTextId(Long messageTextId) {
        this.messageTextId = messageTextId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
