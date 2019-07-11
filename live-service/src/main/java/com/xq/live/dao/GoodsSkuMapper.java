package com.xq.live.dao;

import com.xq.live.model.GoodsSku;
import com.xq.live.vo.in.GoodsSkuInVo;
import com.xq.live.vo.in.GoodsSkuRecommendInVo;
import com.xq.live.vo.in.SalePointInVo;
import com.xq.live.vo.out.GoodsSkuOut;
import com.xq.live.vo.out.GoodsSkuRecommendOut;
import com.xq.live.vo.out.OrderCartOut;
import com.xq.live.vo.out.PageGoodsSkuVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface GoodsSkuMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GoodsSkuInVo record);

    int insertSelective(GoodsSkuInVo record);

    GoodsSku selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsSkuInVo record);

    int updateByPrimaryKey(GoodsSkuInVo record);

    int listTotal(GoodsSkuInVo inVo);

    List<GoodsSkuOut> list(GoodsSkuInVo inVo);

    /*
    要删掉，万达用
    * */
    int listPlazaTotal(GoodsSkuInVo inVo);
    List<GoodsSkuOut> plazaList(GoodsSkuInVo inVo);

    Long countTotal();

    /*活动用*/
    /**
     * 查询条数
     * @param inVo
     * @return
     */
    int selectActTotal(GoodsSkuInVo inVo);
    /**
     * 分页查询参与此活动的商品
     * @param inVo
     * @return
     */
    List<GoodsSkuOut> selectActList(GoodsSkuInVo inVo);

    /**
     * 分页查询参与此活动的商品
     * @param inVo
     * @return
     */
    List<GoodsSkuOut> selectActListNotAutotrophy(GoodsSkuInVo inVo);
    /**
     * 查询条数
     * @param inVo
     * @return
     */
    int selectActOutTotal(GoodsSkuInVo inVo);
    /**
     * 分页查询参与此活动的推荐商品
     * @param inVo
     * @return
     */
    List<GoodsSkuOut> selectOutActList(GoodsSkuInVo inVo);
    /**
     * 不分页查询参与此活动的商品
     * @param inVo
     * @return
     */
    List<GoodsSkuOut> selectNoPageActList(GoodsSkuInVo inVo);
    /*AND*/

    GoodsSkuOut selectDetailBySkuId(Long id);

    List<GoodsSkuOut> selectListBySkuId(List<OrderCartOut> items);

    /**
     * 订单取消后库存释放
     * @param inVo
     * @return
     */
    Integer updateByRollback(List<GoodsSkuInVo> inVo);

    /**
     * 根据销售点信息查询销售点下的商品
     * @param inVo
     * @return
     */
    List<GoodsSkuOut> listBySalepointId(SalePointInVo inVo);

    /**
     * 根据销售点信息查询销售点下的商品总数
     * @param inVo
     * @return
     */
    int listTotalBySalepointId(SalePointInVo inVo);

    /**
     * 通过shopId查询商品
     * @param shopId
     * @return
     */
    List<GoodsSku> listByShopId(Long shopId);

    GoodsSku selectOneGoodsSkuByShopId(Long shopId);

    /*查询库存不足的商品*/
    List<GoodsSku> selectStocklist();

    /*轻餐需要查询41的砍价的促销规则*/
    List<GoodsSkuOut> listForFastFood(GoodsSkuInVo inVo);

    /**
     * 商品置顶之后该商家排序在此商品之前的商品排序后移一位
     * @param inVo
     * @return
     */
    int  updateSortNumBackOff (GoodsSkuInVo inVo);

    /**
     * 查询每日推荐的候选商品
     * @param goodsSkuRecommendInVo
     * @return
     */
    List<GoodsSkuRecommendOut> dailyRecommend(GoodsSkuRecommendInVo goodsSkuRecommendInVo);

    /**
     * 通过ID更新商品的审核状态和上架状态
     * @param goodsSku
     * @return
     */
    int updateStatusByKey(@Param("item")GoodsSku goodsSku);


    List<PageGoodsSkuVo> pageGoodsSkuForAudit(@Param("startTime")String startTime,@Param("endTime")String endTime,
                                              @Param("skuName")String skuName,@Param("start")int start,
                                              @Param("limit")int limit);

    Integer pageGoodsSkuForAuditTotal(@Param("startTime")String startTime,@Param("endTime")String endTime,
                                      @Param("skuName")String skuName);

    void batchDeleteGoodsSku(@Param("list")List<Long> list, @Param("updateTime")Date updateTime);

    /**
     * 通过ID更新商品的审核状态和上架状态
     * @param list
     * @return
     */
    int batchUpdateStatusByKey(@Param("list")List<GoodsSku> list);
    /**
     * 查询指定商家参加指定活动的商品列表
     * @param inVo
     * @return
     */
    List<GoodsSkuOut> listForShopAct(GoodsSkuInVo inVo);

}
