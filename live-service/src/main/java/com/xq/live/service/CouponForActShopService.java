package com.xq.live.service;

/**
 * 专门给新活动建立的，用来参与活动的商家核销完券码后投票数目加5票
 * Created by lipeng on 2018/5/4.
 */
public interface CouponForActShopService {

    /**
     * 定时修改超时订单状态和商品库存数量(普通订单)
     */
    void updategoodskuNumNotAct();

    /**
     * 定时给商品库存不足的商家推送提醒(推送到APP)
     */
    void pushWaringStockForApp();


}
