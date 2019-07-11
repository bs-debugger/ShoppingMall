package com.xq.live.dao;

import com.xq.live.model.ShopAccount;
import com.xq.live.model.ShopAccountDetail;
import com.xq.live.vo.in.ShopAccountInVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商户对账
 */
@Repository
public interface ShopAccountMapper {

    /**
     *  商户对账统计
     * @param shopAccountInVo 搜索
     * @return  商户收/退款明细
     */
    ShopAccount  findShopAccountTotal(ShopAccountInVo shopAccountInVo);

    /**
     * 商户提现列表
     * @param inVo 搜索
     * @return
     */
    List<ShopAccount> searchShopWithdrawList(ShopAccountInVo inVo);

    /**
     * 商户提现列表total
     * @param inVo
     * @return
     */
    int searchShopWithdrawTotal(ShopAccountInVo inVo);

    /**
     * 商户单条明细
     * @param cashId
     * @return
     */
    ShopAccount selectByPrimaryKey(Long cashId);

    /**
     * 审批提现状态
     * @param inVo
     * @return
     */
    int updateApproveStatusById(ShopAccountInVo inVo);


    /**
     * 需要核销票券列表
     * @param  inVo
     * @return
     */
    List<Integer> verificationTicketList(ShopAccountInVo inVo);

    /**
     * 核销票券
     * @param  items
     * @return
     */
    int verificationTicketByTime(List<Integer> items);


    /**
     * 需要核销订单的列表
     * @param inVo
     * @return
     */
    List<Integer> verificationOrderList(ShopAccountInVo inVo);

    /**
     * 核销订单
     * @param items 入口参数
     * @return
     */
    int verificationOrderByTime(List<Integer> items);

    /**
     * 商户单笔核销明细
     * @param inVo
     * @return
     */
    List<ShopAccountDetail> findShopAccountDetailById(ShopAccountInVo inVo);

    /**
     * 商户单笔核销明细total
     * @param inVo
     * @return
     */
    int findShopAccountDetailByIdTotal(ShopAccountInVo inVo);
}
