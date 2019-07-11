package com.xq.live.dao;

import com.xq.live.model.UserConcern;
import com.xq.live.vo.in.UserConcernInVo;
import com.xq.live.vo.out.UserConcernOut;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户关注mapper
 */
@Repository
public interface UserConcernMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserConcernInVo inVo);

    int insertSelective(UserConcernInVo inVo);

    UserConcern selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserConcernInVo inVo);

    int updateByPrimaryKey(UserConcernInVo inVo);

    int listTotal(UserConcernInVo inVo);

    List<UserConcernOut> list(UserConcernInVo inVo);

    UserConcernOut isCollected(UserConcernInVo inVo);
}
