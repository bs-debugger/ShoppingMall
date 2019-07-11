package com.xq.live.dao;

import com.xq.live.model.GlobalConfig;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GlobalConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GlobalConfig record);

    int insertSelective(GlobalConfig record);

    GlobalConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GlobalConfig record);

    int updateByPrimaryKey(GlobalConfig record);

    List<GlobalConfig> list(GlobalConfig record);
}
