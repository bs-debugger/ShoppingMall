package com.xq.live.service;

import com.github.pagehelper.PageInfo;
import com.xq.live.vo.in.PaymentTypeInVo;
import com.xq.live.vo.out.PaymentTypeOut;

public interface PaymentTypeService {

    /**
     * 获取支付类型列表
     * */
    PageInfo<PaymentTypeOut> getList(PaymentTypeInVo paymentTypeInVo);

}
