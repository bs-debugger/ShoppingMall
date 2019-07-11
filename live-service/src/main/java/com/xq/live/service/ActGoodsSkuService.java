package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.ActGoodsSku;
import com.xq.live.model.ActLotteryCategory;
import com.xq.live.model.OrderInfo;
import com.xq.live.model.PullUser;
import com.xq.live.vo.in.ActGoodsSkuInVo;
import com.xq.live.vo.in.ActLotteryCategoryInVo;
import com.xq.live.vo.out.ActGoodsSkuOut;
import com.xq.live.vo.out.ActGoodsSkuRecommendOut;

import java.text.ParseException;
import java.util.List;

/**
 * Created by ss on 2018/11/3.
 * 参与活动的商品
 */
public interface ActGoodsSkuService {

    /**
     * 查询参与此活动的商品类目
     * @param inVo
     * @return
     */
    List<ActGoodsSkuOut> selectByGroupSpu(ActGoodsSkuInVo inVo);

    /**
     * 邀请新人抽奖
     * @param inVo
     * @return
     */
    ActGoodsSkuOut lottery(ActGoodsSkuInVo inVo, Long userId, String userName, String userIp, PullUser pullUser) throws ParseException;

    /**
     * 专区抽奖
     * @param inVo
     * @return
     */
    ActGoodsSkuOut zoneLottery(ActGoodsSkuInVo inVo, Long userId, String userName, String userIp) throws ParseException;

    /**
     * 长期抽奖
     * @param inVo
     * @return
     */
    ActGoodsSkuOut aloneLottery(ActGoodsSkuInVo inVo, Long userId, String userName, String userIp) throws ParseException;


    /**
     * 二级分销
     * 给上级账户增加奖励金
     * @param orderInfo
     */
    void distribution(OrderInfo orderInfo);

    /**
     * 通过订单查询参加活动的团
     * @param orderInfo
     */
    ActGoodsSkuOut getActSkuListAndOrder(OrderInfo orderInfo);

    /**
     *二级分销开团成功
     *修改成功用户订单所产生的奖励金到已获得，并在钱包增加余额
     * 修改失败用户订单所产生的奖励金到已失败
     * @param actGoodsSku
     */
    void succesDistribution(ActGoodsSku actGoodsSku);

    /**
     *二级分销开团失败
     * 修改失败用户订单所产生的奖励金到已失败
     * @param actGoodsSku
     */
    void  failDistribution(ActGoodsSku actGoodsSku);

    Integer updateSku();

    Long add(ActGoodsSkuInVo inVo);

    ActGoodsSku  selectByPrimaryKey(Long id);

    /**
     * 投票数量增加
     * @param inVo
     * @return
     */
    int updateVoteNumUp(ActGoodsSkuInVo inVo);

    /**
     * 分页查询活动列表
     * @return
     */
    Pager<ActLotteryCategory> ActLotteryList(ActLotteryCategoryInVo inVo);

    List<ActGoodsSkuOut> selectActByGoodsSkuId(ActGoodsSkuInVo actGoodsSkuInVo);

    /**
     * 查询活动信息
     * @param actId
     * @param goodsSkuId
     * @return
     */
    List<ActGoodsSkuRecommendOut> selectActInfo(Integer actId, Integer goodsSkuId);

    /**
     * 通过活动id和城市商品
     * @param actGoodsSkuInVo
     * @return
     */
    Pager  <ActGoodsSkuOut> selectGoodsList(ActGoodsSkuInVo actGoodsSkuInVo);

    /**
     * 增加库存
     * @param actGoodsSkuInVo
     * @return
     */
    Integer  addStockNum(ActGoodsSkuInVo actGoodsSkuInVo);

}
