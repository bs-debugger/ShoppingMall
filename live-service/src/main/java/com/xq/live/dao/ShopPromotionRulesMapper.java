package com.xq.live.dao;

import com.xq.live.model.ShopPromotionRules;
import com.xq.live.vo.out.ShopPromotionRulesOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopPromotionRulesMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShopPromotionRules record);

    int insertSelective(ShopPromotionRules record);

    ShopPromotionRules selectByPrimaryKey(Long id);

    /*验证商家是否支持对应满减规则*/
    ShopPromotionRules selectByRules(ShopPromotionRules record);

    List<ShopPromotionRulesOut> selectByShopId(Long shopId);

    int updateByPrimaryKeySelective(ShopPromotionRules record);

    int updateByPrimaryKey(ShopPromotionRules record);
}
