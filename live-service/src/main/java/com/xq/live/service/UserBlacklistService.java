package com.xq.live.service;

import com.xq.live.model.UserBlacklist;

/**
 * 用户黑名单
 * Created by ss on 2019/3/19.
 */
public interface UserBlacklistService {
    /**
     * 查询黑名单
     * @param userName
     * @return
     */
    UserBlacklist selectUserByUserName(String userName);
}
