package com.xq.live.service;

import com.github.pagehelper.PageInfo;
import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.vo.in.BusinessListingsInVo;
import com.xq.live.vo.out.BusinessListingsOut;

public interface BusinessListingsService {

     /**
     * 商家列表信息
     * */
    PageInfo<BusinessListingsOut> getList(BusinessListingsInVo businessListingsInVo);

    /**
     * 店铺所有商品
     * */
     Pager<BusinessListingsOut> getShopList(BusinessListingsInVo businessListingsInVo);

    /**
     * 修改商品信息
     * */
    BaseResp updateGoods(BusinessListingsInVo businessListingsInVo);

    /**
     * 删除商家信息
     * */
    BaseResp deleteBusinesses(BusinessListingsInVo businessListingsInVo);

    /**
     * 编辑商家信息
     * */
    BaseResp updateBusinesses(BusinessListingsInVo businessListingsInVo);

    /**
     * 增删核销员
     * */
    BaseResp updateClerk(BusinessListingsInVo businessListingsInVo);

    /**
     * 增删店铺主图
     * */
    BaseResp updateShopOwnerMap(BusinessListingsInVo businessListingsInVo);
}
