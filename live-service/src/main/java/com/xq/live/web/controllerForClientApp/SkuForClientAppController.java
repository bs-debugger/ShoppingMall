package com.xq.live.web.controllerForClientApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.Coupon;
import com.xq.live.model.Sku;
import com.xq.live.model.SkuAllocation;
import com.xq.live.service.SkuService;
import com.xq.live.vo.in.SkuInVo;
import com.xq.live.vo.out.SkuForTscOut;
import com.xq.live.vo.out.SkuOut;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * SKU controller
 *
 * @author lipeng
 * @date 2018-02-09 10:31
 * @copyright:hbxq
 **/
@RestController
@RequestMapping(value = "/clientApp/sku")
public class SkuForClientAppController {

    @Autowired
    private SkuService skuService;

    /**
     * 查一条记录
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<SkuOut> get(@PathVariable("id") Long id){
        SkuOut skuOut = skuService.selectById(id);
        return new BaseResp<SkuOut>(ResultStatus.SUCCESS, skuOut);
    }

    /**
     * 分页查询列表
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResp<Pager<SkuOut>> list(SkuInVo inVo){
        inVo.setSkuType(Sku.SKU_TYPE_XQQ);
        Pager<SkuOut> result = skuService.list(inVo);
        return new BaseResp<Pager<SkuOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 查热门
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public BaseResp<List<SkuOut>> top(SkuInVo inVo){
        List<SkuOut> result = skuService.top(inVo);
        return new BaseResp<List<SkuOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 新增sku(适合新增推荐菜，套餐，砍价菜,抢购菜)
     * @param sku
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid Sku sku, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        Long skuId = skuService.add(sku);
        return new BaseResp<Long>(ResultStatus.SUCCESS, skuId);
    }

    /**
     * 一键新增享七自营产品(大闸蟹)
     *
     * 入参:skuType(大闸蟹的类型),shopId,opreatorId,opreatorName
     * @param skuAllocation
     * @return
     */
    @RequestMapping(value = "/addSKuAllocation",method = RequestMethod.POST)
    public BaseResp<Integer> addSkuAllocation(SkuAllocation skuAllocation){
         Integer re = skuService.addSkuAllocation(skuAllocation);
         return new BaseResp<Integer>(ResultStatus.SUCCESS,re);
    }

    /**
     * 商家是否支持卖享七自营商品
     * 0 不支持 大于0支持
     *
     * 入参:skuType(大闸蟹的类型),shopId
     * @param skuAllocation
     * @return
     */
    @RequestMapping(value = "/isAdd",method = RequestMethod.GET)
    public BaseResp<Integer> isAdd(SkuAllocation skuAllocation){
        Integer re = skuService.isAdd(skuAllocation);
        return new BaseResp<Integer>(ResultStatus.SUCCESS,re);
    }

    /**
     * 一键删除享七自营产品(大闸蟹)
     *
     * 入参:skuType(大闸蟹的类型),shopId
     * @param skuAllocation
     * @return
     */
    @RequestMapping(value = "/deleteSkuAllocation",method = RequestMethod.POST)
    public BaseResp<Integer> deleteSkuAllocation(SkuAllocation skuAllocation){
        Integer re = skuService.deleteSkuAllocation(skuAllocation);
        return new BaseResp<Integer>(ResultStatus.SUCCESS,re);
    }


    /**
     * 新增食典券
     * @param sku
     * @return
     */
    @RequestMapping(value = "/addSku", method = RequestMethod.POST)
    public BaseResp<Long> addSku(@Valid Sku sku, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        //查询生成的食典券是否存在，如果存在的话那就不用再生成票券了
        Sku skuNew = new Sku();
        BeanUtils.copyProperties(sku,skuNew);
        skuNew.setIsDeleted(Sku.SKU_NO_DELETED);
        Sku skuForAct = skuService.selectForActSku(skuNew);
        if(skuForAct!=null){
             return new BaseResp<Long>(ResultStatus.SUCCESS,skuForAct.getId());
        }
        //查询是否存在已经被删除的食典券,如果存在，则需把删除状态改成正常状态
        skuNew.setIsDeleted(Sku.SKU_IS_DELETED);
        skuForAct = skuService.selectForActSku(skuNew);
        if(skuForAct!=null){
           Integer i =  skuService.updateSku(skuForAct);
            if(i==1){
                return new BaseResp<Long>(ResultStatus.SUCCESS,skuForAct.getId());
            }
        }
        sku.setCouponSkuType(Coupon.OUNPON_TYPE_ACT);
        Long skuId = skuService.addSku(sku);
        return new BaseResp<Long>(ResultStatus.SUCCESS, skuId);
    }

