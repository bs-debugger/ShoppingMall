package com.xq.live.dao;

import com.xq.live.model.VipCard;
import com.xq.live.vo.in.VipCardInVo;
import com.xq.live.vo.out.VipCardOut;

public interface VipCardMapper {
    int deleteByPrimaryKey(Long id);

    int insert(VipCard record);

    int insertSelective(VipCard record);

    VipCard selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(VipCard record);

    int updateByPrimaryKey(VipCard record);

    VipCardOut getDetail(Long id);

    VipCardOut getByUserAndCardType(VipCardInVo record);
}
