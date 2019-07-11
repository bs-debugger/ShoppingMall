package com.xq.live.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xq.live.common.RedisCache;
import com.xq.live.config.GoldConfig;
import com.xq.live.dao.AccountLogMapper;
import com.xq.live.dao.BargainLogMapper;
import com.xq.live.dao.GoldLogMapper;
import com.xq.live.dao.UserAccountMapper;
import com.xq.live.model.*;
import com.xq.live.service.GoldLogService;
import com.xq.live.vo.in.BargainLogInVo;
import com.xq.live.vo.in.GoldLogInVo;
import com.xq.live.vo.out.BargainLogOut;
import com.xq.live.vo.out.GoldLogOut;
import com.xq.live.web.utils.RedEnvelope;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 用户发起砍菜领取金币
 * Created by ss on 2018/8/6.
 */
@Service
public class GoldLogServiceImpl implements GoldLogService{

    @Autowired
    RedisCache redisCache;
    @Autowired
    private GoldLogMapper goldLogMapper;
    @Autowired
    private BargainLogMapper bargainLogMapper;
    @Autowired
    private AccountLogMapper accountLogMapper;
    @Autowired
    private GoldConfig goldConfig;
    @Autowired
    private UserAccountMapper userAccountMapper;
    private Logger logger = Logger.getLogger(GoldLogServiceImpl.class);

    //private static Long viewArticleTime = System.currentTimeMillis();

    /**
     * 更新用户金币
     * @param inVo
     * @return
     * gold/update?refId=218&groupId=3&parentId=1400
     * 菜id发起人id小组id
     */
    @Override
    @Transactional
    public Integer changeState(GoldLogInVo inVo) {
        List<GoldLog> list= goldLogMapper.teamforparent(inVo);
        if (list==null||list.size()<1){
            return null;
        }
        Integer i = goldLogMapper.changeState(list);
        if (i<1){
            logger.error("用户金币到账状态修改失败");
            throw new RuntimeException("用户金币到账状态修改失败");
        }
        for (GoldLog log:list){
            //获取用户账号
            UserAccount userAccount = userAccountMapper.findAccountByUserId(log.getUserId());
            if (log!=null) {
                //修改用户信息和获取日志
                AccountLog accountLog = custom(userAccount, log);
                Integer dogold = accountLogMapper.insert(accountLog);
                if (dogold<1){
                    throw new RuntimeException("用户金币日志添加信息失败");
                }
            }
        }
        return i;
    }

    /**
     * 用户发起领金币(发起人)(砍菜)
     * @param inVo
     * @return
     * gold/initiator?refId=218&userId=1400&shopId=35&amount=
     */
    @Override
    @Transactional
    public BargainLogOut initiator(GoldLogInVo inVo) throws ParseException {
        //判断1小时内是否砍过菜
        String redisKey="BargainGroup_"+"parent_id"+inVo.getUserId()+"sku_id_"+inVo.getRefId()+"group_id_"+"*";
        Set keyArray=redisCache.selAll(redisKey);
        if (keyArray!=null&&keyArray.size()>0){
            return null;
        }
        Integer groupid=goldLogMapper.goldTotal(inVo);
        if (groupid==null||groupid<1){
            inVo.setGroupId(1);
        }else {
            inVo.setGroupId(groupid+1);
        }
        //将发起人的第一条记录放到砍菜表
        Integer parent= this.getBargainByinsert(inVo);
        if (parent<1){
            throw new RuntimeException("用户砍菜记录添加失败");
        }
        //将发起人的第一条记录放到金币
        Integer one=this.getGoldByinsert(inVo);
        if (one<1){
            throw new RuntimeException("用户金币记录添加失败");
        }
        /*创建缓存对象GoldLogOut*/
        this.getGoldByredis(inVo);
         /*创建缓存对象GoldLogOut并且返回*/
        return this.getBargainByredis(inVo);
    }

