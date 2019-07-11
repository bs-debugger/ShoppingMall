package com.xq.live.service;

import com.xq.live.model.Zan;
import com.xq.live.vo.in.VoteInVo;

import java.util.Map;

/**
 * @package: com.xq.live.service
 * @description: 各种统计service
 * @author: zhangpeng32
 * @date: 2018/3/19 20:20
 * @version: 1.0
 */
public interface CountService {
    /**
     * 主题访问量统计
     * @param topicId
     * @return
     */
    Integer topicHits(Long topicId);

    /**
     * 主题转发量统计
     * @param topicId
     * @return
     */
    Integer topicTrans(Long topicId);

    /**
     * 餐厅的人气
     * @param shopId
     * @return
     */
    Integer shopPops(Long shopId);

    /**
     * 投票数据统计(用缓存,每5分钟写一次数据库)
     * @param invo
     * @return
     */
    Integer voteNums(VoteInVo invo);

    /**
     * 实时更新投票数目
     * @param invo
     * @return
     */
    Integer voteNumsNow(VoteInVo invo);

    /**
     * 实时更新人气数目
     * @param zan
     * @return
     */
    Integer zanNumsNow(Zan zan);

    /**
     * 根据userId查询个人主页的点赞数目
     * @param userId
     * @return
     */
    Integer zanTotal(Long userId);

    /**
     * 根据userId查询用户在某个活动的可用投票数目
     * 注:这里没有加入actId，后期业务扩展可自行添加
     * @param userId
     * @return
     */
    Map<String,Integer> actVoteNums(Long userId,Long actId);

}
