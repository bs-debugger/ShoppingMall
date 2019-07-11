package com.xq.live.dao;

import com.xq.live.model.PayRefundReason;
import com.xq.live.vo.in.PayRefundReasonInVo;

import java.util.List;

public interface PayRefundReasonMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayRefundReason record);

    int insertSelective(PayRefundReason record);

    PayRefundReason selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayRefundReason record);

    int updateByPrimaryKey(PayRefundReason record);

    int listTotalPayRefundReason(PayRefundReasonInVo inVo);

    List <PayRefundReason> listPayRefundReason(PayRefundReasonInVo inVo);
}
