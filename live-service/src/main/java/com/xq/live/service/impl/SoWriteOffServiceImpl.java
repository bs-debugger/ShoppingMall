package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.common.RedisCache;
import com.xq.live.config.ActSkuConfig;
import com.xq.live.config.AgioSkuConfig;
import com.xq.live.dao.*;
import com.xq.live.model.*;
import com.xq.live.service.AccountService;
import com.xq.live.service.SoWriteOffService;
import com.xq.live.vo.in.CouponInVo;
import com.xq.live.vo.in.SoInVo;
import com.xq.live.vo.in.SoWriteOffInVo;
import com.xq.live.vo.in.UserAccountInVo;
import com.xq.live.vo.out.SoOut;
import com.xq.live.vo.out.SoWriteOffOut;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-21 18:37
 * @copyright:hbxq
 **/
@Service
public class SoWriteOffServiceImpl implements SoWriteOffService {

    private Logger logger = Logger.getLogger(SoWriteOffServiceImpl.class);

    @Autowired
    private SoWriteOffMapper soWriteOffMapper;

    @Autowired
    private SoMapper soMapper;

    @Autowired
    private SoLogMapper soLogMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private ActSkuConfig actSkuConfig;

    @Autowired
    private AgioSkuConfig agioSkuConfig;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ShopCashierMapper shopCashierMapper;

    @Autowired
    private AccountService accountService;

    @Override
    public Pager<SoWriteOffOut> list(SoWriteOffInVo inVo) {
        Pager<SoWriteOffOut> ret = new Pager<SoWriteOffOut>();
        int total = soWriteOffMapper.listTotal(inVo);
        List<SoWriteOffOut> totalOut = soWriteOffMapper.total(inVo);
        if(total > 0){
            List<SoWriteOffOut> list = soWriteOffMapper.list(inVo);
            for (SoWriteOffOut soWriteOffOut : list) {
                Sku sku = skuMapper.selectByPrimaryKey(soWriteOffOut.getSkuId());
                //折扣券和食典券和砍价券和抢购券
                if((sku.getSkuType()==Sku.SKU_TYPE_XQQ&&sku.getId().equals(agioSkuConfig.getSkuId()))||
                        (sku.getSkuType()==Sku.SKU_TYPE_HDQ)||(sku.getSkuType()==Sku.SKU_TYPE_KJQ)||
                        (sku.getSkuType()==Sku.SKU_TYPE_QGQ)||(sku.getSkuType()==Sku.SKU_TYPE_DHQ)){
                    soWriteOffOut.setServicePrice(BigDecimal.ZERO);
                }else{
                    //普通的平台券
                    soWriteOffOut.setServicePrice(soWriteOffOut.getCouponAmount().divide(new BigDecimal(10), 2, RoundingMode.HALF_UP));
                }
            }
            list.addAll(0,totalOut);//把总销售额和总服务费放到list的第一个数据里面
            ret.setList(list);
        }
        ret.setTotal(total);
        ret.setPage(inVo.getPage());
        return ret;
    }

    //根据shopId查询指定时间内的总金额的应缴金额
    @Override
    public List<SoWriteOffOut> listAmount(SoWriteOffInVo inVo) {
        List<SoWriteOffOut> list=soWriteOffMapper.total(inVo);
        if (list==null||list.size()<=0){
            return null;
        }
        return list;
    }

    @Override
    public List<SoWriteOffOut> top(SoWriteOffInVo inVo) {
        return soWriteOffMapper.list(inVo);
    }

    @Override
    public List<SoWriteOffOut> sotype(SoWriteOffInVo inVo) {
        return soWriteOffMapper.sotype(inVo);
    }

