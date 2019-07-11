package com.xq.live.service.impl;

import com.xq.live.common.ActivityType;
import com.xq.live.common.Pager;
import com.xq.live.dao.ActGoodsSkuMapper;
import com.xq.live.service.ActivityRecommendService;
import com.xq.live.service.ActivityService;
import com.xq.live.service.ShopService;
import com.xq.live.vo.in.ActGoodsSkuInVo;
import com.xq.live.vo.in.ShopInVo;
import com.xq.live.vo.out.ActGoodsSkuOut;
import com.xq.live.vo.out.ActivityRecommendOut;
import com.xq.live.vo.out.ShopOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRecommendService activityRecommendService;

    @Autowired
    private ActGoodsSkuMapper actGoodsSkuMapper;

    @Autowired
    private ShopService shopService;

    @Override
    @Cacheable(value = "bannerActivityRecommendList", key = "'at-banner-activity-'+#p0")
    public List<ActivityRecommendOut> findBannerActivityRecommendByCity(String city) {
        List<ActivityRecommendOut> activityRecommendOutList = activityRecommendService.findActivityRecommend(city);

        List<ActivityRecommendOut> resultList = new ArrayList<>();
        for (ActivityRecommendOut activityRecommendOut : activityRecommendOutList) {
            if (!ActivityType.BANNER.getCode().equals(activityRecommendOut.getType())) {
                continue;
            }
            // 如果是banner活动，则把输出信息直接返回
            resultList.add(activityRecommendOut);
        }
        return resultList;
    }

    @Override
    @Cacheable(value = "benefitsActivityRecommendList", key = "'at-benefits-activity-'+#p0")
    public List<ActGoodsSkuOut> findBenefitsActivityRecommendByCity(String city) {
        List<ActivityRecommendOut> activityRecommendOutList = activityRecommendService.findActivityRecommend(city);

        List<ActGoodsSkuOut> actGoodsSkuOutList = new ArrayList<>();
        for (ActivityRecommendOut activityRecommendOut : activityRecommendOutList) {
            if (!ActivityType.BENEFITS.getCode().equals(activityRecommendOut.getType())) {
                continue;
            }
            // 如果是钜惠推荐则查询活动和商品的信息
            ActGoodsSkuOut actGoodsSkuOut = getActGoodsSkuOut(activityRecommendOut);
            if (actGoodsSkuOut != null) {
                actGoodsSkuOutList.add(actGoodsSkuOut);
            }
        }
        return actGoodsSkuOutList;
    }

    @Override
    @Cacheable(value = "yySeckillActivityRecommendList", key = "'at-yy-seckill-activity-'+#p0")
    public List<ActGoodsSkuOut> findYySeckillActivityRecommendByCity(String city) {
        List<ActivityRecommendOut> activityRecommendOutList = activityRecommendService.findActivityRecommend(city);

        List<ActGoodsSkuOut> actGoodsSkuOutList = new ArrayList<>();
        for (ActivityRecommendOut activityRecommendOut : activityRecommendOutList) {
            if (!ActivityType.YY_SECKILL.getCode().equals(activityRecommendOut.getType())) {
                continue;
            }
            // 如果是1元秒杀则查询活动和商品的信息
            ActGoodsSkuOut actGoodsSkuOut = getActGoodsSkuOut(activityRecommendOut);
            if (actGoodsSkuOut != null) {
                actGoodsSkuOutList.add(actGoodsSkuOut);
            }
        }
        return actGoodsSkuOutList;
    }

    @Override
    @Cacheable(value = "choicenessShopActivityRecommendList", key = "'at-choiceness-shop-activity-'+#p0")
    public List<ShopOut> findChoicenessShopActivityRecommendByCity(String city) {
        List<ActivityRecommendOut> activityRecommendOutList = activityRecommendService.findActivityRecommend(city);

        List<ShopOut> shopOutList = new ArrayList<>();
        for (ActivityRecommendOut activityRecommendOut : activityRecommendOutList) {
            if (!ActivityType.CHOICENESS_SHOP.getCode().equals(activityRecommendOut.getType())) {
                continue;
            }
            // 如果是精选商家，则查询商家信息和商家的部分商品
            ShopInVo shopInVo = new ShopInVo();
            shopInVo.setId(activityRecommendOut.getShopId());
            shopInVo.setCity(activityRecommendOut.getCity());
            shopInVo.setPage(1);
            shopInVo.setRows(1);

            Pager<ShopOut> shopOutPager = shopService.listForGoodsSku(shopInVo);
            List<ShopOut> singleShopOutList = shopOutPager.getList();
            if (singleShopOutList != null && !singleShopOutList.isEmpty()) {
                singleShopOutList.get(0).setPosition(activityRecommendOut.getPosition());
                singleShopOutList.get(0).setActivityLayoutId(activityRecommendOut.getActivityLayoutId());
                shopOutList.add(singleShopOutList.get(0));
            }
        }
        return shopOutList;
    }

    /**
     * 获得商品信息
     * @param activityRecommendOut
     * @return
     */
    private ActGoodsSkuOut getActGoodsSkuOut(ActivityRecommendOut activityRecommendOut) {
        // 如果是1元秒杀则查询活动和商品的信息
        ActGoodsSkuInVo inVo = new ActGoodsSkuInVo();
        inVo.setActId(activityRecommendOut.getActId());
        inVo.setSkuId(activityRecommendOut.getGoodsSkuId());
        ActGoodsSkuOut actGoodsSkuOut = actGoodsSkuMapper.selectInfoByActId(inVo);
        if (actGoodsSkuOut != null) {
            actGoodsSkuOut.setPosition(activityRecommendOut.getPosition());
            actGoodsSkuOut.setActivityLayoutId(activityRecommendOut.getActivityLayoutId());
        }
        return actGoodsSkuOut;
    }

}
