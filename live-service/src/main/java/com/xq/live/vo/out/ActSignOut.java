package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 新平台活动材料出参
 * Created by lipeng on 2018/4/28.
 */
public class ActSignOut {

    private Long id;

    private Long actId;

    private Long refId;//关联id   现在针对的是选手和商家

    private Integer type;//类型 1选手 2商家

    private Long skuId;

    private String shopVideo;

    private String playerVideo;

    private Byte isDeleted;//是否删除 0否 1是

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

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
}
