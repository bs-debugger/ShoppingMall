package com.xq.live.dao;

import com.xq.live.model.PaidLog;
import org.springframework.stereotype.Repository;

@Repository
public interface PaidLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PaidLog record);

    int insertSelective(PaidLog record);

    PaidLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PaidLog record);

    int updateByPrimaryKey(PaidLog record);

    int batchInsert(PaidLog paidLog);
}
