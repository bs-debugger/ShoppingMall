package com.xq.live.dao;

import com.xq.live.model.OrderCouponExpiryConfig;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderCouponExpiryConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderCouponExpiryConfig record);

    int insertSelective(OrderCouponExpiryConfig record);

    OrderCouponExpiryConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderCouponExpiryConfig record);

    int updateByPrimaryKey(OrderCouponExpiryConfig record);

    List<OrderCouponExpiryConfig> listByRefIdAndType(List<OrderCouponExpiryConfig> list);
}
