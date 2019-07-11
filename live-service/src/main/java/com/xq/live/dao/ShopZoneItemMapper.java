package com.xq.live.dao;

import com.xq.live.model.ShopZoneItem;
import com.xq.live.vo.in.ShopZoneItemInVo;
import com.xq.live.vo.out.ShopZoneItemOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopZoneItemMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShopZoneItem record);

    int insertSelective(ShopZoneItem record);

    ShopZoneItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ShopZoneItem record);

    int updateByPrimaryKey(ShopZoneItem record);

    List<ShopZoneItemOut> list(ShopZoneItemInVo inVo);

    int listTotal(ShopZoneItemInVo inVo);
}
