package com.xq.live.web.controllerForManage;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.service.ShopService;
import com.xq.live.vo.in.ShopInVo;
import com.xq.live.vo.out.ShopOut;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by admin on 2019/4/24.
 */
@RestController
@Api(value = "ShopForManage", tags = "商家")
@RequestMapping("/manage/shop")
public class ShopForManageController {
    @Autowired
    private ShopService shopService;

    /**
     * 根据条件查询商家列表信息
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation("根据条件查询商家列表信息")
    public BaseResp<Pager<ShopOut>> list(ShopInVo inVo, HttpServletRequest request){
        Pager<ShopOut> result = shopService.listNormal(inVo);
        return new BaseResp<Pager<ShopOut>>(ResultStatus.SUCCESS, result);
    }


}