    /**
     * 用户发起领金币(发起人)(抢购)
     * @param inVo
     * @return
     * gold/createQg?refId=218&userId=1400&shopId=35&amount=
     */
    @Override
    @Transactional
    public BargainLogOut createQg(GoldLogInVo inVo) {
        //判断1小时内是否砍过菜
        String redisKey="BargainGroup_"+"parent_id"+inVo.getUserId()+"sku_id_"+inVo.getRefId()+"group_id_"+"*";
        Set keyArray=redisCache.selAll(redisKey);
        if (keyArray!=null&&keyArray.size()>0){
            return null;
        }
        Integer groupid=goldLogMapper.goldTotal(inVo);
        if (groupid==null||groupid<1){
            inVo.setGroupId(1);
        }else {
            inVo.setGroupId(groupid+1);
        }
        //将发起人的第一条记录放到砍菜表
        Integer parent= this.getBargainByinsert(inVo);
        if (parent<1){
            throw new RuntimeException("用户砍菜记录添加失败");
        }
        //将发起人的第一条记录放到金币
        Integer one=this.getGoldByinsertQg(inVo);
        if (one<1){
            throw new RuntimeException("用户金币记录添加失败");
        }
        /*创建缓存对象GoldLogOut*/
        this.getGoldByredisQg(inVo);
         /*创建缓存对象GoldLogOut并且返回*/
        return this.getBargainByredisQg(inVo);
    }


    /**
     * 帮忙砍菜人领金币(好友)和砍掉菜的价钱(砍菜)
     * @param inVo
     * @return
     * localhost:8080/gold/helpfriend?shopId=35&userId=52108&refId=218&parentId=1400&groupId=12
     * 商家id-用户id-菜id-发起人id-小组id
     */
    @Override
    @Transactional
    public Map<String, Object> helpfriend(GoldLogInVo inVo) {
        //获取缓存
        String skuKey="BargainGroup_"+"parent_id"+inVo.getParentId()+"sku_id_"+inVo.getRefId()+"group_id_"+inVo.getGroupId();
        //获取缓存
        String key="GoldGroup_"+"parent_id_"+inVo.getParentId()+"ref_id_"+inVo.getRefId()+"group_id_"+inVo.getGroupId();
        Map<String,Object> stringBargainMap=redisCache.get(skuKey, Map.class);
        if (stringBargainMap==null){
            return null;
        }
        Map<String,Object> stringObjectMap=redisCache.get(key, Map.class);
        if (stringObjectMap==null){
            return null;
        }
        BargainLogOut bargainLogOut=(BargainLogOut)stringBargainMap.get(skuKey);
        if (bargainLogOut==null){
            throw null;
        }


        BargainLogInVo skuInVo= new BargainLogInVo();
        skuInVo.setParentId(inVo.getParentId());
        skuInVo.setSkuId(inVo.getRefId());
        skuInVo.setGroupId(inVo.getGroupId());
        skuInVo.setUserId(inVo.getUserId());
        BargainLog skubarlog=bargainLogMapper.getByshiro(skuInVo);

        if (skubarlog==null||skubarlog.getId()==null){
        }else if (skubarlog!=null||skubarlog.getId()!=null){
            throw new RuntimeException("已经帮忙砍过了哦!");
        }

        //更改缓存信息
        bargainLogOut.setPeoplenum(bargainLogOut.getPeoplenum() + 1);
        bargainLogOut.setSkuMoneyNow(bargainLogOut.getSkuMoneyNow().subtract(bargainLogOut.getAmountList().get(bargainLogOut.getPeoplenum()- 2)));


        BargainLogInVo bargainLogInVo = new BargainLogInVo();
        bargainLogInVo.setSkuAmount(bargainLogOut.getAmountList().get(bargainLogOut.getPeoplenum()- 2));
        bargainLogInVo.setShopId(inVo.getShopId());
        bargainLogInVo.setGroupId(inVo.getGroupId());
        bargainLogInVo.setIsDelete(BargainLog.IS_DELETE_NO);
        bargainLogInVo.setUserId(inVo.getUserId());
        bargainLogInVo.setParentId(inVo.getParentId());
        bargainLogInVo.setSkuId(inVo.getRefId());
        Integer parent=bargainLogMapper.insertforparent(bargainLogInVo);
        if (parent<1){
            throw new RuntimeException("用户砍菜记录添加失败");
        }
        BargainLog skulog=bargainLogMapper.selectByPrimaryKey(bargainLogInVo.getId());
        if (skulog==null){
            throw new RuntimeException("没有查询到用户砍菜记录");
        }
        //跟新缓存
        Date date= new Date();
        Long redisout=RedEnvelope.getDatePoor(bargainLogOut.getEndTime(),date);
        if (redisout.equals(0)||redisout.intValue()<0){
            redisCache.del(key);
            redisCache.del(skuKey);
            return null;
        }
        stringBargainMap.put(skuKey, bargainLogOut);
        redisCache.set(skuKey, stringBargainMap, redisout, TimeUnit.SECONDS);
        /*------------*/
        GoldLogOut goldLogOut=(GoldLogOut)stringObjectMap.get(key);
        if (goldLogOut==null){
            throw null;
        }
        goldLogOut.setPeopleNum(goldLogOut.getPeopleNum() + 1);
        inVo.setGoldAmount(goldLogOut.getGoldGroup().get(goldLogOut.getPeopleNum()-1));
        inVo.setStateType(GoldLog.STATE_TYPE_NO);
        inVo.setType(GoldLog.GOLD_TYPE);
        inVo.setIsDelete(GoldLog.IS_DELETE_NO);
        //将记录放到数据库
        Integer one=goldLogMapper.insertforparent(inVo);
        if (one<1){
            throw new RuntimeException("用户金币记录添加失败");
        }
        GoldLog log=goldLogMapper.selectByPrimaryKey(inVo.getId());
        if (log==null){
            throw new RuntimeException("没有查询到用户金币记录");
        }

        //跟新缓存
        stringObjectMap.put(key, goldLogOut);
        redisCache.set(key, stringObjectMap, redisout, TimeUnit.SECONDS);

        Map<String,Object> map = new HashMap<>();
        map.put("gold", log);
        map.put("bargain",skulog);
        return map;
    }

