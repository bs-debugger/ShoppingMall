package com.xq.live.dao;

import com.xq.live.model.CouponSku;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponSkuDao extends BaseDao<CouponSku>{
    int deleteByPrimaryKey(Long id);

    int insert(CouponSku record);

    int insertSelective(CouponSku record);

    CouponSku selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponSku record);

    int updateByPrimaryKey(CouponSku record);
}