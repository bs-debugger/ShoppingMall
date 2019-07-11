package com.xq.live.service;

import com.xq.live.model.OrderCouponPostage;
import com.xq.live.vo.in.OrderCouponPostageInVo;

/**
 * Created by ss on 2019/1/25.
 * 票券运费业务
 */
public interface OrderCouponPostageService {


    Long postageInfo(OrderCouponPostageInVo inVo);

    OrderCouponPostage selectInfo(OrderCouponPostageInVo inVo);

    void updateOrderStatus(OrderCouponPostageInVo inVo);
}
