package com.xq.live.vo.in;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class UserAdvanceInVo extends BaseInVo {

    @ApiModelProperty(value = "审批状态")
    private Integer applyStatus;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "是否发送短信: 1 是 0 否")
    private Integer isSend;

    @ApiModelProperty(value = "驳回原因")
    private String content;

    @ApiModelProperty(value = "图片路径")
    private String imageUrl;

    @ApiModelProperty(value = "cashId集合")
    private List<Integer> items;

    public List<Integer> getItems() {
        return items;
    }

    public void setItems(List<Integer> items) {
        this.items = items;
    }

    public Integer getIsSend() {
        return isSend;
    }

    public void setIsSend(Integer isSend) {
        this.isSend = isSend;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Integer applyStatus) {
        this.applyStatus = applyStatus;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

}
