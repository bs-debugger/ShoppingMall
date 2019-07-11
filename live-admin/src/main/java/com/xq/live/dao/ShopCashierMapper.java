package com.xq.live.dao;

import com.xq.live.model.ShopCashier;

public interface ShopCashierMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShopCashier record);

    int insertSelective(ShopCashier record);

    ShopCashier selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ShopCashier record);

    int updateByPrimaryKey(ShopCashier record);
}