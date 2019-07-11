package com.xq.live.service;

import com.xq.live.model.ShopCashier;
import com.xq.live.vo.in.ShopCashierInVo;

/**
 * 收营员管理接口，提供父账户
 * Created by lipeng on 2018/5/23.
 */
public interface ShopCashierService {

    /**
     * 通过shopId查到该商家的管理员
     * @param shopId
     * @return
     */
    ShopCashier adminByShopId(Long shopId);

    /**
     * 通过shopId和cashierId查询不是管理员且被逻辑删除的用户
     * @param inVo
     * @return
     */
    ShopCashier isHave(ShopCashierInVo inVo);

    /**
     * 添加一个收银员
     * @param inVo
     * @return
     */
    Long add(ShopCashierInVo inVo);

    /**
     * 更新一个核销员信息
     * @param inVo
     * @return
     */
    Integer update(ShopCashierInVo inVo);
}
