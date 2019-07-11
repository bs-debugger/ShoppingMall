package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.*;
import com.xq.live.model.Favorites;
import com.xq.live.model.Shop;
import com.xq.live.model.Topic;
import com.xq.live.service.FavoritesService;
import com.xq.live.vo.in.FavoritesInVo;
import com.xq.live.vo.in.ShopInVo;
import com.xq.live.vo.out.PromotionRulesOut;
import com.xq.live.vo.out.ShopOut;
import com.xq.live.vo.out.ShopPromotionRulesOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lipeng on 2018/3/5.
 */
@Service
public class FavoritesServiceImpl implements FavoritesService {
    @Autowired
    private FavoritesMapper favoritesMapper;

    @Autowired
    private PromotionRulesMapper promotionRulesMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private ShopPromotionRulesMapper shopPromotionRulesMapper;

    @Override
    public Favorites get(Long id) {
        return favoritesMapper.selectByPrimaryKey(id);
    }


    @Override
    public Pager<Shop> list(FavoritesInVo inVo) {
        Pager<Shop> result = new Pager<Shop>();
        int total = favoritesMapper.listTotal(inVo);
        if(total > 0){
            List<Favorites> list = favoritesMapper.list(inVo);
            List<Shop> listForShops = new ArrayList<Shop>();
            for (Favorites favorites : list) {
                Shop shop = shopMapper.selectByPrimaryKey(favorites.getShopId());
                listForShops.add(shop);
            }
            result.setList(listForShops);
        }
        result.setTotal(total);
        result.setPage(inVo.getPage());
        return result;
    }

    /**
     * 根据用户id查询收藏列表，并且查询商家详情 分页查询(修改版)
     * @param inVo
     * @return
     */
    @Override
    public Pager<ShopOut> getSCForList(FavoritesInVo inVo) {
        Pager<ShopOut> result = new Pager<ShopOut>();
        int total = favoritesMapper.listTotal(inVo);
        if(total > 0){
            List<Favorites> list = favoritesMapper.list(inVo);
            List<ShopInVo> listInVo = new ArrayList<ShopInVo>();
            for (Favorites favorites : list) {
                Shop shop = shopMapper.selectByPrimaryKey(favorites.getShopId());
                ShopInVo shopInVo = new ShopInVo();
                shopInVo.setId(shop.getId());
                listInVo.add(shopInVo);
            }
            List<ShopOut> shopOuts = shopMapper.getSCForList(listInVo);
            /**
             * 将用户减免规则加入
             */
            for (ShopOut shopOut : shopOuts) {
                List<String> stringList = new ArrayList<String>();
                List<ShopPromotionRulesOut> shopPromotionRulesOuts = shopPromotionRulesMapper.selectByShopId(shopOut.getId());
                if(shopPromotionRulesOuts!=null&&shopPromotionRulesOuts.size()>0){
                    for (ShopPromotionRulesOut shopPromotionRulesOut : shopPromotionRulesOuts) {
                        stringList.add(shopPromotionRulesOut.getRuleDesc());
                    }
                }else{
                    List<PromotionRulesOut> promotionRulesOuts = promotionRulesMapper.selectByShopId(shopOut.getId());
                    for (PromotionRulesOut promotionRulesOut : promotionRulesOuts) {
                        stringList.add(promotionRulesOut.getRuleDesc());
                    }
                }
                shopOut.setRuleDescs(stringList);
            }
            result.setList(shopOuts);
        }
        result.setTotal(total);
        result.setPage(inVo.getPage());
        return result;
    }


    @Override
    public Long add(Favorites favorites) {
        
        int res = favoritesMapper.insert(favorites);
        if(res < 1){
            return null;
        }
        return favorites.getId();
    }

    @Override
    public Integer delete(Favorites favorites) {
        Favorites re= favoritesMapper.selectByUserIdAndShopId(favorites);
        if(re==null||"".equals(re.getId())){
            return null;
        }
        Integer result= favoritesMapper.deleteByPrimaryKey(re.getId());
        if(result<1){
            return null;
        }
        return result;
    }

    @Override
    public Shop shopDetail(Long shopId) {
        Shop result = shopMapper.selectByPrimaryKey(shopId);
        return result;
    }

    @Override
    public Boolean isCollected(Favorites favorites) {
        Favorites result = favoritesMapper.selectByUserIdAndShopId(favorites);
        if(result==null){
            return false;
        }
        return true;
    }

    @Override
    public Boolean isActive(Favorites favorites) {
        List<Topic> topic = topicMapper.selectByUserId(favorites);
        if(topic!=null||topic.size()>0){
            return true;
        }
        return false;
    }
}
