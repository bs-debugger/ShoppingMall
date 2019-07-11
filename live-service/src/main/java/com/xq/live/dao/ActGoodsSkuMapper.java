package com.xq.live.dao;

import com.xq.live.model.ActGoodsSku;
import com.xq.live.vo.in.ActGoodsSkuInVo;
import com.xq.live.vo.out.ActGoodsSkuOut;
import com.xq.live.vo.out.ActGoodsSkuRecommendOut;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActGoodsSkuMapper {

    int deleteByPrimaryKey(Long id);

    int insert(ActGoodsSku record);

    int insertSelective(ActGoodsSku record);

    ActGoodsSku selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActGoodsSku record);

    int updateByPrimaryKey(ActGoodsSku record);

    /**
     * 查询参与此活动的商品类目
     * @param inVo
     * @return
     */
    List<ActGoodsSkuOut> selectCategoryByActId(ActGoodsSkuInVo inVo);

    /**
     * 查询可抽奖的参与活动商品以及类目
     * @param inVo
     * @return
     */
    List<ActGoodsSkuOut> selectLotteryList(ActGoodsSkuInVo inVo);

    /**
     * 查询参与此活动商品的类目
     * @param inVo
     * @return
     */
    ActGoodsSkuOut selectByActId(ActGoodsSkuInVo inVo);

    /**
     * 查询参与此活动商品的信息
     * @param inVo
     * @return
     */
    ActGoodsSkuOut selectInfoByActId(ActGoodsSkuInVo inVo);

    /**
     * 查询参与此活动商品的类目
     * @param inVo
     * @return
     */
    List<ActGoodsSkuOut> selectHistoryList(ActGoodsSkuInVo inVo);

    Integer selectHistoryTotal(ActGoodsSkuInVo inVo);


    /**
     * 1.通过商品id查询该商品参与的活动列表
     * @param inVo
     * @return
     */
    List<ActGoodsSkuOut> selectActInfoListByGoodsSkuId(ActGoodsSkuInVo inVo);


    /**
     * 抽奖之后减少库存
     * @param record
     * @return
     */
    int updateStockNum(ActGoodsSkuOut record);

    /**
     * 购买订单后库存减少和取消订单后增加
     * @param inVo
     * @return
     */
    int updateCutDownStockNum(ActGoodsSkuInVo inVo);


    /**
     * 订单取消后库存释放
     * @param inVo
     * @return
     */
    Integer updateByRollback(List<ActGoodsSkuInVo> inVo);

    /**
     * 用户参团成功后人数加一
     * @param inVo
     * @return
     */
    int updateCurrentNum(ActGoodsSku inVo);


    /**
     * 修改超时活动状态
     * @param inVo
     * @return
     */
    Integer updateOverTimeActStatus(ActGoodsSku inVo);


    /**
     * 查询拼团活动中已到期的团
     * @return
     */
    List<ActGoodsSku> selectDistributionList();

    List<ActGoodsSku> selectList();

    int countByActId(Long actId);

    /**
     * 投票数量增加
     * @param inVo
     * @return
     */
    int updateVoteNumUp(ActGoodsSkuInVo inVo);

    List<ActGoodsSku> selectListBySkuId(Long skuId);

    /*查询库存不足的商品*/
    List<ActGoodsSku> selectStocklist();

    /**
     * 查询活动信息
     * @param actId
     * @param goodsSkuId
     * @return
     */
    List<ActGoodsSkuRecommendOut> selectActInfo(@Param("actId") Integer actId, @Param("goodsSkuId") Integer goodsSkuId);

    /**
     * 通过活动id和城市商品
     * @param inVo
     * @return
     */
    List<ActGoodsSkuOut> selectGoodsList(ActGoodsSkuInVo inVo);

    Integer selectGoodsTotal(ActGoodsSkuInVo inVo);

}
