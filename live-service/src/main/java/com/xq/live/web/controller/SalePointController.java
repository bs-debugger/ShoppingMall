package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.GoodsSku;
import com.xq.live.model.SalePoint;
import com.xq.live.model.SalePointUser;
import com.xq.live.model.User;
import com.xq.live.service.SalePointService;
import com.xq.live.vo.in.SalePointInVo;
import com.xq.live.vo.in.SalePointUserInVo;
import com.xq.live.vo.out.GoodsSkuOut;
import com.xq.live.vo.out.SalePointOut;
import com.xq.live.vo.out.SalePointUserOut;
import com.xq.live.web.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 销售点controller
 * Created by admin on 2018/9/26.
 */
@RestController
@RequestMapping(value="/salePoint")
public class SalePointController {

    @Autowired
    private SalePointService salePointService;

    /**
     * 获取销售点列表(旧)   --------这个接口为了保证线上兼容,以后会删除掉
     * @param salePointInVo
     * @return
     */
    @RequestMapping(value = "/list" ,method = RequestMethod.GET)
    public BaseResp<Pager<SalePointOut>> list(SalePointInVo salePointInVo) {
        salePointInVo.setType(SalePoint.TYPE_SHOPPING);
        Pager<SalePointOut> result=salePointService.list(salePointInVo);
        return new BaseResp<Pager<SalePointOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 获取销售点列表(新)
     *
     * 注意入参:page,rows,city,type,chainType
     * @param salePointInVo
     * @return
     */
    @RequestMapping(value = "/listNew" ,method = RequestMethod.GET)
    public BaseResp<Pager<SalePointOut>> listNew(SalePointInVo salePointInVo) {
        Pager<SalePointOut> result=salePointService.list(salePointInVo);
        return new BaseResp<Pager<SalePointOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 根据销售点id获取商品列表
     *
     * 注意入参:id,sendType=2   sendType为2代表为门店自提的商品
     * @param salePointInVo
     * @return
     */
    @RequestMapping(value="/getGoodsListBySalePointIdNew",method = RequestMethod.GET)
    public BaseResp<Pager<GoodsSkuOut>> getGoodsListBySalePointIdNew(SalePointInVo salePointInVo) {
        Pager<GoodsSkuOut> result=salePointService.getGoodsListBySalePointId(salePointInVo);
        return new BaseResp<Pager<GoodsSkuOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     *通过userId获取用户销售点信息
     *
     * 注意:该userId是当前用户id，要从网关中获取
     * @return
     */
    @RequestMapping(value="/getSalePointUserByUserId",method = RequestMethod.GET)
    public BaseResp<List<SalePointUser>> getSalePointUserByUserId() {
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<List<SalePointUser>>(ResultStatus.error_param_empty);
        }
        List<SalePointUser> result=salePointService.getSalePointUserByUserId(user.getId());
        return new BaseResp<List<SalePointUser>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 通过ID获取销售点信息
     * @param id
     * @return
     */
    @RequestMapping(value="/getSalePointById",method = RequestMethod.GET)
    public BaseResp<SalePoint> getSalePointById (Long id){
        SalePoint result=salePointService.getSalePointByID(id);
        return new BaseResp<SalePoint>(ResultStatus.SUCCESS, result);
    }

    /**
     * 添加销售点的核销员
     * @param salePointUserInVo
     * @return
     */
    @RequestMapping(value="/addSalePointUser",method = RequestMethod.POST)
    public BaseResp<Integer> addSalePointUser (SalePointUserInVo salePointUserInVo){
        Integer integer = salePointService.addSalePointUser(salePointUserInVo);
        if (integer==null){
            return new BaseResp<Integer>(ResultStatus.error_not_user);
        }
        return new BaseResp<Integer>(ResultStatus.SUCCESS, integer);
    }

    /**
     * 查询销售点的核销员
     * @param salePointUserInVo
     * @return
     */
    @RequestMapping(value="/selectSalepintList",method = RequestMethod.GET)
    public BaseResp<List<SalePointUserOut>> selectSalepintList (SalePointUserInVo salePointUserInVo){
        List<SalePointUserOut> list = salePointService.selectByUserIdAndSalepintList(salePointUserInVo);
        return new BaseResp<List<SalePointUserOut>>(ResultStatus.SUCCESS, list);
    }

    /**
     * 删除销售点的核销员
     * @param salePointUserInVo
     * @return
     */
    @RequestMapping(value="/deleteSalepintUser",method = RequestMethod.POST)
    public BaseResp<Integer> deleteSalepintUser (SalePointUserInVo salePointUserInVo){
        Integer integer = salePointService.deleteSalepintUser(salePointUserInVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, integer);
    }



    /**
     * 通过goodsSkuId获取销售点信息
     *
     * 注意入参:goodsSkuId,locationX,locationY
     * @param salePointInVo
     * @return
     */
    @RequestMapping(value="/getSalePointByGoodsSkuId",method = RequestMethod.GET)
    public BaseResp<List<SalePointOut>> getSalePointByGoodsSkuId(SalePointInVo salePointInVo){
        List<SalePointOut> list = salePointService.getSalePointByGoodsSkuId(salePointInVo);
        return new BaseResp<List<SalePointOut>>(ResultStatus.SUCCESS,list);
    }

}
