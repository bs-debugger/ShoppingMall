package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.OrderAddress;
import com.xq.live.model.User;
import com.xq.live.service.OrderAddressService;
import com.xq.live.vo.out.OrderAddressOut;
import com.xq.live.web.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户收货地址管理
 * Created by ss on 2018/9/4.
 */

@Api(tags = "用户收货地址管理")
@RestController
@RequestMapping(value = "/orderAddress")
public class OrderAddressController {
    @Autowired
    private OrderAddressService orderAddressService;

    /**
     * 根据userid查询用户收货地址信息
     *
     * 注:userId为当前用户的id，从网关中获取
     * @return
     * localhost:8080/orderAddress/getAddressList
     */
    @ApiOperation(value = "据userid查询用户收货地址信息")
    @RequestMapping(value = "/getAddressList", method = RequestMethod.GET)
    public BaseResp<Pager<OrderAddressOut>> list(){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Pager<OrderAddressOut>>(ResultStatus.error_param_empty);
        }
        Pager<OrderAddressOut> result = orderAddressService.getAddressList(user.getId());
        return new BaseResp<Pager<OrderAddressOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 查询单个收货地址信息
     * @param Id
     * @return
     * localhost:8080/orderAddress/getAddress?Id=1
     */
    @ApiOperation(value = "查询单个收货地址信息")
    @RequestMapping(value = "/getAddress", method = RequestMethod.GET)
    public BaseResp<OrderAddressOut> getAddress(Long Id){
        if(Id==null){
            return new BaseResp<OrderAddressOut>(ResultStatus.error_param_empty);
        }
        OrderAddressOut result = orderAddressService.getAddress(Id);
        return new BaseResp<OrderAddressOut>(ResultStatus.SUCCESS, result);
    }

    /**
     * 添加一条收货地址记录
     *
     * 注:userId为当前用户的id，从网关中获取
     * inAddress/dictProvinceId&dictCityId&dictCountyId&detailAddress&chatName&mobile&isDefault
     * @param address
     * @return
     */
    @ApiOperation(value = "添加一条收货地址记录")
    @RequestMapping(value = "/inAddress", method = RequestMethod.POST)
    public BaseResp<Integer> inUserAddress(OrderAddress address) {
        if (address==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        address.setUserId(user.getId());
        Integer result = orderAddressService.inUserAddress(address);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }

    /**
     * 删除一条收货地址记录
     * @param address
     * @return
     * localhost:8080/orderAddress/outAddress?id=1
     */
    @ApiOperation(value = "删除一条收货地址记录")
    @RequestMapping(value = "/outAddress", method = RequestMethod.POST)
    public BaseResp<Integer> outUserAddress(OrderAddress address) {
        if (address==null||address.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        Integer result = orderAddressService.outUserAddress(address);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }

    /**
     * 更新一条收货地址记录
     * @param address
     * @return
     * localhost:8080/orderAddress/upAddress?id=6&isDefault=1
     */
    @ApiOperation(value = "更新一条收货地址记录")
    @RequestMapping(value = "/upAddress", method = RequestMethod.POST)
    public BaseResp<Integer> upUserAddress(OrderAddress address) {
        if (address==null||address.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        Integer result = orderAddressService.upUserAddress(address);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }


}
