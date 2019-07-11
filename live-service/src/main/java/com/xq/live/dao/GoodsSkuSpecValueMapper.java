package com.xq.live.dao;

import com.xq.live.model.GoodsSkuSpecValue;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsSkuSpecValueMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GoodsSkuSpecValue record);

    int insertSelective(GoodsSkuSpecValue record);

    GoodsSkuSpecValue selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsSkuSpecValue record);

    int updateByPrimaryKey(GoodsSkuSpecValue record);
}