    /**
     * 帮忙砍菜人领金币(好友)和砍掉菜的价钱(抢购)
     * @param inVo
     * @return
     * localhost:8080/gold/helpfriendQg?shopId=35&userId=52108&refId=218&parentId=1400&groupId=12
     * 商家id-用户id-菜id-发起人id-小组id
     */
    @Override
    @Transactional
    public Map<String, Object> helpfriendQg(GoldLogInVo inVo) {
        //获取缓存
        String skuKey="BargainGroup_"+"parent_id"+inVo.getParentId()+"sku_id_"+inVo.getRefId()+"group_id_"+inVo.getGroupId();
        //获取缓存
        String key="GoldGroup_"+"parent_id_"+inVo.getParentId()+"ref_id_"+inVo.getRefId()+"group_id_"+inVo.getGroupId();

        Map<String,Object> stringBargainMap=redisCache.get(skuKey, Map.class);
        if (stringBargainMap==null){
            return null;
        }
        Map<String,Object> stringObjectMap=redisCache.get(key, Map.class);
        if (stringObjectMap==null){
            return null;
        }
        BargainLogOut bargainLogOut=(BargainLogOut)stringBargainMap.get(skuKey);
        if (bargainLogOut==null){
            throw null;
        }
        //更改缓存信息
        bargainLogOut.setPeoplenum(bargainLogOut.getPeoplenum() + 1);
        bargainLogOut.setSkuMoneyNow(bargainLogOut.getSkuMoneyNow().subtract(bargainLogOut.getAmountList().get(bargainLogOut.getPeoplenum()- 2)));


        BargainLogInVo bargainLogInVo = new BargainLogInVo();
        bargainLogInVo.setSkuAmount(bargainLogOut.getAmountList().get(bargainLogOut.getPeoplenum()- 2));
        bargainLogInVo.setShopId(inVo.getShopId());
        bargainLogInVo.setGroupId(inVo.getGroupId());
        bargainLogInVo.setIsDelete(BargainLog.IS_DELETE_NO);
        bargainLogInVo.setUserId(inVo.getUserId());
        bargainLogInVo.setParentId(inVo.getParentId());
        bargainLogInVo.setSkuId(inVo.getRefId());
        Integer parent=bargainLogMapper.insertforparent(bargainLogInVo);
        if (parent<1){
            throw new RuntimeException("用户砍菜记录添加失败");
        }
        BargainLog skulog=bargainLogMapper.selectByPrimaryKey(bargainLogInVo.getId());
        if (skulog==null){
            throw new RuntimeException("没有查询到用户砍菜记录");
        }
        //跟新缓存
        Date date= new Date();
        Long redisout=RedEnvelope.getDatePoor(bargainLogOut.getEndTime(),date);
        if (redisout.equals(0)||redisout.intValue()<0){
            redisCache.del(key);
            redisCache.del(skuKey);
            return null;
        }
        stringBargainMap.put(skuKey, bargainLogOut);
        redisCache.set(skuKey, stringBargainMap, redisout, TimeUnit.SECONDS);

        GoldLogOut goldLogOut=(GoldLogOut)stringObjectMap.get(key);
        if (goldLogOut==null){
            throw null;
        }
        goldLogOut.setPeopleNum(goldLogOut.getPeopleNum() + 1);
        inVo.setGoldAmount(goldLogOut.getGoldGroup().get(goldLogOut.getPeopleNum()-1));
        inVo.setStateType(GoldLog.STATE_TYPE_NO);
        inVo.setType(GoldLog.GOLD_TYPE);
        inVo.setIsDelete(GoldLog.IS_DELETE_NO);
        //将记录放到数据库
        Integer one=goldLogMapper.insertforparent(inVo);
        if (one<1){
            throw new RuntimeException("用户金币记录添加失败");
        }
        GoldLog log=goldLogMapper.selectByPrimaryKey(inVo.getId());
        if (log==null){
            throw new RuntimeException("没有查询到用户金币记录");
        }

        //跟新缓存
        stringObjectMap.put(key, goldLogOut);
        redisCache.set(key, stringObjectMap);

        Map<String,Object> map = new HashMap<>();
        map.put("gold", log);
        map.put("bargain",skulog);
        return map;
    }

