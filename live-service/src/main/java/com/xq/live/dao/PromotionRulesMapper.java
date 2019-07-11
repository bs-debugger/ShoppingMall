package com.xq.live.dao;

import com.xq.live.model.PromotionRules;
import com.xq.live.vo.in.ProRuInVo;
import com.xq.live.vo.out.PromotionRulesOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRulesMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PromotionRules record);

    int insertSelective(PromotionRules record);

    PromotionRules selectByPrimaryKey(Long id);

    PromotionRules selectBySkuIdAndSkuCode(ProRuInVo inVo);

    List<PromotionRulesOut> selectByShopId(Long shopId);

    int updateByPrimaryKeySelective(PromotionRules record);

    int updateByPrimaryKey(PromotionRules record);
}
