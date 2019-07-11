package com.xq.live.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class VipCard {

    /**
     * 状态(0 正常,1 禁用,2 挂失 )
     */
    public final static int VIP_STATUS_NORMAL=0;

    public final static int VIP_STATUS_FORBIDDEN=1;

    public final static int VIP_STATUS_REPORT=2;

    /**
     * 有效期类型 0 永久生效 1 期限生效
     */
    public final static int VIP_TIMELIMITTYPE_PERMANENT=0;

    public final static int VIP_TIMELIMITTYPE_LIMIT=1;

    private Long id;

    private Long vipTypeId;

    private Long userId;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private Integer isDeleted;

    private String userName;//会员名称

    private String remark;//备注

    private String cardNo;//会员卡号

    private String showNo;//展示卡号

    private Integer status;//状态(0 正常,1 禁用,2 挂失 )

    private Integer grade;//会员等级

    private String gradeName;//会员等级名称

    private BigDecimal discount;//会员折扣

    private BigDecimal accountAmount;//余额

    private Integer availablePoints;//可用积分

    private Integer totalPoints;//累计积分

    private Integer timeLimitType;//有效期类型 0 永久生效 1 期限生效

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expiryDate;//到期时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVipTypeId() {
        return vipTypeId;
    }

    public void setVipTypeId(Long vipTypeId) {
        this.vipTypeId = vipTypeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo == null ? null : cardNo.trim();
    }

    public String getShowNo() {
        return showNo;
    }

    public void setShowNo(String showNo) {
        this.showNo = showNo == null ? null : showNo.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName == null ? null : gradeName.trim();
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(BigDecimal accountAmount) {
        this.accountAmount = accountAmount;
    }

    public Integer getAvailablePoints() {
        return availablePoints;
    }

    public void setAvailablePoints(Integer availablePoints) {
        this.availablePoints = availablePoints;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Integer getTimeLimitType() {
        return timeLimitType;
    }

    public void setTimeLimitType(Integer timeLimitType) {
        this.timeLimitType = timeLimitType;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
