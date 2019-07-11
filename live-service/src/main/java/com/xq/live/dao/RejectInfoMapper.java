package com.xq.live.dao;


import com.xq.live.model.RejectInfo;

public interface RejectInfoMapper {
    int deleteByPrimaryKey(Long rId);

    int insert(RejectInfo record);

    int insertSelective(RejectInfo record);

    RejectInfo selectByPrimaryKey(Long rId);

    int updateByPrimaryKeySelective(RejectInfo record);

    int updateByPrimaryKey(RejectInfo record);
}