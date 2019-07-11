package com.xq.live.dao;

import com.xq.live.model.GoodsCategory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsCategoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GoodsCategory record);

    int insertSelective(GoodsCategory record);

    GoodsCategory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsCategory record);

    int updateByPrimaryKey(GoodsCategory record);

    List<GoodsCategory> findCategoriesByParentId(Long parentId);

    List<GoodsCategory> findCategoriesById(Long Id);
}
