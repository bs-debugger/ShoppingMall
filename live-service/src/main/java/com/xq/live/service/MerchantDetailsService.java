package com.xq.live.service;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.vo.in.MerchantDetailsInVo;
import com.xq.live.vo.in.UserAccountInVo;
import com.xq.live.vo.out.MerchantDetailsOut;
import com.xq.live.vo.out.ShopZoneOut;
import java.util.List;

public interface MerchantDetailsService {

    /**
     *  商户明细列表
     * @param
     * @return  明细列表
     */
    Pager<MerchantDetailsOut> getList(MerchantDetailsInVo merchantDetailsInVo);

    /**
     *  商户明细头部信息
     * @param
     * @return
     */
    BaseResp getTableList(MerchantDetailsInVo merchantDetailsInVo);

    /**
     * 商户明细详情
     * */
    Pager<MerchantDetailsOut> getDetails(MerchantDetailsInVo merchantDetailsInVo);

    /**
     * 商户明细详情
     * */
    Pager<MerchantDetailsOut> getNoDetails(MerchantDetailsInVo merchantDetailsInVo);

    /**
     * 专区信息
     * */
    List<ShopZoneOut> listAllShopZone();

    /**
     * 商户余额补贴
     * */
    BaseResp updateBusinesses(UserAccountInVo userAccountInVo);
}
