package com.xq.live.vo.in;

import io.swagger.annotations.ApiModelProperty;

public class MerchantDetailsInVo extends BaseInVo{

    @ApiModelProperty(value = "主键id")
    private int id;

    @ApiModelProperty(value = "开始时间")
    private String beginTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;//结束时间

    @ApiModelProperty(value = "查询条件")
    private String keywords;

    @ApiModelProperty(value = "位置")
    private String position;

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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