    /**
     * 新增砍价券
     * @param sku
     * @return
     */
    @RequestMapping(value = "/addSkuForKj", method = RequestMethod.POST)
    public BaseResp<Long> addSkuForKj(@Valid Sku sku, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        if(sku.getSellPrice()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        //查询生成的砍价券是否存在，如果存在的话那就不用再生成票券了
        Sku skuNew = new Sku();
        BeanUtils.copyProperties(sku,skuNew);
        skuNew.setIsDeleted(Sku.SKU_NO_DELETED);
        Sku skuForAct = skuService.selectForActSku(skuNew);
        if(skuForAct!=null){
            return new BaseResp<Long>(ResultStatus.SUCCESS,skuForAct.getId());
        }
        //查询是否存在已经被删除的砍价券,如果存在，则需把删除状态改成正常状态
        skuNew.setIsDeleted(Sku.SKU_IS_DELETED);
        skuForAct = skuService.selectForActSku(skuNew);
        if(skuForAct!=null){
            Integer i =  skuService.updateSku(skuForAct);
            if(i==1){
                return new BaseResp<Long>(ResultStatus.SUCCESS,skuForAct.getId());
            }
        }
        sku.setCouponSkuType(Coupon.OUNPON_TYPE_KJQ);
        Long skuId = skuService.addSku(sku);
        return new BaseResp<Long>(ResultStatus.SUCCESS, skuId);
    }

    /**
     * 新增抢购券
     * @param sku
     * @return
     */
    @RequestMapping(value = "/addSkuForQg", method = RequestMethod.POST)
    public BaseResp<Long> addSkuForQg(@Valid Sku sku, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        if(sku.getSellPrice()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        //查询生成的抢购券是否存在，如果存在的话那就不用再生成票券了
        Sku skuNew = new Sku();
        BeanUtils.copyProperties(sku,skuNew);
        skuNew.setIsDeleted(Sku.SKU_NO_DELETED);
        Sku skuForAct = skuService.selectForActSku(skuNew);
        if(skuForAct!=null){
            return new BaseResp<Long>(ResultStatus.SUCCESS,skuForAct.getId());
        }
        //查询是否存在已经被删除的抢购券,如果存在，则需把删除状态改成正常状态
        skuNew.setIsDeleted(Sku.SKU_IS_DELETED);
        skuForAct = skuService.selectForActSku(skuNew);
        if(skuForAct!=null){
            Integer i =  skuService.updateSku(skuForAct);
            if(i==1){
                return new BaseResp<Long>(ResultStatus.SUCCESS,skuForAct.getId());
            }
        }
        sku.setCouponSkuType(Coupon.OUNPON_TYPE_QGQ);
        Long skuId = skuService.addSku(sku);
        return new BaseResp<Long>(ResultStatus.SUCCESS, skuId);
    }

    /**
     * 新增兑换券
     * @param sku
     * @return
     */
    @RequestMapping(value = "/addSkuForDh", method = RequestMethod.POST)
    public BaseResp<Long> addSkuForDh(@Valid Sku sku, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        if(sku.getSellPrice()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        //查询生成的抢购券是否存在，如果存在的话那就不用再生成票券了
        Sku skuNew = new Sku();
        BeanUtils.copyProperties(sku,skuNew);
        skuNew.setIsDeleted(Sku.SKU_NO_DELETED);
        Sku skuForAct = skuService.selectForActSku(skuNew);
        if(skuForAct!=null){
            return new BaseResp<Long>(ResultStatus.SUCCESS,skuForAct.getId());
        }
        //查询是否存在已经被删除的抢购券,如果存在，则需把删除状态改成正常状态
        skuNew.setIsDeleted(Sku.SKU_IS_DELETED);
        skuForAct = skuService.selectForActSku(skuNew);
        if(skuForAct!=null){
            Integer i =  skuService.updateSku(skuForAct);
            if(i==1){
                return new BaseResp<Long>(ResultStatus.SUCCESS,skuForAct.getId());
            }
        }
        sku.setCouponSkuType(Coupon.OUNPON_TYPE_DHQ);
        Long skuId = skuService.addSku(sku);
        return new BaseResp<Long>(ResultStatus.SUCCESS, skuId);
    }


    /**
     * 逻辑删除sku(在砍菜中可以当作下架使用)
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public BaseResp<Integer> delete(Long id){

        Integer i = skuService.delete(id);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, i);
    }

    /**
     * 更新sku(在砍菜中可以当作上架使用)
     * @param sku
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BaseResp<Integer> update(@Valid Sku sku, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Integer>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        sku.setUpdateTime(new Date());
        Integer skuId = skuService.update(sku);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, skuId);
    }

    /**
     * 查询单个推荐菜
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/getTscForZan",method = RequestMethod.GET)
    public BaseResp<SkuForTscOut> getTscForZan(SkuInVo inVo){
        if(inVo==null||inVo.getId()==null||inVo.getZanUserId()==null){
            return new BaseResp<SkuForTscOut>(ResultStatus.error_param_empty);
        }
        if(inVo.getShopId() == null){
            return new BaseResp<SkuForTscOut>(ResultStatus.error_param_shop_id_empty);
        }
        inVo.setSkuType(Sku.SKU_TYPE_TSC);
        SkuForTscOut tscForZan = skuService.getTscForZan(inVo);
        return  new BaseResp<SkuForTscOut>(ResultStatus.SUCCESS,tscForZan);
    }

    /**
     * 分页特色菜列表
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/tsc", method = RequestMethod.GET)
    public BaseResp<Pager<SkuForTscOut>> tscList(SkuInVo inVo){
        //注意:如果想查询某个推荐菜是否参与活动报名的话，要传递一个actId,判断isSign是否大于0
        if(inVo == null){
            return new BaseResp<Pager<SkuForTscOut>>(ResultStatus.error_param_empty);
        }

        if(inVo.getShopId() == null){
            return new BaseResp<Pager<SkuForTscOut>>(ResultStatus.error_param_shop_id_empty);
        }
        inVo.setSkuType(Sku.SKU_TYPE_TSC);
        Pager<SkuForTscOut> result = skuService.queryTscList(inVo);
        return new BaseResp<Pager<SkuForTscOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 查询单个砍价菜
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/getKjc",method = RequestMethod.GET)
    public BaseResp<SkuForTscOut> getKjc(SkuInVo inVo){
        if(inVo==null||inVo.getId()==null||inVo.getZanUserId()==null){
            return new BaseResp<SkuForTscOut>(ResultStatus.error_param_empty);
        }
        if(inVo.getShopId() == null){
            return new BaseResp<SkuForTscOut>(ResultStatus.error_param_shop_id_empty);
        }
        inVo.setSkuType(Sku.SKU_TYPE_KJC);
        SkuForTscOut tscForZan = skuService.getKjcForZan(inVo);
        return  new BaseResp<SkuForTscOut>(ResultStatus.SUCCESS,tscForZan);
    }

    /**
     * 查询单个抢购菜
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/getQgc",method = RequestMethod.GET)
    public BaseResp<SkuForTscOut> getQgc(SkuInVo inVo){
        if(inVo==null||inVo.getId()==null||inVo.getZanUserId()==null){
            return new BaseResp<SkuForTscOut>(ResultStatus.error_param_empty);
        }
        if(inVo.getShopId() == null){
            return new BaseResp<SkuForTscOut>(ResultStatus.error_param_shop_id_empty);
        }
        inVo.setSkuType(Sku.SKU_TYPE_QGC);
        SkuForTscOut tscForZan = skuService.getQgcForZan(inVo);
        return  new BaseResp<SkuForTscOut>(ResultStatus.SUCCESS,tscForZan);
    }

    /**
     * 查询单个兑换菜
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/getDhc",method = RequestMethod.GET)
    public BaseResp<SkuForTscOut> getDhc(SkuInVo inVo){
        if(inVo==null||inVo.getId()==null||inVo.getZanUserId()==null){
            return new BaseResp<SkuForTscOut>(ResultStatus.error_param_empty);
        }
        if(inVo.getShopId() == null){
            return new BaseResp<SkuForTscOut>(ResultStatus.error_param_shop_id_empty);
        }
        inVo.setSkuType(Sku.SKU_TYPE_DHC);
        SkuForTscOut tscForZan = skuService.getDhcForZan(inVo);
        return  new BaseResp<SkuForTscOut>(ResultStatus.SUCCESS,tscForZan);
    }

    /**
     * 分页砍价菜列表
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/kjcList", method = RequestMethod.GET)
    public BaseResp<Pager<SkuForTscOut>> kjcList(SkuInVo inVo){
        //注意:如果想查询某个砍价菜是否参与活动报名的话，要传递一个actId,判断isSign是否大于0
        if(inVo == null){
            return new BaseResp<Pager<SkuForTscOut>>(ResultStatus.error_param_empty);
        }

        if(inVo.getShopId() == null){
            return new BaseResp<Pager<SkuForTscOut>>(ResultStatus.error_param_shop_id_empty);
        }
        inVo.setSkuType(Sku.SKU_TYPE_KJC);
        Pager<SkuForTscOut> result = skuService.queryKjcList(inVo);
        return new BaseResp<Pager<SkuForTscOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 分页抢购菜列表
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/qgcList", method = RequestMethod.GET)
    public BaseResp<Pager<SkuForTscOut>> qgcList(SkuInVo inVo){
        //注意:如果想查询某个抢购菜是否参与活动报名的话，要传递一个actId,判断isSign是否大于0
        if(inVo == null){
            return new BaseResp<Pager<SkuForTscOut>>(ResultStatus.error_param_empty);
        }

        if(inVo.getShopId() == null){
            return new BaseResp<Pager<SkuForTscOut>>(ResultStatus.error_param_shop_id_empty);
        }
        inVo.setSkuType(Sku.SKU_TYPE_QGC);
        Pager<SkuForTscOut> result = skuService.queryQgcList(inVo);
        return new BaseResp<Pager<SkuForTscOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 分页兑换菜列表
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/dhcList", method = RequestMethod.GET)
    public BaseResp<Pager<SkuForTscOut>> dhcList(SkuInVo inVo){
        //注意:如果想查询某个抢购菜是否参与活动报名的话，要传递一个actId,判断isSign是否大于0
        if(inVo == null){
            return new BaseResp<Pager<SkuForTscOut>>(ResultStatus.error_param_empty);
        }

        if(inVo.getShopId() == null){
            return new BaseResp<Pager<SkuForTscOut>>(ResultStatus.error_param_shop_id_empty);
        }
        inVo.setSkuType(Sku.SKU_TYPE_DHC);
        Pager<SkuForTscOut> result = skuService.queryDhcList(inVo);
        return new BaseResp<Pager<SkuForTscOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 查询单个套餐
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/getAgio",method = RequestMethod.GET)
    public BaseResp<SkuForTscOut> getAgio(SkuInVo inVo){
        if(inVo==null||inVo.getId()==null||inVo.getZanUserId()==null){
            return new BaseResp<SkuForTscOut>(ResultStatus.error_param_empty);
        }
        if(inVo.getShopId() == null){
            return new BaseResp<SkuForTscOut>(ResultStatus.error_param_shop_id_empty);
        }
        inVo.setSkuType(Sku.SKU_TYPE_SJTC);
        SkuForTscOut tscForZan = skuService.getTscForZan(inVo);
        return  new BaseResp<SkuForTscOut>(ResultStatus.SUCCESS,tscForZan);
    }

    /**
     * 分页套餐列表
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/agioList", method = RequestMethod.GET)
    public BaseResp<Pager<SkuForTscOut>> agioList(SkuInVo inVo){
        if(inVo == null){
            return new BaseResp<Pager<SkuForTscOut>>(ResultStatus.error_param_empty);
        }

        if(inVo.getShopId() == null){
            return new BaseResp<Pager<SkuForTscOut>>(ResultStatus.error_param_shop_id_empty);
        }
        inVo.setSkuType(Sku.SKU_TYPE_SJTC);
        Pager<SkuForTscOut> result = skuService.queryTscList(inVo);
        return new BaseResp<Pager<SkuForTscOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 判断是否是首次下单用户 0新用户 其他 老用户
     * @param userId
     * @return
     */
    @RequestMapping(value = "/isNewUser",method = RequestMethod.GET)
    public BaseResp<Integer> isNewUser(Long userId){
        if(userId==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        Integer newUser = skuService.isNewUser(userId);
        if(newUser==0){
            return new BaseResp<Integer>(ResultStatus.SUCCESS,0);
        }
        return new BaseResp<Integer>(ResultStatus.FAIL,newUser);
    }
}
