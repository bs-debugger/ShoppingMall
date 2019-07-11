package com.xq.live.dao;

import com.xq.live.model.GoodsRulesConfig;
import com.xq.live.vo.in.GoodsRulesConfigInVO;

import java.util.List;

public interface GoodsRulesConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GoodsRulesConfig record);

    int insertSelective(GoodsRulesConfig record);

    GoodsRulesConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsRulesConfig record);

    int updateByPrimaryKey(GoodsRulesConfig record);

    List<GoodsRulesConfig> list(GoodsRulesConfigInVO inVo);

    int  listTotal(GoodsRulesConfigInVO inVo);
}
