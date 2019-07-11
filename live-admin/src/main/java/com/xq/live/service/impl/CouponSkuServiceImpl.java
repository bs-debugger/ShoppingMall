package com.xq.live.service.impl;

import com.xq.live.dao.CouponSkuDao;
import com.xq.live.dao.SkuDao;
import com.xq.live.model.CouponSku;
import com.xq.live.model.Sku;
import com.xq.live.service.CouponSkuService;
import com.xq.live.vo.BaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-12 15:04
 * @copyright:hbxq
 **/
@Service
public class CouponSkuServiceImpl implements CouponSkuService {

    @Autowired
    private CouponSkuDao couponSkuDao;

    @Autowired
    private SkuDao skuDao;

    public List<CouponSku> queryWithPg(BaseVo inVo) throws Exception {
        Integer rowCount = queryWithCount(inVo);
        inVo.getPager().setRowCount(rowCount);
        return couponSkuDao.queryWithPg(inVo);
    }

    @Override
    public int queryWithCount(BaseVo inVo) throws Exception {
        return couponSkuDao.queryWithCount(inVo);
    }

    @Override
    public CouponSku queryById(Long id) throws Exception {
        return couponSkuDao.selectByPrimaryKey(id);
    }

    @Override
    public int add(CouponSku couponSku) throws Exception {
        Sku sku = skuDao.selectByCode(couponSku.getSkuCode());
        couponSku.setSkuId(sku.getId());
        couponSku.setSkuName(sku.getSkuName());
        couponSku.setCreateTime(new Date());
        return couponSkuDao.insert(couponSku);
    }
    @Override
    public int update(CouponSku couponSku) throws Exception {
        couponSku.setUpdateTime(new Date());
        return couponSkuDao.updateByPrimaryKeySelective(couponSku);
    }
    @Override
    public int delete(Long[] ids) throws Exception {
        int result = 0;
        for (Long id : ids) {
            // 删除用户
            couponSkuDao.deleteByPrimaryKey(id);
            result++;
        }
        return result;
    }
}
