package com.xq.live.dao;

import com.xq.live.model.ActTopic;
import com.xq.live.vo.in.ActTopicInVo;
import com.xq.live.vo.out.ActTopicOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActTopicMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ActTopicInVo record);

    int insertSelective(ActTopicInVo record);

    ActTopic selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActTopicInVo record);

    int updateByPrimaryKey(ActTopicInVo record);

    int countByActId(Long actId);

    int listTotal(ActTopicInVo inVo);

    List<ActTopicOut> listForNewAct(ActTopicInVo inVo);

    List<ActTopic> selectByTopicId(ActTopicInVo actTopicInVo);

    int updateForVoteNums(ActTopicInVo topicInVo);

    ActTopicOut zanAndHitTotal(ActTopicInVo inVo);
}
