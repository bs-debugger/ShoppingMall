package com.xq.live.web.controller;


import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.ActOrder;
import com.xq.live.model.User;
import com.xq.live.service.ActOrderService;
import com.xq.live.web.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 活动订单controller
 * @author
 * @create 2018-02-07 17:14
 **/
@RestController
@RequestMapping(value = "/actOrder")
public class ActOrderController {

    @Autowired
    private ActOrderService actOrderService;

    /**
     * 新增记录
     * @param actOrder
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<ActOrder> addUser(ActOrder actOrder){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<ActOrder>(ResultStatus.error_param_empty);
        }
        actOrder.setUserId(user.getId());
        //1、必填参数校验
        if (actOrder==null||actOrder.getActGoodsSkuId()==null){
            return new BaseResp<ActOrder>(ResultStatus.error_param_empty);
        }
        ActOrder order  = actOrderService.add(actOrder);
        return new BaseResp<ActOrder>(ResultStatus.SUCCESS, order);
    }
}
