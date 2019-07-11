package com.xq.live.dao;

import com.xq.live.model.UserAdvance;
import com.xq.live.vo.in.UserAdvanceInVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户提现
 * @date  2019-4-10
 * @author zhangmm
 */
@Repository
public interface UserAdvanceMapper {

    /**
     * 用户提现列表
     * @param inVo 入参
     * @return
     */
    List<UserAdvance> searchUserAdvanceList(UserAdvanceInVo inVo);

    /**
     * 列表total
     * @param inVo
     * @return
     */
    int searchUserAdvanceTotal(UserAdvanceInVo inVo);

    /**
     * 用户提现批量审批/驳回
     * @param items
     * @param applyStatus
     * @return
     */
    int  batchMotifyApplyStatus(@Param("list") List<Integer> items,@Param("applyStatus") Integer applyStatus);
}