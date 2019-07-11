package com.xq.live.dao;

import com.xq.live.model.DictCity;
import com.xq.live.vo.in.DictInVo;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@CacheConfig(cacheNames = "dictCity")
public interface DictCityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DictCity record);

    int insertSelective(DictCity record);

    DictCity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DictCity record);

    int updateByPrimaryKey(DictCity record);

    @Cacheable(value = "1h",key = "'city_' + #p0.provinceId.toString()")
    List<DictCity> list(DictInVo inVo);
}