    /**
     * 是否领取过金币或者人数已满(砍菜)
     * @param inVo
     * @return
     * gold/getshior?refId=218&userId=1400&groupId=
     */
    @Override
    public Integer getshior(GoldLogInVo inVo) {
        Date time = new Date();

        BargainLogInVo skuInVo= new BargainLogInVo();
        skuInVo.setParentId(inVo.getParentId());
        skuInVo.setSkuId(inVo.getRefId());
        skuInVo.setGroupId(inVo.getGroupId());
        skuInVo.setUserId(inVo.getUserId());
        BargainLog skulog=bargainLogMapper.getByshiro(skuInVo);
        if (skulog==null){
            String skuKey="BargainGroup_"+"parent_id"+inVo.getParentId()+"sku_id_"+inVo.getRefId()+"group_id_"+inVo.getGroupId();
            Map<String,Object> stringObjectMap=redisCache.get(skuKey, Map.class);
            if (stringObjectMap==null){
                return 5;//用户购买成功，缓存删除，砍菜已结束
            }
            BargainLogOut bargainLogOut=(BargainLogOut)stringObjectMap.get(skuKey);
            System.out.println(bargainLogOut.getEndTime().getTime());
            if (bargainLogOut.getPeoplenum()+1>goldConfig.getNum()){
                return 3;//砍菜人数已满
            }else if (time.getTime()>=bargainLogOut.getEndTime().getTime()){
                //获取缓存
                String key="GoldGroup_"+"parent_id_"+inVo.getParentId()+"ref_id_"+inVo.getRefId()+"group_id_"+inVo.getGroupId();
                redisCache.del(key);
                redisCache.del(skuKey);
                return 4;//砍菜时间已失效
            }else{
                return 1;//可以领取
            }
        }else{
            return 2;//已经砍过菜了
        }
    }

