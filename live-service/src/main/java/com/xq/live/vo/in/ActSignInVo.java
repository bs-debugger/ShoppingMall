package com.xq.live.vo.in;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 新平台活动报名材料入参
 * Created by lipeng on 2018/4/28.
 */
public class ActSignInVo {

    private Long id;

    @NotNull(message = "actId必填")
    private Long actId;//活动id

    @NotNull(message = "refId必填")
    private Long refId;//关联id   现在针对的是选手和商家

    @NotNull(message = "type必填")
    private Integer type;//类型 1选手 2商家

    private Long skuId;

    private String shopVideo;

    private String playerVideo;

    private Byte isDeleted;//是否删除 0否 1是

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private String skuName;//推荐菜的名字

    private String skuInfo;//推荐菜的简介

    private String picUrl;//推荐菜的图片

    private Integer age;//年龄

    private Integer height;//身高

    private String picIds;//图片和视频组

    private String actUserName;//报名参加填写的名字

    private String userInfo;//选手简介


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

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getShopVideo() {
        return shopVideo;
    }

    public void setShopVideo(String shopVideo) {
        this.shopVideo = shopVideo == null ? null : shopVideo.trim();
    }

    public String getPlayerVideo() {
        return playerVideo;
    }

    public void setPlayerVideo(String playerVideo) {
        this.playerVideo = playerVideo == null ? null : playerVideo.trim();
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuInfo() {
        return skuInfo;
    }

    public void setSkuInfo(String skuInfo) {
        this.skuInfo = skuInfo;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getPicIds() {
        return picIds;
    }

    public void setPicIds(String picIds) {
        this.picIds = picIds;
    }

    public String getActUserName() {
        return actUserName;
    }

    public void setActUserName(String actUserName) {
        this.actUserName = actUserName;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }
}
