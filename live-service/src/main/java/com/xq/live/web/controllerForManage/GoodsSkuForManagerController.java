package com.xq.live.web.controllerForManage;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.service.GoodsSkuService;
import com.xq.live.vo.in.AduitGoodsSkuInVo;
import com.xq.live.vo.in.BatchDeleteGoodsSkuCommand;
import com.xq.live.vo.in.GoodsSkuInVo;
import com.xq.live.vo.in.PageGoodsSkuCommand;
import com.xq.live.vo.out.GoodsSkuOut;
import com.xq.live.vo.out.PageGoodsSkuVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xuzhen
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2019/4/2315:12
 */
@RestController
@RequestMapping("/manage/goodsSku")
public class GoodsSkuForManagerController {

    @Autowired
    private GoodsSkuService goodsSkuService;

    @ApiOperation(value = "商户商品审核")
    @RequestMapping(value = "/auditGoodsSku",method = RequestMethod.POST)
    public BaseResp<Integer> auditGoodsSku(@RequestBody AduitGoodsSkuInVo inVo){
        Integer ret = goodsSkuService.auditGoodsSku(inVo);
        if(ret==1){
            return new BaseResp<>(ResultStatus.SUCCESS, ret);
        }else{
            return new BaseResp<>(ResultStatus.error_param_empty,ret);
        }
    }


    @RequestMapping(value = "/pageListForAudit", method = RequestMethod.POST)
    public BaseResp<Pager<PageGoodsSkuVo>> pageListForAudit(@RequestBody PageGoodsSkuCommand command){
        Pager<PageGoodsSkuVo> result = goodsSkuService.pageGoodsSkuForAudit(command);
        return new BaseResp<Pager<PageGoodsSkuVo>> (ResultStatus.SUCCESS, result);
    }


    @RequestMapping(value = "/batchDeleteGoodsSku", method = RequestMethod.POST)
    public BaseResp<Integer> batchDeleteGoodsSku(@RequestBody BatchDeleteGoodsSkuCommand command){
        int ret = goodsSkuService.batchDeleteGoodsSku(command);
        return new BaseResp<Integer> (ResultStatus.SUCCESS, ret);
    }
}
