package com.xq.live.dao;

import com.xq.live.model.Zhibo;
import com.xq.live.vo.in.ZhiboInVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZhiboMapper {
    int deleteByPrimaryKey(Long id);

    Long insert(Zhibo record);

    int insertSelective(Zhibo record);

    Zhibo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Zhibo record);

    int updateByPrimaryKey(Zhibo record);

    List<Zhibo> list(ZhiboInVo inVo);

    int listTotal(ZhiboInVo inVo);
}