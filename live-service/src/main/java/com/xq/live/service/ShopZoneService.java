package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.ShopZone;
import com.xq.live.vo.in.ShopZoneInVo;
import com.xq.live.vo.in.ShopZoneItemInVo;
import com.xq.live.vo.out.ShopZoneItemOut;
import com.xq.live.vo.out.ShopZoneOut;

import java.util.List;

/**
 * 专区相关Service
 * Created by lipeng on 2019/1/4.
 */
public interface ShopZoneService {
    Pager<ShopZone> list(ShopZoneInVo inVo);

    Pager<ShopZoneItemOut> listItem(ShopZoneItemInVo inVo);

    List<ShopZoneOut> listAllShopZone(ShopZoneInVo inVo);
}
