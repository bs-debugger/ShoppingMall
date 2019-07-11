package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by ss on 2018/11/5.
 * 邀请新人记录
 */
public class PullUserOut {

    private Long id;

    private Long userId;

    private String picUrl;

    private Integer isDeleted;

    private Integer pullNum;//邀请人数

    private Integer pullAllNum;//总拉取人数

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private Integer type;//1:兼职邀请新用户，2:邀请新用户吃螃蟹 3;拉新人送旅游景点门票 4;核销卷送酒店入住

    private Integer haveNum;//抽取次数

    public void setHaveNum(Integer haveNum) {
        this.haveNum = haveNum;
    }

    public Integer getHaveNum() {
        return haveNum;
    }

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

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getPullNum() {
        return pullNum;
    }

    public void setPullNum(Integer pullNum) {
        this.pullNum = pullNum;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPullAllNum() {
        return pullAllNum;
    }

    public void setPullAllNum(Integer pullAllNum) {
        this.pullAllNum = pullAllNum;
    }
}
