package com.xq.live.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class PullUser {

    /*
    *1:兼职邀请新用户，2:邀请新用户吃螃蟹, 3拉新人送旅游景点门票 5参团邀请新人注册 6邀请新人注册购买秒杀商品
    * */
    public final static Integer PULL_TYPE_QZ =1;//兼职邀请新用户

    public final static Integer PULL_TYPE_PX =2;//邀请新用户吃螃蟹

    public final static Integer PULL_TYPE_LXJD =3;//拉新人送旅游景点门票

    public final static Integer PULL_TYPE_PT =5;//拉新人开团

    public final static Integer PULL_TYPE_MS =6;//邀请新人注册购买秒杀商品


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

    private Long groupId; //团id
    private Long parentId;//邀请人


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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}

