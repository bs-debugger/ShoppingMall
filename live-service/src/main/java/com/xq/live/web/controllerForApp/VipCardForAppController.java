package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.service.ActShopService;
import com.xq.live.service.GoodsSkuService;
import com.xq.live.service.VipCardService;
import com.xq.live.vo.in.ActShopInVo;
import com.xq.live.vo.in.GoodsSkuInVo;
import com.xq.live.vo.out.ActShopOut;
import com.xq.live.vo.out.GoodsSkuOut;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by admin on 2019/5/20.
 */
@Api(tags = "会员信息-VipCardController")
@RestController
@RequestMapping(value = "/app/vipCard")
public class VipCardForAppController {
    @Autowired
    private VipCardService vipCardService;

    @Autowired
    private GoodsSkuService goodsSkuService;

    @Autowired
    private ActShopService actShopService;

    @ApiOperation(value = "添加优惠券")
    @ResponseBody
    @RequestMapping(value = "/addVipCoupon", method = RequestMethod.POST)
    public BaseResp<Long> addVipCoupon(@RequestBody  GoodsSkuInVo inVo, BindingResult result, HttpServletRequest request){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        goodsSkuService.updateForVip(inVo);//优惠券的价格需要单独计算
        Long id = goodsSkuService.addGoodsSkuAndAct(inVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS,id);
    }

    @ApiOperation(value = "修改优惠券")
    @ResponseBody
    @RequestMapping(value = "/updateVipCoupon", method = RequestMethod.POST)
    public BaseResp<Integer> updateVipCoupon(@RequestBody GoodsSkuInVo inVo, HttpServletRequest request){
        goodsSkuService.updateForVip(inVo);//优惠券的价格需要单独计算
        Integer re =goodsSkuService.updateGoodsSkuAndAct(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS,re);
    }

    @ApiOperation(value = "优惠券列表")
    @ResponseBody
    @RequestMapping(value = "/listVipCoupon", method = RequestMethod.POST)
    public BaseResp<Pager<GoodsSkuOut>> listVipCoupon(@RequestBody GoodsSkuInVo inVo, HttpServletRequest request){
        Pager<GoodsSkuOut> result = goodsSkuService.list(inVo);
        return new BaseResp<Pager<GoodsSkuOut>>(ResultStatus.SUCCESS,result);
    }

    @ApiOperation(value = "添加单品套餐")
    @ResponseBody
    @RequestMapping(value = "/addVipGoods", method = RequestMethod.POST)
    public BaseResp<Long> addVipGoods(@RequestBody GoodsSkuInVo inVo, BindingResult result, HttpServletRequest request){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        Long id = goodsSkuService.addGoodsSkuAndAct(inVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS,id);
    }

    @ApiOperation(value = "修改单品套餐")
    @ResponseBody
    @RequestMapping(value = "/updateVipGoods", method = RequestMethod.POST)
    public BaseResp<Integer> updateVipGoods(@RequestBody GoodsSkuInVo inVo, HttpServletRequest request){
        Integer re =goodsSkuService.updateGoodsSkuAndAct(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS,re);
    }

    @ApiOperation(value = "单品套餐列表")
    @ResponseBody
    @RequestMapping(value = "/listVipGoods", method = RequestMethod.POST)
    public BaseResp<Pager<GoodsSkuOut>> listVipGoods(@RequestBody GoodsSkuInVo inVo, HttpServletRequest request){
        Pager<GoodsSkuOut> result = goodsSkuService.list(inVo);
        return new BaseResp<Pager<GoodsSkuOut>>(ResultStatus.SUCCESS,result);
    }


    @ApiOperation(value = "添加买单打折")
    @ResponseBody
    @RequestMapping(value = "/addShopDiscount", method = RequestMethod.POST)
    public BaseResp<Long> addShopDiscount(@RequestBody ActShopInVo inVo, HttpServletRequest request){
        Long id = actShopService.addForVip(inVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS,id);
    }

    @ApiOperation(value = "修改买单打折")
    @ResponseBody
    @RequestMapping(value = "/updateShopDiscount", method = RequestMethod.POST)
    public BaseResp<Integer> updateShopDiscount(@RequestBody ActShopInVo inVo, HttpServletRequest request){
        Integer re =actShopService.updateForVip(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS,re);
    }

    @ApiOperation(value = "买单打折列表")
    @ResponseBody
    @RequestMapping(value = "/listShopDiscount", method = RequestMethod.POST)
    public BaseResp<Pager<ActShopOut>> listShopDiscount(@RequestBody ActShopInVo inVo, HttpServletRequest request){
        Pager<ActShopOut> result = actShopService.listForVip(inVo);
        return new BaseResp<Pager<ActShopOut>>(ResultStatus.SUCCESS,result);
    }

    @ApiOperation(value = "获取单个买单打折")
    @ResponseBody
    @RequestMapping(value = "/getShopDiscount/{id}", method = RequestMethod.POST)
    public BaseResp<ActShopOut> getShopDiscount(@PathVariable("id") Long id){
        ActShopOut result = actShopService.getShopDiscountById(id);
        return new BaseResp<ActShopOut>(ResultStatus.SUCCESS,result);
    }

    @ApiOperation(value = "删除买单打折列表")
    @ResponseBody
    @RequestMapping(value = "/deleteShopDiscount/{id}", method = RequestMethod.POST)
    public BaseResp<Integer> deleteShopDiscount(@PathVariable("id") Long id){
        Integer result = actShopService.deleteShopDiscount(id);
        return new BaseResp<Integer>(ResultStatus.SUCCESS,result);
    }
}
