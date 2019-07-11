package com.xq.live.vo.in;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 活动主题入参
 * Created by lipeng on 2018/6/29.
 */
public class ActTopicInVo extends BaseInVo{
    private Long id;
    @NotNull(message = "actId必填")
    private Long actId;
    @NotNull(message = "topicId必填")
    private Long topicId;

    private String actTopicCode;
    //@NotNull(message = "userId必填")
    private Long userId;

    private Integer applyStatus;

    private Integer voteNum;

    private Date createTime;

    private Date updateTime;

    private Integer sortType;//排序类型 1最新   不传为最热

    private Integer zanUserId;//点赞人的用户id(当前登陆人的用户id)


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

    public Integer getSortType() {
        return sortType;
    }

    public void setSortType(Integer sortType) {
        this.sortType = sortType;
    }

    public Integer getZanUserId() {
        return zanUserId;
    }

    public void setZanUserId(Integer zanUserId) {
        this.zanUserId = zanUserId;
    }
}
