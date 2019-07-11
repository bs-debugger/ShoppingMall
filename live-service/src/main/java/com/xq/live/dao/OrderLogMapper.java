package com.xq.live.dao;

import com.xq.live.model.OrderLog;
import com.xq.live.vo.in.OrderInfoInVo;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderLog record);

    int batchInsert(OrderLog orderLog);

    int insertSelective(OrderLog record);

    OrderLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderLog record);

    int updateByPrimaryKey(OrderLog record);
}
