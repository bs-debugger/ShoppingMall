package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.config.ActSkuConfig;
import com.xq.live.config.AgioSkuConfig;
import com.xq.live.dao.ActShopMapper;
import com.xq.live.dao.CouponMapper;
import com.xq.live.dao.PromotionRulesMapper;
import com.xq.live.dao.SkuMapper;
import com.xq.live.model.Coupon;
import com.xq.live.model.Sku;
import com.xq.live.service.CouponService;
import com.xq.live.vo.in.CouponInVo;
import com.xq.live.vo.in.SkuInVo;
import com.xq.live.vo.out.CouponOut;
import com.xq.live.vo.out.PromotionRulesOut;
import com.xq.live.vo.out.SkuForTscOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-08 21:36
 * @copyright:hbxq
 **/
@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private ActShopMapper actShopMapper;

    @Autowired
    private PromotionRulesMapper promotionRulesMapper;

    @Autowired
    private AgioSkuConfig agioSkuConfig;

    @Autowired
    private ActSkuConfig actSkuConfig;

    @Override
    public Coupon get(Long id) {
        return couponMapper.selectByPrimaryKey(id);
    }

    @Override
    public Pager<CouponOut> list(CouponInVo inVo) {
        Pager<CouponOut> result = new Pager<CouponOut>();
        int total = couponMapper.listTotal(inVo);
        if(total > 0){
            List<CouponOut> list = couponMapper.list(inVo);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        result.setTotal(total);
        return result;
    }

    @Override
    public List<CouponOut> top(CouponInVo inVo) {
        return couponMapper.list(inVo);
    }

    @Override
    public Long add(Coupon coupon) {
        int res = couponMapper.insert(coupon);
        if(res < 1){
            return null;
        }
        return coupon.getId();
    }

    @Override
    public CouponOut getByCouponCode(String couponCode) {
        return couponMapper.getByCouponCode(couponCode);
    }

    @Override
    public CouponOut selectById(Long id){
        return couponMapper.selectById(id);
    }

    @Override
    public Integer useLimit(Long userId) {
        return couponMapper.useLimit(userId);
    }

    @Override
    public Pager<CouponOut> listShopUser(CouponInVo inVo) {

        Pager<CouponOut> result = new Pager<CouponOut>();
        int total = couponMapper.listTotal(inVo);
        List<CouponOut> list = new ArrayList<CouponOut>();
        if(total > 0){
            list = couponMapper.list(inVo);
            SkuInVo skuInVo = new SkuInVo();
            skuInVo.setSkuType(Sku.SKU_TYPE_SJTC);

            //此接口完全无需折扣券
            /*int agioTotal = skuMapper.tscListTotal(skuInVo);
            if(agioTotal==0){
                //没有套餐,那就不能使用套餐券
                Iterator<CouponOut> sListIterator = list.iterator();
                while (sListIterator.hasNext()) {
                    CouponOut str = sListIterator.next();
                    if (str.getSkuId()==agioSkuConfig.getSkuId()) {
                        sListIterator.remove();
                    }
                }
            }*/
            Integer integer = actShopMapper.searchForShopId(inVo.getShopId());
            List<PromotionRulesOut> promotionRulesOuts = promotionRulesMapper.selectByShopId(inVo.getShopId());
            List<Long> listLong = new ArrayList<Long>();
            for (PromotionRulesOut promotionRulesOut : promotionRulesOuts) {
                //判断金额是否比满的金额大，大或者等于的话就加入进去
                int i = inVo.getFinalAmount().compareTo(promotionRulesOut.getManAmount());
                if(i == 0 || i ==1) {
                    listLong.add(promotionRulesOut.getSkuId());
                }
            }

            //删除已经过期的代金券和删除折扣券和删除所有活动券
            Iterator<CouponOut> sListIteratorAll = list.iterator();
            while (sListIteratorAll.hasNext()) {
                CouponOut str = sListIteratorAll.next();
                int i = str.getExpiryDate().compareTo(new Date());
                if (i<0) {
                    sListIteratorAll.remove();
                }
                /*if(str.getSkuId().equals(agioSkuConfig.getSkuId())){
                    sListIteratorAll.remove();
                }
                if(str.getSkuId().equals(actSkuConfig.getSkuIdOther())){
                    sListIteratorAll.remove();
                }*/
                //删除所有活动券，砍价券，抢购券，兑换券s
                if(str.getType()==Coupon.OUNPON_TYPE_ACT||str.getType()==Coupon.OUNPON_TYPE_KJQ
                        ||str.getType()==Coupon.OUNPON_TYPE_QGQ
                        ||str.getType()==Coupon.OUNPON_TYPE_DHQ){
                    sListIteratorAll.remove();
                }
            }


            //用户没有参加活动，无法使用活动券
            if(integer<1){
                Iterator<CouponOut> sListIterator = list.iterator();
                while (sListIterator.hasNext()) {
                    CouponOut str = sListIterator.next();
                    if (str.getSkuId().equals(actSkuConfig.getSkuId())) {
                        sListIterator.remove();
                    }
                }
            }

            //删除不满足商家优惠力度的代金券
            if(promotionRulesOuts!=null&&promotionRulesOuts.size()>0){
                Iterator<CouponOut> sListIterator = list.iterator();
                while (sListIterator.hasNext()) {
                    CouponOut str = sListIterator.next();
                    if (!listLong.contains(str.getSkuId())) {
                        sListIterator.remove();
                    }
                }
            }
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        result.setTotal(list.size());
        return result;
    }
}
