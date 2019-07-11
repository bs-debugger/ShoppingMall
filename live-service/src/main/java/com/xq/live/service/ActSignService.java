package com.xq.live.service;

import com.xq.live.exception.ActSignFailException;
import com.xq.live.model.ActSign;
import com.xq.live.vo.in.ActSignInVo;

/**
 * 新平台活动报名材料Service
 * Created by lipeng on 2018/4/28.
 */
public interface ActSignService {

    /**
     * 查询新平台活动是否报名
     * @param inVo
     * @return
     */
    ActSign isSign(ActSignInVo inVo);

    /**
     * 报名注册
     * @param inVo
     * @return
     */
    Long add(ActSignInVo inVo) throws ActSignFailException;
}
