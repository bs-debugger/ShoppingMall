package com.xq.live.dao;

import com.xq.live.model.UserOpinion;
import com.xq.live.vo.in.UserOpinionInVo;
import com.xq.live.vo.out.UserOpinionOut;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserOpinionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserOpinion record);

    int insertSelective(UserOpinion record);

    UserOpinion selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserOpinion record);

    int updateByPrimaryKey(UserOpinion record);

    /* 查看用户本人意见*/
    List<UserOpinionOut> selectByUserId(UserOpinionInVo inVo);
    /* 查看用户本人意见的条数*/
    int listTotal(UserOpinionInVo inVo);
    /*添加用户反馈数据*/
    Integer insertForOpinion(UserOpinionInVo inVo);
    /*修改意见状态*/
    Integer updateByUser(UserOpinionInVo inVo);
}