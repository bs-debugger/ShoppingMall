package com.xq.live.dao;

import com.xq.live.model.ActSign;
import com.xq.live.vo.in.ActSignInVo;
import org.springframework.stereotype.Repository;

@Repository
public interface ActSignMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ActSignInVo record);

    int insertSelective(ActSignInVo record);

    ActSign selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActSignInVo record);

    int updateByPrimaryKey(ActSignInVo record);

    int countByActId(Long actId);

    ActSign isSign(ActSignInVo record);
}
