package com.xq.live.web.controllerForManage;

import com.github.pagehelper.PageInfo;
import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.service.ShopEnterService;
import com.xq.live.vo.in.ShopEnterAuditInVo;
import com.xq.live.vo.in.ShopEnterInVo;
import com.xq.live.vo.out.ShopEnterOut;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/manage/shopEnter")
@Api(value = "ShopEnterForManage", tags = "商家入驻")
@RestController
public class ShopEnterForManageController {

    @Autowired
    private ShopEnterService shopEnterService;

    @PostMapping("/showList")
    @ApiOperation("分页查询商家入驻列表")
    public BaseResp<PageInfo<ShopEnterOut>> showList(ShopEnterInVo ShopEnterInVo) {
        return new BaseResp<>(ResultStatus.SUCCESS, shopEnterService.showList(ShopEnterInVo));
    }

    @PutMapping("/operationById")
    @ApiOperation("根据ID操作商家入驻信息（true通过/false驳回）")
    public BaseResp<Boolean> operationById(@Valid ShopEnterAuditInVo shopEnterAuditInVo) {
        return new BaseResp<>(ResultStatus.SUCCESS, shopEnterService.operationById(shopEnterAuditInVo));
    }

}
