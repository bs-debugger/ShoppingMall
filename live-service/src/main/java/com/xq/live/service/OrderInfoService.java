package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.OrderInfo;
import com.xq.live.vo.in.OrderCouponInVo;
import com.xq.live.vo.in.OrderInfoInVo;
import com.xq.live.vo.in.OrderWriteOffInVo;
import com.xq.live.vo.in.WeixinInVo;
import com.xq.live.vo.out.OrderInfoOut;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 商城系统订单相关service
 * Created by lipeng on 2018/9/5.
 */
public interface OrderInfoService {
    OrderInfoOut getDetail(Long id);

    OrderInfoOut getDetailNew(Long id);

    Pager<OrderInfoOut> list(OrderInfoInVo inVo);

    Pager<OrderInfoOut> listFree(OrderInfoInVo inVo);

    Pager<OrderInfoOut> listAll(OrderInfoInVo inVo);

    Long create(OrderInfoInVo inVo);

    Long createNew(OrderInfoInVo inVo);

    Long createForShop(OrderInfoInVo inVo);

    Long createNewForFree(OrderInfoInVo inVo);

    Long batchCreateForXq(OrderInfoInVo inVo);

    Long createRo(OrderInfoInVo inVo);

    Long createVo(OrderInfoInVo inVo);

    OrderInfo get(Long orderId);

    int paid(OrderInfoInVo inVo);

    Integer confirmCeceipt(Long id);

    OrderInfo getByCode(String orderCode);

    int paidCoupon(OrderInfoInVo inVo);

    int paidCouponNew(OrderInfoInVo inVo,WeixinInVo weixinInVo) throws ParseException;

    int paidShopOrder(OrderInfoInVo inVo, WeixinInVo attachInVo);

    int paidMDZT(OrderInfoInVo inVo);

    int paidRo(OrderInfoInVo inVo);

    OrderInfoOut getDetailByOrderCode(String orderCode);

    Integer useOrderInfo(OrderInfoInVo inVo);

    List<OrderInfoInVo> getAllOrderList(OrderInfoInVo inVo);

    List<OrderInfoOut> getSonOrderList(OrderInfoOut orderInfoOut);

    Map<String,String> queryShopTurnover(OrderInfoInVo inVo);

    Map<String, String> queryShopTurnoverV1(OrderInfoInVo inVo);

    int batchHxCouponToKafka(OrderCouponInVo inVo);

    Integer isNewUser(Long userId);

    BigDecimal  totalAmount(OrderWriteOffInVo orderWriteOffInVo);

    Integer notPayList(OrderInfoInVo inVo);

}
