package com.xq.live.dao;

import com.xq.live.model.PayRefundLog;

import java.util.List;

public interface PayRefundLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayRefundLog record);

    int insertSelective(PayRefundLog record);

    PayRefundLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayRefundLog record);

    int updateByPrimaryKey(PayRefundLog record);

    List<PayRefundLog> listByOutRefundNo(String outRefundNo);

}
