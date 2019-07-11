package com.xq.live.dao;

import com.xq.live.model.DeliveryMethod;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryMethodMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DeliveryMethod record);

    int insertSelective(DeliveryMethod record);

    DeliveryMethod selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeliveryMethod record);

    int updateByPrimaryKey(DeliveryMethod record);

    List<DeliveryMethod> findListByTemplateId(Long deliveryTemplateId);
}
