package com.xq.live.dao;

import com.xq.live.model.PaidLog;

public interface PaidLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PaidLog record);

    int insertSelective(PaidLog record);

    PaidLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PaidLog record);

    int updateByPrimaryKey(PaidLog record);
}