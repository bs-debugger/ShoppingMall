package com.xq.live.vo.in;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @Description 活动入参
 * Created by zhangpeng32 on 2018/2/7.
 */
public class ActInfoInVo extends BaseInVo {
    @ApiModelProperty(value = "活动id")
    private Long id;
    @ApiModelProperty(value = "活动名字")
    private String actName;

    private Long userId;

    private String userName;

    private String userIp;

    private Integer sourceType;
    @ApiModelProperty(value = "活动详情描述")
    private String actDesc;

    private Long actSku;
    @ApiModelProperty(value = "活动范围1.小程序;2,app")
    private Integer actRange;//活动范围：1.小程序;2,app

    private Long shopId;
    @ApiModelProperty(value = "类型 0为全部(包括参与的和未参与的) 1只查询参与的")
    private Integer type;//类型 0为全部(包括参与的和未参与的) 1只查询参与的
    @ApiModelProperty(value = "活动报名开始时间")
    private Date applyStartTime;
    @ApiModelProperty(value = "活动报名结束时间")
    private Date applyEndTime;
    @ApiModelProperty(value = "活动报名规则")
    private String applyRules;

    public Long getActSku() {
        return actSku;
    }

    public void setActSku(Long actSku) {
        this.actSku = actSku;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
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

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public String getActDesc() {
        return actDesc;
    }

    public Integer getActRange() {
        return actRange;
    }

    public void setActRange(Integer actRange) {
        this.actRange = actRange;
    }

    public void setActDesc(String actDesc) {
        this.actDesc = actDesc;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getApplyStartTime() {
        return applyStartTime;
    }

    public void setApplyStartTime(Date applyStartTime) {
        this.applyStartTime = applyStartTime;
    }

    public Date getApplyEndTime() {
        return applyEndTime;
    }

    public void setApplyEndTime(Date applyEndTime) {
        this.applyEndTime = applyEndTime;
    }

    public String getApplyRules() {
        return applyRules;
    }

    public void setApplyRules(String applyRules) {
        this.applyRules = applyRules;
    }
}
