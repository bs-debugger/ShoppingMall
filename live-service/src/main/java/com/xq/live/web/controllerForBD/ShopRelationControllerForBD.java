package com.xq.live.web.controllerForBD;

import com.github.pagehelper.PageInfo;
import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.service.ShopRelationService;
import com.xq.live.vo.in.ShopRelationInVo;
import com.xq.live.vo.out.ShopRelationOut;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/bd/shopRelation")
@Api(value = "ShopRelationForBD", tags = "商户认领")
@RestController
public class ShopRelationControllerForBD {

    @Autowired
    private ShopRelationService shopRelationService;

    @PostMapping("/showList")
    @ApiOperation("查询商户认领列表")
    public BaseResp<PageInfo<ShopRelationOut>> showList(@RequestBody ShopRelationInVo shopRelationInVo) {
        return new BaseResp<>(ResultStatus.SUCCESS, shopRelationService.showList(shopRelationInVo));
    }

    @PostMapping("/save")
    @ApiOperation("BD认领/解除商户")
    public BaseResp<PageInfo<ShopRelationOut>> save(@RequestBody ShopRelationInVo shopRelationInVo) {
        return shopRelationService.save(shopRelationInVo) ? BaseResp.success() : BaseResp.fail();
    }

}
