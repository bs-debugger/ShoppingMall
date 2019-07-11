package com.xq.live.dao;

import com.xq.live.model.SalePointGoods;
import com.xq.live.vo.in.SalePointInVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalePointGoodsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SalePointGoods record);

    int insertSelective(SalePointGoods record);

    SalePointGoods selectByPrimaryKey(Long id);

    List<SalePointGoods> selectBySkuId(Long skuId);

    int updateByPrimaryKeySelective(SalePointGoods record);

    int updateByPrimaryKey(SalePointGoods record);

    int deleteGoodsSkuBySalePointId(SalePointInVo salePointInVo);
}
