package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.UserConcern;
import com.xq.live.vo.in.UserConcernInVo;
import com.xq.live.vo.out.UserConcernOut;

import java.util.Map;

/**
 * 用户关注service
 * Created by lipeng on 2018/7/16.
 */
public interface UserConcernService {
    UserConcern get(Long id);

    Pager<UserConcernOut> list(UserConcernInVo inVo);

    UserConcernOut isCollected(UserConcernInVo inVo);

    Long add(UserConcernInVo inVo);

    Integer delete(UserConcernInVo inVo);

    Map<String,Integer> nums(UserConcernInVo inVo);
}
