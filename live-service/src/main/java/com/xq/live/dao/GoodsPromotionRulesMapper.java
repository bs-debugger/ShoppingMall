package com.xq.live.dao;

import com.xq.live.model.GoodsPromotionRules;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsPromotionRulesMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GoodsPromotionRules record);

    int insertSelective(GoodsPromotionRules record);

    GoodsPromotionRules selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsPromotionRules record);

    int updateByPrimaryKey(GoodsPromotionRules record);

    /**
     * 通过ID查询商品规则
     * @param list
     * @return
     */
    List<GoodsPromotionRules> selectByIds(@Param("list")List<Long> list);

    /**
     * 通过商品SKUId 查询商品活动规则
     * @param list
     * @return
     */
    List<GoodsPromotionRules> selectBySkuIds(@Param("list")List<Long> list);
}
