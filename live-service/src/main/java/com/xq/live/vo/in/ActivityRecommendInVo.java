package com.xq.live.vo.in;

import com.xq.live.model.ActivityRecommend;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

public class ActivityRecommendInVo {

    @NotNull(message = "活动布局ID不能为空")
    @ApiModelProperty("活动布局ID")
    private Integer activityLayoutId; // 活动布局ID

    List<ActivityRecommend> activityRecommendList;

    public Integer getActivityLayoutId() {
        return activityLayoutId;
    }

    public void setActivityLayoutId(Integer activityLayoutId) {
        this.activityLayoutId = activityLayoutId;
    }

    public List<ActivityRecommend> getActivityRecommendList() {
        return activityRecommendList;
    }

    public void setActivityRecommendList(List<ActivityRecommend> activityRecommendList) {
        this.activityRecommendList = activityRecommendList;
    }

}
