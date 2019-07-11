package com.xq.live.service;

import com.xq.live.model.OrderCart;
import com.xq.live.vo.out.OrderCartOut;

import java.util.List;

/**
 * 购物车接口Service
 * Created by lipeng on 2018/10/4.
 */
public interface OrderCartService {
    Integer update(OrderCart orderCart);

    Long add(OrderCart orderCart);

    Integer updateByGoodsSkuIdAndUserId(OrderCart orderCart);

    OrderCartOut findByGoodsSkuIdAndUserId(OrderCart orderCart);

    Integer deleteOrderCart(List<OrderCart> list);
}
