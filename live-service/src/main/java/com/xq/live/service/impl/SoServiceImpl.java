package com.xq.live.service.impl;

import com.xq.live.common.*;
import com.xq.live.config.ActSkuConfig;
import com.xq.live.config.ConstantsConfig;
import com.xq.live.dao.*;
import com.xq.live.model.*;
import com.xq.live.poientity.SoDetailEntity;
import com.xq.live.service.AccountService;
import com.xq.live.service.GoldLogService;
import com.xq.live.service.SoService;
import com.xq.live.service.UploadService;
import com.xq.live.vo.in.*;
import com.xq.live.vo.out.ShopAllocationOut;
import com.xq.live.vo.out.SoForOrderOut;
import com.xq.live.vo.out.SoOut;
import com.xq.live.vo.out.SoWriteOffOut;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-09 14:29
 * @copyright:hbxq
 **/
@Service
@Transactional
public class SoServiceImpl implements SoService {

    private Logger logger = Logger.getLogger(SoServiceImpl.class);

    @Autowired
    private SoMapper soMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private CouponSkuMapper couponSkuMapper;

    @Autowired
    private SoDetailMapper soDetailMapper;

    @Autowired
    private SoLogMapper soLogMapper;

    @Autowired
    private SoShopLogMapper soShopLogMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private PaidLogMapper paidLogMapper;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private PromotionRulesMapper promotionRulesMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ShopAllocationMapper shopAllocationMapper;

    @Autowired
    private SoWriteOffMapper soWriteOffMapper;

    @Autowired
    private ShopCashierMapper shopCashierMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ConstantsConfig constantsConfig;

    @Autowired
    private ActSkuConfig actSkuConfig;

    @Autowired
    private GoldLogService goldLogService;


    @Override
    public Pager<SoOut> list(SoInVo inVo) {
        Pager<SoOut> ret = new Pager<SoOut>();
        int total = soMapper.listTotal(inVo);
        if (total > 0) {
            List<SoOut> list = soMapper.list(inVo);
            ret.setList(list);
        }
        ret.setTotal(total);
        ret.setPage(inVo.getPage());
        return ret;
    }

    @Override
    public BigDecimal totalAmount(SoInVo inVo){
        SoShopLog aa = soShopLogMapper.totalAmount(inVo);//只查询平台代收的商家订单
        SoOut bb = soMapper.totalAmount(inVo);//只查询已核销的食典券和已核销的砍价券
        BigDecimal a = BigDecimal.ZERO;
        BigDecimal b = BigDecimal.ZERO;
        if(aa!=null){
            a =  aa.getSoAmount().subtract(aa.getServicePrice()).setScale(2, BigDecimal.ROUND_UP);
        }
        if(bb!=null){
            b =  bb.getSoAmount().subtract(bb.getServicePrice()).setScale(2, BigDecimal.ROUND_UP);
        }
        BigDecimal total = a.add(b).setScale(2, BigDecimal.ROUND_UP);
        return total;
    }

    @Override
    public List<SoOut> findSoList(SoInVo inVo) {
        List<SoOut> list = soMapper.list(inVo);
        if (list==null||list.size()<1){
            return null;
        }
        for (SoOut soOut : list) {
            soOut.setSkuType(skuMapper.selectByPrimaryKey(soOut.getSkuId()).getSkuType());
            if(soOut.getSkuType()==Sku.SKU_TYPE_HDQ) {//食典券收取服务费1元
                soOut.setRealAmount(soOut.getUnitPrice().subtract(BigDecimal.ONE));
            }else if(soOut.getSkuType()==Sku.SKU_TYPE_KJQ){//砍价券收取服务费1.5元
                soOut.setRealAmount(soOut.getUnitPrice().subtract(new BigDecimal(1.5)));
            }else if(soOut.getSkuType()==Sku.SKU_TYPE_QGQ){//抢购券收取服务费0元
                soOut.setRealAmount(soOut.getUnitPrice().subtract(BigDecimal.ZERO));
            }else if(soOut.getSkuType()==Sku.SKU_TYPE_DHQ){//兑换券收取服务费0元
                soOut.setRealAmount(soOut.getUnitPrice().subtract(BigDecimal.ZERO));
            }
            soOut.setRuleDesc(" ");
        }

        Iterator<SoOut> sListIterator = list.iterator();
        while (sListIterator.hasNext()) {
            SoOut str = sListIterator.next();
            if ((str.getSkuType()==Sku.SKU_TYPE_HDQ||str.getSkuType()==Sku.SKU_TYPE_KJQ||str.getSkuType()==Sku.SKU_TYPE_QGQ
                    ||str.getSkuType()==Sku.SKU_TYPE_DHQ)
                    &&str.getSoStatus()==So.SO_STATUS_WAIT_PAID) {
                sListIterator.remove();
            }
        }
        return list;
    }

