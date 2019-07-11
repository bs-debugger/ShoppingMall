package com.xq.live.dao;

import com.xq.live.model.OrderItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderItem record);

    int batchInsert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    /**
     * 根据订单编号查询商品信息
     * @param inVo
     * @return
     */
    List<OrderItem> getForCodelist(OrderItem inVo);

}
