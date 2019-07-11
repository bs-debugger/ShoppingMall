package com.xq.live.web.controllerForWeb;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.Shop;
import com.xq.live.model.ShopEnter;
import com.xq.live.model.User;
import com.xq.live.service.ShopEnterService;
import com.xq.live.service.UserService;
import com.xq.live.vo.out.ShopEnterOut;
import com.xq.live.web.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by lipeng on 2018/3/8.
 */
@RestController
@RequestMapping(value = "/website/shopEnter")
public class ShopEnterForWebController {

    @Autowired
    private ShopEnterService shopEnterService;


    @Autowired
    private UserService userService;


    /**
     * 商家入驻---新增一条记录
     *
     * 注:此处的userId是当前用户的，但是userName不是当前用户的，所以userId要从网关中取
     * @param shopEnter
     * @param result
     * @return
     */
    @RequestMapping(value = "add",method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid ShopEnter shopEnter, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        if(user.getUserType()==User.USER_TYPE_SJ){
            return new BaseResp<Long>(ResultStatus.error_user_is_shop_cashier);
        }
        shopEnter.setUserId(user.getId());
        ShopEnterOut shopEnterOut = shopEnterService.selectByUserIdAndShopName(shopEnter);
        if(shopEnterOut!=null){
            return new BaseResp<Long>(ResultStatus.error_user_shop_exist,shopEnter.getUserId());
        }
        Long skuId = shopEnterService.add(shopEnter);
        return new BaseResp<Long>(ResultStatus.SUCCESS, skuId);
    }

    /**
     * 通过shopName查询是否有相同店家
     * @param shopName
     * @return
     */
    @RequestMapping(value = "/searchByShopName",method = RequestMethod.GET)
    public BaseResp<Shop> searchByShopName(String shopName){
        if(shopName==null){
            return new BaseResp<Shop>(ResultStatus.error_param_empty);
        }
        Shop shop = shopEnterService.searchByShopName(shopName);
        return new BaseResp<Shop>(ResultStatus.SUCCESS,shop);
    }

    /**
     * 通过userId查询商家入驻信息
     * @param userId
     * @return
     */
    @RequestMapping(value = "/searchByUserId",method = RequestMethod.GET)
    public BaseResp<ShopEnterOut> searchByUserId(Long userId){
        if(userId==null){
            return new BaseResp<ShopEnterOut>(ResultStatus.error_param_empty);
        }
        ShopEnterOut shopEnterOut = shopEnterService.searchByUserId(userId);
        return new BaseResp<ShopEnterOut>(ResultStatus.SUCCESS,shopEnterOut);
    }

    /**
     *   审批通过后，插入shop表,更改user状态
     * @param shopEnter
     * @return
     */
    @RequestMapping(value = "/addShop",method = RequestMethod.GET)
    public BaseResp<Integer> addShop(ShopEnter shopEnter){
        if(shopEnter==null||shopEnter.getUserId()==null||shopEnter.getShopName()==null){
                 return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        Integer integer = shopEnterService.addShop(shopEnter);
        if(integer==null){
            return new BaseResp<Integer>(-1,"查询结果异常",null);
        }
        return new BaseResp<Integer>(0,"成功",null);
    }





}
