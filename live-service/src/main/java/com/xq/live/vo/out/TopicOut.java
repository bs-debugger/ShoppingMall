package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xq.live.model.Attachment;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 主题实体类
 */
public class TopicOut {
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

    @NotNull(message = "userId必填")
    private Long userId;

    @NotNull(message = "userName必填")
    private String userName;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private Integer isDeleted;//是否删除 0 否 1 是

    private Integer tpStatus;//状态 0 待审 1 审核通过 2审核不通过

    private List<Attachment> picUrls;//上传图片组

    private String nickName;//昵称

    private String iconUrl;//文章用户的头像

    private int commentNum;//文章列表的评论数

    private Integer zan;//点赞数

    private Integer isZan;//是否点赞

    private Integer viewNum;//浏览数目

    private Integer topicType;//文章类型

    private Integer hitNum;//浏览量(根据相同用户--可刷访问次数)

    private Integer userType;//发表文章用户的类型  1普通用户 2 商家用户

    private Integer transNum;//转发量

    private Integer shopType;//

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

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public Integer getZan() {
        return zan;
    }

    public void setZan(Integer zan) {
        this.zan = zan;
    }

    public Integer getIsZan() {
        return isZan;
    }

    public void setIsZan(Integer isZan) {
        this.isZan = isZan;
    }

    public Integer getViewNum() {
        return viewNum;
    }

    public void setViewNum(Integer viewNum) {
        this.viewNum = viewNum;
    }

    public Integer getTopicType() {
        return topicType;
    }

    public void setTopicType(Integer topicType) {
        this.topicType = topicType;
    }

    public Integer getHitNum() {
        return hitNum;
    }

    public void setHitNum(Integer hitNum) {
        this.hitNum = hitNum;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getTransNum() {
        return transNum;
    }

    public void setTransNum(Integer transNum) {
        this.transNum = transNum;
    }
}
