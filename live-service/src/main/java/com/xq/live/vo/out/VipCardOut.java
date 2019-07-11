package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xq.live.model.VipGradeConfig;
import com.xq.live.model.VipType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by admin on 2019/5/16.
 */
public class VipCardOut {
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

    private VipGradeConfig vipGradeConfig;

    private VipType vipType;

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
        this.userName = userName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getShowNo() {
        return showNo;
    }

    public void setShowNo(String showNo) {
        this.showNo = showNo;
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
        this.gradeName = gradeName;
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

    public VipGradeConfig getVipGradeConfig() {
        return vipGradeConfig;
    }

    public void setVipGradeConfig(VipGradeConfig vipGradeConfig) {
        this.vipGradeConfig = vipGradeConfig;
    }

    public VipType getVipType() {
        return vipType;
    }

    public void setVipType(VipType vipType) {
        this.vipType = vipType;
    }
}
