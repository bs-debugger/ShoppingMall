package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.CouponSku;
import com.xq.live.service.CouponSkuService;
import com.xq.live.vo.in.CouponSkuInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-09 13:20
 * @copyright:hbxq
 **/
@RestController
@RequestMapping(value = "/cs")
public class CouponSkuController {

    @Autowired
    private CouponSkuService couponSkuService;

    /**
     * 新增券基础信息
     * @param couponSku
     * @param result
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid CouponSku couponSku, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        CouponSku sku = new CouponSku();
        sku.setIsDeleted(CouponSku.COUPON_SKU_NO_DELETED);
        sku.setSkuId(couponSku.getSkuId());
        CouponSku cs = couponSkuService.selectBySkuId(sku);
        if(cs != null){
            return new BaseResp<Long>(ResultStatus.error_coupon_sku_exist);
        }
        Long id = couponSkuService.add(couponSku);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResp<List<CouponSku>> add(CouponSkuInVo inVo){
        List<CouponSku> ret = couponSkuService.list(inVo);
        return new BaseResp<List<CouponSku>>(ResultStatus.SUCCESS, ret);
    }
}
