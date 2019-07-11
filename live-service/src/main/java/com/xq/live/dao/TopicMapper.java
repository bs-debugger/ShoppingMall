package com.xq.live.dao;

import com.xq.live.model.Favorites;
import com.xq.live.model.Topic;
import com.xq.live.vo.in.TopicInVo;
import com.xq.live.vo.out.TopicForZanOut;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@CacheConfig(cacheNames = "topic")
public interface TopicMapper{

    int deleteByPrimaryKey(Long id);

    int insert(Topic record);

    int insertSelective(Topic record);

    Topic selectByPrimaryKey(Long id);

    TopicForZanOut selectByZan(TopicInVo inVo);

    List<Topic> selectByUserId(Favorites favorites);

    int updateByPrimaryKeySelective(Topic record);

    int updateByPrimaryKey(Topic record);

    List<TopicForZanOut> list(TopicInVo inVo);

    List<TopicForZanOut> myList(TopicInVo inVo);

    Integer zanTotalForUser(TopicInVo inVo);

    int listTotal(TopicInVo inVo);

    int myListTotal(TopicInVo inVo);
}
