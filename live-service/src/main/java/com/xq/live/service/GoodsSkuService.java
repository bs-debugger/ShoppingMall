package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.GoodsSku;
import com.xq.live.vo.in.*;
import com.xq.live.vo.out.*;

import java.util.List;

/**
 * 商城系统Sku的Service
 * Created by lipeng on 2018/8/29.
 */
public interface GoodsSkuService {

    GoodsSku selectOne(Long id);

    Long add(GoodsSkuInVo inVo);

    Integer update(GoodsSkuInVo inVo);

    Pager<GoodsSkuOut> list(GoodsSkuInVo inVo);

    Pager<GoodsSkuOut> listNewForPlaza(GoodsSkuInVo inVo);

    List<GoodsSkuOut> top(GoodsSkuInVo inVo);

    GoodsSkuOut selectDetailBySkuId(Long id);

    /**
     * 查询单个商品详情带上销售点
     * @param inVo
     * @return
     */
    GoodsSkuOut selectPlaceBySkuId(GoodsSkuInVo inVo);

    List<GoodsSkuOut> selectListBySkuId(List<OrderCartOut> items);

    /**
     * 分页查询参与此活动的商品
     * @param inVo
     * @return
     */
    Pager<GoodsSkuOut> actList(GoodsSkuInVo inVo);

    /**
     * 分页查询活动结束的商品
     * @param inVo
     * @return
     */
    Pager<ActGoodsSkuOut> actEndList(GoodsSkuInVo inVo);

    /**
     * 分页查询参与此活动的商品
     * @param inVo
     * @return
     */
    List<GoodsSkuOut> actListHome(GoodsSkuInVo inVo);

    /**
     * 分页查询参与此活动的推荐商品
     * @param inVo
     * @return
     */
    Pager<GoodsSkuOut> actOutList(GoodsSkuInVo inVo);


    /**
     * 不分页查询参与此活动的商品
     * @param inVo
     * @return
     */
    List<GoodsSkuOut> actListAll(GoodsSkuInVo inVo);

    /**
     * 添加商品,并把商品加入到活动当中
     * @param inVo
     * @return
     */
    Long addGoodsSkuAndAct(GoodsSkuInVo inVo);

    /**
     * 添加平台商品,并把商品加入到活动当中(页面用,后期可在此基础上面扩展)
     * @param inVo
     * @return
     */
    Long addPlatformGoodsSkuAndAct(GoodsSkuInVo inVo);

    /**
     * 更新商品,并把商品对应的活动更新
     * @param inVo
     * @return
     */
    Integer updateGoodsSkuAndAct(GoodsSkuInVo inVo);

    List<GoodsSku> ListByshopId(Long shopId);

    Long addPlatformGoodsSkuAndSalePoint(GoodsSkuInVo inVo);

    /**
     * 商品置顶
     * @param id
     * @return
     */
    Integer setGoodsSkuTop(Long id);

    /**
     * 获取每日随机推荐的商品
     * @param goodsSkuRecommendInVo
     * @return
     */
    GoodsSkuRecommendOut dailyRecommend(GoodsSkuRecommendInVo goodsSkuRecommendInVo);

    /**
     * 审核商品状态
     * @param inVo
     * @return
     */
    Integer auditGoodsSku(AduitGoodsSkuInVo inVo);

    /**
     * 查询商品审核列表
     * @param command
     * @return
     */
    Pager<PageGoodsSkuVo> pageGoodsSkuForAudit(PageGoodsSkuCommand command);

    /**
     * 批量删除商品SKU
     * @param command
     * @return
     */
    int batchDeleteGoodsSku(BatchDeleteGoodsSkuCommand command);

    /**
     * 会员优惠券的实际金额要根据慢金额和减金额来计算实际多动金额和单独购买金额
     * @param inVo
     * @return
     */
    GoodsSkuInVo updateForVip(GoodsSkuInVo inVo);

}
