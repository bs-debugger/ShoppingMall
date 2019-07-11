package com.xq.live.dao;

import com.xq.live.model.Sku;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.annotation.Resources;

@Repository
public interface SkuDao extends BaseDao<Sku>{

    int deleteByPrimaryKey(Long id);

    int insert(Sku record);

    Sku selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Sku record);

    int updateByPrimaryKey(Sku record);

    Sku selectByCode(String skuCode);

}