    /**
     * 是否领取过金币或者人数已满(抢购)
     * @param inVo
     * @return
     * gold/getshiorQg?refId=218&userId=1400&groupId=
     */
    @Override
    public Integer getshiorQg(GoldLogInVo inVo) {
        Date time = new Date();

        BargainLogInVo skuInVo= new BargainLogInVo();
        skuInVo.setParentId(inVo.getParentId());
        skuInVo.setSkuId(inVo.getRefId());
        skuInVo.setGroupId(inVo.getGroupId());
        skuInVo.setUserId(inVo.getUserId());
        BargainLog skulog=bargainLogMapper.getByshiro(skuInVo);
        if (skulog==null){
            String skuKey="BargainGroup_"+"parent_id"+inVo.getParentId()+"sku_id_"+inVo.getRefId()+"group_id_"+inVo.getGroupId();
            Map<String,Object> stringObjectMap=redisCache.get(skuKey, Map.class);
            if (stringObjectMap==null){
                return 5;//用户购买成功，缓存删除，抢购已结束
            }
            BargainLogOut bargainLogOut=(BargainLogOut)stringObjectMap.get(skuKey);
            System.out.println(bargainLogOut.getEndTime().getTime());
            if (bargainLogOut.getPeoplenum()+1>goldConfig.getQgnum()){
                return 3;//抢购人数已满
            }else if (time.getTime()>=bargainLogOut.getEndTime().getTime()){
                //获取缓存
                String key="GoldGroup_"+"parent_id_"+inVo.getParentId()+"ref_id_"+inVo.getRefId()+"group_id_"+inVo.getGroupId();
                redisCache.del(key);
                redisCache.del(skuKey);
                return 4;//抢购时间已失效
            }else{
                return 1;//可以领取
            }
        }else{
            return 2;//已经砍过菜了
        }
    }


    /*
    * 修改用户金币方法
    *
    * */
    public AccountLog custom(UserAccount userAccount,GoldLog goldLog)  throws RuntimeException{
        AccountLog accountLog = new AccountLog();
        accountLog.setAccountId(userAccount.getId());
        accountLog.setUserName(userAccount.getUserName());
        if (userAccount.getGold() == null){
            userAccount.setGold(0);
        }
        accountLog.setPreGold(userAccount.getGold());//操作前金币
        accountLog.setAfterGold(userAccount.getGold() + goldLog.getGoldAmount());//操作后金币
        accountLog.setOperateGold(goldLog.getGoldAmount());//操作金币
        accountLog.setOperateType(AccountLog.OPERATE_TYPE_INCOME);//加钱
        StringBuilder remark = new StringBuilder();
        remark.append("砍价拼菜");//标题
        accountLog.setRemark(remark.toString());
        accountLog.setUserId(userAccount.getUserId());
        accountLog.setAccountName(userAccount.getAccountName());
        System.out.println(accountLog.getRemark());

        //更改用户金币
        UserAccount account = new UserAccount();
        account.setUserId(userAccount.getUserId());
        account.setVersionNo(userAccount.getVersionNo());
        account.setGold(userAccount.getGold() + goldLog.getGoldAmount());
        Integer i=userAccountMapper.goldByUserId(account);
        if (i < 1) {
            throw new RuntimeException("账户金币修改失败!");
        }
        return accountLog;
    }


    /*写入砍菜记录表*/
    private Integer getBargainByinsert(GoldLogInVo inVo){
        BargainLogInVo bargainLogInVo = new BargainLogInVo();
        bargainLogInVo.setSkuId(inVo.getRefId());
        bargainLogInVo.setShopId(inVo.getShopId());
        bargainLogInVo.setUserId(inVo.getUserId());
        bargainLogInVo.setIsDelete(BargainLog.IS_DELETE_NO);
        bargainLogInVo.setGroupId(inVo.getGroupId());
        bargainLogInVo.setSkuAmount(BigDecimal.ZERO);
        //将发起人的第一条记录放到砍菜表
        Integer parent=bargainLogMapper.insertforparent(bargainLogInVo);
        return parent;
    }

