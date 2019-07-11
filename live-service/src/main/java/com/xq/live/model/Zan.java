package com.xq.live.model;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class Zan {
    /**
     * 点赞操作  1点赞 2取消点赞
     */
    public final static int ZAN_ADD = 1;   //点赞

    public final static int ZAN_DELETE = 2;   //取消点赞

    /**
     * 点赞类型 1 活动 2 主题 3 直播 4 评论 5 商家 6 推荐菜
     */
    public final static int ZAN_ACT = 1;

    public final static int ZAN_TOPIC = 2;

    public final static int ZAN_ZHIBO = 3;

    public final static int ZAN_COMMENT = 4;

    public final static int ZAN_SHOP = 5;

    public final static int ZAN_SKU = 6;

    private Long id;

    @NotNull(message = "refId必填")
    private Long refId;

    @NotNull(message = "type点赞类型必填")
    private Integer type;

    //@NotNull(message = "userId必填")
    private Long userId;

    private Byte isDeleted;

    private Date createTime;

    private Integer zanType;//操作类型

    private Long actId;//当对活动选手进行投票的时候,活动的actId

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getZanType() {
        return zanType;
    }

    public void setZanType(Integer zanType) {
        this.zanType = zanType;
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }
}
