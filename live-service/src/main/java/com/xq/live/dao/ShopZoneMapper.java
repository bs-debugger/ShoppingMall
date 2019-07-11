package com.xq.live.dao;

import com.xq.live.model.ShopZone;
import com.xq.live.vo.in.ShopZoneInVo;
import com.xq.live.vo.out.ShopZoneOut;

import java.util.List;

public interface ShopZoneMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShopZone record);

    int insertSelective(ShopZone record);

    ShopZone selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ShopZone record);

    int updateByPrimaryKey(ShopZone record);

    List<ShopZone> list(ShopZoneInVo inVo);

    int listTotal(ShopZoneInVo inVo);

    List<ShopZoneOut> listAllShopZone(ShopZoneInVo inVo);
}
