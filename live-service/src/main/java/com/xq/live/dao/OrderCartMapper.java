package com.xq.live.dao;

import com.xq.live.model.OrderCart;
import com.xq.live.vo.out.OrderCartOut;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderCartMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderCart record);

    int insertSelective(OrderCart record);

    OrderCart selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderCart record);

    int updateByPrimaryKey(OrderCart record);

    Integer updateByGoodsSkuIdAndUserId(OrderCart orderCart);

    Integer updateBatchByGoodsSkuIdAndUserId(@Param("userId")Long userId,@Param("orderCarts")List<OrderCart> orderCarts);

    OrderCartOut findByGoodsSkuIdAndUserId(OrderCart orderCart);
}
