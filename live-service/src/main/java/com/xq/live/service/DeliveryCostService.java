package com.xq.live.service;

import com.xq.live.model.DeliveryMethod;
import com.xq.live.vo.in.OrderAddressInVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * com.xq.live.service
 * 配送费用service
 * @author zhangpeng32
 * Created on 2018/9/15 下午12:26
 * @Description:
 */
public interface DeliveryCostService {

    /**
     * 配送费用计算
     * @param orderAddressInVo  配送地址
     * @param weight 重量
     * @return
     */
    public BigDecimal calculateCost(OrderAddressInVo orderAddressInVo, BigDecimal weight,Integer piece,BigDecimal bulk,Long tempateId,Integer formulaMode);

    List<DeliveryMethod> findListByTemplateId(Long tempateId);

    Map<String,Object> findByGoodsSkuId(Long goodsSkuId);
}
