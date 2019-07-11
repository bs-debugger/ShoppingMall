package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.OrderCoupon;
import com.xq.live.model.OrderCouponSend;
import com.xq.live.model.PaidLog;
import com.xq.live.vo.in.OrderCouponInVo;
import com.xq.live.vo.in.OrderWriteOffInVo;
import com.xq.live.vo.out.OrderCouponHxOut;
import com.xq.live.vo.out.OrderCouponOut;

import java.util.Map;

/**
 * 商城系统订单票券Service
 * Created by lipeng on 2018/9/14.
 */
public interface OrderCouponService {
    OrderCouponOut selectById(Long id);

    OrderCouponOut getDetail(OrderCouponInVo inVo);

    OrderCoupon getByCouponCode(String couponCode);

    OrderCouponOut getDetailByCouponCode(String couponCode);

    Pager<OrderCouponOut> list(OrderCouponInVo inVo);

    Integer updateCoupon(OrderCoupon orderCoupon);

    int paidCouponSendAmount(PaidLog inVo);

    Integer sendCoupon(OrderCouponSend inVo);

    Pager<OrderCouponOut> listForSend(OrderCouponInVo inVo);

    Pager<OrderCouponOut> listCoupon(OrderCouponInVo inVo);

    Pager<OrderCouponOut> listCouponForShopUser(OrderCouponInVo inVo);

    Integer sendVersionCoupon(OrderCouponSend inVo);

    /*
    * 赠送票券*/
    Integer complimentaryTickets(OrderCouponSend inVo);

    /*
    * 获取票券信息,判断是否退款
    * */
    OrderCoupon getOrderCouponInfo(OrderCouponSend inVo);

    int addUserAccountToShop(OrderCouponInVo orderCouponInVo);

    int addUserAccountToShopV1(OrderCouponInVo orderCouponInVo);

    int hxCoupon(OrderCouponInVo inVo);

    int hxCouponV2(OrderCouponInVo inVo);

    int useCoupon(OrderCouponInVo inVo);

    OrderCouponHxOut hxJurisdiction(Long id, Long userId, Long shopId);

    Map<String,Object> orderCouponExport (OrderCouponInVo inVo);

    Map<String,Object> orderWriteCouponExport(OrderWriteOffInVo inVo);

    int useCouponForRedPacket(OrderCouponInVo inVo);

    /**
     * 3.7，3.8用户首单核销发放奖励金
     * @return
     */
    int girlsDay(OrderCouponOut orderCouponOut);

    String girlsDayFirstWriteOff(OrderCouponOut orderCouponOut);

    Pager<OrderCouponOut> listForVip(OrderCouponInVo inVo);
}
