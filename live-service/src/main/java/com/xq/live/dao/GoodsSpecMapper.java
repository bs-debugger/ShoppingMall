package com.xq.live.dao;

import com.xq.live.model.GoodsSpec;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsSpecMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GoodsSpec record);

    int insertSelective(GoodsSpec record);

    GoodsSpec selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsSpec record);

    int updateByPrimaryKey(GoodsSpec record);
}
