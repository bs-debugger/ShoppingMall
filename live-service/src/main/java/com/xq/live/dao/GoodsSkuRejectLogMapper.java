package com.xq.live.dao;

import com.xq.live.model.GoodsSkuRejectLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsSkuRejectLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GoodsSkuRejectLog record);

    int insertSelective(GoodsSkuRejectLog record);

    GoodsSkuRejectLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsSkuRejectLog record);

    int updateByPrimaryKey(GoodsSkuRejectLog record);

    /**
     * 查询出商品最新的驳回详情
     * @param skuId
     * @return
     */
    GoodsSkuRejectLog selectLasterByGoodsSku(@Param("skuId")Long skuId);

    void batchInsert(@Param("list") List<GoodsSkuRejectLog> list);
}