package com.xq.live.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 新平台活动报名材料实体类
 */
public class ActSign {

    /**
     * 是否删除
     */
    public final static int ACT_SIGN_NO_DELETED = 0; //未删除

    public final static int ACT_SIGN_IS_DELETED = 1;//已删除

    /**
     * type 1 选手  2 商家
     */
    public final static int ACT_SIGN_TYPE_PLAYER = 1;   //选手

    public final static int ACT_SIGN_TYPE_SHOP = 2;   //商家

    private Long id;

    private Long actId;

    private Long refId;

    private Integer type;

    private Long skuId;

    private String shopVideo;

    private String playerVideo;

    private Byte isDeleted;

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
