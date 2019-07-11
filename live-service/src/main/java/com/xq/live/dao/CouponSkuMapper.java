package com.xq.live.dao;

import com.xq.live.model.CouponSku;
import com.xq.live.vo.in.CouponSkuInVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 券基础信息Dao
 */
@Repository
public interface CouponSkuMapper {

    int deleteByPrimaryKey(Long id);

    int insert(CouponSku record);

    int insertSelective(CouponSku record);

    CouponSku selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponSku record);

    int updateByPrimaryKey(CouponSku record);

    List<CouponSku> list(CouponSkuInVo inVo);

    CouponSku selectBySkuId(CouponSku record);
}
