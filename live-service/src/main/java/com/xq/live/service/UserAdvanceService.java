package com.xq.live.service;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.vo.in.UserAdvanceInVo;
import com.xq.live.vo.out.UserAdvanceOut;

import java.util.List;

/**
 * 用户提现
 */
public interface UserAdvanceService {

    /**
     * 用户提现列表
     * @param inVo 入参
     * @return
     */
    Pager<UserAdvanceOut> searchUserAdvanceList(UserAdvanceInVo inVo);

    /**
     * 审批通过
     * @param cashIds
     * @return
     */
    BaseResp batchApplyPass(List<Integer> cashIds);

    /**
     * 审批驳回
     * @param inVo
     * @return
     */
    BaseResp batchApplyReject(UserAdvanceInVo inVo);
}
