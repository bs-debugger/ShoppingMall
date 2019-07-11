package com.xq.live.service.impl;

import com.xq.live.dao.PromotionRulesMapper;
import com.xq.live.dao.ShopMapper;
import com.xq.live.dao.ShopPromotionRulesMapper;
import com.xq.live.model.PromotionRules;
import com.xq.live.model.Shop;
import com.xq.live.model.ShopPromotionRules;
import com.xq.live.service.PromotionRulesService;
import com.xq.live.vo.out.PromotionRulesOut;
import com.xq.live.vo.out.ShopPromotionRulesOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lipeng on 2018/3/30.
 */
@Service
public class PromotionRulesServiceImpl implements PromotionRulesService{

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private PromotionRulesMapper promotionRulesMapper;

    @Autowired
    private ShopPromotionRulesMapper shopPromotionRulesMapper;

    @Override
    public List<PromotionRulesOut> selectByShopId(Long shopId) {
        List<PromotionRulesOut> promotionRulesOuts = promotionRulesMapper.selectByShopId(shopId);
        return promotionRulesOuts;
    }

    @Override
    public List<ShopPromotionRulesOut> selectByShopIdNew(Long shopId) {
        List<ShopPromotionRulesOut> promotionRulesOuts = shopPromotionRulesMapper.selectByShopId(shopId);
        return promotionRulesOuts;
    }

    /**
     * 验证商家是否支持对应满减规则
     * @param record
     * @return
     */
    @Override
    public ShopPromotionRules selectByRules(ShopPromotionRules record) {
        return shopPromotionRulesMapper.selectByRules(record);
    }

    @Override
    public int update(PromotionRules rules) {
        return promotionRulesMapper.updateByPrimaryKeySelective(rules);
    }

    @Override
    @Transactional
    public Long add(PromotionRules rules) {
        int res = promotionRulesMapper.insert(rules);
        if(res < 1){
            return null;
        }
        return rules.getId();
    }

    @Override
    @Transactional
    public int deleteById(Long id) {
        return promotionRulesMapper.deleteByPrimaryKey(id);
    }

    @Override
    public PromotionRules selectByPrimaryKey(Long id) {
        PromotionRules rules=promotionRulesMapper.selectByPrimaryKey(id);
        return rules;
    }

    /**
     * 添加规则
     * @param
     * @return
     */
    @Override
    public Integer addPromotionRules() {

        List<PromotionRules> rules= new ArrayList<PromotionRules>(20);
        rules.add(new PromotionRules(new Long(3), "17095464", "10元现金券", 1, "满100减10", new BigDecimal(100), new BigDecimal(10)));
        rules.add(new PromotionRules(new Long(3), "17095464", "10元现金券", 1, "满180减10", new BigDecimal(180), new BigDecimal(10)));
        rules.add(new PromotionRules(new Long(6), "06809587", "20元现金券", 1, "满100减20", new BigDecimal(100), new BigDecimal(20)));
        rules.add(new PromotionRules(new Long(8), "32345673", "40元现金券", 1, "满200减40", new BigDecimal(200), new BigDecimal(40)));
        rules.add(new PromotionRules(new Long(7), "22437998", "30元现金券", 1, "满190减30", new BigDecimal(190), new BigDecimal(30)));
        rules.add(new PromotionRules(new Long(5), "92059847", "5元现金券", 1, "满50减5", new BigDecimal(50), new BigDecimal(5)));
        rules.add(new PromotionRules(new Long(32), "24283853", "25元现金券", 1, "满120减25", new BigDecimal(120), new BigDecimal(25)));
        rules.add(new PromotionRules(new Long(31), "46475752", "15元现金券", 1, "满105减15", new BigDecimal(105), new BigDecimal(15)));
        rules.add(new PromotionRules(new Long(33), "24287942", "35元现金券", 1, "满150减35", new BigDecimal(150), new BigDecimal(35)));
        rules.add(new PromotionRules(new Long(280), "4644572", "100元现金券", 1, "满1000减100", new BigDecimal(1000), new BigDecimal(100)));

        rules.add(new PromotionRules(new Long(3), "17095464", "10元现金券", 1, "满100减10", new BigDecimal(100), new BigDecimal(10)));
        rules.add(new PromotionRules(new Long(3), "17095464", "10元现金券", 1, "满100减10", new BigDecimal(100), new BigDecimal(10)));
        rules.add(new PromotionRules(new Long(5), "92059847", "5元现金券", 1, "满50减5", new BigDecimal(50), new BigDecimal(5)));
        rules.add(new PromotionRules(new Long(5), "92059847", "5元现金券", 1, "满50减5", new BigDecimal(50), new BigDecimal(5)));
        rules.add(new PromotionRules(new Long(5), "92059847", "5元现金券", 1, "满50减5", new BigDecimal(50), new BigDecimal(5)));
        rules.add(new PromotionRules(new Long(5), "92059847", "5元现金券", 1, "满50减5", new BigDecimal(50), new BigDecimal(5)));
        rules.add(new PromotionRules(new Long(7), "22437998", "30元现金券", 1, "满190减30", new BigDecimal(190), new BigDecimal(30)));
        rules.add(new PromotionRules(new Long(7), "22437998", "30元现金券", 1, "满190减30", new BigDecimal(190), new BigDecimal(30)));
        rules.add(new PromotionRules(new Long(6), "06809587", "20元现金券", 1, "满100减20", new BigDecimal(100), new BigDecimal(20)));
        rules.add(new PromotionRules(new Long(280),"4644572","100元现金券",1,"满1000减100",new BigDecimal(1000),new BigDecimal(100)));

        List<Shop> shopList =shopMapper.selectNotShop();
        for (Shop shop:shopList){
            int i= (int) (Math.random()*(20)+1);
            PromotionRules promotionRules=rules.get(i-1);
            promotionRules.setShopId(shop.getId().intValue());
            promotionRulesMapper.insert(promotionRules);
        }
        return null;
    }

    @Override
    @Transactional
    public int deleteByIdNew(Long id) {
        return shopPromotionRulesMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateNew(ShopPromotionRules rules) {
        return shopPromotionRulesMapper.updateByPrimaryKeySelective(rules);
    }

    @Override
    @Transactional
    public Long addNew(ShopPromotionRules rules) {
        int res = shopPromotionRulesMapper.insert(rules);
        if(res < 1){
            return null;
        }
        return rules.getId();
    }
}
