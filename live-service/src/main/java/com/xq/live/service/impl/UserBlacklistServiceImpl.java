package com.xq.live.service.impl;

import com.xq.live.dao.UserBlacklistMapper;
import com.xq.live.model.UserBlacklist;
import com.xq.live.service.UserBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 黑名单
 * Created by ss on 2019/3/19.
 */
@Service
public class UserBlacklistServiceImpl implements UserBlacklistService{

    @Autowired
    private UserBlacklistMapper userBlacklistMapper;



    /**
     * 查询黑名单
     * @param userName
     * @return
     */
    @Override
    public UserBlacklist selectUserByUserName(String userName) {
        return userBlacklistMapper.selectByUserName(userName);
    }
}
