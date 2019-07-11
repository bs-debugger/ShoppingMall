package com.xq.live.dao;

import com.xq.live.model.GoodsSpecValue;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsSpecValueMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GoodsSpecValue record);

    int insertSelective(GoodsSpecValue record);

    GoodsSpecValue selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsSpecValue record);

    int updateByPrimaryKey(GoodsSpecValue record);
}
