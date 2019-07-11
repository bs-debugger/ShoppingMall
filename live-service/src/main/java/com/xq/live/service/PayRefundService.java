package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.PayRefundApplication;
import com.xq.live.model.PayRefundReason;
import com.xq.live.vo.in.PayRefundInVo;
import com.xq.live.vo.in.PayRefundReasonInVo;

import java.text.ParseException;
import java.util.Map;

/**
 * Created by admin on 2018/10/19.
 */
public interface PayRefundService {
    /**
     *判断order_coupon商城卷当前持有人是否为支付人，赠送之后持有人和支付人不同
     * @param out_trade_no
     * @return
     */
    Boolean isPaidUser(String out_trade_no);

    Long addPayRefund(PayRefundApplication payRefundApplication);

    Pager<PayRefundApplication> listPayRefundApplication(PayRefundInVo payRefundInVo);

    int ListTotalPayRefundApplication(PayRefundInVo payRefundInVo);

    Pager<PayRefundReason> listReason(PayRefundReasonInVo inVo);

    /**
     * 向微信发送退款申请
     * 主要用作自动退款
     * @param orderId 订单id
     * @return
     */
    void refund(Long orderId);

    /**
     * 保存退款结果
     * 处理相关订单信息
     * @param refundMap
     */
    void saveRefungResult(Map<String, String> refundMap)throws ParseException;

}

