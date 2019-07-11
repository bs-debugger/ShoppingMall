package com.xq.live.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xq.live.common.ActivityType;
import com.xq.live.common.LocationUtils;
import com.xq.live.common.RedisCache;
import com.xq.live.common.ResultStatus;
import com.xq.live.dao.ActivityLayoutMapper;
import com.xq.live.dao.ActivityRecommendMapper;
import com.xq.live.exception.AppException;
import com.xq.live.model.ActivityLayout;
import com.xq.live.model.ActivityRecommend;
import com.xq.live.service.ActivityRecommendService;
import com.xq.live.service.ActivityService;
import com.xq.live.vo.in.ActivityLayoutInVo;
import com.xq.live.vo.in.ActivityRecommendInVo;
import com.xq.live.vo.out.ActGoodsSkuOut;
import com.xq.live.vo.out.ActivityRecommendOut;
import com.xq.live.vo.out.ShopOut;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ActivityRecommendServiceImpl implements ActivityRecommendService {

    @Autowired
    private ActivityLayoutMapper activityLayoutMapper;

    @Autowired
    private ActivityRecommendMapper activityRecommendMapper;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private RedisCache redisCache;

    @Override
    @Cacheable(value = "activityRecommendList", key = "'at-activity-recommend-'+#p0")
    public List<ActivityRecommendOut> findActivityRecommend(String city) {
        return activityRecommendMapper.findActivityRecommend(city);
    }

    @Override
    public JSONObject findActivityRecommendPage(ActivityLayoutInVo activityLayoutInVo) {
        // 根据城市和活动布局查询活动布局信息
        List<ActivityLayout> activityLayoutList = activityLayoutMapper.findActivityLayout(activityLayoutInVo);

        JSONObject result = new JSONObject();

        for (ActivityLayout activityLayout : activityLayoutList) {
            Object object = getActivityContentByTypeAndCity(activityLayout.getType(), activityLayout.getCity(), activityLayoutInVo.getPosition(), activityLayoutInVo.getLocationX(), activityLayoutInVo.getLocationY());
            if (object != null) {
                result.put("at-" + activityLayout.getType(), object);
            }
        }

        return result;
    }

    @Override
    @Transactional
    public JSONObject saveActivityRecommend(ActivityRecommendInVo activityRecommendInVo) {
        // 查询活动布局信息
        ActivityLayout activityLayout = activityLayoutMapper.selectByPrimaryKey(activityRecommendInVo.getActivityLayoutId());
        // 活动推荐列表
        List<ActivityRecommend> activityRecommendList = activityRecommendInVo.getActivityRecommendList();

        // 先删掉之前的活动推荐信息
        activityRecommendMapper.removeByLayoutId(activityLayout.getId(), activityLayout.getCity());

        for (ActivityRecommend activityRecommend : activityRecommendList) {
            if (ActivityType.BANNER.getCode().equals(activityLayout.getType()) && StringUtils.isBlank(activityRecommend.getImgUrl())) {
                throw new AppException(ResultStatus.ERROR_ACTIVITY_BANNER_IMG);
            }
            if (!activityLayout.getId().equals(activityRecommend.getActivityLayoutId())) {
                throw new AppException(ResultStatus.ERROR_ACTIVITY_TYPE);
            }

            Date nowDate = new Date();
            activityRecommend.setCreateTime(nowDate);
            activityRecommend.setUpdateTime(nowDate);

            activityRecommendMapper.insertSelective(activityRecommend);
        }

        // 先删除redis缓存再查询新活动信息
        removeActivityRedisCache(activityLayout.getType(), activityLayout.getCity());

        JSONObject result = new JSONObject();
        result.put("at-" + activityLayout.getType(), getActivityContentByTypeAndCity(activityLayout.getType(), activityLayout.getCity(), activityLayout.getPosition()));
        return result;
    }

    @Override
    @Transactional
    public JSONObject sort(Integer srcActivityRecommendId, Integer targetActivityRecommendId, Integer activityType) {
        ActivityRecommend srcActivityRecommend = activityRecommendMapper.selectByPrimaryKey(srcActivityRecommendId);

        ActivityRecommend targetActivityRecommend = activityRecommendMapper.selectByPrimaryKey(targetActivityRecommendId);

        if (srcActivityRecommend == null || targetActivityRecommend == null
                || !srcActivityRecommend.getActivityLayoutId().equals(targetActivityRecommend.getActivityLayoutId())
                || !srcActivityRecommend.getCity().equals(targetActivityRecommend.getCity())) {
            throw new AppException(ResultStatus.error_invalid_argument);
        }

        ActivityLayout activityLayout = activityLayoutMapper.selectByTypeAndCity(activityType, srcActivityRecommend.getCity());

        if (!activityLayout.getId().equals(srcActivityRecommend.getActivityLayoutId())) {
            throw new AppException(ResultStatus.error_invalid_argument);
        }

        Integer srcSort = srcActivityRecommend.getSort();
        Integer targetSort = targetActivityRecommend.getSort();
        srcActivityRecommend.setSort(targetSort);
        targetActivityRecommend.setSort(srcSort);

        activityRecommendMapper.updateByPrimaryKeySelective(srcActivityRecommend);
        activityRecommendMapper.updateByPrimaryKeySelective(targetActivityRecommend);

        // 先删除redis缓存再查询新活动信息
        removeActivityRedisCache(activityType, srcActivityRecommend.getCity());

        JSONObject result = new JSONObject();
        result.put("at-" + activityType, getActivityContentByTypeAndCity(activityType, srcActivityRecommend.getCity(), activityLayout.getPosition()));
        return result;
    }

    @Override
    public JSONObject removeById(Integer id) {
        ActivityRecommend activityRecommend = activityRecommendMapper.selectByPrimaryKey(id);
        if (activityRecommend == null) {
            throw new AppException(ResultStatus.ERROR_ACTIVITY_NOT_FOUND);
        }

        ActivityLayout activityLayout = activityLayoutMapper.selectByPrimaryKey(activityRecommend.getActivityLayoutId());
        if (activityLayout == null) {
            throw new AppException(ResultStatus.error_invalid_argument);
        }

        // 逻辑删除
        activityRecommend.setIsDeleted(true);
        activityRecommend.setUpdateTime(new Date());
        activityRecommendMapper.updateByPrimaryKeySelective(activityRecommend);

        // 先删除redis缓存再查询新活动信息
        removeActivityRedisCache(activityLayout.getType(), activityLayout.getCity());

        JSONObject result = new JSONObject();
        result.put("at-" + activityLayout.getType(), getActivityContentByTypeAndCity(activityLayout.getType(), activityLayout.getCity(), activityLayout.getPosition()));
        return result;
    }

    /**
     * 根据活动类型和城市获取活动内容
     * @param activityType
     * @param city
     * @param locationX
     * @param locationY
     * @return
     */
    private Object getActivityContentByTypeAndCity(Integer activityType, String city, String position, BigDecimal locationX, BigDecimal locationY) {
        if (ActivityType.BANNER.getCode().equals(activityType)) {
            return filterPositionForRecommend(activityService.findBannerActivityRecommendByCity(city), position);
        } else if (ActivityType.BENEFITS.getCode().equals(activityType)) {
            return filterPositionForAct(activityService.findBenefitsActivityRecommendByCity(city), position);
        } else if (ActivityType.YY_SECKILL.getCode().equals(activityType)) {
            return filterPositionForAct(activityService.findYySeckillActivityRecommendByCity(city), position);
        } else if (ActivityType.CHOICENESS_SHOP.getCode().equals(activityType)) {
            return filterPositionForShop(activityService.findChoicenessShopActivityRecommendByCity(city), position, locationX, locationY);
        }
        return null;
    }

    /**
     * 根据活动类型和城市获取活动内容
     * @param activityType
     * @param city
     * @return
     */
    private Object getActivityContentByTypeAndCity(Integer activityType, String city, String position) {
        return getActivityContentByTypeAndCity(activityType, city, position, null, null);
    }

    /**
     * 过滤不符合的布局推荐
     * @param activityRecommendOutList
     * @param position
     * @return
     */
    private List<ActivityRecommendOut> filterPositionForRecommend(List<ActivityRecommendOut> activityRecommendOutList, String position) {
        List<ActivityRecommendOut> filterActivityRecommendOutList = new ArrayList<>();
        for (ActivityRecommendOut activityRecommendOut : activityRecommendOutList) {
            if (StringUtils.isBlank(position) || position.equalsIgnoreCase(activityRecommendOut.getPosition())) {
                filterActivityRecommendOutList.add(activityRecommendOut);
            }
        }
        return filterActivityRecommendOutList;
    }

    /**
     * 过滤不符合的布局推荐
     * @param actGoodsSkuOutList
     * @param position
     * @return
     */
    private List<ActGoodsSkuOut> filterPositionForAct(List<ActGoodsSkuOut> actGoodsSkuOutList, String position) {
        List<ActGoodsSkuOut> filterActGoodsSkuOutList = new ArrayList<>();
        for (ActGoodsSkuOut actGoodsSkuOut : actGoodsSkuOutList) {
            if (StringUtils.isBlank(position) || position.equalsIgnoreCase(actGoodsSkuOut.getPosition())) {
                filterActGoodsSkuOutList.add(actGoodsSkuOut);
            }
        }
        return filterActGoodsSkuOutList;
    }

    /**
     * 过滤不符合的布局推荐
     * @param shopOutList
     * @param position
     * @return
     */
    private List<ShopOut> filterPositionForShop(List<ShopOut> shopOutList, String position, BigDecimal locationX, BigDecimal locationY) {
        List<ShopOut> filterShopOutList = new ArrayList<>();
        for (ShopOut shopOut : shopOutList) {
            if (StringUtils.isBlank(position) || position.equalsIgnoreCase(shopOut.getPosition())) {
                if (locationX != null && locationY != null && shopOut.getLocationX() != null && shopOut.getLocationY() != null) {
                    double distance = LocationUtils.getDistance(locationX.doubleValue(), locationY.doubleValue(), shopOut.getLocationX().doubleValue(), shopOut.getLocationY().doubleValue());
                    shopOut.setDistance(Double.valueOf(distance).intValue());
                }
                filterShopOutList.add(shopOut);
            }
        }
        return filterShopOutList;
    }

    /**
     * 根据活动类型和城市删除缓存的活动内容
     * @param activityType
     * @param city
     */
    private void removeActivityRedisCache(Integer activityType, String city) {
        // 先删除对应城市的活动和布局缓存
        redisCache.del("at-activity-recommend-" + city);
        // 再删除对应的活动内容缓存
        if (ActivityType.BANNER.getCode().equals(activityType)) {
            redisCache.del("at-banner-activity-" + city);
        } else if (ActivityType.BENEFITS.getCode().equals(activityType)) {
            redisCache.del("at-benefits-activity-" + city);
        } else if (ActivityType.YY_SECKILL.getCode().equals(activityType)) {
            redisCache.del("at-yy-seckill-activity-" + city);
        } else if (ActivityType.CHOICENESS_SHOP.getCode().equals(activityType)) {
            redisCache.del("at-choiceness-shop-activity-" + city);
        }
    }

}
