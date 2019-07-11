package com.xq.live.dao;

import com.xq.live.model.GoodsSpuSpec;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsSpuSpecMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GoodsSpuSpec record);

    int insertSelective(GoodsSpuSpec record);

    GoodsSpuSpec selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsSpuSpec record);

    int updateByPrimaryKey(GoodsSpuSpec record);
}
