package com.xq.live.dao;

import com.xq.live.model.SoLog;
import org.springframework.stereotype.Repository;

@Repository
public interface SoLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SoLog record);

    SoLog selectByPrimaryKey(Long id);

}