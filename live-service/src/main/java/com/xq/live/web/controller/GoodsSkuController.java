package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.GoodsSku;
import com.xq.live.model.User;
import com.xq.live.service.GoodsSkuService;
import com.xq.live.vo.in.GoodsSkuInVo;
import com.xq.live.vo.in.GoodsSkuRecommendInVo;
import com.xq.live.vo.out.ActGoodsSkuOut;
import com.xq.live.vo.out.GoodsSkuOut;
import com.xq.live.vo.out.GoodsSkuRecommendOut;
import com.xq.live.web.utils.UserContext;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 *商城系统Sku的controller
 *@author lipeng
 *@create 2018-08-29 18:04
 **/
@RestController
@RequestMapping("/goodsSku")
public class GoodsSkuController {

    @Autowired
    private GoodsSkuService goodsSkuService;

    /**
     * 根据id查询一条记录
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<GoodsSku> selectOne(@PathVariable("id") Long id){
        GoodsSku re = goodsSkuService.selectOne(id);
        return new BaseResp<GoodsSku>(ResultStatus.SUCCESS, re);
    }

    /**
     * 根据id查询一条记录的详情(过一段时间弃用接口)
     * @param id
     * @return
     */
    @RequestMapping(value = "/selectDetailBySkuId", method = RequestMethod.GET)
    public BaseResp<GoodsSkuOut> selectDetailBySkuId(Long id){
        GoodsSkuOut re = goodsSkuService.selectDetailBySkuId(id);
        return new BaseResp<GoodsSkuOut>(ResultStatus.SUCCESS, re);
    }

    /**
     * 根据id查询一条记录的详情
     * @param inVo
     * (locationX) ，(locationY),id,(actId)
     * @return
     */
    @RequestMapping(value = "/selectDetailBySkuIdNew", method = RequestMethod.GET)
    public BaseResp<GoodsSkuOut> selectDetailBySkuId(GoodsSkuInVo inVo){
        User user = UserContext.getUserSession();
        inVo.setUserId(user.getId());
        GoodsSkuOut re = goodsSkuService.selectPlaceBySkuId(inVo);
        return new BaseResp<GoodsSkuOut>(ResultStatus.SUCCESS, re);
    }

    /**
     * 新增
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid GoodsSkuInVo inVo, BindingResult result){
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        Long id = goodsSkuService.add(inVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 更新(可以通过此接口来更改用户的上架和下架)
     * 注:入参基本上与add接口相同,并且再多传递一个id
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BaseResp<Integer> update(GoodsSkuInVo inVo){
        Integer result = goodsSkuService.update(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }

    /**
     * 逻辑删除
     * 注:入参 id
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public BaseResp<Integer> delete(GoodsSkuInVo inVo){
        inVo.setIsDeleted(GoodsSku.GOODS_SKU_IS_DELETED);
        Integer result = goodsSkuService.update(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }


    /**
     * 分页查询记录列表(最新)
     *
     * 注意入参:1.平台邮购商品:page,rows,supId,spuType=10,sendType=1,categoryId
     *         2.门店自提:page,rows,spuId,spuType=10,sendType=2,categoryId
     *
     *         page,rows,spuType=10,categoryId,status=1
     *
     * 礼品券包含:1.平台邮购礼品券  2到店自提礼品券
     *
     * 平台邮购的商品包含:1.平台邮购的实物商品(属于spuId为1,2的)   2平台邮购的礼品券(属于spuId为3的)
     *
     * 门店自提的商品包含:1.门店自提的实物商品(属于spuId为4的)  2.门店自提的礼品券(属于spuId为3的)
     *
     * 注意以后的新入参:page,rows,spuType=10,(sendType,)categoryId,status=(1,3),(singleType),locationX,locationY   (用类目来区分商品)
     *
     * 注意:1.商品没有所谓的平台邮购和门店自提的概念,只有订单和票券才有
     *     2.商品只有按照类目来分的,没有按照配送方式来分的
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/listNew", method = RequestMethod.GET)
    public BaseResp<Pager<GoodsSkuOut>> listNew(GoodsSkuInVo inVo){
        Pager<GoodsSkuOut> result = goodsSkuService.list(inVo);
        return new BaseResp<Pager<GoodsSkuOut>> (ResultStatus.SUCCESS, result);
    }


    /**
     * 分页查询参与活动的商品列表(可以通过传入status字段来判断是否查询下架商品,一般来说is_deleted字段不会轻易设置为1)
     * @param inVo
     * @return
     * actId categoryId
     */
    @RequestMapping(value = "/listForAct", method = RequestMethod.GET)
    public BaseResp<Pager<GoodsSkuOut>> listForAct(GoodsSkuInVo inVo){
        inVo.setRankingByVoteNum(1);
        Pager<GoodsSkuOut> result = goodsSkuService.actList(inVo);
        return new BaseResp<Pager<GoodsSkuOut>> (ResultStatus.SUCCESS, result);
    }
    /**
     * 分页查询参与活动的商品列表(参与结束的商品 团购用)
     * @param inVo
     * @return
     * actId categoryId
     */
    @RequestMapping(value = "/listForEndAct", method = RequestMethod.GET)
    public BaseResp<Pager<ActGoodsSkuOut>> actEndList(GoodsSkuInVo inVo){
        Pager<ActGoodsSkuOut> result = goodsSkuService.actEndList(inVo);
        return new BaseResp<Pager<ActGoodsSkuOut>> (ResultStatus.SUCCESS, result);
    }


    /**
     * 分页查询参与活动的推荐商品列表(排除单个商品,或则是类目)
     * @param inVo
     * @return
     * actId categoryId id
     */
    @RequestMapping(value = "/listForActOut", method = RequestMethod.GET)
    public BaseResp<Pager<GoodsSkuOut>> listForActOut(GoodsSkuInVo inVo){
        Pager<GoodsSkuOut> result = goodsSkuService.actOutList(inVo);
        return new BaseResp<Pager<GoodsSkuOut>> (ResultStatus.SUCCESS, result);
    }

    /**
     * 查看热门
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public BaseResp<List<GoodsSkuOut>> top(GoodsSkuInVo inVo){
        List<GoodsSkuOut> result = goodsSkuService.top(inVo);
        return new BaseResp<List<GoodsSkuOut>> (ResultStatus.SUCCESS, result);
    }

    @GetMapping("/dailyRecommend")
    @ApiOperation("每日随机推荐")
    public BaseResp<GoodsSkuRecommendOut> dailyRecommend(GoodsSkuRecommendInVo goodsSkuRecommendInVo) {
        return new BaseResp<GoodsSkuRecommendOut>(ResultStatus.SUCCESS, goodsSkuService.dailyRecommend(goodsSkuRecommendInVo));
    }

}
