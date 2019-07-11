package com.xq.live.dao;

import com.xq.live.model.GoodsSkuDetail;

import java.util.List;

public interface GoodsSkuDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GoodsSkuDetail record);

    int insertSelective(GoodsSkuDetail record);

    GoodsSkuDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsSkuDetail record);

    int updateByPrimaryKey(GoodsSkuDetail record);

    List<GoodsSkuDetail> listBySkuId(Long skuId);
}
