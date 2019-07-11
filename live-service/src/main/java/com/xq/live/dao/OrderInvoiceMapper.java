package com.xq.live.dao;

import com.xq.live.model.OrderInvoice;
import com.xq.live.vo.in.OrderInvoiceInVo;
import com.xq.live.vo.out.OrderInvoiceOut;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OrderInvoiceMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderInvoice record);

    int insertSelective(OrderInvoice record);

    OrderInvoice selectByPrimaryKey(Long id);

    OrderInvoice selectByOrderCode(OrderInvoice record);

    int updateByPrimaryKeySelective(OrderInvoice record);

    int updateByPrimaryKey(OrderInvoice record);

    List<OrderInvoiceOut> list(OrderInvoiceInVo inVo);

    int listTotal(OrderInvoiceInVo inVo);

    List<OrderInvoiceOut> selectListByTemp(OrderInvoiceInVo orderInvoiceInVo);

    /**
     * 批量同意发票审核查询
     * */
    List<OrderInvoice> selectById(OrderInvoiceInVo orderInvoiceInVo);

    /**
     * 批量同意发票审核修改
     * */
    int updateById(OrderInvoice record);

    /**
     * 获取驳回原因
     * */
    List<Map<String,Object>> getReason();
}
