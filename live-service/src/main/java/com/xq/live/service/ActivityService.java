package com.xq.live.service;

import com.xq.live.vo.out.ActGoodsSkuOut;
import com.xq.live.vo.out.ActivityRecommendOut;
import com.xq.live.vo.out.ShopOut;

import java.util.List;

public interface ActivityService {

    /**
     * 根据城市获取Banner活动
     * @param city
     * @return
     */
    List<ActivityRecommendOut> findBannerActivityRecommendByCity(String city);

    /**
     * 根据城市获取钜惠推荐活动
     * @param city
     * @return
     */
    List<ActGoodsSkuOut> findBenefitsActivityRecommendByCity(String city);

    /**
     * 根据城市获取1元秒杀活动
     * @param city
     * @return
     */
    List<ActGoodsSkuOut> findYySeckillActivityRecommendByCity(String city);

    /**
     * 根据城市获取精选商家活动
     * @param city
     * @return
     */
    List<ShopOut> findChoicenessShopActivityRecommendByCity(String city);

}
