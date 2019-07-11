package com.xq.live.vo.out;

import com.xq.live.model.ActInfo;

import java.util.Date;

/**
 * 签到记录出参
 */
public class ActRegisterOut {
    private Long id;

    private Long userId;

    private Long actId;

    private Integer isDeleted;

    private Date createTime;

    private Date updateTime;

    private Integer isRegister;//是否签到  0未签到  1已签到

    private Integer registerTotal;//签到总数

    private ActInfo actInfo;//活动详情

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

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
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

    public Integer getRegisterTotal() {
        return registerTotal;
    }

    public void setRegisterTotal(Integer registerTotal) {
        this.registerTotal = registerTotal;
    }

    public Integer getIsRegister() {
        return isRegister;
    }

    public void setIsRegister(Integer isRegister) {
        this.isRegister = isRegister;
    }

    public ActInfo getActInfo() {
        return actInfo;
    }

    public void setActInfo(ActInfo actInfo) {
        this.actInfo = actInfo;
    }
}
