package com.xq.live.service;/**
 * 评论service
 *
 * @author zhangpeng32
 * @create 2018-02-05 18:55
 */

import com.xq.live.common.Pager;
import com.xq.live.model.Favorites;
import com.xq.live.model.Shop;
import com.xq.live.vo.in.FavoritesInVo;
import com.xq.live.vo.out.ShopOut;

import java.util.List;

/**
 * 评论service
 * @author zhangpeng32
 * @create 2018-02-05 18:55
 **/
public interface FavoritesService {
    /**
     * 根据主键查询一条记录
     * @param id
     * @return
     */
    Favorites get(Long id);


    /**
     * 根据用户id查询收藏列表，并且查询商家详情
     */
    /**
     * 分页查询
     * @param inVo
     * @return
     */
    Pager<Shop> list(FavoritesInVo inVo);

    /**
     * 根据用户id查询收藏列表，并且查询商家详情
     */
    /**
     * 分页查询
     * @param inVo
     * @return
     */
    public Pager<ShopOut> getSCForList(FavoritesInVo inVo);

    /**
     * 根据用户id和商家id,增加一条记录到收藏列表中
     * @param favorites
     * @return
     */
    Long add(Favorites favorites);

    /**
     * 根据用户id和商家id,删除一体记录
     */
     Integer delete(Favorites favorites);

    /**
     * 根据商家id跳转到商家页面
     */
     Shop shopDetail(Long shopId);

    /**
     * 根据用户id和商家id，查询数据
     */
    Boolean isCollected(Favorites favorites);

    /**
     * 根据用户id查询是否有动态消息
     * @param favorites
     * @return
     */
    Boolean isActive(Favorites favorites);
}
