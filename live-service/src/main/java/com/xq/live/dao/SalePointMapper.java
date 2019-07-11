package com.xq.live.dao;

import com.xq.live.model.SalePoint;
import com.xq.live.vo.in.SalePointInVo;
import com.xq.live.vo.out.SalePointOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalePointMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SalePoint record);

    int insertSelective(SalePoint record);

    SalePoint selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SalePoint record);

    int updateByPrimaryKey(SalePoint record);

    List<SalePointOut>  list(SalePointInVo inVo);

    int listTotal (SalePointInVo inVo);

    List<SalePointOut> listSalepointByGoodsSkuId(SalePointInVo salePointInVo);
}
