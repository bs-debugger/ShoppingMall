package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.ShopCashier;
import com.xq.live.service.ShopCashierService;
import com.xq.live.vo.in.ShopCashierInVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 收营员管理接口，提供父账户
 * Created by lipeng on 2018/5/23.
 */
@RestController
@RequestMapping(value = "/app/shopCashier")
public class ShopCashierForAppController {

    @Autowired
    private ShopCashierService shopCashierService;

    /**
     * 通过shopId查到该商家的管理员
     * @param shopId
     * @return
     */
    @RequestMapping(value = "/adminByShopId",method = RequestMethod.GET)
    public BaseResp<ShopCashier> adminByShopId(Long shopId){
        if(shopId==null){
            return new BaseResp<ShopCashier>(ResultStatus.error_param_empty);
        }
        ShopCashier shopCashier = shopCashierService.adminByShopId(shopId);
        return new BaseResp<ShopCashier>(ResultStatus.SUCCESS,shopCashier);
    }

    /**
     * 添加一个收银员
     * @param inVo
     * @param result
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid ShopCashierInVo inVo, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        inVo.setIsDeleted(ShopCashier.SHOP_CASHIER_IS_DELETED);//查询已经逻辑删除的数据
        ShopCashier have = shopCashierService.isHave(inVo);
        //如果已经有一条被逻辑删除的记录，那么直接更改其状态即可
        if(have!=null){
             ShopCashierInVo shopCashierInVo = new ShopCashierInVo();
            BeanUtils.copyProperties(have, shopCashierInVo);
            shopCashierInVo.setIsDeleted(ShopCashier.SHOP_CASHIER_NO_DELETED);
            shopCashierInVo.setUpdatorId(inVo.getParentId());//修改人的id，应该是当前操作人的id,只有管理员才有权限添加修改核销员，所以直接为parentId即可
            shopCashierService.update(shopCashierInVo);
            return new  BaseResp<Long>(ResultStatus.SUCCESS,shopCashierInVo.getId());
        }
        inVo.setIsDeleted(ShopCashier.SHOP_CASHIER_NO_DELETED);//将状态变成未删除，插入数据
        ShopCashier ishave = shopCashierService.isHave(inVo);
        if(ishave!=null){
            return new BaseResp<Long>(ResultStatus.error_many_cashier,ishave.getId());
        }
        inVo.setCreatorId(inVo.getParentId());
        inVo.setUpdatorId(inVo.getParentId());
        inVo.setIsAdmin(ShopCashier.SHOP_CASHIER_NO_ADMIN);
        Long add = shopCashierService.add(inVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS,add);
    }

    /**
     * 逻辑删除一个收银员
     * @param inVo
     * @param result
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public BaseResp<Long> delete(@Valid ShopCashierInVo inVo, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        inVo.setIsDeleted(ShopCashier.SHOP_CASHIER_NO_DELETED);
        ShopCashier have = shopCashierService.isHave(inVo);
        //当查询出来的结果为空的时候，但是商家端页面有这个核销员，证明这条数据是脏数据，是以前通过客服添加的老数据，则直接默认删除成功
        if(have==null){
            return  new BaseResp<Long>(ResultStatus.SUCCESS);
        }
        ShopCashierInVo shopCashierInVo = new ShopCashierInVo();
        BeanUtils.copyProperties(have, shopCashierInVo);
        shopCashierInVo.setIsDeleted(ShopCashier.SHOP_CASHIER_IS_DELETED);//将删除状态变为1
        shopCashierService.update(shopCashierInVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS,have.getId());
    }
}
