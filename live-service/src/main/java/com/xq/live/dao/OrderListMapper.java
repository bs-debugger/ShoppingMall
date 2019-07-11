package com.xq.live.dao;

import com.xq.live.model.OrderInfo;
import com.xq.live.vo.in.OrderListInVo;
import com.xq.live.vo.out.OrderCouponOut;
import com.xq.live.vo.out.OrderListOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderListMapper {

    /**
    * 订单列表
    * */
    List<OrderListOut> getList(OrderListInVo orderListInVo);

    Long getListTotal(OrderListInVo orderListInVo);

    /**
     * 获取订单信息
     * */
    OrderCouponOut selectByPrimaryKey(Long orderId);
}
