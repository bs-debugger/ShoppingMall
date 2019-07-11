package com.xq.live.dao;

import com.xq.live.model.ActRegister;
import com.xq.live.vo.out.ActRegisterOut;
import org.springframework.stereotype.Repository;

@Repository
public interface ActRegisterMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ActRegister record);

    int insertSelective(ActRegister record);

    ActRegister selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActRegister record);

    int updateByPrimaryKey(ActRegister record);

    ActRegisterOut findRegisterDetailByUserId(ActRegister record);
}
