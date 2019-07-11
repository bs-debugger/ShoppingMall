package com.xq.live.model;

import java.util.Date;

public class SoLog {

    //操作类型 1待支付 2 已支付 3已核销 10取消
    public final static int SO_STATUS_WAIT_PAID = 1;
    public final static int SO_STATUS_PAID = 2;
    public final static int SO_STATUS_APPLIED = 3;
    public final static int SO_STATUS_CANCEL = 10;

    private Long id;

    private Long soId;

    private Integer operateType;

    private Long userId;

    private String userName;

    private Date createTime;

    private String userIp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSoId() {
        return soId;
    }

    public void setSoId(Long soId) {
        this.soId = soId;
    }

    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
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
}