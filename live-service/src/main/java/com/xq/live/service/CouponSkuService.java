package com.xq.live.service;

import com.xq.live.model.CouponSku;
import com.xq.live.vo.in.CouponSkuInVo;

import java.util.List;

/**
 * 券基础信息
 *
 * @author zhangpeng32
 * @date 2018-02-09 13:21
 * @copyright:hbxq
 **/
public interface CouponSkuService {

    /**
     * 查询列表
     * @param inVo
     * @return
     */
    List<CouponSku> list(CouponSkuInVo inVo);

    /**
     * 新增
     * @param couponSku
     * @return
     */
    Long add(CouponSku couponSku);

    /**
     * 查询单条记录
     * @param sku
     * @return
     */
    CouponSku selectBySkuId(CouponSku sku);
}
