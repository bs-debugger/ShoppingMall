package com.xq.live.dao;

import com.xq.live.model.AccessLog;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface AccessLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AccessLog record);

    int insertSelective(AccessLog record);

    AccessLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AccessLog record);

    int updateByPrimaryKey(AccessLog record);

    int checkRecordExist(AccessLog record);

    int countViewNum(Map<String, Object> map);
}