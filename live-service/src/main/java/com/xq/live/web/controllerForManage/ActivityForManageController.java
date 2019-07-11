package com.xq.live.web.controllerForManage;

import com.alibaba.fastjson.JSONObject;
import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.service.ActivityRecommendService;
import com.xq.live.vo.in.ActivityLayoutInVo;
import com.xq.live.vo.in.ActivityRecommendInVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RequestMapping("/manage/activity")
@Api(value = "ActivityForManage", tags = "活动推荐")
@RestController
public class ActivityForManageController {

    @Autowired
    private ActivityRecommendService activityRecommendService;

    @PostMapping("/findActivityRecommend")
    @ApiOperation("查询活动推荐")
    public BaseResp<JSONObject> findActivityRecommend(@RequestBody ActivityLayoutInVo activityLayoutInVo) {
        return new BaseResp<>(ResultStatus.SUCCESS, activityRecommendService.findActivityRecommendPage(activityLayoutInVo));
    }

    @PostMapping("/saveActivityRecommend")
    @ApiOperation("保存活动推荐")
    public BaseResp<JSONObject> saveActivityRecommend(@Valid @RequestBody ActivityRecommendInVo activityRecommendInVo) {
        return new BaseResp<>(ResultStatus.SUCCESS, activityRecommendService.saveActivityRecommend(activityRecommendInVo));
    }

    @PostMapping("/sort")
    @ApiOperation("对换两个活动的排序")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "srcActivityRecommendId", value = "原活动推荐ID", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "targetActivityRecommendId", value = "要互换排序的活动推荐ID", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "activityType", value = "活动类型", paramType = "query", dataType = "int")
    })
    public BaseResp<JSONObject> sort(@ApiIgnore Integer srcActivityRecommendId, @ApiIgnore Integer targetActivityRecommendId, @ApiIgnore Integer activityType) {
        return new BaseResp<>(ResultStatus.SUCCESS, activityRecommendService.sort(srcActivityRecommendId, targetActivityRecommendId, activityType));
    }

    @DeleteMapping("/removeById/{id}")
    @ApiOperation("根据活动推荐ID删除活动推荐")
    @ApiImplicitParam(name = "id", value = "活动推荐ID", paramType = "path", dataType = "int")
    public BaseResp<JSONObject> removeById(@ApiIgnore @PathVariable("id") Integer id) {
        return new BaseResp<>(ResultStatus.SUCCESS, activityRecommendService.removeById(id));
    }

}
