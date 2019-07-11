package com.xq.live.dao;

import com.xq.live.model.DeliveryTemplateShop;
import com.xq.live.vo.in.DeliveryTemplateShopInVo;
import com.xq.live.vo.out.DeliveryTemplateOut;
import com.xq.live.vo.out.DeliveryTemplateShopOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryTemplateShopMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DeliveryTemplateShop record);

    int insertSelective(DeliveryTemplateShop record);

    DeliveryTemplateShop selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeliveryTemplateShop record);

    int updateByPrimaryKey(DeliveryTemplateShop record);

    List<DeliveryTemplateShopOut> list(DeliveryTemplateShopInVo inVo);

    int listTotal(DeliveryTemplateShopInVo inVo);
}
