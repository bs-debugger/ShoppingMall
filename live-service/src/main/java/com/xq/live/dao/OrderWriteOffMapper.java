package com.xq.live.dao;

import com.xq.live.model.OrderWriteOff;
import com.xq.live.vo.in.OrderWriteOffInVo;
import com.xq.live.vo.out.OrderWriteOffOut;
import com.xq.live.vo.out.OrderWriteOffResultOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderWriteOffMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderWriteOff record);

    int insertSelective(OrderWriteOff record);

    OrderWriteOff selectByPrimaryKey(Long id);

    OrderWriteOffOut selectDetailByPrimaryKey(Long id);

    OrderWriteOffOut selectDetailByOrderCouponId(Long orderCouponId);

    OrderWriteOffOut selectDetailByOrderCouponCode(String orderCouponCode);

    int updateByPrimaryKeySelective(OrderWriteOff record);

    int updateByPrimaryKey(OrderWriteOff record);

    List<OrderWriteOffOut> list(OrderWriteOffInVo orderWriteOffInVo);

    int listTotal(OrderWriteOffInVo orderWriteOffInVo);

    int batchInsert(OrderWriteOff orderWriteOff);

    List<OrderWriteOffOut> selectTotalAmount(OrderWriteOffInVo orderWriteOffInVo);

    int updateByShopId(OrderWriteOffInVo inVo);

    OrderWriteOffOut  selectTotalPrice(OrderWriteOffInVo orderWriteOffInVo);

    List<OrderWriteOffResultOut> selectWanDaWriteOff(OrderWriteOffResultOut in);

    List<OrderWriteOff> selectDailyByUserId(OrderWriteOffInVo orderWriteOffInVo);
}
