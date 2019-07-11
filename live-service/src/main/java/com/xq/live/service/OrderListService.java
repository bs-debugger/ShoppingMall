package com.xq.live.service;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.vo.in.OrderCouponInVo;
import com.xq.live.vo.in.OrderListInVo;
import com.xq.live.vo.out.OrderListOut;

public interface OrderListService {

    /**
     * 获取订单列表
     * */
    Pager<OrderListOut> getList(OrderListInVo orderListInVo);

    /**
     * 获取订单列表条数
     * */
    BaseResp getListTotal(OrderListInVo orderListInVo);

    /**
     *订单状态修改
     * */
    BaseResp updateOrderList(OrderCouponInVo inVo);

}
