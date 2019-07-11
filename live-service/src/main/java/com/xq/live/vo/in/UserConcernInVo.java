package com.xq.live.vo.in;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 用户关注入参
 * Created by lipeng on 2018/7/16.
 */
public class UserConcernInVo extends BaseInVo{
    private Long id;

    @NotNull(message = "userId必填")
    private Long userId;//关注人的用户id

    @NotNull(message = "type必填")
    private Integer type;//关注类型   1关注的是用户

    @NotNull(message = "refId必填")
    private Long refId;//关联id
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private Long viewUserId;//当前用户的userId

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
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

    public Long getViewUserId() {
        return viewUserId;
    }

    public void setViewUserId(Long viewUserId) {
        this.viewUserId = viewUserId;
    }
}