    /*创建缓存对象BargainLogOut砍菜)*/
    private BargainLogOut getBargainByredis(GoldLogInVo inVo){
        //获取砍菜金额合集
        List<BigDecimal> skulist=RedEnvelope.RedDivied(inVo.getAmount().doubleValue(), goldConfig.getNum()-1);

        //生成失效时间
        Long outdate= new Long(60*60*1000);
        Date nowDate=new Date();
        Date endtime=new Date(nowDate.getTime()+outdate);
        //创建缓存对象BargainLogOut
        BargainLogOut bargainLogOut = new BargainLogOut();
        bargainLogOut.setUserId(inVo.getUserId());
        bargainLogOut.setGroupId(inVo.getGroupId());
        bargainLogOut.setShopId(inVo.getShopId());
        bargainLogOut.setAmountList(skulist);
        bargainLogOut.setBeginTime(nowDate);
        bargainLogOut.setEndTime(endtime);
        bargainLogOut.setPeoplenum(1);
        bargainLogOut.setSkuId(inVo.getRefId());
        bargainLogOut.setSkuMoneyNow(inVo.getSkuMoneyOut());//开始菜的现价和原价相等
        bargainLogOut.setSkuMoneyOut(inVo.getSkuMoneyOut());//菜的原价
        bargainLogOut.setSkuMoneyMin(inVo.getSkuMoneyMin());//菜的底价

        String skuKey="BargainGroup_"+"parent_id"+inVo.getUserId()+"sku_id_"+inVo.getRefId()+"group_id_"+inVo.getGroupId();
        Map<String,Object> stringBargainMap=new HashMap<String,Object>();
        stringBargainMap.put(skuKey, bargainLogOut);
        redisCache.set(skuKey, stringBargainMap, 1l, TimeUnit.HOURS);

        return bargainLogOut;
    }

    /*创建缓存对象BargainLogOut(抢购)*/
    private BargainLogOut getBargainByredisQg(GoldLogInVo inVo){
        //获取砍菜金额合集
        List<BigDecimal> skulist=RedEnvelope.RedDivied(inVo.getAmount().doubleValue(), goldConfig.getQgnum()-1);

        //生成失效时间
        Long outdate= new Long(60*60*1000);
        Date nowDate=new Date();
        Date endtime=new Date(nowDate.getTime()+outdate);
        //创建缓存对象BargainLogOut
        BargainLogOut bargainLogOut = new BargainLogOut();
        bargainLogOut.setUserId(inVo.getUserId());
        bargainLogOut.setGroupId(inVo.getGroupId());
        bargainLogOut.setShopId(inVo.getShopId());
        bargainLogOut.setAmountList(skulist);
        bargainLogOut.setBeginTime(nowDate);
        bargainLogOut.setEndTime(endtime);
        bargainLogOut.setPeoplenum(1);
        bargainLogOut.setSkuId(inVo.getRefId());
        bargainLogOut.setSkuMoneyNow(inVo.getSkuMoneyOut());//开始菜的现价和原价相等
        bargainLogOut.setSkuMoneyOut(inVo.getSkuMoneyOut());//菜的原价
        bargainLogOut.setSkuMoneyMin(inVo.getSkuMoneyMin());//菜的底价

        String skuKey="BargainGroup_"+"parent_id"+inVo.getUserId()+"sku_id_"+inVo.getRefId()+"group_id_"+inVo.getGroupId();
        Map<String,Object> stringBargainMap=new HashMap<String,Object>();
        stringBargainMap.put(skuKey, bargainLogOut);
        redisCache.set(skuKey, stringBargainMap, 1l, TimeUnit.HOURS);

        return bargainLogOut;
    }

    /*将发起人的第一条记录放到金币(砍菜)*/
    private Integer getGoldByinsert(GoldLogInVo inVo){
        //获取金币合集
        Random random = new Random();
        int num = random.nextInt(goldConfig.getMax()) % (goldConfig.getMax() - goldConfig.getMin() + 1) + goldConfig.getMin();
        List<Integer> goldlist= RedEnvelope.divideRedPackage(num,goldConfig.getNum());

        inVo.setGoldAmount(goldlist.get(0));
        inVo.setIsDelete(GoldLog.IS_DELETE_NO);
        inVo.setStateType(GoldLog.STATE_TYPE_NO);
        inVo.setType(GoldLog.GOLD_TYPE);
        //将发起人的第一条记录放到金币
        Integer one=goldLogMapper.insertforparent(inVo);
        return one;
    }

