package com.xq.live.dao;

import com.xq.live.model.CashApply;
import com.xq.live.vo.in.CashApplyInVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CashApplyMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CashApply record);

    int insertSelective(CashApply record);

    CashApply selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CashApply record);

    int updateByPrimaryKey(CashApply record);

    int listTotal(CashApplyInVo inVo);

    List<CashApply> list(CashApplyInVo inVo);

    List<CashApply> selectByUserId(Long userId);

    //修改申请状态
    Integer paystart(CashApply inVo);

    /**
     * 批量修改符合条件平台订单的对账状态
     * @param inVo
     * @return
     */
    Integer updateOrderCouponByShopIds(CashApplyInVo inVo);

    /**
     * 批量修改符合条件商家订单的对账状态
     * @param inVo
     * @return
     */
    Integer updateOrderInfoByShopIds(CashApplyInVo inVo);

    /**
     * 通过账户ID查询申请记录
     * @param accountId
     * @return
     */
    List<CashApply> selectListByAccountId(@Param("accountId")Long accountId);
}
