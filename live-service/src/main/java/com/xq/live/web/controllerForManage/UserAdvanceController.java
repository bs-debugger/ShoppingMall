package com.xq.live.web.controllerForManage;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.service.UserAdvanceService;
import com.xq.live.vo.in.UserAdvanceInVo;
import com.xq.live.vo.out.UserAdvanceOut;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "用户提现-userAdvanceController")
@RestController
@RequestMapping("/manage/userAdvance")
public class UserAdvanceController {

    @Resource
    UserAdvanceService userAdvanceService;

    @ApiOperation(value = "用户提现记录")
    @PostMapping(value = "/searchUserAdvanceList")
    public BaseResp<Pager<UserAdvanceOut>> searchUserAdvanceList(@RequestBody UserAdvanceInVo inVo) {
        Pager<UserAdvanceOut> pager = userAdvanceService.searchUserAdvanceList(inVo);
        return new BaseResp<Pager<UserAdvanceOut>>(ResultStatus.SUCCESS, pager);
    }

    @ApiOperation(value = "审批通过(支持批量)")
    @PostMapping(value = "/batchApplyPass")
    @ApiImplicitParam(name = "items", value = "提现编号", required = true, dataType = "Integer")
    public BaseResp batchApplyPass(@RequestBody List<Integer> items) {
        return userAdvanceService.batchApplyPass(items);
    }

    @ApiOperation(value = "审批驳回(支持批量)")
    @PostMapping(value = "/batchApplyReject")
    public BaseResp batchApplyReject(@RequestBody UserAdvanceInVo inVo) {
        return userAdvanceService.batchApplyReject(inVo);
    }

}
