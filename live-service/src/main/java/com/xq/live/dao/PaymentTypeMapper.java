package com.xq.live.dao;

import com.xq.live.vo.in.PaymentTypeInVo;
import com.xq.live.vo.out.PaymentTypeOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentTypeMapper {

    /**
     * 支付类型列表
     * */
    List<PaymentTypeOut> getList(PaymentTypeInVo paymentTypeInVo);

}
