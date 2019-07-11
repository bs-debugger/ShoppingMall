package com.xq.live.service;

import com.xq.live.model.GoldLog;
import com.xq.live.vo.in.GoldLogInVo;
import com.xq.live.vo.out.BargainLogOut;

import java.text.ParseException;
import java.util.Map;

/**
 * 用户发起砍菜领取金币
 * Created by ss on 2018/8/6.
 */
public interface GoldLogService {

    /**
     * 更新用户金币
     * @param inVo
     * @return
     */
    Integer changeState(GoldLogInVo inVo);

    /**
     * 用户发起领金币(发起人)(砍价)
     * @param inVo
     * @return
     */
    BargainLogOut initiator(GoldLogInVo inVo) throws ParseException;

    /**
     * 用户发起领金币(发起人)(抢购)
     * @param inVo
     * @return
     */
    BargainLogOut createQg(GoldLogInVo inVo);

    /**
     * 帮砍人领金币(好友)(砍价)
     * @param inVo
     * @return
     */
    Map<String,Object> helpfriend(GoldLogInVo inVo);

    /**
     * 帮砍人领金币(好友)(抢购)
     * @param inVo
     * @return
     */
    Map<String,Object> helpfriendQg(GoldLogInVo inVo);

    /**
     * 是否领取过金币或者人数已满(砍价)
     * @param inVo
     * @return
     */
    Integer getshior(GoldLogInVo inVo);

    /**
     * 是否领取过金币或者人数已满(抢购)
     * @param inVo
     * @return
     */
    Integer getshiorQg(GoldLogInVo inVo);



}
