package com.xq.live.dao;

import com.xq.live.model.PayRefundApplication;
import com.xq.live.vo.in.PayRefundInVo;

import java.util.List;

public interface PayRefundApplicationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayRefundApplication record);

    int insertSelective(PayRefundApplication record);

    PayRefundApplication selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayRefundApplication record);

    int updateByPrimaryKey(PayRefundApplication record);

    int ListTotalPayRefundApplication(PayRefundInVo inVo);

    List<PayRefundApplication> listPayRefundApplication(PayRefundInVo inVo);

}
