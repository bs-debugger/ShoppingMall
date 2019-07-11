package com.xq.live.model;

import java.util.Date;

public class AccessLog {

    /**
     * 用户来源 1 小程序 2 H5 3 安卓 4 IOS
     */
    public final static int SOURCE_MINI_APP = 1;

    public final static int SOURCE_H5 = 2;

    public final static int SOURCE_ANDRIOD = 3;

    public final static int SOURCE_IOS = 4;

    public final static int BIZ_TYPE_SHOP_VIEW = 1; //商家浏览日志

    public final static int BIZ_TYPE_ACT_VIEW = 2; //活动浏览日志

    public final static int BIZ_TYPE_TOPIC_VIEW = 3;//文章浏览日志

    private Long id;

    private Long refId;

    private Long userId;

    private String userName;

    private String userIp;

    private Integer source;

    private Date createTime;

    private Integer bizType;




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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp == null ? null : userIp.trim();
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getBizType() {
        return bizType;
    }

    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    }
}
