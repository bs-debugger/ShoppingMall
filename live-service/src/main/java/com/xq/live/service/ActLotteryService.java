package com.xq.live.service;

import com.xq.live.model.ActLottery;
import com.xq.live.vo.in.ActLotteryInVo;

/**
 * Created by admin on 2019/1/8.
 */
public interface ActLotteryService {
    ActLottery selectUserLottery(ActLotteryInVo inVo);

    Integer add(ActLotteryInVo inVo);

    Integer hxAdd(ActLotteryInVo inVo,Long id);
}