    @Override
    public Pager<SoOut> findSoListForShop(SoInVo inVo) {
        Pager<SoOut> ret = new Pager<SoOut>();
        int total = soMapper.listForShopTotal(inVo);
        if(total>0){
            List<SoOut> list = soMapper.listForShop(inVo);
            for (SoOut soOut : list) {
            /*SoShopLog soShopLog = new SoShopLog();

                 soShopLog.setOperateType(soOut.getSoStatus());
                 soShopLog.setSoId(soOut.getId());
                 SoShopLog in = soShopLogMapper.selectBySoId(soShopLog);*/
                User user = userMapper.selectByPrimaryKey(soOut.getUserId());

                if(user!=null) {
                    soOut.setMobile(user.getMobile());
                    soOut.setUserIconUrl(user.getIconUrl());
                }
                Shop shop = shopMapper.selectByPrimaryKey(soOut.getShopId());
                Sku sku = skuMapper.selectByPrimaryKey(soOut.getSkuId());
                //soOut.setShopId(in.getShopId());
                if(shop!=null) {
                    soOut.setShopName(shop.getShopName());
                    soOut.setLogoUrl(shop.getLogoUrl());
                }
                if(sku!=null) {
                    soOut.setInPrice(sku.getInPrice());
                    soOut.setSkuName(sku.getSkuName());
                    soOut.setRealAmount(soOut.getSoAmount().subtract(sku.getSellPrice()));
                }else{
                    soOut.setRealAmount(soOut.getSoAmount());
                }
            }
            ret.setList(list);
        }
        ret.setTotal(total);
        ret.setPage(inVo.getPage());
        return ret;
    }

    @Override
    public Map<String,Object> soDetailExport(SoInVo inVo) {
        //查询平台订单(食典券订单)-----只查询已被核销的食典券和已被核销的砍价券
        List<SoOut> list = soMapper.listNoPage(inVo);//里面的soStatus为3
        for (SoOut soOut : list) {
            soOut.setSkuType(skuMapper.selectByPrimaryKey(soOut.getSkuId()).getSkuType());
            if(soOut.getSkuType()==Sku.SKU_TYPE_HDQ) {//食典券收取服务费1元
                soOut.setRealAmount(soOut.getUnitPrice().subtract(BigDecimal.ONE));
                soOut.setAccountAmount(soOut.getUnitPrice().subtract(BigDecimal.ONE));
            }else if(soOut.getSkuType()==Sku.SKU_TYPE_KJQ){//砍价券收取服务费1.5元
                soOut.setRealAmount(soOut.getUnitPrice().subtract(new BigDecimal(1.5)));
                soOut.setAccountAmount(soOut.getUnitPrice().subtract(new BigDecimal(1.5)));
            }
            soOut.setRuleDesc(" ");
            soOut.setServicePrice(BigDecimal.ZERO);
            soOut.setCoupon("无");
        }


        //查询商家订单(平台代收的商家订单)
        List<SoOut> listForShop = soMapper.listForShopNoPage(inVo);
        for (SoOut soOut : listForShop) {

            Sku sku = skuMapper.selectByPrimaryKey(soOut.getSkuId());
            if(sku!=null) {
                soOut.setInPrice(sku.getInPrice());
                soOut.setSkuName(sku.getSkuName());
                soOut.setServicePrice(sku.getSellPrice());
                soOut.setRealAmount(soOut.getSoAmount().subtract(sku.getSellPrice()));
                soOut.setCoupon(sku.getSkuName());
                /*soOut.setAccountAmount(soOut.getSoAmount().add(sku.getInPrice()));*/
                soOut.setAccountAmount(soOut.getSoAmount());
            }else{
                soOut.setServicePrice(BigDecimal.ZERO);
                soOut.setRealAmount(soOut.getSoAmount());
                soOut.setCoupon("无");
                soOut.setAccountAmount(soOut.getSoAmount());
            }
        }

        //将两个结果集合并
        list.addAll(listForShop);
        Collections.sort(list);//根据交易时间进行排序
        Map<String,Object> map = new HashMap<String, Object>();
        List<SoDetailEntity> SoDetailEntityList = new ArrayList<SoDetailEntity>();
        int i=0;
        BigDecimal totalAcAmount = BigDecimal.ZERO;//总应收款
        BigDecimal totalService = BigDecimal.ZERO;//总服务费
        BigDecimal totalReAmount = BigDecimal.ZERO;//实收款
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (SoOut soOut : list) {
            i++;
            SoDetailEntity entity = new SoDetailEntity();
            entity.setIndex(i + "");
            entity.setPaidTime(formatter.format(soOut.getPaidTime()));
            entity.setSoId(soOut.getId());
            if(soOut.getSoType()==1) {
               if (soOut.getSkuType()==1){
                   entity.setType("享七券");
               }else if (soOut.getSkuType()==3){
                   entity.setType("食典券");
               }else if (soOut.getSkuType()==4){
                   entity.setType("砍价券");
               }else if (soOut.getSkuType()==5){
                   entity.setType("商家套餐");
               }
            }else if(soOut.getSoType()==2){
                entity.setType("买单");
            }
            entity.setAccountAmount(soOut.getAccountAmount().toString());
            entity.setCoupon(soOut.getCoupon());
            entity.setServicePrice(soOut.getServicePrice().toString());
            entity.setRealAmount(soOut.getRealAmount().toString());
            SoDetailEntityList.add(entity);
            totalAcAmount = totalAcAmount.add(soOut.getAccountAmount());
            totalService = totalService.add(soOut.getServicePrice());
            totalReAmount = totalReAmount.add(soOut.getRealAmount());
        }
        map.put("list",SoDetailEntityList);
        map.put("totalAcAmount",totalAcAmount);
        map.put("totalService",totalService);
        map.put("totalReAmount",totalReAmount);

        return map;
    }

    @Override
    public Integer alllistTotal(SoInVo inVo) {
        Integer all=null;
        Integer sku=soMapper.skulistTotal(inVo);//食典券,砍价券数量
        Integer so=soMapper.solistTotal(inVo);//商家订单数量
        all=sku+so;
        return all;
    }

