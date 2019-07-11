package com.xq.live.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xq.live.dao.PaymentTypeMapper;
import com.xq.live.service.PaymentTypeService;
import com.xq.live.vo.in.PaymentTypeInVo;
import com.xq.live.vo.out.PaymentTypeOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentTypeServiceImpl implements PaymentTypeService {

    @Autowired
    private PaymentTypeMapper paymentTypeMapper;

    /**
     * 支付类型列表
     * */
    @Override
    public PageInfo<PaymentTypeOut> getList(PaymentTypeInVo paymentTypeInVo) {
        PageHelper.startPage(paymentTypeInVo.getPage(), paymentTypeInVo.getRows());
        List<PaymentTypeOut> orderInvoiceOutList = paymentTypeMapper.getList(paymentTypeInVo);
        return new PageInfo<>(orderInvoiceOutList);
    }
}
