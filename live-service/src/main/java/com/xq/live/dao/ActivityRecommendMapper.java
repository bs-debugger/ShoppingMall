package com.xq.live.dao;

import com.xq.live.model.ActivityRecommend;
import com.xq.live.vo.out.ActivityRecommendOut;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRecommendMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(ActivityRecommend record);

    int insertSelective(ActivityRecommend record);

    ActivityRecommend selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ActivityRecommend record);

    int updateByPrimaryKey(ActivityRecommend record);

    /**
     * 根据城市查询所有的活动推荐
     * @param city
     * @return
     */
    List<ActivityRecommendOut> findActivityRecommend(String city);

    /**
     * 根据活动布局删除活动推荐信息
     * @return
     */
    int removeByLayoutId(@Param("activityLayoutId") Integer activityLayoutId, @Param("city") String city);

}