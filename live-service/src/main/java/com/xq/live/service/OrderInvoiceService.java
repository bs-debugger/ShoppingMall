package com.xq.live.service;

import com.github.pagehelper.PageInfo;
import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.model.OrderInvoice;
import com.xq.live.vo.in.OrderInvoiceInVo;
import com.xq.live.vo.out.OrderInvoiceOut;


/**
 * 订单发票Service
 * Created by lipeng on 2018/12/22.
 */
public interface OrderInvoiceService {
    Pager<OrderInvoiceOut> list(OrderInvoiceInVo inVo);

    Long add(OrderInvoice inVo);

    /**
     * 查询订单是否已经开发票或者在开发票申请中
     * true 是
     * false 未开或被驳回
     * @param inVo
     * @return
     */
    Boolean checkOrderInvoice(OrderInvoice inVo);

    /**
     * 分页查询申请状态的发票列表
     * @param orderInvoiceInVo
     * @return
     */
    PageInfo<OrderInvoiceOut> showListByTemp(OrderInvoiceInVo orderInvoiceInVo);

    /**
     * 批量同意and驳回发票申请
     * */
    BaseResp agreeApply(OrderInvoiceInVo orderInvoiceInVo);

    /**
     * 驳回原因
     * */
    BaseResp getReason();
}
