package com.xq.live.service;

import com.alibaba.fastjson.JSONObject;
import com.xq.live.vo.in.ActivityLayoutInVo;
import com.xq.live.vo.in.ActivityRecommendInVo;
import com.xq.live.vo.out.ActivityRecommendOut;

import java.util.List;

public interface ActivityRecommendService {

    /**
     * 根据城市查询所有的活动推荐
     * @param city
     * @return
     */
    List<ActivityRecommendOut> findActivityRecommend(String city);

    /**
     * 查询页面活动的推荐集合
     * @param activityLayoutInVo
     * @return
     */
    JSONObject findActivityRecommendPage(ActivityLayoutInVo activityLayoutInVo);

    /**
     * 保存活动推荐
     * @param activityRecommendInVo
     * @return
     */
    JSONObject saveActivityRecommend(ActivityRecommendInVo activityRecommendInVo);

    /**
     * 对换两个活动的排序
     * @param srcActivityRecommendId
     * @param targetActivityRecommendId
     * @param activityType
     * @return
     */
    JSONObject sort(Integer srcActivityRecommendId, Integer targetActivityRecommendId, Integer activityType);

    /**
     * 根据活动推荐ID删除活动推荐
     * @param id
     * @return
     */
    JSONObject removeById(Integer id);

}
