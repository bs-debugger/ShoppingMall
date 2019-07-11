package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.DeliveryTemplateShopMapper;
import com.xq.live.service.DeliveryTemplateShopService;
import com.xq.live.vo.in.DeliveryTemplateShopInVo;
import com.xq.live.vo.out.DeliveryTemplateOut;
import com.xq.live.vo.out.DeliveryTemplateShopOut;
import com.xq.live.vo.out.OrderInvoiceOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商家运费模板ServiceImpl
 * Created by lipeng on 2018/12/26.
 */
@Service
public class DeliveryTemplateShopServiceImpl implements DeliveryTemplateShopService{
    @Autowired
    private DeliveryTemplateShopMapper deliveryTemplateShopMapper;

    @Override
    public Pager<DeliveryTemplateShopOut> list(DeliveryTemplateShopInVo inVo) {
        Pager<DeliveryTemplateShopOut> result = new Pager<DeliveryTemplateShopOut>();
        int listTotal = deliveryTemplateShopMapper.listTotal(inVo);
        if(listTotal > 0){
            List<DeliveryTemplateShopOut> list = deliveryTemplateShopMapper.list(inVo);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        result.setTotal(listTotal);
        return result;
    }
}
