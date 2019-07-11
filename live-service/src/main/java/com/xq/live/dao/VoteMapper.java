package com.xq.live.dao;

import com.xq.live.model.Vote;
import com.xq.live.vo.in.VoteInVo;
import com.xq.live.vo.out.VoteOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Vote record);

    int insertSelective(Vote record);

    Vote selectByPrimaryKey(Long id);

    List<Vote> canVote(VoteInVo inVo);

    List<Vote> canGetSku(VoteInVo inVo);

    int updateByPrimaryKeySelective(Vote record);

    int updateByPrimaryKey(Vote record);

    int deleteByInVo(VoteInVo inVo);

    /**
     * 根据活动查询投票数
     * @param actId
     * @return
     */
    int countByActId(Long actId);

    List<Vote> getDaysList(VoteInVo inVo);

    List<VoteOut> listUserRanking(VoteInVo inVo);

    int listUserRankingTotal(VoteInVo inVo);
}