    /*将发起人的第一条记录放到金币(抢购)*/
    private Integer getGoldByinsertQg(GoldLogInVo inVo){
        //获取金币合集
        Random random = new Random();
        int num = random.nextInt(goldConfig.getMax()) % (goldConfig.getMax() - goldConfig.getMin() + 1) + goldConfig.getMin();
        List<Integer> goldlist= RedEnvelope.divideRedPackage(num,goldConfig.getQgnum());

        inVo.setGoldAmount(goldlist.get(0));
        inVo.setIsDelete(GoldLog.IS_DELETE_NO);
        inVo.setStateType(GoldLog.STATE_TYPE_NO);
        inVo.setType(GoldLog.GOLD_TYPE);
        //将发起人的第一条记录放到金币
        Integer one=goldLogMapper.insertforparent(inVo);
        return one;
    }


    /*创建缓存对象GoldLogOut(砍菜)*/
    private void getGoldByredis(GoldLogInVo inVo){
        //获取金币合集
        Random random = new Random();
        int num = random.nextInt(goldConfig.getMax()) % (goldConfig.getMax() - goldConfig.getMin() + 1) + goldConfig.getMin();
        List<Integer> goldlist= RedEnvelope.divideRedPackage(num,goldConfig.getNum());

        //生成失效时间
        Long outdate= new Long(60*60*1000);
        Date nowDate=new Date();
        Date endtime=new Date(nowDate.getTime()+outdate);

        GoldLogOut goldLogOut=new GoldLogOut();
        goldLogOut.setUserId(inVo.getUserId());
        goldLogOut.setGoldSum(num);//金币总额
        goldLogOut.setRefId(inVo.getRefId());
        goldLogOut.setGoldGroup(goldlist);
        goldLogOut.setBeginTime(nowDate);
        goldLogOut.setEndTime(endtime);
        goldLogOut.setPeopleNum(1);
        goldLogOut.setGroupId(inVo.getGroupId());
        goldLogOut.setGoldAmount(goldlist.get(0));
        String key="GoldGroup_"+"parent_id_"+inVo.getUserId()+"ref_id_"+inVo.getRefId()+"group_id_"+inVo.getGroupId();
        Map<String,Object> stringObjectMap=new HashMap<String,Object>();
        stringObjectMap.put(key, goldLogOut);
        redisCache.set(key, stringObjectMap, 1l, TimeUnit.HOURS);
    }

    /*创建缓存对象GoldLogOut(抢购)*/
    private void getGoldByredisQg(GoldLogInVo inVo){
        //获取金币合集
        Random random = new Random();
        int num = random.nextInt(goldConfig.getMax()) % (goldConfig.getMax() - goldConfig.getMin() + 1) + goldConfig.getMin();
        List<Integer> goldlist= RedEnvelope.divideRedPackage(num,goldConfig.getQgnum());

        //生成失效时间
        Long outdate= new Long(60*60*1000);
        Date nowDate=new Date();
        Date endtime=new Date(nowDate.getTime()+outdate);

        GoldLogOut goldLogOut=new GoldLogOut();
        goldLogOut.setUserId(inVo.getUserId());
        goldLogOut.setGoldSum(num);//金币总额
        goldLogOut.setRefId(inVo.getRefId());
        goldLogOut.setGoldGroup(goldlist);
        goldLogOut.setBeginTime(nowDate);
        goldLogOut.setEndTime(endtime);
        goldLogOut.setPeopleNum(1);
        goldLogOut.setGroupId(inVo.getGroupId());
        goldLogOut.setGoldAmount(goldlist.get(0));
        String key="GoldGroup_"+"parent_id_"+inVo.getUserId()+"ref_id_"+inVo.getRefId()+"group_id_"+inVo.getGroupId();
        Map<String,Object> stringObjectMap=new HashMap<String,Object>();
        stringObjectMap.put(key, goldLogOut);
        redisCache.set(key, stringObjectMap, 1l, TimeUnit.HOURS);
    }


}
