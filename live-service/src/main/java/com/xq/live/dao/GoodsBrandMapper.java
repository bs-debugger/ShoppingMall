package com.xq.live.dao;

import com.xq.live.model.GoodsBrand;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsBrandMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GoodsBrand record);

    int insertSelective(GoodsBrand record);

    GoodsBrand selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsBrand record);

    int updateByPrimaryKey(GoodsBrand record);
}
