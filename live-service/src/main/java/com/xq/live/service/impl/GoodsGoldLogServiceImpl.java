package com.xq.live.service.impl;

import com.alibaba.fastjson.JSON;
import com.xq.live.common.RedisCache;
import com.xq.live.config.GoldConfig;
import com.xq.live.config.WeiXinTeamplateMsg;
import com.xq.live.dao.*;
import com.xq.live.model.*;
import com.xq.live.service.GoodsGoldLogService;
import com.xq.live.service.MessageService;
import com.xq.live.service.WeiXinPushService;
import com.xq.live.vo.in.ActGoodsSkuInVo;
import com.xq.live.vo.in.GoodsBargainLogInVo;
import com.xq.live.vo.in.GoodsGoldLogInVo;
import com.xq.live.vo.out.*;
import com.xq.live.web.utils.RedEnvelope;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by ss on 2018/11/3.
 * 商城金币相关
 */
@Service
public class GoodsGoldLogServiceImpl implements GoodsGoldLogService{

    @Autowired
    RedisCache redisCache;

    @Autowired
    private GoldConfig goldConfig;

    @Autowired
    private GoodsGoldLogMapper goodsGoldLogMapper;

    @Autowired
    private GoodsBargainLogMapper goodsBargainLogMapper;

    @Autowired
    private GoodsSkuMapper goodsSkuMapper;

    @Autowired
    private AccountLogMapper accountLogMapper;

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private WeiXinPushService weiXinPushService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ActInfoMapper actInfoMapper;

    @Autowired
    private ActGoodsSkuMapper actGoodsSkuMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private GoodsPromotionRulesMapper goodsPromotionRulesMapper;

    private Logger logger = Logger.getLogger(GoldLogServiceImpl.class);




