package com.xq.live.dao;

import com.xq.live.model.ActLottery;
import com.xq.live.vo.in.ActLotteryInVo;

public interface ActLotteryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ActLottery record);

    int insertSelective(ActLottery record);

    ActLottery selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActLottery record);

    int updateByPrimaryKey(ActLottery record);

    ActLottery selectUserLottery(ActLotteryInVo record);

    int updateDownTotalNumber(ActLotteryInVo record);

    int updateUpTotalNumber(ActLotteryInVo record);
}
