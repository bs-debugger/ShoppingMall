package com.xq.live.service;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.vo.in.PayRefundApplicationInVO;
import com.xq.live.vo.out.PayRefundApplicationOut;



/**
 * 退款申请详情
 * @author lm
 * @date  2019-04-17
 */
public interface RefundApplicationService {

    /**
     *  退款申请列表
     * @param payRefundApplicationVO
     * @return  退款列表
     */
    Pager<PayRefundApplicationOut> getList(PayRefundApplicationInVO payRefundApplicationVO);

    /**
     *  退款申请头部图表
     * @param
     * @return  退款列表
     */
    BaseResp getTableList();

    /**
     * 批量同意退款申请
     *
     * */
    BaseResp agreeRefund(PayRefundApplicationInVO payRefundApplicationVO);

    /**
     * 批量驳回退款申请
     *
     * */
    BaseResp refuseRefund(PayRefundApplicationInVO payRefundApplicationVO);
}
