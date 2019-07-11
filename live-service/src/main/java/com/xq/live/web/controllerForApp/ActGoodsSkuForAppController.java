package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.service.ActGoodsSkuService;
import com.xq.live.vo.in.ActGoodsSkuInVo;
import com.xq.live.vo.out.ActGoodsSkuOut;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by ss on 2018/11/3.
 * 参与活动商品
 */
@RestController
@RequestMapping("/app/actGoodsSku")
public class ActGoodsSkuForAppController {

    @Autowired
    private ActGoodsSkuService actGoodsSkuService;

    /**
     * 报名参加活动
     * 入参:
     *     actId,skuId,shopId,ruleDesc,stockNum(999)
     *     goodsPromotionRules:shopId,actAmount
     *
     *
     * @param inVo
     * @return
     */
    @ApiOperation(value = "报名参加活动",notes = "报名参加活动")

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public BaseResp<Long> add(@RequestBody ActGoodsSkuInVo inVo){
        ActGoodsSkuInVo temInVo = new ActGoodsSkuInVo();
        temInVo.setSkuId(inVo.getSkuId());
        List<ActGoodsSkuOut> actGoodsSkuOuts = actGoodsSkuService.selectActByGoodsSkuId(temInVo);
        for (ActGoodsSkuOut actGoodsSkuOut : actGoodsSkuOuts) {
            if(actGoodsSkuOut.getActId().equals(inVo.getActId())){
                return new BaseResp<Long>(ResultStatus.error_act_goods_sku_is_sign);
            }
        }
        Long re = actGoodsSkuService.add(inVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS,re);
    }


    /**
     * 增加活动商品库存
     * 入参:
     *     id
     *     stockNum
     *
     *
     * @param inVo
     * @return
     */
    @ApiOperation(value = "增加活动商品库存",notes = "actGoodsSkuId,stockNum必传")

    @RequestMapping(value = "/addStockNum",method = RequestMethod.POST)
    public BaseResp<Integer> addStockNum(@RequestBody ActGoodsSkuInVo inVo){
        if(inVo.getId()==0||inVo.getStockNum()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
       Integer result= actGoodsSkuService.addStockNum(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS,result);
    }

}
