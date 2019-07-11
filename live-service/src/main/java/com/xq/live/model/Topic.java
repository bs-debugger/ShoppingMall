package com.xq.live.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 主题实体类
 */
public class Topic {
    private Long id;

    @NotBlank(message = "title必填")
    private String title;

    private String content;

    private String summary;     //摘要

    private String homePic;     //封面

    private String smallHomePic;     //封面裁剪图

    private String picIds;  //主题包含的图片id

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    //@NotNull(message = "userId必填")
    private Long userId;

    //@NotNull(message = "userName必填")
    private String userName;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private Integer isDeleted;

    private Integer tpStatus;

    private List<Attachment> picUrls;

    private String nickName;

    private Integer hitNum;

    private Integer topicType;//文章类型

    private Integer transNum;//转发量

    private Integer shopType;//1 平台视频文章 2用户视频文章

    public Integer getShopType() {
        return shopType;
    }

    public void setShopType(Integer shopType) {
        this.shopType = shopType;
    }

    public String getSmallHomePic() {
        return smallHomePic;
    }

    public void setSmallHomePic(String smallHomePic) {
        this.smallHomePic = smallHomePic;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getTpStatus() {
        return tpStatus;
    }

    public void setTpStatus(Integer tpStatus) {
        this.tpStatus = tpStatus;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPicIds() {
        return picIds;
    }

    public void setPicIds(String picIds) {
        this.picIds = picIds;
    }

    public List<Attachment> getPicUrls() {
        return picUrls;
    }

    public void setPicUrls(List<Attachment> picUrls) {
        this.picUrls = picUrls;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getHomePic() {
        return homePic;
    }

    public void setHomePic(String homePic) {
        this.homePic = homePic;
    }

    public Integer getHitNum() {
        return hitNum;
    }

    public void setHitNum(Integer hitNum) {
        this.hitNum = hitNum;
    }

    public Integer getTopicType() {
        return topicType;
    }

    public void setTopicType(Integer topicType) {
        this.topicType = topicType;
    }

    public Integer getTransNum() {
        return transNum;
    }

    public void setTransNum(Integer transNum) {
        this.transNum = transNum;
    }
}
