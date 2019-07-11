package com.xq.live.web.controllerForManage;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.service.ActGoodsSkuService;
import com.xq.live.vo.in.ActGoodsSkuInVo;
import com.xq.live.vo.out.ActGoodsSkuOut;
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
@Api(value = "ActGoodsSkuForManage", tags = "活动商品")
@RequestMapping("/manage/actGoodsSku")
public class ActGoodsSkuForManageController {
    @Autowired
    private ActGoodsSkuService actGoodsSkuService;


    /**
     * 通过活动id和城市商品
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/selectGoodsList", method = RequestMethod.GET)
    @ApiOperation("通过活动id和城市商品")
    public BaseResp<Pager<ActGoodsSkuOut>> selectGoodsList(ActGoodsSkuInVo inVo, HttpServletRequest request){
        Pager<ActGoodsSkuOut> result = actGoodsSkuService.selectGoodsList(inVo);
        return new BaseResp<Pager<ActGoodsSkuOut>>(ResultStatus.SUCCESS, result);
    }
}
