package com.xq.live.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xq.live.common.RedisCache;
import com.xq.live.dao.GoodsCategoryMapper;
import com.xq.live.dao.ShopGoodsCategoryConfigMapper;
import com.xq.live.model.GoodsCategory;
import com.xq.live.service.GoodsCategoryService;
import com.xq.live.vo.out.ShopGoodsCategoryConfigOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by lipeng on 2018/11/1.
 */
@Service
public class GoodsCategoryServiceImpl implements GoodsCategoryService{
    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;

    @Autowired
    private ShopGoodsCategoryConfigMapper shopGoodsCategoryConfigMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public List<GoodsCategory> list() {
        String key = "goodsCategory";
        JSONArray goodsCategory = redisCache.get(key, JSONArray.class);
        if(goodsCategory!=null){
            List<GoodsCategory> goodsCategories = JSONObject.parseArray(goodsCategory.toJSONString(), GoodsCategory.class);
            return goodsCategories;
        }
        List<GoodsCategory> categoriesByParentId = goodsCategoryMapper.findCategoriesByParentId(0L);
        JSONArray objects = JSONArray.parseArray(JSON.toJSONString(categoriesByParentId));
        redisCache.set(key,objects,1L, TimeUnit.HOURS);
        return categoriesByParentId;
    }

    @Override
    public List<GoodsCategory> list(Long parentId) {
        List<GoodsCategory> categoriesByParentId = goodsCategoryMapper.findCategoriesByParentId(parentId);
        return categoriesByParentId;
    }

    @Override
    public List<GoodsCategory> listById(Long id) {
        List<GoodsCategory> categoriesById = goodsCategoryMapper.findCategoriesById(id);
        return categoriesById;
    }

    @Override
    public List<ShopGoodsCategoryConfigOut> selectGoodsCategoryByShopCategoryId(Long shopCategoryId) {
        List<ShopGoodsCategoryConfigOut> shopGoodsCategoryConfigOuts = shopGoodsCategoryConfigMapper.selectByShopCategoryId(shopCategoryId);
        return shopGoodsCategoryConfigOuts;
    }
}
