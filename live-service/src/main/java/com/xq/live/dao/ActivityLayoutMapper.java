package com.xq.live.dao;

import com.xq.live.model.ActivityLayout;
import com.xq.live.vo.in.ActivityLayoutInVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLayoutMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(ActivityLayout record);

    int insertSelective(ActivityLayout record);

    ActivityLayout selectByPrimaryKey(Integer id);

    ActivityLayout selectByTypeAndCity(@Param("type") Integer type, @Param("city") String city);

    int updateByPrimaryKeySelective(ActivityLayout record);

    int updateByPrimaryKey(ActivityLayout record);

    /**
     * 根据城市和活动布局查询活动布局信息
     * @param activityLayoutInVo
     * @return
     */
    List<ActivityLayout> findActivityLayout(ActivityLayoutInVo activityLayoutInVo);

}