package com.xq.live.dao;

import com.xq.live.model.GoodsSpuDesc;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsSpuDescMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GoodsSpuDesc record);

    int insertSelective(GoodsSpuDesc record);

    GoodsSpuDesc selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsSpuDesc record);

    int updateByPrimaryKeyWithBLOBs(GoodsSpuDesc record);

    int updateByPrimaryKey(GoodsSpuDesc record);

    /**
     * 通过spuID查询
     * @param spuId
     * @return
     */
    GoodsSpuDesc selectBySpuId(@Param("spuId")Long spuId);
}
