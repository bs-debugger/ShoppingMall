package com.xq.live.dao;

import com.xq.live.model.DeliveryFree;

import java.util.List;

public interface DeliveryFreeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DeliveryFree record);

    int insertSelective(DeliveryFree record);

    DeliveryFree selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeliveryFree record);

    int updateByPrimaryKey(DeliveryFree record);

    List<DeliveryFree> findListByTemplateId(Long deliveryTemplateId);
}
