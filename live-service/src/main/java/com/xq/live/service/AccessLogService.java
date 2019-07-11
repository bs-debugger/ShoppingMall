package com.xq.live.service;

import com.xq.live.model.AccessLog;
import com.xq.live.model.User;
import com.xq.live.vo.in.UserInVo;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-25 11:29
 * @copyright:hbxq
 **/
public interface AccessLogService {
    /**
     * 记录用户登录日志
     * @param user
     * @return
     */
    public Long createLog(User user);
}
