package com.xq.live.vo.out;

import java.util.Date;

/**
 * 活动主题出餐
 * Created by lipeng on 2018/6/29.
 */
public class ActTopicOut {
    private Long id;

    private Long actId;

    private Long topicId;

    private String actTopicCode;

    private Long userId;

    private Integer applyStatus;

    private Integer voteNum;

    private Date createTime;

    private Date updateTime;

    private String title;//文章标题

    private String summary;//文章摘要

    private String homePic;

    private String content;//文章内容

    private Integer hitNum;//文章点击量

    private Integer topicType;//文章类型

    private Integer transNum;//文章转发量

    private String userName;//用户名称

    private String nickName;//用户昵称

    private String iconUrl;//用户头像

    private Integer userType;//用户类型

    private Integer zan;//点赞数目

    private Integer isZan;//是否点赞 0未赞   大于0已赞

    private Integer zanTotal;//点赞的总数目

    private Integer actHitNum;//此活动中关于文章的所有浏览量

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getActTopicCode() {
        return actTopicCode;
    }

    public void setActTopicCode(String actTopicCode) {
        this.actTopicCode = actTopicCode == null ? null : actTopicCode.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Integer applyStatus) {
        this.applyStatus = applyStatus;
    }

    public Integer getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(Integer voteNum) {
        this.voteNum = voteNum;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
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

    public Integer getZanTotal() {
        return zanTotal;
    }

    public void setZanTotal(Integer zanTotal) {
        this.zanTotal = zanTotal;
    }

    public Integer getActHitNum() {
        return actHitNum;
    }

    public void setActHitNum(Integer actHitNum) {
        this.actHitNum = actHitNum;
    }

    public String getHomePic() {
        return homePic;
    }

    public void setHomePic(String homePic) {
        this.homePic = homePic;
    }
}
