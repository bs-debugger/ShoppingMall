package com.xq.live.dao;

import com.xq.live.model.OrderInvoiceConfig;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderInvoiceConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderInvoiceConfig record);

    int insertSelective(OrderInvoiceConfig record);

    OrderInvoiceConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderInvoiceConfig record);

    int updateByPrimaryKey(OrderInvoiceConfig record);

    List<OrderInvoiceConfig> listByRefIdAndType(List<OrderInvoiceConfig> list);
}
