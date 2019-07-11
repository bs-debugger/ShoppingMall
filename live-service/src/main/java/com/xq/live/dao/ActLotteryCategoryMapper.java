package com.xq.live.dao;

import com.xq.live.model.ActLotteryCategory;
import com.xq.live.vo.in.ActLotteryCategoryInVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActLotteryCategoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ActLotteryCategory record);

    int insertSelective(ActLotteryCategory record);

    ActLotteryCategory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActLotteryCategory record);

    int updateByPrimaryKey(ActLotteryCategory record);

    List<ActLotteryCategory> list(ActLotteryCategoryInVo inVo);

    Integer listTotal(ActLotteryCategoryInVo inVo);
}