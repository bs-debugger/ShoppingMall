package com.xq.live.dao;

import com.xq.live.model.So;
import com.xq.live.vo.out.SoOut;
import org.springframework.stereotype.Repository;

@Repository
public interface SoDao extends BaseDao<SoOut>{
    int deleteByPrimaryKey(Long id);

    int insert(So record);

    int insertSelective(So record);

    So selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(So record);

    int updateByPrimaryKey(So record);

    SoOut findById(Long id);
}