package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.vo.in.DeliveryTemplateShopInVo;
import com.xq.live.vo.out.DeliveryTemplateShopOut;

/**
 * 商家运费模板Service
 * Created by lipeng on 2018/12/26.
 */
public interface DeliveryTemplateShopService {
    Pager<DeliveryTemplateShopOut> list(DeliveryTemplateShopInVo inVo);
}
