package com.xq.live.service.impl;

import com.xq.live.dao.UserDao;
import com.xq.live.model.SysRoleRel;
import com.xq.live.model.User;
import com.xq.live.service.UserService;
import com.xq.live.vo.BaseVo;
import com.xq.live.vo.UserInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-10 1:01
 * @copyright:hbxq
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public List<User> queryWithPg(BaseVo inVo) throws Exception {
        Integer rowCount = queryWithCount(inVo);
        inVo.getPager().setRowCount(rowCount);
        return userDao.queryWithPg(inVo);
    }

    @Override
    public int queryWithCount(BaseVo inVo) throws Exception {
        return userDao.queryWithCount(inVo);
    }

    @Override
    public User queryUserById(UserInVo inVo) throws Exception {
        return userDao.selectByPrimaryKey(inVo.getId());
    }

    @Override
    public int addUser(UserInVo inVo) throws Exception {
        return userDao.addUser(inVo);
    }

    @Override
    public int updateUser(UserInVo inVo) throws Exception {
        inVo.setUpdateTime(new Date());
        return userDao.updateUser(inVo);
    }

    @Override
    public int delete(Long[] ids) throws Exception {
        int result = 0;
        for (Long id : ids) {
            // 删除用户
            userDao.delete(id);
            result++;
        }
        return result;
    }
}
