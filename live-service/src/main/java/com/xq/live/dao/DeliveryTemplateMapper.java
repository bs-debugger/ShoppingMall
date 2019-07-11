package com.xq.live.dao;

import com.xq.live.model.DeliveryTemplate;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryTemplateMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DeliveryTemplate record);

    int insertSelective(DeliveryTemplate record);

    DeliveryTemplate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeliveryTemplate record);

    int updateByPrimaryKey(DeliveryTemplate record);
}
