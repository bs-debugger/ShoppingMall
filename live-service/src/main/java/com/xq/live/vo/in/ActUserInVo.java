package com.xq.live.vo.in;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 活动选手入参
 * Created by lipeng on 2018/4/27.
 */
public class ActUserInVo extends BaseInVo{

    private Long id;

    @NotNull(message = "actId必填")
    private Long actId;//活动id

    //@NotNull(message = "userId必填")
    private Long userId;//选手id

    private String userCode;//选手编号

    @NotNull(message = "actUserName必填")
    private String actUserName;//报名参加填写的名字

    @NotNull(message = "picIds必填")
    private String picIds;//图片和视频组

    private Byte applyStatus;//审批状态

    @NotNull(message = "age必填")
    private Integer age;//年龄

    @NotNull(message = "height必填")
    private Integer height;//身高

    private Integer voteNum;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;//开始时间

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;//截止时间

    private Long voteUserId;//投票人的userId

    private Integer type;//查询类型  null 不分组，查询单个list   2 分组 ，查询分组后的信息

    private Integer isLuoxuan;//是否落选

    private Integer sortType;//参与报名的选手排序规则  不传 最热  1最新

    private String userInfo;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode == null ? null : userCode.trim();
    }

    public String getActUserName() {
        return actUserName;
    }

    public void setActUserName(String actUserName) {
        this.actUserName = actUserName == null ? null : actUserName.trim();
    }

    public String getPicIds() {
        return picIds;
    }

    public void setPicIds(String picIds) {
        this.picIds = picIds == null ? null : picIds.trim();
    }

    public Byte getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Byte applyStatus) {
        this.applyStatus = applyStatus;
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

    public Integer getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(Integer voteNum) {
        this.voteNum = voteNum;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getVoteUserId() {
        return voteUserId;
    }

    public void setVoteUserId(Long voteUserId) {
        this.voteUserId = voteUserId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsLuoxuan() {
        return isLuoxuan;
    }

    public void setIsLuoxuan(Integer isLuoxuan) {
        this.isLuoxuan = isLuoxuan;
    }

    public Integer getSortType() {
        return sortType;
    }

    public void setSortType(Integer sortType) {
        this.sortType = sortType;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }
}
