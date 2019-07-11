package com.xq.live.dao;

import com.xq.live.model.SoDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SoDetail record);

    int insertSelective(SoDetail record);

    SoDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SoDetail record);

    int updateByPrimaryKey(SoDetail record);

}