    /**
     * 更新用户金币
     * @param inVo
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public Integer changeState(GoodsGoldLogInVo inVo) {
        List<GoodsGoldLog> list= goodsGoldLogMapper.teamForParent(inVo);
        if (list==null||list.size()<1){
            return null;
        }
        //修改用户金币到账状态
        Integer i = goodsGoldLogMapper.changeState(list);
        if (i<1){
            logger.error("用户金币到账状态修改失败");
            /*throw new RuntimeException("用户金币到账状态修改失败");*/
        }
        for (GoodsGoldLog log:list){
            //获取用户账号
            UserAccount userAccount = userAccountMapper.findAccountByUserId(log.getUserId());
            if (log!=null) {
                //修改用户信息和获取日志
                AccountLog accountLog = custom(userAccount, log);
                Integer dogold = accountLogMapper.insert(accountLog);
                if (dogold<1){
                    logger.error("用户金币日志添加信息失败");
                    /*throw new RuntimeException("用户金币日志添加信息失败");*/
                }
            }
        }
        String skuKey="ActId_"+inVo.getActId()+"GoodsBargainGroup_"+"parent_id_"+inVo.getParentId()+"sku_id_"+inVo.getRefId()+"group_id_"+inVo.getGroupId();
        String key="ActId_"+inVo.getActId()+"GoodsGoldGroup_"+"parent_id_"+inVo.getParentId()+"ref_id_"+inVo.getRefId()+"group_id_"+inVo.getGroupId();
        redisCache.hdel("ActId_"+inVo.getActId()+"GoodsGoldGroup_parent_id_" + inVo.getParentId(), key);
        redisCache.hdel("ActId_"+inVo.getActId()+"GoodsBargainGroup_parent_id_" + inVo.getParentId(), skuKey);

        GoodsBargainLog bargainLog= new GoodsBargainLog();
        bargainLog.setIsDelete(GoodsBargainLog.IS_DELETE_YES);
        bargainLog.setGroupId(inVo.getGroupId());
        bargainLog.setSkuId(inVo.getRefId());
        bargainLog.setParentId(inVo.getParentId());
        goodsBargainLogMapper.updateIsdelete(bargainLog);
        GoodsGoldLog goodsGoldLog = new GoodsGoldLog();
        goodsGoldLog.setIsDelete(GoodsGoldLog.IS_DELETE_YES);
        goodsGoldLog.setGroupId(inVo.getGroupId());
        goodsGoldLog.setParentId(inVo.getParentId());
        goodsGoldLog.setRefId(inVo.getRefId());
        goodsGoldLogMapper.updateIsdelete(goodsGoldLog);
        return i;
    }

    /**
     * 用户发起领金币(发起人)(砍价)
     * @param inVo
     * @return
     */
    @Override
    @Transactional
    public GoodsBargainLogOut initiator(GoodsGoldLogInVo inVo) throws ParseException {
        Date time = new Date();
        Map<Object,String> keyArrays = redisCache.hmget("ActId_" + inVo.getActId() + "GoodsBargainGroup_parent_id_" + inVo.getUserId());
        if(keyArrays==null||keyArrays.size()==0){
            /*查询用户发起过的次数(用来当作小组编号)*/
            Integer groupid=goodsGoldLogMapper.goldTotal(inVo);
            if (groupid==null||groupid<1){
                inVo.setGroupId(1);
            }else {
                inVo.setGroupId(groupid+1);
            }
            //将发起人的第一条记录放到砍价表
            Integer parent= this.getBargainByinsert(inVo);
            if (parent<1){
                throw new RuntimeException("用户砍价记录添加失败");
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
        }else{
            for (Map.Entry<Object, String> a: keyArrays.entrySet()){
                Object key =a.getKey();
                String value=a.getValue();
                if (key!=null||value!=null){
                    GoodsBargainLogOut goodsBargainLogOut = JSON.parseObject(value, GoodsBargainLogOut.class);
                    if (time.getTime()-goodsBargainLogOut.getEndTime().getTime()>=0){
                        //获取缓存
                        String skuKey="ActId_"+inVo.getActId()+"GoodsBargainGroup_"+"parent_id_" + inVo.getUserId()+"sku_id_"+goodsBargainLogOut.getSkuId()+"group_id_"+goodsBargainLogOut.getGroupId();
                        String valueKey="ActId_"+inVo.getActId()+"GoodsGoldGroup_"+"parent_id_" + inVo.getUserId()+"ref_id_"+goodsBargainLogOut.getSkuId()+"group_id_"+goodsBargainLogOut.getGroupId();
                        redisCache.hdel("ActId_" + inVo.getActId() + "GoodsGoldGroup_parent_id_" + inVo.getUserId(), valueKey);
                        redisCache.hdel("ActId_" + inVo.getActId() + "GoodsBargainGroup_parent_id_" + inVo.getUserId(), skuKey);
                        //砍菜时间已失效
                        /*查询用户发起过的次数(用来当作小组编号)*/
                        Integer groupid=goodsGoldLogMapper.goldTotal(inVo);
                        if (groupid==null||groupid<1){
                            inVo.setGroupId(1);
                        }else {
                            inVo.setGroupId(groupid+1);
                        }
                        //将发起人的第一条记录放到砍价表
                        Integer parent= this.getBargainByinsert(inVo);
                        if (parent<1){
                            throw new RuntimeException("用户砍价记录添加失败");
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
                    }else {
                        if (inVo.getRefId().toString().equals(goodsBargainLogOut.getSkuId().toString())){
                           return goodsBargainLogOut;
                        }
                    }
                }
            }
                   /*查询用户发起过的次数(用来当作小组编号)*/
            Integer groupid=goodsGoldLogMapper.goldTotal(inVo);
            if (groupid==null||groupid<1){
                inVo.setGroupId(1);
            }else {
                inVo.setGroupId(groupid+1);
            }
            //将发起人的第一条记录放到砍价表
            Integer parent= this.getBargainByinsert(inVo);
            if (parent<1){
                throw new RuntimeException("用户砍价记录添加失败");
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
    }

    /**
     * 帮砍人领金币(好友)(砍价)
     * @param inVo
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> helpFriend(GoodsGoldLogInVo inVo) {
        //获取缓存
        String skuKey="ActId_"+inVo.getActId()+"GoodsBargainGroup_"+"parent_id_"+inVo.getParentId()+"sku_id_"+inVo.getRefId()+"group_id_"+inVo.getGroupId();
        //获取缓存
        String key="ActId_"+inVo.getActId()+"GoodsGoldGroup_"+"parent_id_"+inVo.getParentId()+"ref_id_"+inVo.getRefId()+"group_id_"+inVo.getGroupId();

        String hgetBargain = (String)redisCache.hget("ActId_"+inVo.getActId()+"GoodsBargainGroup_parent_id_" + inVo.getParentId(),skuKey);
        if (hgetBargain==null||hgetBargain.length()<1){
            return null;
        }
        String hgetGold = (String)redisCache.hget("ActId_"+inVo.getActId()+"GoodsGoldGroup_parent_id_" + inVo.getParentId(),key);
        if (hgetGold==null||hgetGold.length()<1){
            return null;
        }

        GoodsBargainLogOut bargainLogOut = JSON.parseObject(hgetBargain, GoodsBargainLogOut.class);
        if (bargainLogOut==null){
            throw null;
        }
        GoodsBargainLogInVo skuInVo= new GoodsBargainLogInVo();
        skuInVo.setParentId(inVo.getParentId());
        skuInVo.setSkuId(inVo.getRefId());
        skuInVo.setGroupId(inVo.getGroupId());
        skuInVo.setUserId(inVo.getUserId());
        GoodsBargainLog skuBargainlog=goodsBargainLogMapper.getByShiro(skuInVo);
        if (skuBargainlog==null||skuBargainlog.getId()==null){
        }else if (skuBargainlog!=null||skuBargainlog.getId()!=null){
            return null;
        }
        //更改缓存信息
        bargainLogOut.setPeoplenum(bargainLogOut.getPeoplenum() + 1);
        bargainLogOut.setSkuMoneyNow(bargainLogOut.getSkuMoneyNow().subtract(bargainLogOut.getAmountList().get(bargainLogOut.getPeoplenum() - 2)));
        redisCache.hset("ActId_"+inVo.getActId()+"GoodsBargainGroup_parent_id_" + inVo.getParentId(), skuKey, JSON.toJSONString(bargainLogOut));
        /**
         * 砍价人数已满4人进行推送
         */
        if (bargainLogOut.getPeoplenum()>=4){
            GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(inVo.getRefId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date date=new java.util.Date();
            String str=sdf.format(date);

            ShopOut shopOut=shopMapper.findShopOutById(bargainLogOut.getShopId());
            String shopName="享七自营";
            if(shopOut!=null&&shopOut.getShopName()!=null){
                shopName=shopOut.getShopName();
            }
            String keyWords=goodsSku.getSkuName()+","+bargainLogOut.getSkuMoneyNow()+","+str+","+shopName+",您的砍价小组人数已满，砍价成功";//商品名称 + 当前砍价 + 完成时间 + 商家名称 + 备注 此处说明的逗号为中文逗号
            weiXinPushService.pushByUserId(WeiXinTeamplateMsg.templateId_TYPE_ACT_BARGAIN, "",keyWords,inVo.getParentId());
            //满4人进行推送之后发送消息到小程序的消息列表
            messageService.addMessage("砍价成功", "您的 "+goodsSku.getSkuName()+" 人数已满"+bargainLogOut.getPeoplenum()+"。", MessageText.MESSAGE_TEXT_TYPE_PRIVATE, inVo.getParentId(), inVo.getParentId());


        }
        GoodsBargainLogInVo bargainLogInVo = new GoodsBargainLogInVo();
        bargainLogInVo.setSkuAmount(bargainLogOut.getAmountList().get(bargainLogOut.getPeoplenum()- 2));
        bargainLogInVo.setShopId(inVo.getShopId());
        bargainLogInVo.setGroupId(inVo.getGroupId());
        bargainLogInVo.setIsDelete(GoodsBargainLog.IS_DELETE_NO);
        bargainLogInVo.setUserId(inVo.getUserId());
        bargainLogInVo.setParentId(inVo.getParentId());
        bargainLogInVo.setSkuId(inVo.getRefId());
        Integer parent=goodsBargainLogMapper.insertForParent(bargainLogInVo);
        if (parent<1){
            throw new RuntimeException("用户砍价记录添加失败");
        }
        GoodsBargainLog skulog=goodsBargainLogMapper.selectByPrimaryKey(bargainLogInVo.getId());
        if (skulog==null){
            throw new RuntimeException("没有查询到用户砍价记录");
        }
        //判断是否过期 更新缓存
        Date date= new Date();
        Long redisout=RedEnvelope.getDatePoor(bargainLogOut.getEndTime(),date);
        if (redisout.equals(0)||redisout.intValue()<0){
            redisCache.hdel("ActId_"+inVo.getActId()+"GoodsGoldGroup_parent_id_" + inVo.getParentId(), key);
            redisCache.hdel("ActId_"+inVo.getActId()+"GoodsBargainGroup_parent_id_" + inVo.getParentId(), skuKey);
            return null;
        }

        /*------------*/
        GoodsGoldLogOut goldLogOut = JSON.parseObject(hgetGold, GoodsGoldLogOut.class);
        if (goldLogOut==null){
            throw null;
        }
        goldLogOut.setPeopleNum(goldLogOut.getPeopleNum() + 1);
        inVo.setGoldAmount(goldLogOut.getGoldGroup().get(goldLogOut.getPeopleNum()-1));
        inVo.setStateType(GoodsGoldLog.STATE_TYPE_NO);
        inVo.setType(GoodsGoldLog.GOLD_TYPE);
        inVo.setIsDelete(GoodsGoldLog.IS_DELETE_NO);
        //将记录放到数据库
        Integer one=goodsGoldLogMapper.insertForParent(inVo);
        if (one<1){
            throw new RuntimeException("用户金币记录添加失败");
        }
        GoodsGoldLog log=goodsGoldLogMapper.selectByPrimaryKey(inVo.getId());
        if (log==null){
            throw new RuntimeException("没有查询到用户金币记录");
        }

        //跟新缓存
        redisCache.hset("ActId_"+inVo.getActId()+"GoodsGoldGroup_parent_id_" + inVo.getParentId(), key, JSON.toJSONString(goldLogOut));

        Map<String,Object> map = new HashMap<>();
        map.put("gold", log);
        map.put("bargain",skulog);
        return map;
    }

    /**
     * 是否领取过金币或者人数已满(砍价)
     * @param inVo
     * @return
     */
    @Override
    public Integer getShior(GoodsGoldLogInVo inVo) {
        //获取活动商品
        ActGoodsSkuInVo actGoodsSkuInVo = new ActGoodsSkuInVo();
        actGoodsSkuInVo.setSkuId(inVo.getRefId());
        actGoodsSkuInVo.setActId(inVo.getActId());
        ActGoodsSkuOut actGoodsSkuOut = actGoodsSkuMapper.selectByActId(actGoodsSkuInVo);

        Date time = new Date();
        GoodsBargainLogInVo skuInVo= new GoodsBargainLogInVo();
        skuInVo.setParentId(inVo.getParentId());
        skuInVo.setSkuId(inVo.getRefId());
        skuInVo.setGroupId(inVo.getGroupId());
        skuInVo.setUserId(inVo.getUserId());
        GoodsBargainLog skulog=goodsBargainLogMapper.getByShiro(skuInVo);
        if (skulog==null||skulog.getId()==null){
            String skuKey="ActId_"+inVo.getActId()+"GoodsBargainGroup_"+"parent_id_"+inVo.getParentId()+"sku_id_"+inVo.getRefId()+"group_id_"+inVo.getGroupId();
            String hget = (String)redisCache.hget("ActId_"+inVo.getActId()+"GoodsBargainGroup_parent_id_" + inVo.getParentId(),skuKey);
            if (hget==null||hget.length()<1){
                return null;//用户购买成功，缓存删除，砍菜已结束
            }
            GoodsBargainLogOut goodsBargainLogOut = JSON.parseObject(hget, GoodsBargainLogOut.class);
            if (time.getTime()-goodsBargainLogOut.getEndTime().getTime() >=0){

                //获取缓存
                String key="ActId_"+inVo.getActId()+"GoodsGoldGroup_"+"parent_id_"+inVo.getParentId()+"ref_id_"+inVo.getRefId()+"group_id_"+inVo.getGroupId();
                redisCache.hdel("ActId_"+inVo.getActId()+"GoodsGoldGroup_parent_id_" + inVo.getParentId(), key);
                redisCache.hdel("ActId_" + inVo.getActId() + "GoodsBargainGroup_parent_id_" + inVo.getParentId(), skuKey);

                GoodsBargainLog bargainLog= new GoodsBargainLog();
                bargainLog.setIsDelete(GoodsBargainLog.IS_DELETE_YES);
                bargainLog.setGroupId(inVo.getGroupId());
                bargainLog.setSkuId(inVo.getRefId());
                bargainLog.setParentId(inVo.getParentId());
                bargainLog.setUserId(inVo.getUserId());
                goodsBargainLogMapper.updateIsdelete(bargainLog);
                GoodsGoldLog goodsGoldLog = new GoodsGoldLog();
                goodsGoldLog.setIsDelete(GoodsGoldLog.IS_DELETE_YES);
                goodsGoldLog.setGroupId(inVo.getGroupId());
                goodsGoldLog.setParentId(inVo.getParentId());
                goodsGoldLog.setRefId(inVo.getRefId());
                goodsGoldLog.setUserId(inVo.getUserId());
                goodsGoldLogMapper.updateIsdelete(goodsGoldLog);

                return 4;//砍菜时间已失效
            }else if (goodsBargainLogOut.getPeoplenum()+1>actGoodsSkuOut.getPeopleNum()){
                return 3;//砍菜人数已满
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
    public AccountLog custom(UserAccount userAccount,GoodsGoldLog goldLog)  throws RuntimeException{
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
        remark.append("砍价");//标题
        accountLog.setRemark(remark.toString());
        accountLog.setType(AccountLog.TYPE_GOLD);
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

    /*
    * 模糊查询缓存
    * */
     @Override
     public GoodsBargainLogOut selectByRedis(GoodsGoldLogInVo inVo){
        Date time = new Date();
        String redisKey="ActId_"+inVo.getActId()+"GoodsBargainGroup_"+"parent_id_"+inVo.getParentId()+"sku_id_"+inVo.getRefId()+"group_id_"+inVo.getGroupId();
        String hget = (String)redisCache.hget("ActId_"+inVo.getActId()+"GoodsBargainGroup_parent_id_" + inVo.getParentId(),redisKey);
        GoodsBargainLogOut goodsBargainLogOut = JSON.parseObject(hget, GoodsBargainLogOut.class);
        if (time.getTime()-goodsBargainLogOut.getEndTime().getTime()>=0){
        //获取缓存
         String skuKey="ActId_"+inVo.getActId()+"GoodsBargainGroup_"+"parent_id_"+inVo.getParentId()+"sku_id_"+inVo.getRefId()+"group_id_"+goodsBargainLogOut.getGroupId();
         String valueKey="ActId_"+inVo.getActId()+"GoodsGoldGroup_"+"parent_id_"+inVo.getParentId()+"ref_id_"+inVo.getRefId()+"group_id_"+goodsBargainLogOut.getGroupId();
         redisCache.hdel("ActId_"+inVo.getActId()+"GoodsGoldGroup_parent_id_" + inVo.getParentId(), valueKey);
         redisCache.hdel("ActId_"+inVo.getActId()+"GoodsBargainGroup_parent_id_" + inVo.getParentId(), skuKey);
                    GoodsBargainLog bargainLog= new GoodsBargainLog();
                    bargainLog.setIsDelete(GoodsBargainLog.IS_DELETE_YES);
                    bargainLog.setGroupId(inVo.getGroupId());
                    bargainLog.setSkuId(inVo.getRefId());
                    bargainLog.setParentId(inVo.getParentId());
                    goodsBargainLogMapper.updateIsdelete(bargainLog);
                    GoodsGoldLog goodsGoldLog = new GoodsGoldLog();
                    goodsGoldLog.setIsDelete(GoodsGoldLog.IS_DELETE_YES);
                    goodsGoldLog.setGroupId(inVo.getGroupId());
                    goodsGoldLog.setParentId(inVo.getParentId());
                    goodsGoldLog.setRefId(inVo.getRefId());
                    goodsGoldLogMapper.updateIsdelete(goodsGoldLog);
         //砍菜时间已失效
         return null;
         }else {
         return goodsBargainLogOut;
         }
    }

    /*写入砍菜记录表*/
    private Integer getBargainByinsert(GoodsGoldLogInVo inVo){
        GoodsBargainLogInVo bargainLogInVo = new GoodsBargainLogInVo();
        bargainLogInVo.setSkuId(inVo.getRefId());
        bargainLogInVo.setShopId(inVo.getShopId());
        bargainLogInVo.setUserId(inVo.getUserId());
        bargainLogInVo.setIsDelete(GoodsBargainLog.IS_DELETE_NO);
        bargainLogInVo.setGroupId(inVo.getGroupId());
        bargainLogInVo.setSkuAmount(BigDecimal.ZERO);
        //将发起人的第一条记录放到砍菜表
        Integer parent=goodsBargainLogMapper.insertForParent(bargainLogInVo);
        return parent;
    }

    /*创建缓存对象BargainLogOut砍菜)*/
    private GoodsBargainLogOut getBargainByredis(GoodsGoldLogInVo inVo){
        //获取商品的价格
        ActGoodsSkuInVo actGoodsSkuInVo = new ActGoodsSkuInVo();
        actGoodsSkuInVo.setSkuId(inVo.getRefId());
        actGoodsSkuInVo.setActId(inVo.getActId());
        ActGoodsSkuOut actGoodsSkuOut = actGoodsSkuMapper.selectByActId(actGoodsSkuInVo);
        GoodsPromotionRules goodsPromotionRules= goodsPromotionRulesMapper.selectByPrimaryKey(actGoodsSkuOut.getGoodsPrId());
        GoodsSkuOut goodsSkuOut=goodsSkuMapper.selectDetailBySkuId(inVo.getRefId());
        goodsSkuOut.setActInfo(actInfoMapper.selectByPrimaryKey(inVo.getActId()));
        //获取砍菜金额合集
        List<BigDecimal> skulist = new ArrayList<BigDecimal>();
        if (goodsSkuOut.getSellPrice().subtract(goodsPromotionRules.getActAmount()).doubleValue()==0){
            for (int i=0;i<actGoodsSkuOut.getPeopleNum() - 1;i++){
                skulist.add(BigDecimal.ZERO);
            }
        }else {
            skulist= RedEnvelope.RedDivied(goodsSkuOut.getSellPrice().subtract(goodsPromotionRules.getActAmount()).doubleValue(), actGoodsSkuOut.getPeopleNum() - 1);
        }
        //生成失效时间
        Long outdate= new Long(60*60*1000);
        Date nowDate=new Date();
        Date endtime=new Date(nowDate.getTime()+outdate);
        //创建缓存对象BargainLogOut
        GoodsBargainLogOut bargainLogOut = new GoodsBargainLogOut();
        bargainLogOut.setActId(inVo.getActId());
        bargainLogOut.setGoodsSkuOut(goodsSkuOut);
        bargainLogOut.setUserId(inVo.getUserId());
        bargainLogOut.setGroupId(inVo.getGroupId());
        bargainLogOut.setShopId(inVo.getShopId());
        bargainLogOut.setAmountList(skulist);
        bargainLogOut.setBeginTime(nowDate);
        bargainLogOut.setMaxPeoplenum(actGoodsSkuOut.getPeopleNum());
        bargainLogOut.setEndTime(endtime);
        bargainLogOut.setPeoplenum(new Integer(1));
        bargainLogOut.setSkuId(inVo.getRefId());
        bargainLogOut.setSkuMoneyNow(goodsSkuOut.getSellPrice());//开始菜的现价和原价相等
        bargainLogOut.setSkuMoneyOut(goodsSkuOut.getSellPrice());//菜的原价
        bargainLogOut.setSkuMoneyMin(goodsPromotionRules.getActAmount());//菜的底价
        bargainLogOut.setTotleKey("ActId_" + inVo.getActId() + "GoodsBargainGroup_parent_id_" + inVo.getUserId());
        String skuKey="ActId_"+inVo.getActId()+"GoodsBargainGroup_"+"parent_id_"+inVo.getUserId()+"sku_id_"+inVo.getRefId()+"group_id_"+inVo.getGroupId();
        bargainLogOut.setValueKey(skuKey);
        redisCache.hset("ActId_" + inVo.getActId() + "GoodsBargainGroup_parent_id_" + inVo.getUserId(), skuKey, JSON.toJSONString(bargainLogOut));
        return bargainLogOut;
    }


    /*将发起人的第一条记录放到金币(砍菜)*/
    private Integer getGoldByinsert(GoodsGoldLogInVo inVo){
        ActGoodsSkuInVo actGoodsSkuInVo = new ActGoodsSkuInVo();
        actGoodsSkuInVo.setActId(inVo.getActId());
        actGoodsSkuInVo.setSkuId(inVo.getRefId());
        ActGoodsSkuOut actGoodsSku=actGoodsSkuMapper.selectByActId(actGoodsSkuInVo);
        //获取金币合集
        Random random = new Random();
        int num = random.nextInt(goldConfig.getMax()) % (goldConfig.getMax() - goldConfig.getMin() + 1) + goldConfig.getMin();
        List<Integer> goldlist= RedEnvelope.divideRedPackage(num,actGoodsSku.getPeopleNum());

        inVo.setGoldAmount(goldlist.get(0));
        inVo.setIsDelete(GoodsGoldLog.IS_DELETE_NO);
        inVo.setStateType(GoodsGoldLog.STATE_TYPE_NO);
        inVo.setType(GoodsGoldLog.GOLD_TYPE);
        //将发起人的第一条记录放到金币
        Integer one=goodsGoldLogMapper.insertForParent(inVo);
        return one;
    }


    /*创建缓存对象GoldLogOut(砍菜)*/
    private void getGoldByredis(GoodsGoldLogInVo inVo){
        ActGoodsSkuInVo actGoodsSkuInVo = new ActGoodsSkuInVo();
        actGoodsSkuInVo.setSkuId(inVo.getRefId());
        actGoodsSkuInVo.setActId(inVo.getActId());
        ActGoodsSkuOut actGoodsSkuOut = actGoodsSkuMapper.selectByActId(actGoodsSkuInVo);
        //获取金币合集
        Random random = new Random();
        int num = random.nextInt(goldConfig.getMax()) % (goldConfig.getMax() - goldConfig.getMin() + 1) + goldConfig.getMin();
        List<Integer> goldlist= RedEnvelope.divideRedPackage(num,actGoodsSkuOut.getPeopleNum());

        //生成失效时间
        Long outdate= new Long(60*60*1000);
        Date nowDate=new Date();
        Date endtime=new Date(nowDate.getTime()+outdate);

        GoodsGoldLogOut goldLogOut=new GoodsGoldLogOut();
        goldLogOut.setActId(inVo.getActId());
        goldLogOut.setUserId(inVo.getUserId());
        goldLogOut.setGoldSum(num);//金币总额
        goldLogOut.setRefId(inVo.getRefId());
        goldLogOut.setGoldGroup(goldlist);
        goldLogOut.setBeginTime(nowDate);
        goldLogOut.setEndTime(endtime);
        goldLogOut.setPeopleNum(1);
        goldLogOut.setGroupId(inVo.getGroupId());
        goldLogOut.setGoldAmount(goldlist.get(0));
        goldLogOut.setTotleKey("ActId_" + inVo.getActId() + "GoodsGoldGroup_parent_id_" + inVo.getUserId());
        String key="ActId_"+inVo.getActId()+"GoodsGoldGroup_"+"parent_id_"+inVo.getUserId()+"ref_id_"+inVo.getRefId()+"group_id_"+inVo.getGroupId();
        goldLogOut.setValueKey(key);

        redisCache.hset("ActId_" + inVo.getActId() + "GoodsGoldGroup_parent_id_" + inVo.getUserId(), key, JSON.toJSONString(goldLogOut));
    }

}
