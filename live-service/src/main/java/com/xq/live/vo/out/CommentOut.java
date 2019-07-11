package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-03-07 20:05
 * @copyright:hbxq
 **/
public class CommentOut implements Comparable<CommentOut>{
    private Long id;

    @NotNull(message = "refId必填")
    private Long refId; //关联的id
    @NotNull(message = "cmtType必填")
    private Integer cmtType;  //评论类型 1 活动 2 主题 3 直播 4 评论 5 商家
    @NotNull(message = "content必填")
    private String content;
    @NotNull(message = "userId必填")
    private Long userId;
    @NotNull(message = "userName必填")
    private String userName;
    @NotNull(message = "nickName必填")
    private String nickName;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String userIp;

    private String userPic;  //用户头像

    private int zan;  //点赞数

    private Integer isZan;   //是否点赞 0 未点 1 点过

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

    public Integer getCmtType() {
        return cmtType;
    }

    public void setCmtType(Integer cmtType) {
        this.cmtType = cmtType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
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
        this.userName = userName == null ? null : userName.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp == null ? null : userIp.trim();
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public int getZan() {
        return zan;
    }

    public void setZan(int zan) {
        this.zan = zan;
    }

    public Integer getIsZan() {
        return isZan;
    }

    public void setIsZan(Integer isZan) {
        this.isZan = isZan;
    }

    @Override
    public int compareTo(CommentOut o) {
        int i = o.getZan() - this.getZan();
        return i;
    }
}