    @Override
    @Transactional
    public Long add(SoWriteOff soWriteOff) {
        //1、新增核销记录
        Integer ret = soWriteOffMapper.insert(soWriteOff);
        if(ret < 1){
            return null;
        }
        Long id = soWriteOff.getId();

        //2、更新订单表状态为已核销，记录订单状态变更日志
        SoInVo soInVo = new SoInVo();
        soInVo.setId(soWriteOff.getSoId());
        soInVo.setSoStatus(So.SO_STATUS_APPLIED);       //已核销
        soInVo.setUserId(soWriteOff.getUserId());
        soInVo.setUserName(soWriteOff.getUserName());
        int soRet = soMapper.hexiao(soInVo);
        if(soRet > 0){
            try {
                //2.1 订单日志
                SoLog soLog = new SoLog();
                soLog.setSoId(soInVo.getId());
                soLog.setUserId(soInVo.getUserId());
                soLog.setUserName(soInVo.getUserName());
                soLog.setOperateType(SoLog.SO_STATUS_APPLIED);
                soLogMapper.insert(soLog);
            }catch (Exception e){
                logger.error("核销保存订单日志失败,soId : "+ soInVo.getId());
            }
        }

        //3、更新抵用券状态为已使用
        Coupon coupon = new Coupon();
        coupon.setId(soWriteOff.getCouponId());
        coupon.setIsUsed(Coupon.COUPON_IS_USED_YES);
        coupon.setUsedTime(new Date());
        coupon.setUserId(soWriteOff.getUserId());
        coupon.setShopId(soWriteOff.getShopId());   //使用券的商家
        coupon.setShopCashierId(soWriteOff.getCashierId()); //核销人
        couponMapper.useCoupon(coupon);

        /*
        当核销的是活动券的时候，根据配置的活动id，来对当前活动的券的使用人投票次数加5，
        当查缓存不存在的时候，则证明用户没有投过票，核销完之后，用户的可用投票次数为6
         */
        Sku sku = skuMapper.selectByPrimaryKey(soWriteOff.getSkuId());
        if(sku.getSkuType()==Sku.SKU_TYPE_HDQ){
            String keyUser = "actVoteNumsUser_" + actSkuConfig.getActId() + "_" +soWriteOff.getUserId();
            Integer integer = redisCache.get(keyUser, Integer.class);
            if(integer==null){
                redisCache.set(keyUser,6,1l, TimeUnit.DAYS);
            }else{
                redisCache.set(keyUser,integer+5,1l,TimeUnit.DAYS);
            }
        }

        //4.在核销的时候判断一下是否核销的是砍价券，如果是则在对应商家的钱包里面加入余额和扣除服务费
        ShopCashier shopCashier = shopCashierMapper.adminByShopId(soWriteOff.getShopId());
        if(sku.getSkuType()==Sku.SKU_TYPE_KJQ && shopCashier!=null && shopCashier.getCashierId()!=null) {
            //5. 账户日志
            UserAccountInVo accountInVo = new UserAccountInVo();
            accountInVo.setUserId(shopCashier.getCashierId());
            accountInVo.setOccurAmount(soWriteOff.getCouponAmount());
            accountService.income(accountInVo, "用户买单，订单号：" + soWriteOff.getSoId());
            //6. 收取服务费，记录日志
            UserAccountInVo accountInVoForFw = new UserAccountInVo();
            accountInVoForFw.setUserId(shopCashier.getCashierId());
            accountInVoForFw.setOccurAmount(new BigDecimal(1.5));
            accountService.payout(accountInVoForFw, "砍价券订单平台收取服务费:1.5元");
            SoWriteOff writeOff = new SoWriteOff();
            writeOff.setId(id);
            writeOff.setIsBill(SoWriteOff.SO_WRITE_OFF_IS_BILL);
            soWriteOffMapper.updateByPrimaryKeySelective(writeOff);
        }

        if(sku.getSkuType()==Sku.SKU_TYPE_HDQ && shopCashier!=null && shopCashier.getCashierId()!=null) {
            //5. 账户日志
            UserAccountInVo accountInVo = new UserAccountInVo();
            accountInVo.setUserId(shopCashier.getCashierId());
            accountInVo.setOccurAmount(soWriteOff.getCouponAmount());
            accountService.income(accountInVo, "用户买单，订单号：" + soWriteOff.getSoId());
            //6. 收取服务费，记录日志
            UserAccountInVo accountInVoForFw = new UserAccountInVo();
            accountInVoForFw.setUserId(shopCashier.getCashierId());
            accountInVoForFw.setOccurAmount(BigDecimal.ONE);
            accountService.payout(accountInVoForFw, "食典券订单平台收取服务费:1元");
            SoWriteOff writeOff = new SoWriteOff();
            writeOff.setId(id);
            writeOff.setIsBill(SoWriteOff.SO_WRITE_OFF_IS_BILL);
            soWriteOffMapper.updateByPrimaryKeySelective(writeOff);
        }

        /* 当核销的是7.7元的活动券的时候，给当前券的使用人的可用投票次数加2，
        * 当查缓存不存在的时候，则证明用户没有投过票，核销完之后，用户的可用投票次数为3
        * */
        /*if(soWriteOff.getSkuId().equals(actSkuConfig.getSkuIdOther())){
            String key = "actVoteNums_" + soWriteOff.getUserId();
            Integer integer = redisCache.get(key, Integer.class);
            if(integer==null){
                redisCache.set(key,3,1l, TimeUnit.DAYS);
            }else{
                redisCache.set(key,integer+2,1l,TimeUnit.DAYS);
            }
        }*/
        return id;
    }

    @Override
    public SoWriteOff get(Long id) {
        return soWriteOffMapper.selectByPrimaryKey(id);
    }

    @Override
    public int validInput(SoWriteOff soWriteOff) {
        Coupon coupon = couponMapper.selectByPrimaryKey(soWriteOff.getCouponId());
        if(coupon == null){
            return 1;  //券不存在
        }

        if(coupon.getUserId().compareTo(soWriteOff.getUserId()) != 0){
            return 1;  //券不存在
        }

        if(coupon.getIsUsed() == Coupon.COUPON_IS_USED_YES){
            return 2; //券已使用
        }
        return 0;
    }

    @Override
    public Integer canGetAgio(SoInVo inVo) {
        Integer integer = soWriteOffMapper.canGetAgio(inVo);
        return integer;
    }

    @Override
    public int listTotal(SoWriteOffInVo inVo) {
        return soWriteOffMapper.listTotal(inVo);
    }

    @Override
    @Transactional
    public int updateByShopId(SoWriteOffInVo soWriteOffInVo) {
        int i = soWriteOffMapper.updateByShopId(soWriteOffInVo);
        return i;
    }
}
