package com.xq.live.dao;

import com.xq.live.model.ServiceConfig;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ServiceConfig record);

    int insertSelective(ServiceConfig record);

    ServiceConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ServiceConfig record);

    int updateByPrimaryKey(ServiceConfig record);

    List<ServiceConfig> listByRefIdAndType(List<ServiceConfig> list);
}
