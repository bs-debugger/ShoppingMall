package com.xq.live.dao;

import com.xq.live.model.Shop;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopDao extends BaseDao<Shop>{
    int deleteByPrimaryKey(Long id);

    int insert(Shop record);

    int insertSelective(Shop record);

    Shop selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Shop record);

    int updateByPrimaryKey(Shop record);
}