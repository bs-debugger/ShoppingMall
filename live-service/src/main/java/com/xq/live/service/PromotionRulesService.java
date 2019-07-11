package com.xq.live.service;

import com.xq.live.model.PromotionRules;
import com.xq.live.model.ShopPromotionRules;
import com.xq.live.vo.out.PromotionRulesOut;
import com.xq.live.vo.out.ShopPromotionRulesOut;

import java.util.List;

/**
 * Created by lipeng on 2018/3/30.
 */
public interface PromotionRulesService {

    /**
     * 根据商家id查询商家所需券规则
     * @param shopId
     * @return
     */
    List<PromotionRulesOut> selectByShopId(Long shopId);

    List<ShopPromotionRulesOut> selectByShopIdNew(Long shopId);

    /**
     * 验证商家是否支持对应满减规则
     * @param record
     * @return
     */
    ShopPromotionRules selectByRules(ShopPromotionRules record);

    /**
     * 更新修改促销规则
     * @param rules
     * @return
     */
    int update(PromotionRules rules);

    /**
     * 新增促销规则
     * @param rules
     * @return
     */
    Long add(PromotionRules rules);

    /**
     * 通过id去删除一个促销规则
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 通过id去查看一个促销规则
     * @param id
     * @return
     */
    PromotionRules selectByPrimaryKey(Long id);

    /**
     * 添加规则
     * @param
     * @return
     */
    Integer addPromotionRules();

    int deleteByIdNew(Long id);

    int updateNew(ShopPromotionRules rules);

    Long addNew(ShopPromotionRules rules);
}
