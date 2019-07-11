package com.xq.live.dao;

import com.xq.live.model.SysUser;
import com.xq.live.model.User;
import com.xq.live.vo.BaseVo;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends BaseDao<User>{
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int addUser(BaseVo inVo);

    int updateUser(BaseVo inVo);
}