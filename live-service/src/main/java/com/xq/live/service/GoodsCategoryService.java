package com.xq.live.service;

import com.xq.live.model.GoodsCategory;
import com.xq.live.vo.out.ShopGoodsCategoryConfigOut;

import java.util.List;

/**
 * Created by lipeng on 2018/11/1.
 */
public interface GoodsCategoryService {
    List<GoodsCategory> list();

    List<GoodsCategory> list(Long parentId);

    List<ShopGoodsCategoryConfigOut> selectGoodsCategoryByShopCategoryId(Long shopCategoryId);

    List<GoodsCategory> listById(Long id);
}
