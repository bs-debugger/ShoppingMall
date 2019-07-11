package com.xq.live.dao;

import com.xq.live.model.ActTimeRules;

public interface ActTimeRulesMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ActTimeRules record);

    int insertSelective(ActTimeRules record);

    ActTimeRules selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActTimeRules record);

    int updateByPrimaryKey(ActTimeRules record);
}