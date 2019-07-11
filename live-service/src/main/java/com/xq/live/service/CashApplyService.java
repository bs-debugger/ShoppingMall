package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.CashApply;
import com.xq.live.vo.in.CashApplyInVo;

import java.util.List;

/**
 * com.xq.live.service
 *  提现业务service
 * @author zhangpeng32
 * Created on 2018/5/6 下午4:54
 * @Description:
 */
public interface CashApplyService {
    /**
     * 创建提现申请
     * @param cashApply
     * @return
     */
    public Long create(CashApply cashApply);

    /**
     * 提现记录列表查询
     * @param inVo
     * @return
     */
    public Pager<CashApply> list(CashApplyInVo inVo);

    /**
     * 通过shopId查询商家的提现
     * @param shopId
     * @return
     */
    List<CashApply> selectByUserId(Long shopId);

    /**
     * 通过id查询单个提现详情
     * @param id
     * @return
     */
    CashApply get(Long id);

    /**
     * 创建提现申请
     * 不生成申请日志
     * @param cashApply
     * @return
     */
    public Long createNew(CashApply cashApply);

    /**
     * /**
     * 查询提现服务费金额
     * 输入提现申请金额cashAmount，返回可提现金额cashAmount 和服务费serviceAmount
     *例如服务费比例为 1% ，用户余额为101，当用户申请提现cashAmount=100时返回实际可提现金额cashAmount=100，服务费serviceAmount=1，
     * 当用户申请提现cashAmount=101时返回实际可提现金额cashAmount=100，服务费serviceAmount=1
     * @param cashApplyInVo
     * @return
     */
    CashApplyInVo getServiceAmount(CashApplyInVo cashApplyInVo);

    /**
     * 修改申请状态
     * @param cashApplyInVo
     * @return
     */
    Integer paystart(CashApply cashApplyInVo);

    /**
     * 批量修改一个商家时间段内符合条件的的订单信息
     * @param vo
     * @return
     */
    Integer  updateOrderCouponList(CashApplyInVo vo);

    /**
     * 通过账户ID查询提现记录
     * @param accountId
     * @return
     */
    List<CashApply> selectListByAccountId(Long accountId);
}
