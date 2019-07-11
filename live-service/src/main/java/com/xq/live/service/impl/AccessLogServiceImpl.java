package com.xq.live.service.impl;

import com.xq.live.dao.AccessLogMapper;
import com.xq.live.model.AccessLog;
import com.xq.live.model.User;
import com.xq.live.service.AccessLogService;
import com.xq.live.vo.in.UserInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 登录日志逻辑处理service
 * @author zhangpeng32
 * @date 2018-02-25 11:30
 * @copyright:hbxq
 **/
@Service
public class AccessLogServiceImpl implements AccessLogService {

    @Autowired
    private AccessLogMapper accessLogMapper;

    @Override
    public Long createLog(User user) {
        AccessLog accessLog = new AccessLog();
        accessLog.setUserId(user.getId());
        accessLog.setUserName(user.getUserName());
        accessLog.setUserIp(user.getUserIp());
        accessLog.setSource(user.getSourceType());
        int ret = accessLogMapper.insert(accessLog);
        if(ret < 1){
            return null;
        }
        return accessLog.getId();
    }
}
