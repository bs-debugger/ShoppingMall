package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.vo.in.ActTopicInVo;
import com.xq.live.vo.out.ActTopicOut;

/**
 * 主题活动Service
 * Created by lipeng on 2018/6/29.
 */
public interface ActTopicService {
    /**
     * 主题活动报名接口
     * @param inVo
     * @return
     */
    Long add(ActTopicInVo inVo);

    /**
     * 分页查询列表
     * @param inVo
     * @return
     */
    Pager<ActTopicOut> listForNewAct(ActTopicInVo inVo);

    /**
     * 查询总点赞量和总浏览量
     * @param inVo
     * @return
     */
    ActTopicOut zanAndHitTotal(ActTopicInVo inVo);
}
