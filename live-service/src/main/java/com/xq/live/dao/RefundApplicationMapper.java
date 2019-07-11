package com.xq.live.dao;

import com.xq.live.vo.in.PayRefundApplicationInVO;
import com.xq.live.vo.out.OrderInfoBoOut;
import com.xq.live.vo.out.PayRefundApplicationOut;
import com.xq.live.vo.out.RefundTablesOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefundApplicationMapper {
    /**
     * 获取退款申请列表
     * */
    List<PayRefundApplicationOut> getList(PayRefundApplicationInVO payRefundApplicationVO);


    int getListCount(PayRefundApplicationInVO payRefundApplicationVO);
    /**
     * 获取退款申请头部图表
     * */
    List<RefundTablesOut> getTableList();

    /**
     * 根据ID获取退款信息
     * */
    List<PayRefundApplicationOut> selectById(PayRefundApplicationInVO payRefundApplicationVO);

    /**
     * 批量更新數據
     * */
    int updateById(PayRefundApplicationOut payRefundApplicationVO);

    /**
     * 通过orderCode查询订单信息
     * @param orderCode
     * @return
     */
    OrderInfoBoOut selectByOrderCode(String orderCode);

    /**
     * 余额退款
     * @return
     */
    String refundYE(OrderInfoBoOut orderInfoBo);

    PayRefundApplicationOut getByOutRefundNo(String outRefundNo);
}
