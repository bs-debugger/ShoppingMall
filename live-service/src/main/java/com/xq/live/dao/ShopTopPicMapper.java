package com.xq.live.dao;

import com.xq.live.model.ShopTopPic;
import com.xq.live.vo.out.ShopTopPicOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopTopPicMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShopTopPic record);

    int insertSelective(ShopTopPic record);

    ShopTopPic selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ShopTopPic record);

    int updateByPrimaryKey(ShopTopPic record);

    /**
     * 根据shop_id查询图片列表
     * @param shopId
     * @return
     */
    List<ShopTopPicOut> selectByShopId(Long shopId);
}