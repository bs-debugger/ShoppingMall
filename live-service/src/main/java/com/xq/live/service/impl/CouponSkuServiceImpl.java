package com.xq.live.service.impl;

import com.xq.live.dao.CouponSkuMapper;
import com.xq.live.model.CouponSku;
import com.xq.live.service.CouponSkuService;
import com.xq.live.vo.in.CouponSkuInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-09 13:22
 * @copyright:hbxq
 **/
@Service
public class CouponSkuServiceImpl implements CouponSkuService {

    @Autowired
    private CouponSkuMapper couponSkuMapper;

    @Override
    public List<CouponSku> list(CouponSkuInVo inVo) {
        return couponSkuMapper.list(inVo);
    }

    @Override
    public Long add(CouponSku couponSku) {
        int ret = couponSkuMapper.insert(couponSku);
        if(ret < 1){
            return null;
        }
        return couponSku.getId();
    }

    @Override
    public CouponSku selectBySkuId(CouponSku sku){
        return couponSkuMapper.selectBySkuId(sku);
    }
}
