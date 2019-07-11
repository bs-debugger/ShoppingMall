package com.xq.live.web.controllerForManage;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.service.OrderListService;
import com.xq.live.vo.in.OrderCouponInVo;
import com.xq.live.vo.in.OrderListInVo;
import com.xq.live.vo.out.OrderListOut;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "订单列表-OrderListController")
@RestController
@RequestMapping("manage/orderList")
public class OrderListController {

    @Autowired
    private OrderListService orderListService;


     /**
     * 订单列表
     * */
    @ApiOperation(value = "获取订单列表")
    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    public BaseResp<Pager<OrderListOut>> getList(@RequestBody OrderListInVo orderListInVo){
        return new BaseResp<>(ResultStatus.SUCCESS, orderListService.getList(orderListInVo));
    }

    /**
     * 订单列表条数
     * */
    @ApiOperation(value = "获取订单列表数量")
    @RequestMapping(value = "/getListTotal", method = RequestMethod.POST)
    public BaseResp getListTotal(@RequestBody OrderListInVo orderListInVo){
        return orderListService.getListTotal(orderListInVo);
    }


    /**
     * 订单列表状态修改
     * */
    @ApiOperation(value = "获取订单列表数量")
    @RequestMapping(value = "/updateOrderList", method = RequestMethod.POST)
    public BaseResp updateOrderList(@RequestBody OrderCouponInVo inVo){
        return orderListService.updateOrderList(inVo);
    }
}
