package com.xq.live.vo.in;

import io.swagger.annotations.ApiModelProperty;

public class PlatformReconciliationInVo  extends BaseInVo{

    @ApiModelProperty(value = "开始时间")
    private String beginTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;//结束时间

    @ApiModelProperty(value = "查询条件")
    private String keywords;

    @ApiModelProperty(value = "订单类型 普通型_1,砍价型_4,秒杀型_5,抽奖型_6,团购型_7")
    private Integer flagType;

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Integer getFlagType() {
        return flagType;
    }

    public void setFlagType(Integer flagType) {
        this.flagType = flagType;
    }
}
