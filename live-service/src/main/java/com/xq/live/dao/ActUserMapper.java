package com.xq.live.dao;

import com.xq.live.model.ActUser;
import com.xq.live.vo.in.ActUserInVo;
import com.xq.live.vo.out.ActUserOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ActUserInVo record);

    int insertSelective(ActUserInVo record);

    ActUser selectByPrimaryKey(Long id);

    List<ActUser> selectByUserId(ActUserInVo inVo);

    int updateByPrimaryKeySelective(ActUserInVo record);

    int updateByPrimaryKey(ActUserInVo record);

    int updateForVoteNums(ActUserInVo record);

    int countByActId(Long actId);

    ActUserOut findByInVo(ActUserInVo inVo);

    List<ActUserOut> listForNewAct(ActUserInVo inVo);

    int listTotal(ActUserInVo inVo);

    //批量更新活动落选名单
    int udateByLuoXuan(List<ActUserOut> userOuts);

    //批量更新活动落选名单
    int udateByLuoXuanTwo(List<ActUserOut> userOuts);




}
