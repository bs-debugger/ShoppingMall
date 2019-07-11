package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.common.RandomStringUtil;
import com.xq.live.dao.*;
import com.xq.live.model.CouponSku;
import com.xq.live.model.Sku;
import com.xq.live.model.SkuAllocation;
import com.xq.live.service.SkuService;
import com.xq.live.vo.in.ActSkuInVo;
import com.xq.live.vo.in.SkuInVo;
import com.xq.live.vo.out.SkuForTscOut;
import com.xq.live.vo.out.SkuOut;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-09 10:34
 * @copyright:hbxq
 **/
@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private SoMapper soMapper;

    @Autowired
    private CouponSkuMapper couponSkuMapper;

    @Autowired
    private SkuAllocationMapper skuAllocationMapper;

    @Autowired
    private ActSkuMapper actSkuMapper;

    private Logger logger = Logger.getLogger(SoServiceImpl.class);

    @Override
    public Sku get(Long id) {
        return skuMapper.selectByPrimaryKey(id);
    }

    @Override
    public Pager<SkuOut> list(SkuInVo inVo) {
        Pager<SkuOut> result =  new Pager<SkuOut>();
        int total = skuMapper.listTotal(inVo);
        if(total > 0){
            List<SkuOut> list = skuMapper.list(inVo);
            /*if(inVo!=null&&inVo.getUserId()!=null){
                int i = soMapper.selectByUserIdTotal(inVo.getUserId());//判断是否是新下单用户 0为首次下单
                if(i==0){
                    for (SkuOut skuOut : list) {
                        skuOut.setSellPrice(BigDecimal.ZERO);
                    }
                }
            }*/
            result.setList(list);
        }
        result.setTotal(total);
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public List<SkuOut> listHome(SkuInVo inVo) {
            List<SkuOut> list = skuMapper.list(inVo);
            return list;
    }

    @Override
    public Pager<SkuForTscOut> queryTscList(SkuInVo inVo){
        Pager<SkuForTscOut> result =  new Pager<SkuForTscOut>();
        int total = skuMapper.tscListTotal(inVo);
        if(total > 0){
            List<SkuForTscOut> list = skuMapper.queryTscList(inVo);
            result.setList(list);
        }
        result.setTotal(total);
        result.setPage(inVo.getPage());
        return result;
    }

    /**
     * 分页查询砍价菜  首页用
     * @param inVo
     * @return
     */
    @Override
    public List<SkuForTscOut> queryKjcListHome(SkuInVo inVo) {
        List<SkuForTscOut> list = skuMapper.queryTscList(inVo);
        return list;
    }

    @Override
    public Pager<SkuForTscOut> queryKjcList(SkuInVo inVo){
        Pager<SkuForTscOut> result =  new Pager<SkuForTscOut>();
        int total = skuMapper.kjcListTotal(inVo);
        if(total > 0){
            List<SkuForTscOut> list = skuMapper.queryKjcList(inVo);
            result.setList(list);
        }
        result.setTotal(total);
        result.setPage(inVo.getPage());
        return result;
    }

    /**
     * 分页查询抢购菜
     * @param inVo
     * @return
     */
    @Override
    public Pager<SkuForTscOut> queryQgcList(SkuInVo inVo) {
        Pager<SkuForTscOut> result =  new Pager<SkuForTscOut>();
        int total = skuMapper.qgcListTotal(inVo);
        if(total > 0){
            List<SkuForTscOut> list = skuMapper.queryQgcList(inVo);

            result.setList(list);
        }
        result.setTotal(total);
        result.setPage(inVo.getPage());
        return result;
    }

    /**
     * 分页查询抢购菜  首页用
     * @param inVo
     * @return
     */
    @Override
    public List<SkuForTscOut> queryQgcListHome(SkuInVo inVo) {
        List<SkuForTscOut> list = skuMapper.queryQgcList(inVo);
        return list;
    }


    //查询小程序里面的抢购菜列表，之所以要单独与商家端区分开来，是为了把已经下架的，并且期限超过7天的剔除
    @Override
    public Pager<SkuForTscOut> queryQgcNewList(SkuInVo inVo) {
        Pager<SkuForTscOut> result =  new Pager<SkuForTscOut>();
        int total = skuMapper.qgcListTotal(inVo);
        if(total > 0){
            long time1 = new Date().getTime();
            List<SkuForTscOut> list = skuMapper.queryQgcList(inVo);
            //把抢购券订单剔除,判断抢购菜已下单并且最后的更新时间加7天之后都小于当前系统时间
            Iterator<SkuForTscOut> sListIterator = list.iterator();
            while (sListIterator.hasNext()) {
                SkuForTscOut str = sListIterator.next();
                long time = str.getUpdateTime().getTime() + 7 * 24 * 60 * 60 * 1000;
                if ((str.getIsDeleted()==Sku.SKU_IS_DELETED)&&(time<time1)) {
                    sListIterator.remove();
                }
            }
            result.setList(list);
        }
        result.setTotal(total);
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public Pager<SkuForTscOut> queryDhcList(SkuInVo inVo) {
        Pager<SkuForTscOut> result =  new Pager<SkuForTscOut>();
        int total = skuMapper.dhcListTotal(inVo);
        if(total > 0){
            List<SkuForTscOut> list = skuMapper.queryDhcList(inVo);

            result.setList(list);
        }
        result.setTotal(total);
        result.setPage(inVo.getPage());
        return result;
    }


    @Override
    public SkuForTscOut getTscForZan(SkuInVo inVo) {
        SkuForTscOut tscForZan = skuMapper.getTscForZan(inVo);
        return tscForZan;
    }

    @Override
    public SkuForTscOut getKjcForZan(SkuInVo inVo) {
        SkuForTscOut tscForZan = skuMapper.getKjcForZan(inVo);
        return tscForZan;
    }

    @Override
    public SkuForTscOut getQgcForZan(SkuInVo inVo) {
        SkuForTscOut tscForZan = skuMapper.getQgcForZan(inVo);
        return tscForZan;
    }

    @Override
    public SkuForTscOut getDhcForZan(SkuInVo inVo) {
        SkuForTscOut tscForZan = skuMapper.getDhcForZan(inVo);
        return tscForZan;
    }


    @Override
    public List<SkuOut> top(SkuInVo inVo) {
        return skuMapper.list(inVo);
    }

    @Override
    public Long add(Sku sku) {
        sku.setSkuCode(RandomStringUtil.getRandomCode(8, 0));
        int res = skuMapper.insert(sku);

        //将推荐菜与shop_id关联起来
        SkuInVo vo = new SkuInVo();
        vo.setShopId(sku.getShopId());
        vo.setId(sku.getId());
        int i = skuMapper.insertSkuShop(vo);

        if(res < 1 || i < 1){
            return null;
        }
        return sku.getId();
    }

    @Override
    @Transactional
    public Integer   addSkuAllocation(SkuAllocation skuAllocation) {
        List<SkuAllocation> list = skuAllocationMapper.list(skuAllocation);

        for (SkuAllocation allocation : list) {
            Sku sku = new Sku();
            BeanUtils.copyProperties(allocation, sku);
            sku.setSkuCode(RandomStringUtil.getRandomCode(8, 0));
            sku.setSkuType(Sku.SKU_TYPE_DHC);
            sku.setOpreatorId(skuAllocation.getOpreatorId());
            sku.setType(Integer.valueOf(skuAllocation.getSkuType()));
            sku.setOpreatorName(skuAllocation.getOpreatorName());
            int res = skuMapper.insert(sku);

            //将推荐菜与shop_id关联起来
            SkuInVo vo = new SkuInVo();
            vo.setShopId(skuAllocation.getShopId());
            vo.setId(sku.getId());
            int i = skuMapper.insertSkuShop(vo);

            //和活动act_id关联
            ActSkuInVo asvo=new ActSkuInVo();
            asvo.setActId(skuAllocation.getActId());
            asvo.setSkuId(sku.getId());
            asvo.setShopId(skuAllocation.getShopId());
            int j = actSkuMapper.insert(asvo);

            if(res < 1 || i < 1||j<1){
                return null;
            }
        }
        return list.size();
    }

    @Override
    public Integer isAdd(SkuAllocation skuAllocation) {
        SkuInVo vo = new SkuInVo();
        vo.setType(Integer.valueOf(skuAllocation.getSkuType()));
        vo.setSkuType(Sku.SKU_TYPE_DHC);
        vo.setShopId(skuAllocation.getShopId());
        int listTotal = skuMapper.tscListTotal(vo);
        return listTotal;
    }

    @Override
    public Integer deleteSkuAllocation(SkuAllocation skuAllocation) {
        SkuInVo vo = new SkuInVo();
        vo.setType(Integer.valueOf(skuAllocation.getSkuType()));
        vo.setSkuType(Sku.SKU_TYPE_DHC);
        vo.setShopId(skuAllocation.getShopId());
        vo.setActId(skuAllocation.getActId());//删除时增加根据活动id过滤条件
        int listTotal = skuMapper.updateToDeleteSkuAllocation(vo);
        return listTotal;
    }

    @Override
    @Transactional
    public Long addSku(Sku sku) {
        sku.setSkuCode(RandomStringUtil.getRandomCode(8, 0));
        sku.setIsDeleted(Sku.SKU_NO_DELETED);
        int res = skuMapper.insert(sku);
        if(res<1){
            return null;
        }
        Sku sku1 = skuMapper.selectByPrimaryKey(sku.getId());
        CouponSku couponSku = new CouponSku();
        couponSku.setIsDeleted(CouponSku.COUPON_SKU_NO_DELETED);
        couponSku.setSkuId(sku.getId());
        couponSku.setAmount(sku.getSellPrice());
        //couponSku.setCreateTime(new Date());
        couponSku.setSkuCode(sku1.getSkuCode());
        couponSku.setSkuName(sku1.getSkuName());
        couponSku.setType(sku.getCouponSkuType());
        //couponSku.setUpdateTime(new Date());
        int insert = couponSkuMapper.insert(couponSku);
        //如果插入失败，直接回滚
        if(insert<1){
            logger.error("票券id："+ sku.getId() + " 插入信息失败");
            throw new RuntimeException("票券id："+ sku.getId() + " 插入信息失败");
        }
        return sku.getId();
    }

    @Override
    @Transactional
    public Integer updateSku(Sku skuForAct) {
        skuForAct.setIsDeleted(Sku.SKU_NO_DELETED);
        int i = skuMapper.updateByPrimaryKeySelective(skuForAct);

        CouponSku couponSku = new CouponSku();
        couponSku.setSkuId(skuForAct.getId());
        couponSku.setIsDeleted(CouponSku.COUPON_SKU_IS_DELETED);
        CouponSku couponSkuNew = couponSkuMapper.selectBySkuId(couponSku);
        couponSkuNew.setIsDeleted(CouponSku.COUPON_SKU_NO_DELETED);
        int j = couponSkuMapper.updateByPrimaryKeySelective(couponSkuNew);
        if(i<1||j<1){
            logger.error("票券id："+ skuForAct.getId() + " 更新信息失败");
            throw new RuntimeException("票券id："+ skuForAct.getId() + " 更新信息失败");
        }
        return 1;
    }

    @Override
    public Sku selectForActSku(Sku inVo) {
        Sku sku = skuMapper.selectForActSku(inVo);
        return sku;
    }

    @Override
    public SkuOut selectById(Long id) {
        return skuMapper.selectById(id);
    }

    @Override
    public Integer isNewUser(Long userId) {
        int i = soMapper.selectByUserIdTotal(userId);
        return i;
    }

    @Override
    public Integer delete(Long id) {
        Sku sku = new Sku();
        sku.setId(id);
        sku.setIsDeleted(Sku.SKU_IS_DELETED);
        int i = skuMapper.updateByPrimaryKeySelective(sku);
        return i;
    }

    @Override
    public Integer update(Sku sku) {
        int i = skuMapper.updateByPrimaryKeySelective(sku);
        return i;
    }

}
