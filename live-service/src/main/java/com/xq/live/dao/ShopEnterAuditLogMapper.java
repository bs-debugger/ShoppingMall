package com.xq.live.dao;

import com.xq.live.model.ShopEnterAuditLog;

public interface ShopEnterAuditLogMapper {

    int deleteByPrimaryKey(Long id);

    int insert(ShopEnterAuditLog record);

    int insertSelective(ShopEnterAuditLog record);

    ShopEnterAuditLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ShopEnterAuditLog record);

    int updateByPrimaryKey(ShopEnterAuditLog record);

}