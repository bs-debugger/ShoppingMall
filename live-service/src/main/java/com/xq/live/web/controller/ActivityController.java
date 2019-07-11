package com.xq.live.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.service.ActivityRecommendService;
import com.xq.live.vo.in.ActivityLayoutInVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/activity")
@Api(value = "Activity", tags = "活动布局/推荐")
@RestController
public class ActivityController {

    @Autowired
    private ActivityRecommendService activityRecommendService;

    @PostMapping("/findActivityRecommendPage")
    @ApiOperation("查询活动推荐")
    public BaseResp<JSONObject> findActivityRecommendPage(@RequestBody ActivityLayoutInVo activityLayoutInVo) {
        return new BaseResp<>(ResultStatus.SUCCESS, activityRecommendService.findActivityRecommendPage(activityLayoutInVo));
    }

}