    @Override
    @Transactional
    public Long create(SoInVo inVo) {
        //1、查询SKU信息
        Sku sku = skuMapper.selectByPrimaryKey(inVo.getSkuId());
        if(sku == null){
            return null;
        }
        //2、计算订单金额
        BigDecimal soAmount = BigDecimal.valueOf(inVo.getSkuNum()).multiply(sku.getSellPrice());

        //3、保存订单信息
        inVo.setSoAmount(soAmount);
        inVo.setSoStatus(So.SO_STATUS_WAIT_PAID);
        inVo.setSoType(So.SO_TYPE_PT);
        int ret = soMapper.insert(inVo);
        if (ret < 1) {
            logger.error("保存订单失败,userId : "+inVo.getUserId()+" skuId : "+inVo.getSkuId());
            return null;
        }
        Long id = inVo.getId();
        //4、保存订单明细信息
        inVo.setId(id);
        this.saveSoDetail(inVo, sku);

        //5、保存订单日志
        this.saveSoLog(inVo, SoLog.SO_STATUS_WAIT_PAID);

        //6、判断支付方式，如果是享七支付，则直接扣减账户余额，并修改订单状态，如果是微信支付，则只生成待支付的订单
        inVo.setId(id);
        if(inVo.getPayType() == So.SO_PAY_TYPE_XQ){
            UserAccount account = accountService.findAccountByUserId(inVo.getUserId());
            if(account == null){    //账户信息不存在，请检查账户
                logger.error("用户id："+ inVo.getUserId() + " 的账户信息不存在，请检查！");
                throw new RuntimeException("用户id："+ inVo.getUserId() + " 的账户信息不存在，请检查！");
            }
            if(account.getAccountAmount().compareTo(soAmount) == -1){   //账户余额小于订单金额
                logger.error("用户id："+ inVo.getUserId() + " 的账户余额不足，无法使用余额支付");
                throw new RuntimeException("用户id："+ inVo.getUserId() + " 的账户余额不足，无法使用余额支付");
            }
            UserAccountInVo accountInVo = new UserAccountInVo();
            accountInVo.setUserId(inVo.getUserId());
            accountInVo.setOccurAmount(soAmount);
            accountService.payout(accountInVo, "订单支付，订单号："+ id);

            //更新订单支付状态，写入订单日志
            this.paid(inVo);
        }

        return id;
    }

    @Override
    public Long createForShop(SoInVo inVo) {
        //1、查询SKU信息
        if(inVo.getSkuId()!=null){
            Sku sku = skuMapper.selectByPrimaryKey(inVo.getSkuId());
            if(sku == null){
                return null;
            }
        }
        //1、保存订单信息
        inVo.setSoStatus(So.SO_STATUS_WAIT_PAID);
        inVo.setSoType(So.SO_TYPE_SJ);
        int ret = soMapper.insert(inVo);
        if (ret < 1) {
            logger.error("保存订单失败,userId : "+inVo.getUserId());
            return null;
        }
        Long id = inVo.getId();
        //3、保存订单明细信息
        inVo.setId(id);
        //4、保存订单日志
        this.saveSoShopLog(inVo, SoLog.SO_STATUS_WAIT_PAID);



        return id;
    }

    @Override
    public Long freeOrder(SoInVo inVo){
        //1、查询SKU信息
        Sku sku = skuMapper.selectByPrimaryKey(inVo.getSkuId());
        //2、计算订单金额
        BigDecimal soAmount = BigDecimal.ZERO;

        //3、保存订单信息,免费订单的状态直接写为“已支付”
        inVo.setSoAmount(soAmount);
        inVo.setSoStatus(So.SO_STATUS_PAID);
        inVo.setSoType(So.SO_TYPE_PT);
        int ret = soMapper.insert(inVo);
        if (ret < 1) {
            return null;
        }
        Long id = inVo.getId();

        //4、保存订单明细信息
        inVo.setId(id);
        this.saveSoDetail(inVo, sku);

        //5.生成代金券
        this.createCoupon(inVo);

        //6、保存订单日志，订单状态:已支付
        this.saveSoLog(inVo, SoLog.SO_STATUS_PAID);
        return id;
    }

    @Override
    public Long freeOrderForAct(SoInVo inVo) {
        //1、查询SKU信息
        Sku sku = skuMapper.selectByPrimaryKey(inVo.getSkuId());
        //2、计算订单金额
        BigDecimal soAmount = BigDecimal.ZERO;

        //3、保存订单信息,免费订单的状态直接写为“已支付”
        inVo.setSoAmount(soAmount);
        inVo.setSoStatus(So.SO_STATUS_PAID);
        inVo.setSoType(So.SO_TYPE_PT);
        int ret = soMapper.insert(inVo);
        if (ret < 1) {
            return null;
        }
        Long id = inVo.getId();

        //4、保存订单明细信息
        inVo.setId(id);
        this.saveSoDetail(inVo, sku);

        //5.生成活动代金券(只能参与活动的商家才能使用)
        this.createCouponForAct(inVo);

        //6、保存订单日志，订单状态:已支付
        this.saveSoLog(inVo, SoLog.SO_STATUS_PAID);
        return id;
    }

    /*@Override
    public Long freeOrderForAgio(SoInVo inVo) {
        //1、查询SKU信息
        Sku sku = skuMapper.selectByPrimaryKey(inVo.getSkuId());
        //2、计算订单金额
        BigDecimal soAmount = BigDecimal.ZERO;

        //3、保存订单信息,免费订单的状态直接写为“已支付”
        inVo.setSoAmount(soAmount);
        inVo.setSoStatus(So.SO_STATUS_PAID);
        inVo.setSoType(So.SO_TYPE_PT);
        int ret = soMapper.insert(inVo);
        if (ret < 1) {
            return null;
        }
        Long id = inVo.getId();

        //4、保存订单明细信息
        inVo.setId(id);
        this.saveSoDetail(inVo, sku);

        //5.生成套餐折扣券(只能被套餐使用)
        this.createCouponForAgio(inVo);

        //6、保存订单日志，订单状态:已支付
        this.saveSoLog(inVo, SoLog.SO_STATUS_PAID);
        return id;
    }*/

