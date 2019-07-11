package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.ActShop;
import com.xq.live.vo.in.ActShopInVo;
import com.xq.live.vo.out.ActShopByShopIdOut;
import com.xq.live.vo.out.ActShopOut;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-03-07 15:05
 * @copyright:hbxq
 **/
public interface ActShopService {
    /**
     * 分页查询参与商家列表信息
     * @param inVo
     * @return
     */
    Pager<ActShopOut> list(ActShopInVo inVo);

    /**
     * 分页查询参与商家列表信息(针对的是新活动，带有开始时间和截止时间，可以多次投票)
     * @param inVo
     * @return
     */
    Pager<ActShopOut> listForNewAct(ActShopInVo inVo);


    /**
     * 不分页查询参此活动的商家
     * @param inVo
     * @return
     */
    List<ActShopOut> listActForId(ActShopInVo inVo);

    /**
     * 根据活动id查询参此活动的商家
     * @param id
     * @return
     */
    List<ActShopOut> listShopForAct(Long id);

    //批量更新活动落选名单
    int udateByLuo(List<ActShopOut> shopOuts);

    //批量更新第二轮活动落选名单
    int udateByLuoTwo(List<ActShopOut> shopOuts);

    /**
     * 查询参与商家列表信息
     * @param inVo
     * @return
     */
    List<ActShopOut> top(ActShopInVo inVo);

    /**
     * 商家报名活动
     * @param actShop
     * @return
     */
    Long add(ActShop actShop);

    /**
     * 查询商家是否报名
     * @param inVo
     * @return
     */
    ActShop findByInVo(ActShopInVo inVo);

    List<ActShopByShopIdOut> listForActByShopId(ActShopInVo inVo);


    /**
     * 查询商家是否参与用活动券的活动
     * @return
     */
    Integer searchForShopId(Long shopId);

    /**
     * 查询商家是否参与用活动券的活动
     * @return
     */
    Integer searchForShopIdNew(ActShopInVo inVo);

    /**
     * 商家报名会员买单打折活动
     * @param inVo
     * @return
     */
    Long addForVip(ActShopInVo inVo);

    /**
     * 商家修改会员买单打折活动
     * @param inVo
     * @return
     */
    Integer updateForVip(ActShopInVo inVo);

    /**
     * 商家参加的买单打折活动列表
     * @param inVo
     * @return
     */
    Pager<ActShopOut> listForVip(ActShopInVo inVo);

    /**
     *根据id获取商家买单打折
     * @param id
     * @return
     */
    ActShopOut getShopDiscountById(Long id);

    /**
     * 根据id删除商家买单打折
     * @param id
     * @return
     */
    Integer deleteShopDiscount(Long id);

}
