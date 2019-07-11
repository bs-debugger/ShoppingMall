package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.OrderInfo;
import com.xq.live.model.Vote;
import com.xq.live.vo.in.VoteInVo;
import com.xq.live.vo.out.VoteOut;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-21 20:57
 * @copyright:hbxq
 **/
public interface VoteService {

    /**
     * 查一条记录
     * @param id
     * @return
     */
    public Vote get(Long id);

    /**
     * 新增
     * @param vote
     * @return
     */
    public Long add(VoteInVo vote);

    /**
     * 取消投票
     * @param id
     * @return
     */
    public int delete(Long id);


    /**
     * 根据活动id、店铺id和用户id取消投票
     * @param inVo
     * @return
     */
    public int deleteByInVo(VoteInVo inVo);


    /**
     * 针对新平台活动，判断是否能够投票
     * @param inVo
     * @return
     */
    public Integer canVote(VoteInVo inVo);

    /**
     * 判断用户投票后是否有资格领活动券
     * @param inVo
     * @return
     */
    Integer canGetSku(VoteInVo inVo);

    /**
     * 获取用户当天投票记录
     * @param inVo
     * @return
     */
    List<Vote> getDaysList(VoteInVo inVo);

    /**
     * 活动商品核销后的自动投票
     * @param orderInfo
     * @return
     */
    public Integer addByOrderIdFroAct(OrderInfo orderInfo);

    /**
     * 获取用户投票排行榜
     * @param inVo
     * @return
     */
    Pager<VoteOut> selectUserRanking(VoteInVo inVo);
}