    //判断用户是否领过卷，0没有，1有过
    @Override
    public Integer hadBeenGiven(SoInVo inVo) {
        Integer i=soMapper.hadBeenGiven(inVo);
        if (i>0){
            return 1;
        }
        return 0;
    }
    //根据订单ID获取userID(代码不可用！重写)
/*    @Override
    public Long getUserIDBySoId(SoInVo inVo) {
        Long i=soMapper.getUserIDBySoId(inVo);
        return i;
    }*/


    @Override
    public SoOut get(Long id) {
        return soMapper.selectByPk(id);
    }

    @Override
    public SoOut selectByPkForShop(Long id) {
        return soMapper.selectByPkForShop(id);
    }

    @Override
    public So selectForShop(Long id) {
        return soMapper.selectByPrimaryKey(id);
    }


    @Override
    public SoForOrderOut getForOrder(Long id) {
        SoForOrderOut soForOrderOut = soMapper.selectByPkForOrder(id);
        if(soForOrderOut==null||soForOrderOut.getUserId()==null){
            return null;
        }
        User user = userMapper.selectByPrimaryKey(soForOrderOut.getUserId());
        if(user==null){
            return null;
        }
        soForOrderOut.setMoblie(user.getMobile());
        return soForOrderOut;
    }


    @Override
    public Integer paid(SoInVo inVo) {
        //1、更新订单状态
        inVo.setSoStatus(So.SO_STATUS_PAID);
        int ret = soMapper.paid(inVo);
        if (ret > 0) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, 3);     //有效时间为3个月
            //2、支付成功生成抵用券
            this.createCoupon(inVo);
            //3、支付日志
            this.savePayLog(inVo);
            //4、订单日志
            this.saveSoLog(inVo, SoLog.SO_STATUS_PAID);
        }
        return ret;
    }

    @Override
    @Transactional
    public Integer paidForShop(SoInVo inVo) {
        //1、更新订单状态
        inVo.setSoStatus(So.SO_STATUS_PAID);

        ShopAllocationInVo shopAllocationInVo = new ShopAllocationInVo();
        shopAllocationInVo.setShopId(inVo.getShopId());
        ShopAllocationOut shopAllocationOut = shopAllocationMapper.selectByPrimaryKey(shopAllocationInVo);
        ShopCashier shopCashier = shopCashierMapper.adminByShopId(inVo.getShopId());
        if(shopAllocationOut.getPaymentMethod()==ShopAllocation.SHOP_ALLOCATION_DS){
            //注意完成对账功能之前，一定要保证该券先调用核销
            //平台代收，要收取服务费，并且还要完成对账操作(把核销之后的券的对账改成已对账状态)
            UserAccountInVo accountInVo = new UserAccountInVo();
            accountInVo.setUserId(shopCashier.getCashierId());//商家管理员的用户id
            accountInVo.setOccurAmount(inVo.getSoAmount());
            accountService.income(accountInVo, "用户买单，订单号：" + inVo.getId());

            //当商家订单中，并没有买券，则不做核销和收取服务费
            if(inVo.getSkuId()!=null&&inVo.getCouponId()!=null) {
                /** 完成结算操作 */
                SoWriteOff soWriteOff = soWriteOffMapper.selectByCouponId(inVo.getCouponId());
                soWriteOff.setIsBill(SoWriteOff.SO_WRITE_OFF_IS_BILL);
                soWriteOffMapper.updateByPrimaryKeySelective(soWriteOff);

                Sku sku = skuMapper.selectByPrimaryKey(inVo.getSkuId());//查询被核销的券,进而查询要收取的服务费
                UserAccountInVo accountInVoForFw = new UserAccountInVo();
                accountInVoForFw.setUserId(shopCashier.getCashierId());
                accountInVoForFw.setOccurAmount(sku.getSellPrice());
                accountService.payout(accountInVoForFw, "商家订单平台代收,收取服务费,票券号：" + inVo.getCouponId());
            }
        }else if(shopAllocationOut.getPaymentMethod()==ShopAllocation.SHOP_ALLOCATION_ZS){
            //商家自收，服务费另外算，不完成对账操作,此代码需后期补充
           /* UserAccountInVo accountInVo = new UserAccountInVo();
            accountInVo.setUserId(inVo.getUserId());
            accountInVo.setOccurAmount(inVo.getSoAmount());
            accountService.income(accountInVo, "用户买单，订单号：" + inVo.getId());*/
        }



        int ret = soMapper.paid(inVo);
        if (ret > 0) {
            SoShopLog logInVo = new SoShopLog();
            logInVo.setSoId(inVo.getId());
            logInVo.setOperateType(SoShopLog.OPERATE_TYPE_WAIT_PAID);//通过查询下单的数据，来货单商家订单的商家id
            SoShopLog soShopLog = soShopLogMapper.selectBySoId(logInVo);
            inVo.setSkuId(soShopLog.getSkuId());//为了让paidForShop接口的skuId的值能获取到
            inVo.setShopId(soShopLog.getShopId());//为了能让wxNotifyForShop接口的shopId的值能够获取到
            //2、商家订单日志
            this.saveSoShopLog(inVo, SoLog.SO_STATUS_PAID);
        }
        return ret;
    }

    @Override
    @Transactional
    public int paidForAct(SoInVo inVo) {
        ShopCashier shopCashier = shopCashierMapper.adminByShopId(inVo.getShopId());
        //1、更新订单状态
        inVo.setSoStatus(So.SO_STATUS_PAID);
        int ret = soMapper.paid(inVo);
        if (ret > 0) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, 3);     //有效时间为3个月
            //2、支付成功生成抵用券
            this.createCouponForAct(inVo);
            //3、支付日志
            this.savePayLog(inVo);
            //4、订单日志
            this.saveSoLog(inVo, SoLog.SO_STATUS_PAID);
            //5. 账户日志
           /* UserAccountInVo accountInVo = new UserAccountInVo();
            accountInVo.setUserId(shopCashier.getCashierId());
            accountInVo.setOccurAmount(inVo.getSoAmount());
            accountService.income(accountInVo, "用户买单，订单号：" + inVo.getId());*/
            //6. 收取服务费，记录日志
           /* UserAccountInVo accountInVoForFw = new UserAccountInVo();
            accountInVoForFw.setUserId(shopCashier.getCashierId());
            accountInVoForFw.setOccurAmount(BigDecimal.ONE);
            accountService.payout(accountInVoForFw, "活动订单平台收取服务费:1元");*/
            //用户对选手的可用投票次数加5
            String keyUser = "actVoteNumsUser_" + actSkuConfig.getActId() + "_" +inVo.getUserId();
            Integer i = redisCache.get(keyUser, Integer.class);
            if(i==null){
                redisCache.set(keyUser,6,1l, TimeUnit.DAYS);
            }else{
                redisCache.set(keyUser,i+5,1l,TimeUnit.DAYS);
            }
        }
        return ret;
    }

    @Override
    @Transactional
    public int paidForKc(SoInVo inVo) {
        ShopCashier shopCashier = shopCashierMapper.adminByShopId(inVo.getShopId());
        //1、更新订单状态
        inVo.setSoStatus(So.SO_STATUS_PAID);
        int ret = soMapper.paid(inVo);
        if (ret > 0) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, 3);     //有效时间为3个月
            //2、支付成功生成抵用券
            this.createCouponForKj(inVo);
            //3、支付日志
            this.savePayLog(inVo);
            //4、订单日志
            this.saveSoLog(inVo, SoLog.SO_STATUS_PAID);
            if(inVo.getType()== WeixinInVo.WEI_XIN_TYPE_KJ) {
                //5. 金币变动的账户日志(要判断是否是直接用原价购买的，如果是，则不需要变动金币)
                GoldLogInVo goldLogInVo = new GoldLogInVo();
                goldLogInVo.setGroupId(inVo.getGroupId());
                goldLogInVo.setRefId(inVo.getDishSkuId());
                goldLogInVo.setParentId(inVo.getUserId());
                Integer integer = goldLogService.changeState(goldLogInVo);
                logger.info("userid"+inVo.getUserId()+"skuid="+inVo.getDishSkuId()+"groupid="+inVo.getGroupId());
                //删除缓存
                String key="GoldGroup_"+"parent_id_"+inVo.getUserId()+"ref_id_"+inVo.getDishSkuId()+"group_id_"+inVo.getGroupId();
                String skuKey="BargainGroup_"+"parent_id"+inVo.getUserId()+"sku_id_"+inVo.getDishSkuId()+"group_id_"+inVo.getGroupId();
                logger.info("key"+key);
                logger.info("skuKey"+skuKey);
                redisCache.del(key);
                redisCache.del(skuKey);
            }
        }
        return ret;
    }

    @Override
    @Transactional
    public int paidForQg(SoInVo inVo) {
        ShopCashier shopCashier = shopCashierMapper.adminByShopId(inVo.getShopId());
        //1、更新订单状态
        inVo.setSoStatus(So.SO_STATUS_PAID);
        int ret = soMapper.paid(inVo);
        if (ret > 0) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.WEEK_OF_YEAR, 1);     //有效时间为1周
            //2、支付成功生成抵用券
            this.createCouponForQg(inVo);
            //3、支付日志
            this.savePayLog(inVo);
            //4、订单日志
            this.saveSoLog(inVo, SoLog.SO_STATUS_PAID);
            //5. 删除所买抢购菜的缓存信息
            String key="Qgc_"+"skuId_"+inVo.getDishSkuId()+"_parentId_"+inVo.getUserId();
            logger.info("key" + key);
            redisCache.del(key);

        }
        return ret;
    }

    @Override
    @Transactional
    public int paidForDh(SoInVo inVo) {
        ShopCashier shopCashier = shopCashierMapper.adminByShopId(inVo.getShopId());
        //1、更新订单状态
        inVo.setSoStatus(So.SO_STATUS_PAID);
        int ret = soMapper.paid(inVo);
        if (ret > 0) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.WEEK_OF_YEAR, 1);     //有效时间为1周
            //2、支付成功生成抵用券
            this.createCouponForDh(inVo);
            //3、支付日志
            this.savePayLog(inVo);
            //4、订单日志
            this.saveSoLog(inVo, SoLog.SO_STATUS_PAID);
            //5. 给用户金币
            if(inVo.getType()== WeixinInVo.WEI_XIN_TYPE_KJ) {
                //5. 金币变动的账户日志(要判断是否是直接用原价购买的，如果是，则不需要变动金币)
                GoldLogInVo goldLogInVo = new GoldLogInVo();
                goldLogInVo.setGroupId(inVo.getGroupId());
                goldLogInVo.setRefId(inVo.getDishSkuId());
                goldLogInVo.setParentId(inVo.getUserId());
                Integer integer = goldLogService.changeState(goldLogInVo);
                logger.info("userid"+inVo.getUserId()+"skuid="+inVo.getDishSkuId()+"groupid="+inVo.getGroupId());
                //删除缓存
                String key="GoldGroup_"+"parent_id_"+inVo.getUserId()+"ref_id_"+inVo.getDishSkuId()+"group_id_"+inVo.getGroupId();
                String skuKey="BargainGroup_"+"parent_id"+inVo.getUserId()+"sku_id_"+inVo.getDishSkuId()+"group_id_"+inVo.getGroupId();
                logger.info("key"+key);
                logger.info("skuKey"+skuKey);
                redisCache.del(key);
                redisCache.del(skuKey);
            }
            //6. 删除所买兑换菜的缓存信息
            /*String redisKey="BargainGroup_"+"parent_id"+inVo.getUserId()+"sku_id_"+inVo.getDishSkuId()+"group_id_"+inVo.getGroupId();
            String key="GoldGroup_"+"parent_id_"+inVo.getUserId()+"ref_id_"+inVo.getDishSkuId()+"group_id_"+inVo.getGroupId();
            logger.info("key"+key);
            redisCache.del(key);
            redisCache.del(redisKey);*/

        }
        return ret;
    }

    @Override
    public Integer selectSkuAllocation(Long userId) {
        return soMapper.selectSkuAllocation(userId);
    }


    @Override
    public Integer finished(SoInVo inVo) {
        return null;
    }

    @Override
    public Integer cancel(SoInVo inVo) {
        //1、更新订单状态
        inVo.setSoStatus(So.SO_STATUS_CANCEL);
        int ret = soMapper.cancel(inVo);

        if (ret > 0) {
            this.saveSoLog(inVo, SoLog.SO_STATUS_CANCEL);
        }
        return ret;
    }

    @Override
    public Integer selectByUserIdTotal(Long userId){
        return soMapper.selectByUserIdTotal(userId);
    }


    /**
     * 生成券二维码图片并上传到腾讯云服务器
     * @param code
     * @return
     */
    private String uploadQRCodeToCos(String code) {
        String imagePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "static" + File.separator + "images" + File.separator + "logo.jpg";
        String destPath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "upload" + File.separator + code + ".jpg";
        String text = constantsConfig.getDomainXqUrl() + "/cp/getByCode/"+code;

        //生成logo图片到destPath
        try {
            QRCodeUtil.encode(text, imagePath, destPath, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //上传文件到腾讯云cos--缩放0.8
        String imgUrl = uploadService.uploadFileToCos(destPath, "coupon");
        int i=0;
        do {
            i++;
            if (imgUrl==null){
                imgUrl=uploadService.uploadFileToCos(destPath, "coupon");
            }
            if (imgUrl!=null){
                break;
            }
            if (i==4){
                break;
            }
        }while (true);

        if (StringUtils.isEmpty(imgUrl)) {
            return null;
        }

        //删除服务器上临时文件
        uploadService.deleteTempImage(new Triplet<String, String, String>(destPath, null, null));
        return imgUrl;
    }

    /**
     * 保存订单明细信息
     * @param inVo
     * @return
     */
    private Integer saveSoDetail(SoInVo inVo, Sku sku){
        SoDetail sd = new SoDetail();
        sd.setSoId(inVo.getId());
        sd.setSkuId(inVo.getSkuId());
        sd.setSkuCode(sku.getSkuCode());
        sd.setSkuName(sku.getSkuName());
        sd.setSkuNum(inVo.getSkuNum());
        sd.setUnitPrice(sku.getSellPrice());
        //将食典券和砍菜券所对应的菜的信息加入其中
        if(inVo.getDishSkuId()!=null&&inVo.getDishSkuName()!=null){
            sd.setDishSkuId(inVo.getDishSkuId());
            sd.setDishSkuName(inVo.getDishSkuName());
        }
        return soDetailMapper.insert(sd);
    }

    /**
     * 保存订单日志
     * @return
     */
    private Integer saveSoLog(SoInVo inVo, int operateType){
        int result = 0;
        SoLog soLog = new SoLog();
        soLog.setSoId(inVo.getId());
        soLog.setUserId(inVo.getUserId());
        soLog.setUserName(inVo.getUserName());
        soLog.setUserIp(inVo.getUserIp());
        soLog.setOperateType(operateType);
        try {
            result = soLogMapper.insert(soLog);
        } catch (Exception e) {
            logger.error("保存订单操作日志失败，订单soId :"+ inVo.getId()+" 操作类型，operateType: "+operateType);
        }
        return result;
    }

    /**
     * 保存商家订单日志
     * @return
     */
    private Integer saveSoShopLog(SoInVo inVo, int operateType){
        int result = 0;
        SoShopLog soShopLog = new SoShopLog();
        soShopLog.setSoId(inVo.getId());
        soShopLog.setUserId(inVo.getUserId());
        soShopLog.setUserName(inVo.getUserName());
        soShopLog.setUserIp(inVo.getUserIp());
        soShopLog.setShopId(inVo.getShopId());
        soShopLog.setSkuId(inVo.getSkuId());
        soShopLog.setOperateType(operateType);
        try {
            result = soShopLogMapper.insert(soShopLog);
        } catch (Exception e) {
            logger.error("保存商家订单操作日志失败，订单soId :"+ inVo.getId()+" 操作类型，operateType: "+operateType);
        }
        return result;
    }

    /**
     * 根据订单信息生成代金券
     * @param inVo
     * @return
     */
    private int createCoupon(SoInVo inVo){
        int res = 0;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 3);     //有效时间为3个月
        //2、支付成功生成抵用券
        for (int i = 0; i < inVo.getSkuNum(); i++) {
            //3、查询券信息
            Sku sku = skuMapper.selectByPrimaryKey(inVo.getSkuId());
            CouponSku couponSkuNew = new CouponSku();
            couponSkuNew.setSkuId(inVo.getSkuId());
            couponSkuNew.setIsDeleted(CouponSku.COUPON_SKU_NO_DELETED);
            CouponSku couponSku = couponSkuMapper.selectBySkuId(couponSkuNew);
            SoOut soOut = this.get(inVo.getId());
            Coupon coupon = new Coupon();
            coupon.setSoId(inVo.getId());
            coupon.setCouponCode(RandomStringUtil.getRandomCode(10, 0));
            coupon.setSkuId(sku.getId());
            coupon.setSkuCode(sku.getSkuCode());
            coupon.setSkuName(sku.getSkuName());
            coupon.setCouponAmount(couponSku.getAmount());
            if(sku.getSkuType()==Sku.SKU_TYPE_HDQ){
                coupon.setType(Coupon.OUNPON_TYPE_ACT);
            }else {
                coupon.setType(Coupon.COUPON_TYPE_PLAT);
            }
            coupon.setUserId(soOut.getUserId());
            coupon.setUserName(soOut.getUserName());
            //上传二维码图片到腾讯COS服务器
            String qrcodeUrl = uploadQRCodeToCos(coupon.getCouponCode());
            coupon.setQrcodeUrl(qrcodeUrl);
            coupon.setExpiryDate(cal.getTime());
            couponMapper.insert(coupon);
            res++;
        }
        return res;
    }

    /**
     * 根据订单信息生成活动代金券
     * @param inVo
     * @return
     */
    private int createCouponForAct(SoInVo inVo){
        int res = 0;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 3);     //有效时间为3个月
        //2、支付成功生成抵用券
        for (int i = 0; i < inVo.getSkuNum(); i++) {
            //3、查询券信息
            Sku sku = skuMapper.selectByPrimaryKey(inVo.getSkuId());
            CouponSku couponSkuNew = new CouponSku();
            couponSkuNew.setSkuId(inVo.getSkuId());
            couponSkuNew.setIsDeleted(CouponSku.COUPON_SKU_NO_DELETED);
            CouponSku couponSku = couponSkuMapper.selectBySkuId(couponSkuNew);
            SoOut soOut = this.get(inVo.getId());
            Coupon coupon = new Coupon();
            coupon.setSoId(inVo.getId());
            coupon.setCouponCode(RandomStringUtil.getRandomCode(10, 0));
            coupon.setSkuId(sku.getId());
            coupon.setSkuCode(sku.getSkuCode());
            coupon.setSkuName(sku.getSkuName());
            coupon.setCouponAmount(couponSku.getAmount());
            coupon.setType(Coupon.OUNPON_TYPE_ACT);//这里为活动券，只能参与活动的商家使用
            coupon.setUserId(soOut.getUserId());
            coupon.setUserName(soOut.getUserName());
            coupon.setDishSkuId(inVo.getDishSkuId());
            coupon.setShopId(inVo.getShopId());//这里的shopId为这张券所对应的商家id
            //上传二维码图片到腾讯COS服务器
            String qrcodeUrl = uploadQRCodeToCos(coupon.getCouponCode());
            coupon.setQrcodeUrl(qrcodeUrl);
            coupon.setExpiryDate(cal.getTime());
            couponMapper.insert(coupon);
            res++;
        }
        return res;
    }

    /**
     * 根据订单信息生成砍价券
     * @param inVo
     * @return
     */
    private int createCouponForKj(SoInVo inVo){
        int res = 0;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);     //有效时间为1个月
        //2、支付成功生成抵用券
        for (int i = 0; i < inVo.getSkuNum(); i++) {
            //3、查询券信息
            Sku sku = skuMapper.selectByPrimaryKey(inVo.getSkuId());
            CouponSku couponSkuNew = new CouponSku();
            couponSkuNew.setSkuId(inVo.getSkuId());
            couponSkuNew.setIsDeleted(CouponSku.COUPON_SKU_NO_DELETED);
            CouponSku couponSku = couponSkuMapper.selectBySkuId(couponSkuNew);
            SoOut soOut = this.get(inVo.getId());
            Coupon coupon = new Coupon();
            coupon.setSoId(inVo.getId());
            coupon.setCouponCode(RandomStringUtil.getRandomCode(10, 0));
            coupon.setSkuId(sku.getId());
            coupon.setSkuCode(sku.getSkuCode());
            coupon.setSkuName(sku.getSkuName());
            coupon.setCouponAmount(couponSku.getAmount());
            coupon.setType(Coupon.OUNPON_TYPE_KJQ);//这里为砍价券，只能对应商家才能核销
            coupon.setUserId(soOut.getUserId());
            coupon.setUserName(soOut.getUserName());
            coupon.setDishSkuId(inVo.getDishSkuId());
            coupon.setShopId(inVo.getShopId());//这里的shopId为这张券所对应的商家id
            //上传二维码图片到腾讯COS服务器
            String qrcodeUrl = uploadQRCodeToCos(coupon.getCouponCode());
            coupon.setQrcodeUrl(qrcodeUrl);
            coupon.setExpiryDate(cal.getTime());
            couponMapper.insert(coupon);
            res++;
        }
        return res;
    }

    /**
     * 根据订单信息生成抢购券
     * @param inVo
     * @return
     */
    private int createCouponForQg(SoInVo inVo){
        int res = 0;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);     //有效时间为1周
        //2、支付成功生成抵用券
        for (int i = 0; i < inVo.getSkuNum(); i++) {
            //3、查询券信息
            Sku sku = skuMapper.selectByPrimaryKey(inVo.getSkuId());
            CouponSku couponSkuNew = new CouponSku();
            couponSkuNew.setSkuId(inVo.getSkuId());
            couponSkuNew.setIsDeleted(CouponSku.COUPON_SKU_NO_DELETED);
            CouponSku couponSku = couponSkuMapper.selectBySkuId(couponSkuNew);
            SoOut soOut = this.get(inVo.getId());
            Coupon coupon = new Coupon();
            coupon.setSoId(inVo.getId());
            coupon.setCouponCode(RandomStringUtil.getRandomCode(10, 0));
            coupon.setSkuId(sku.getId());
            coupon.setSkuCode(sku.getSkuCode());
            coupon.setSkuName(sku.getSkuName());
            coupon.setCouponAmount(couponSku.getAmount());
            coupon.setType(Coupon.OUNPON_TYPE_QGQ);//这里为抢购券，只能对应商家才能核销
            coupon.setUserId(soOut.getUserId());
            coupon.setUserName(soOut.getUserName());
            coupon.setDishSkuId(inVo.getDishSkuId());
            coupon.setShopId(inVo.getShopId());//这里的shopId为这张券所对应的商家id
            //上传二维码图片到腾讯COS服务器
            String qrcodeUrl = uploadQRCodeToCos(coupon.getCouponCode());
            coupon.setQrcodeUrl(qrcodeUrl);
            coupon.setExpiryDate(cal.getTime());
            couponMapper.insert(coupon);
            res++;
        }
        return res;
    }

    /**
     * 根据订单信息生成兑换券
     * @param inVo
     * @return
     */
    private int createCouponForDh(SoInVo inVo){
        int res = 0;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 5);     //有效时间为5天
        //2、支付成功生成抵用券
        for (int i = 0; i < inVo.getSkuNum(); i++) {
            //3、查询券信息
            Sku sku = skuMapper.selectByPrimaryKey(inVo.getSkuId());
            CouponSku couponSkuNew = new CouponSku();
            couponSkuNew.setSkuId(inVo.getSkuId());
            couponSkuNew.setIsDeleted(CouponSku.COUPON_SKU_NO_DELETED);
            CouponSku couponSku = couponSkuMapper.selectBySkuId(couponSkuNew);
            SoOut soOut = this.get(inVo.getId());
            Coupon coupon = new Coupon();
            coupon.setSoId(inVo.getId());
            coupon.setCouponCode(RandomStringUtil.getRandomCode(10, 0));
            coupon.setSkuId(sku.getId());
            coupon.setSkuCode(sku.getSkuCode());
            coupon.setSkuName(sku.getSkuName());
            coupon.setCouponAmount(couponSku.getAmount());
            coupon.setType(Coupon.OUNPON_TYPE_DHQ);//这里为兑换券，只能对应商家才能核销
            coupon.setUserId(soOut.getUserId());
            coupon.setUserName(soOut.getUserName());
            coupon.setDishSkuId(inVo.getDishSkuId());
            coupon.setShopId(inVo.getShopId());//这里的shopId为这张券所对应的商家id
            //上传二维码图片到腾讯COS服务器
            String qrcodeUrl = uploadQRCodeToCos(coupon.getCouponCode());
            coupon.setQrcodeUrl(qrcodeUrl);
            coupon.setExpiryDate(cal.getTime());
            couponMapper.insert(coupon);
            res++;
        }
        return res;
    }

    /**
     * 根据订单信息生成折扣券
     * @param inVo
     * @return
     */
    /*private int createCouponForAgio(SoInVo inVo){
        int res = 0;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 3);     //有效时间为3个月
        //2、支付成功生成抵用券
        for (int i = 0; i < inVo.getSkuNum(); i++) {
            //3、查询券信息
            Sku sku = skuMapper.selectByPrimaryKey(inVo.getSkuId());
            CouponSku couponSku = couponSkuMapper.selectBySkuId(inVo.getSkuId());
            SoOut soOut = this.get(inVo.getId());
            Coupon coupon = new Coupon();
            coupon.setSoId(inVo.getId());
            coupon.setCouponCode(RandomStringUtil.getRandomCode(10, 0));
            coupon.setSkuId(sku.getId());
            coupon.setSkuCode(sku.getSkuCode());
            coupon.setSkuName(sku.getSkuName());
            coupon.setCouponAmount(couponSku.getAmount());
            coupon.setType(Coupon.OUNPON_TYPE_AGIO);// 这里套餐折扣券(只能被套餐使用)
            coupon.setUserId(soOut.getUserId());
            coupon.setUserName(soOut.getUserName());
            //上传二维码图片到腾讯COS服务器
            String qrcodeUrl = uploadQRCodeToCos(coupon.getCouponCode());
            coupon.setQrcodeUrl(qrcodeUrl);
            coupon.setExpiryDate(cal.getTime());
            couponMapper.insert(coupon);
            res++;
        }
        return res;
    }*/

    /**
     * 保存支付日志
     * @param inVo
     * @return
     */
    private int savePayLog(SoInVo inVo){
        int res = 0;
        PaidLog paidLog = new PaidLog();
        paidLog.setSoId(inVo.getId());
        paidLog.setPaidType(So.SO_PAY_TYPE_WX);
        paidLog.setUserId(inVo.getUserId());
        paidLog.setUserName(inVo.getUserName());
        try {
            res = paidLogMapper.insert(paidLog);
        } catch (Exception e) {
            logger.error("支付保存订单日志失败,soId : " + inVo.getId());
        }
        return res;
    }
}
