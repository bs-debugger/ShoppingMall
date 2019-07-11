package com.xq.live.service.impl;

import com.xq.live.common.RedisCache;
import com.xq.live.dao.OrderCartMapper;
import com.xq.live.model.OrderCart;
import com.xq.live.service.OrderCartService;
import com.xq.live.vo.out.OrderCartListOut;
import com.xq.live.vo.out.OrderCartOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

/**
 * 购物车ServiceImpl
 * Created by lipeng on 2018/10/4.
 */
@Service
public class OrderCartServiceImpl implements OrderCartService{
    @Autowired
    private OrderCartMapper orderCartMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public Integer update(OrderCart orderCart) {
        return orderCartMapper.updateByPrimaryKeySelective(orderCart);
    }

    @Override
    public Long add(OrderCart orderCart) {
        int insert = orderCartMapper.insert(orderCart);
        if(insert<1){
            return null;
        }
        return orderCart.getId();
    }

    @Override
    public Integer updateByGoodsSkuIdAndUserId(OrderCart orderCart) {
        return orderCartMapper.updateByGoodsSkuIdAndUserId(orderCart);
    }

    @Override
    public OrderCartOut findByGoodsSkuIdAndUserId(OrderCart orderCart) {
        return orderCartMapper.findByGoodsSkuIdAndUserId(orderCart);
    }

    @Override
    @Transactional
    public Integer deleteOrderCart(List<OrderCart> list) {
        Long userId = list.get(0).getUserId();
        //查询购物车信息
        String key = "orderCart_" + userId;
        OrderCartListOut orderCartListOut = redisCache.get(key, OrderCartListOut.class);
        orderCartListOut = orderCartListOut!=null?orderCartListOut:new OrderCartListOut();
        List<OrderCartOut> items = orderCartListOut.getItems();

        Iterator<OrderCartOut> sListIterator = items.iterator();
        while (sListIterator.hasNext()) {
            OrderCartOut str = sListIterator.next();
            for (OrderCart orderCart : list) {
                if (str.getGoodsSkuId().equals(orderCart.getGoodsSkuId())) {
                    sListIterator.remove();
                }
            }
        }
        Integer integer = orderCartMapper.updateBatchByGoodsSkuIdAndUserId(userId, list);
        orderCartListOut.setItems(items);
        redisCache.set(key, orderCartListOut);
        return integer;
    }
}
