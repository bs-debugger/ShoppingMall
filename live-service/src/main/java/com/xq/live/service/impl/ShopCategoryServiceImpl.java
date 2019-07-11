package com.xq.live.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xq.live.common.RedisCache;
import com.xq.live.dao.ShopCategoryMapper;
import com.xq.live.model.ShopCategory;
import com.xq.live.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by lipeng on 2018/11/17.
 */
@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {

    @Autowired
    private ShopCategoryMapper shopCategoryMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public List<ShopCategory> list() {
        String key = "shopCategory";
        JSONArray shopCategory = redisCache.get(key, JSONArray.class);
        if(shopCategory!=null){
            List<ShopCategory> shopCategories = JSONObject.parseArray(shopCategory.toJSONString(), ShopCategory.class);
            return shopCategories;
        }
        List<ShopCategory> categoriesByParentId = shopCategoryMapper.findCategoriesByParentId(0L);
        JSONArray objects = JSONArray.parseArray(JSON.toJSONString(categoriesByParentId));
        redisCache.set(key,objects,1L, TimeUnit.HOURS);
        return categoriesByParentId;
    }

}
