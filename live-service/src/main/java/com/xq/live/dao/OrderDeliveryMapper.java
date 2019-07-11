package com.xq.live.dao;

import com.xq.live.model.OrderDelivery;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDeliveryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderDelivery record);

    int insertSelective(OrderDelivery record);

    OrderDelivery selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderDelivery record);

    int updateByPrimaryKey(OrderDelivery record);

    OrderDelivery selectByOrderCode(String orderCode);

    OrderDelivery selectByOrderCouponCode(String orderCode);
}
