package com.xq.live.service;

import com.xq.live.model.ActRegister;
import com.xq.live.vo.out.ActRegisterOut;

/**
 * 签到记录相关service
 */
public interface ActRegisterService {
    ActRegisterOut findRegisterDetailByUserId(ActRegister actRegister);

    Long add(ActRegister actRegister);
}
