package com.xq.live.web.controllerForBD;

import com.github.pagehelper.PageInfo;
import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.service.BusinessAllographService;
import com.xq.live.vo.in.BusinessAllographInVo;
import com.xq.live.vo.out.BusinessAllographOut;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping("/bd/businessAllograph")
@Api(value = "BusinessAllographForBD", tags = "代签商户")
@RestController
public class BusinessAllographControllerForBD {

    @Autowired
    private BusinessAllographService businessAllographService;

    @PostMapping("/showList")
    @ApiOperation("查询代签商户列表")
    public BaseResp<PageInfo<BusinessAllographOut>> showList(@RequestBody BusinessAllographInVo businessAllographInVo) {
        return new BaseResp(ResultStatus.SUCCESS, businessAllographService.showList(businessAllographInVo));
    }

    @PostMapping("/save")
    @ApiOperation("保存代签商户信息")
    public BaseResp save(@RequestBody @Valid BusinessAllographInVo businessAllographInVo) {
        return businessAllographService.save(businessAllographInVo) ? BaseResp.success() : BaseResp.fail();
    }

}
