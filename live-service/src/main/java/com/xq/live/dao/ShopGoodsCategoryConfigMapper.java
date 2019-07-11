package com.xq.live.dao;

import com.xq.live.model.ShopGoodsCategoryConfig;
import com.xq.live.vo.out.ShopGoodsCategoryConfigOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopGoodsCategoryConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShopGoodsCategoryConfig record);

    int insertSelective(ShopGoodsCategoryConfig record);

    ShopGoodsCategoryConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ShopGoodsCategoryConfig record);

    int updateByPrimaryKey(ShopGoodsCategoryConfig record);

    List<ShopGoodsCategoryConfigOut> selectByShopCategoryId(Long ShopCategoryId);
}
