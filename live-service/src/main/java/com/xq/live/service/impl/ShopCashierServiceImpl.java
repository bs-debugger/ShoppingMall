package com.xq.live.service.impl;

import com.xq.live.dao.ShopCashierMapper;
import com.xq.live.model.ShopCashier;
import com.xq.live.service.ShopCashierService;
import com.xq.live.vo.in.ShopCashierInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 收营员管理接口，提供父账户
 * Created by lipeng on 2018/5/23.
 */
@Service
public class ShopCashierServiceImpl implements ShopCashierService{
    @Autowired
    private ShopCashierMapper shopCashierMapper;

    @Override
    public ShopCashier adminByShopId(Long shopId) {
        ShopCashier shopCashier = shopCashierMapper.adminByShopId(shopId);
        return shopCashier;
    }

    @Override
    public ShopCashier isHave(ShopCashierInVo inVo) {
        ShopCashier have = shopCashierMapper.isHave(inVo);
        return have;
    }

    @Override
    public Long add(ShopCashierInVo inVo) {
        int res = shopCashierMapper.insert(inVo);
        if(res < 1){
            return null;
        }
        return inVo.getId();
    }

    @Override
    public Integer update(ShopCashierInVo inVo) {
        int i = shopCashierMapper.updateByPrimaryKeySelective(inVo);
        return i;
    }
}
