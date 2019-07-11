package com.xq.live.service;

import com.xq.live.vo.in.ActRankingInVo;
import com.xq.live.vo.out.ActRankingOut;

import java.util.List;

/**
 * Created by ss on 2019/3/1.
 * 获奖商品查询
 */
public interface ActRankingService {
    /**
     * 查询获奖商品列表
     * @param inVo
     * @return
     */
    List<ActRankingOut> selectActInfoList(ActRankingInVo inVo);
}
