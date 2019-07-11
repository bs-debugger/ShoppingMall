package com.xq.live.dao;

import com.xq.live.model.OrderCouponPostage;

public interface OrderCouponPostageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderCouponPostage record);

    int insertSelective(OrderCouponPostage record);

    OrderCouponPostage selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderCouponPostage record);

    int updateByPrimaryKey(OrderCouponPostage record);
}