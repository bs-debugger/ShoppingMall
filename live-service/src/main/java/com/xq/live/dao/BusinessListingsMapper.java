package com.xq.live.dao;

import com.xq.live.model.GoodsSku;
import com.xq.live.model.ShopCashier;
import com.xq.live.vo.in.BusinessListingsInVo;
import com.xq.live.vo.out.BusinessListingsOut;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BusinessListingsMapper {

    /**
     * 商家列表信息
     * */
    List<BusinessListingsOut> getList(BusinessListingsInVo businessListingsInVo);

    /**
     * 商家列表信息count
     * */
    int getCount(BusinessListingsInVo businessListingsInVo);

    /**
     * 店铺所有商品
     * */
    List<BusinessListingsOut> getShopList(BusinessListingsInVo businessListingsInVo);

    /**
     * 编辑商品信息
     * */
    void updateGoods(BusinessListingsInVo businessListingsInVo);

    /**
     * 商家列表核销员信息
     * */
    List<ShopCashier> getClerkList(String id);

    /**
     * 删除商家信息
     * */
    void deleteBusinesses(BusinessListingsInVo businessListingsInVo);

    /**
     * 店铺主图集合
     * */
    List<Map<String, Object>> getShopOwnerList(String id);

    /**
     * 编辑商家信息
     * */
    void updateBusinesses(BusinessListingsInVo businessListingsInVo);

    /**
     * 删除核销员
     * */
    void updateClerk(BusinessListingsInVo businessListingsInVo);

    /**
     * 新增核销员
     * */
    void insertClerk(BusinessListingsInVo businessListingsInVo);

    /**
     * 获取核销员cashier_id
     * */
    String getCashier();

    /**
     * 商铺主图删除
     * */
    void updateShopOwnerMap(BusinessListingsInVo businessListingsInVo);

    /**
     * 商铺主图新增
     * */
    Integer insertShopOwnerMap(BusinessListingsInVo businessListingsInVo);

    /**
     * 中间表新增
     * */
    void insertShopTopPic(BusinessListingsInVo businessListingsInVo);
}
