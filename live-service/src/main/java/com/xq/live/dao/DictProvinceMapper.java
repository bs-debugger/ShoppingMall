package com.xq.live.dao;

import com.xq.live.model.DictProvince;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@CacheConfig(cacheNames = "dictProvince")
public interface DictProvinceMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DictProvince record);

    int insertSelective(DictProvince record);

    DictProvince selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DictProvince record);

    int updateByPrimaryKey(DictProvince record);

    @Cacheable(value = "1h")
    List<DictProvince> list();
}
