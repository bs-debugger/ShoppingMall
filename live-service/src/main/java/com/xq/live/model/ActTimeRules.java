package com.xq.live.model;

import java.util.Date;

public class ActTimeRules {
    private Long id;

    private Integer ruleType;//关联类型(0 商品关联act_goods_sku ,1 商家act_shop

    private Long refId;//关联id

    private Date createTime;

    private Date updateTime;

    private Integer isDeleted;

    private String weekDays;//可用日期(周)逗号分隔

    private Integer startHour;//可用开始时间(时)

    private Integer startMinuts;//可用开始时间(分)

    private Integer endHour;//可用结束时间(时)

    private Integer endMinuts;//可用结束时间(分)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRuleType() {
        return ruleType;
    }

    public void setRuleType(Integer ruleType) {
        this.ruleType = ruleType;
    }

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
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

    public String getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(String weekDays) {
        this.weekDays = weekDays == null ? null : weekDays.trim();
    }

    public Integer getStartHour() {
        return startHour;
    }

    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }

    public Integer getStartMinuts() {
        return startMinuts;
    }

    public void setStartMinuts(Integer startMinuts) {
        this.startMinuts = startMinuts;
    }

    public Integer getEndHour() {
        return endHour;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }

    public Integer getEndMinuts() {
        return endMinuts;
    }

    public void setEndMinuts(Integer endMinuts) {
        this.endMinuts = endMinuts;
    }
}
