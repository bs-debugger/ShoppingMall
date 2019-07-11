package com.xq.live.dao;

import com.xq.live.model.UserBlacklist;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBlacklistMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserBlacklist record);

    int insertSelective(UserBlacklist record);

    UserBlacklist selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserBlacklist record);

    int updateByPrimaryKey(UserBlacklist record);

    UserBlacklist selectByUserName(String userName);
}