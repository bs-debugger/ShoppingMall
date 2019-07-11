package com.xq.live.dao;

import com.xq.live.model.ShopCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopCategoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShopCategory record);

    int insertSelective(ShopCategory record);

    ShopCategory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ShopCategory record);

    int updateByPrimaryKey(ShopCategory record);

    List<ShopCategory> findCategoriesByParentId(Long parentId);

    List<ShopCategory> selectShopCateByList(List<Long> list);
}
