package com.xq.live.service;

import com.xq.live.model.ServiceAmount;
import com.xq.live.vo.in.ServiceAmountInVo;

import java.util.List;

/**
 * 缴纳服务费Service
 * Created by lipeng on 2018/7/27.
 */
public interface ServiceAmountService {
    /**
     * 添加缴费记录
     * @param serviceAmountInVo
     * @return
     */
    Long add(ServiceAmountInVo serviceAmountInVo);

    /**
     * 通过shopId查询商家的缴费记录
     * @param shopId
     * @return
     */
    List<ServiceAmount> selectByShopId(Long shopId);

    /**
     * 通过shopId查询商家的所有缴费记录-
     * @param record
     * @return
     */
    List<ServiceAmount> allByShopId(ServiceAmountInVo record);
}
