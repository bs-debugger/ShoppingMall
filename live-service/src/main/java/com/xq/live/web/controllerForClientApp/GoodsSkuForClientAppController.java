package com.xq.live.web.controllerForClientApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.GoodsSku;
import com.xq.live.service.GoodsSkuService;
import com.xq.live.vo.in.GoodsSkuInVo;
import com.xq.live.vo.out.GoodsSkuOut;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 *商城系统Sku的controller
 *@author lipeng
 *@create 2018-08-29 18:04
 **/
@Api(tags = "商品相关服务-GoodsSkuForAppController")
@RestController
@RequestMapping("/clientApp/goodsSku")
public class GoodsSkuForClientAppController {

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
     * locationX ，locationY
     * @return
     */
    @RequestMapping(value = "/selectDetailBySkuIdNew", method = RequestMethod.GET)
    public BaseResp<GoodsSkuOut> selectDetailBySkuId(GoodsSkuInVo inVo){
        GoodsSkuOut re = goodsSkuService.selectPlaceBySkuId(inVo);
        return new BaseResp<GoodsSkuOut>(ResultStatus.SUCCESS, re);
    }

    /**
     * 新增---该接口先不用
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
     * 更新(可以通过此接口来更改用户的上架和下架)---该接口先不用
     * 注:id,status
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BaseResp<Integer> update(GoodsSkuInVo inVo){
        Integer result = goodsSkuService.update(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }

    /**
     * 逻辑删除---该接口先不用
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
     * 注意以后的新入参:page,rows,spuType=10,status=1,shopId   (用类目来区分商品)
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
     * 注意以后的新入参:page,rows,spuType=10,status=1,shopId   (用类目来区分商品)
     *
     * 注意:1.商品没有所谓的平台邮购和门店自提的概念,只有订单和票券才有
     *     2.商品只有按照类目来分的,没有按照配送方式来分的
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/listNewForPlaza", method = RequestMethod.GET)
    public BaseResp<Pager<GoodsSkuOut>> listNewForPlaza(GoodsSkuInVo inVo){
        Pager<GoodsSkuOut> result = goodsSkuService.listNewForPlaza(inVo);
        return new BaseResp<Pager<GoodsSkuOut>> (ResultStatus.SUCCESS, result);
    }


    /**
     * 分页查询参与活动的商品列表
     * @param inVo
     * @return
     * actId categoryId
     */
    @ApiOperation(value = "分页查询参与活动的商品列表",notes = "根据活动id和分页相关参数查询参与活动的商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "actId", value = "活动id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "rows", value = "行数", required = true, dataType = "int")
    })
    @RequestMapping(value = "/listForAct", method = RequestMethod.GET)
    public BaseResp<Pager<GoodsSkuOut>> listForAct(GoodsSkuInVo inVo){
        Pager<GoodsSkuOut> result = goodsSkuService.actList(inVo);
        return new BaseResp<Pager<GoodsSkuOut>> (ResultStatus.SUCCESS, result);
    }

    /**
     * 分页查询参与活动的推荐商品列表
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

    /**
     * 添加商品,并把商品加入到活动当中
     * skuName,skuPic,sellPrice,stockNum,shopId,miniNum(1),sendType(2),singleType(1),expiryDate(90)
     * goodsSkuSpecValue:specValueId(23)
     * goodsSpuInvo:(spuName,spuType(10),categoryId,brandId(1),
     *               goodsSpuSpec:specId(7),
     *               goodsSpuDesc:title,content
     *                 )
     * actGoodsSkuInVos:{
     *     (actId,shopId,categoryId,peopleNum,ruleDesc,stockNum
     *      goodsPromotionRules:ruleType,ruleDesc,shopId,actAmount)
     *      ....
     * }
     * @param inVo
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/addGoodsSkuAndAct",method = RequestMethod.POST)
    public BaseResp<Long> addGoodsSkuAndAct(@RequestBody @Valid GoodsSkuInVo inVo, BindingResult result,HttpServletRequest request){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        Long id = goodsSkuService.addGoodsSkuAndAct(inVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS,id);
    }

    /**
     * 添加平台商品,并把商品加入到活动当中(页面用,后期可在此基础上面扩展)
     * skuName,skuPic,sellPrice,stockNum,shopId,miniNum(1),sendType(2),singleType(1),expiryDate(90),
     * (realWeight),(piece),(bulk),marketPrice,costPrice,otherMarketPrice,unit
     * goodsSkuSpecValue:specValueId(23)
     * goodsSpuInvo:(spuName,spuType(10),categoryId,brandId(1),
     *               goodsSpuSpec:specId(7),
     *               goodsSpuDesc:title,content
     *                 )
     * actGoodsSkuInVos:{
     *     (actId,shopId,categoryId,peopleNum,ruleDesc,stockNum,startTime,dueTime
     *      goodsPromotionRules:ruleType,ruleDesc,shopId,actAmount)
     *      ....
     * }
     * deliveryTemplateInVo:(
     *     (name, shipping_address, is_free, calculate_type,shop_id)
     *     {
     *         (delivery_template_id), (region(目前先不传,我们这边写死), region_desc,first_piece,
     *         first_weight, first_bulk, first_amount,second_piece, second_weight,
     *         second_bulk, second_amount, delivery_method_type(0平台配送 1顺丰  2圆通 3中通), is_default(0))
     *     }...
     * )
     * @param inVo
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/addPlatformGoodsSkuAndAct",method = RequestMethod.POST)
    public BaseResp<Long> addPlatformGoodsSkuAndAct(@RequestBody @Valid GoodsSkuInVo inVo, BindingResult result,HttpServletRequest request){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        Long id = goodsSkuService.addPlatformGoodsSkuAndAct(inVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS,id);
    }

    /**
     * 更新商品,并把商品对应的活动更新
     * id,skuCode,skuName,skuPic,sellPrice,stockNum,shopId,miniNum(1),sendType(2),singleType(1),expiryDate(90)
     * goodsSkuSpecValue:specValueId(23),id
     * goodsSpuInvo:(id,spuName,spuType(10),categoryId,brandId(1),
     *               goodsSpuSpec:specId(7),id
     *               goodsSpuDesc:title,content,id
     *                 )
     * actGoodsSkuInVos:{
     *     (actId,shopId,categoryId,peopleNum,ruleDesc,stockNum,id,isDeleted
     *      goodsPromotionRules:id,ruleType,ruleDesc,shopId,actAmount)
     *      ....
     * }
     * @param inVo
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/updateGoodsSkuAndAct",method = RequestMethod.POST)
    public BaseResp<Integer> updateGoodsSkuAndAct(@RequestBody @Valid GoodsSkuInVo inVo, BindingResult result,HttpServletRequest request){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Integer>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        Integer id = goodsSkuService.updateGoodsSkuAndAct(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS,id);
    }
}
