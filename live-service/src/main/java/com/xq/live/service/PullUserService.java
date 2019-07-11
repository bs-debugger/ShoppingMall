package com.xq.live.service;

import com.xq.live.model.PullUser;
import com.xq.live.vo.out.PullUserOut;

/**
 * 用户邀请的service
 * Created by lipeng on 2018/8/15.
 */
public interface PullUserService {

    PullUser get(Long id);

    Integer update(PullUser invo);

    /**
     * 查看邀请螃蟹人数
     * @return
     */
    PullUser getInfo(Long id);

    /**
     * 查看邀请新人人数
     * @return
     */
    PullUserOut getForUserList(PullUser pullUser);

    /**
     * 清空邀请螃蟹人数
     * @return
     */
    Integer updatePullNumsPx(PullUser record);

    /**
     * 邀请人数加一
     * @return
     */
    Integer updatePullNumsUp(PullUser record);

    /**
     * 拼团人数加一
     * @return
     */
    Integer updateActGroupNumUp(PullUser record,Long OrderId);


    /**
     * 配置邀请
     * @return
     */
    Integer insertPx(PullUser record);


}
