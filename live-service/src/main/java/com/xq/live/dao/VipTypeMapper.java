package com.xq.live.dao;

import com.xq.live.model.VipType;

public interface VipTypeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(VipType record);

    int insertSelective(VipType record);

    VipType selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(VipType record);

    int updateByPrimaryKey(VipType record);
}