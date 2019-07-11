package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.ShopZoneItemMapper;
import com.xq.live.dao.ShopZoneMapper;
import com.xq.live.model.ShopZone;
import com.xq.live.service.ShopZoneService;
import com.xq.live.vo.in.ShopZoneInVo;
import com.xq.live.vo.in.ShopZoneItemInVo;
import com.xq.live.vo.out.ShopZoneItemOut;
import com.xq.live.vo.out.ShopZoneOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 专区相关ServiceImpl
 * Created by lipeng on 2019/1/4.
 */
@Service
public class ShopZoneServiceImpl implements ShopZoneService{
    @Autowired
    private ShopZoneMapper shopZoneMapper;

    @Autowired
    private ShopZoneItemMapper shopZoneItemMapper;

    @Override
    public Pager<ShopZone> list(ShopZoneInVo inVo) {
        Pager<ShopZone> result =  new Pager<ShopZone>();
        int total = shopZoneMapper.listTotal(inVo);
        if(total > 0){
            List<ShopZone> list = shopZoneMapper.list(inVo);
            result.setList(list);
        }
        result.setTotal(total);
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public Pager<ShopZoneItemOut> listItem(ShopZoneItemInVo inVo) {
        Pager<ShopZoneItemOut> result =  new Pager<ShopZoneItemOut>();
        int total = shopZoneItemMapper.listTotal(inVo);
        if(total > 0){
            List<ShopZoneItemOut> list = shopZoneItemMapper.list(inVo);
            for (ShopZoneItemOut itemOut:list){
                ShopZone shopZone = shopZoneMapper.selectByPrimaryKey(itemOut.getShopZoneId());
                itemOut.setShopZone(shopZone);
            }
            result.setList(list);
        }
        result.setTotal(total);
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public List<ShopZoneOut> listAllShopZone(ShopZoneInVo inVo) {
        return shopZoneMapper.listAllShopZone(inVo);
    }
}
