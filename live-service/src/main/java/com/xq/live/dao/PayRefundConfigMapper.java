package com.xq.live.dao;

import com.xq.live.model.PayRefundConfig;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayRefundConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayRefundConfig record);

    int insertSelective(PayRefundConfig record);

    PayRefundConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayRefundConfig record);

    int updateByPrimaryKey(PayRefundConfig record);

    List<PayRefundConfig> listByRefIdAndType(List<PayRefundConfig> list);
}
