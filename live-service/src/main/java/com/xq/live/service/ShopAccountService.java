package com.xq.live.service;


import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.model.AccountLog;
import com.xq.live.model.ShopAccount;
import com.xq.live.model.ShopAccountDetail;
import com.xq.live.vo.in.IdInVo;
import com.xq.live.vo.in.ShopAccountInVo;
import com.xq.live.vo.out.ShopAccountOut;

/**
 * 商户对账详情
 * @author zhangmm
 * @date  2019-04-08
 */
public interface ShopAccountService {

    /**
     *  商户对账统计
     * @param inVo 搜索
     * @return  商户收/退款明细
     */
    ShopAccount findShopAccountTotal(ShopAccountInVo inVo);

    /**
     * 商户提现列表
     * @param inVo 搜索
     * @return
     */
    Pager<ShopAccountOut> searchShopWithdrawList(ShopAccountInVo inVo);

    /**
     * 审批通过
     * @param idInVo
     * @return
     */
    BaseResp approvePass(IdInVo idInVo);

    /**
     * 审批驳回
     * @param inVo
     * @return
     */
    BaseResp approveReject(ShopAccountInVo inVo);

    /**
     * 商户单笔核销明细
     * @param inVo
     * @return
     */
    Pager<ShopAccountDetail> findShopAccountDetailById(ShopAccountInVo inVo);

    /**
     * 店铺金额变更操作日志
     * @param inVo
     * @return
     */
    Pager<AccountLog> findAccountLog(ShopAccountInVo inVo);

    /**
     * 手动刷新
     * @return
     */
    ShopAccount refreshShopAccountTotal(ShopAccountInVo inVo);
}
