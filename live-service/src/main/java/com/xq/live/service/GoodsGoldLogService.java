package com.xq.live.service;

import com.xq.live.vo.in.GoodsGoldLogInVo;
import com.xq.live.vo.out.GoodsBargainLogOut;

import java.text.ParseException;
import java.util.Map;

/**
 * Created by ss on 2018/11/3.
 * 商城金币相关
 */
public interface GoodsGoldLogService {
    /**
     * 更新用户金币
     * @param inVo
     * @return
     */
    Integer changeState(GoodsGoldLogInVo inVo);

    /**
     * 用户发起领金币(发起人)(砍价)
     * @param inVo
     * @return
     */
    GoodsBargainLogOut initiator(GoodsGoldLogInVo inVo) throws ParseException;


    /**
     * 帮砍人领金币(好友)(砍价)
     * @param inVo
     * @return
     */
    Map<String,Object> helpFriend(GoodsGoldLogInVo inVo);

    /**
     * 是否领取过金币或者人数已满(砍价)
     * @param inVo
     * @return
     */
    Integer getShior(GoodsGoldLogInVo inVo);

    GoodsBargainLogOut selectByRedis(GoodsGoldLogInVo inVo);

}
