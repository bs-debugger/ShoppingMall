package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.OrderWriteOff;
import com.xq.live.vo.in.OrderWriteOffInVo;
import com.xq.live.vo.out.OrderWriteOffOut;
import com.xq.live.vo.out.OrderWriteOffResultOut;

import java.util.List;
import java.util.Map;

/**
 * 商城礼品券使用service
 * Created by lipeng on 2018/9/17.
 */
public interface OrderWriteOffService {
    Long add(OrderWriteOff orderWriteOff);

    Pager<OrderWriteOffOut> list(OrderWriteOffInVo inVo);

    OrderWriteOffOut getDetail(Long id);

    OrderWriteOffOut getDetailByOrderCouponCode(String orderCouponCode);

    List<OrderWriteOffOut> listAmount(OrderWriteOffInVo inVo);

    Pager<OrderWriteOffOut> listForShop(OrderWriteOffInVo inVo);

    int updateByShopId(OrderWriteOffInVo inVo);

    Map<String,Object> selectWanDaWriteOff (OrderWriteOffResultOut inVo);
}
