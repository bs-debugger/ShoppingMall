package com.xq.live.dao;

import com.xq.live.model.OrderCouponSend;
import com.xq.live.vo.out.OrderCouponSendOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderCouponSendMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderCouponSend record);

    int insertSelective(OrderCouponSend record);

    OrderCouponSend selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderCouponSend record);

    int updateByPrimaryKey(OrderCouponSend record);

    List<OrderCouponSendOut> list(OrderCouponSend record);
}
