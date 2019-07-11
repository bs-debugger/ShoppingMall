package com.xq.live.dao;

import com.xq.live.model.DictCounty;
import com.xq.live.vo.in.DictInVo;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@CacheConfig(cacheNames = "dictCounty")
public interface DictCountyMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DictCounty record);

    int insertSelective(DictCounty record);

    DictCounty selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DictCounty record);

    int updateByPrimaryKey(DictCounty record);

    @Cacheable(value = "1h",key = "'county_' + #p0.cityId.toString()")
    List<DictCounty> list(DictInVo inVo);
}
