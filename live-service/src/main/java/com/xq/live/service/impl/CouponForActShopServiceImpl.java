package com.xq.live.service.impl;

import com.xq.live.common.RedisCache;
import com.xq.live.config.ActSkuConfig;
import com.xq.live.dao.*;
import com.xq.live.model.*;
import com.xq.live.service.ActGoodsSkuService;
import com.xq.live.service.CouponForActShopService;
import com.xq.live.service.UserService;
import com.xq.live.vo.in.ActGoodsSkuInVo;
import com.xq.live.vo.in.GoodsSkuInVo;
import com.xq.live.vo.in.OrderInfoInVo;
import com.xq.live.vo.in.UserInVo;
import com.xq.live.vo.out.ActOrderOut;
import com.xq.live.web.utils.GtPush;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 专门给新活动建立的，用来参与活动的商家核销完券码后投票数目加5票
 * 注:现在准备专门做定时任务的serviceImpl
 * Created by lipeng on 2018/5/4.
 */
@Service
public class CouponForActShopServiceImpl implements CouponForActShopService{

    private Logger logger = Logger.getLogger(GoldLogServiceImpl.class);

    @Autowired
    private ActShopMapper actShopMapper;

    @Autowired
    private ActGoodsSkuMapper actGoodsSkuMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ActOrderMapper actOrderMapper;

    @Autowired
    private OrderLogMapper orderLogMapper;

    @Autowired
    private SoWriteOffMapper soWriteOffMapper;

    @Autowired
    private ActGroupMapper actGroupMapper;

    @Autowired
    private ActSkuConfig actSkuConfig;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private GoodsSkuMapper goodsSkuMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private ActGoodsSkuService actGoodsSkuService;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private ActInfoMapper actInfoMapper;

    @Autowired
    private  ShopAccountLogMapper shopAccountLogMapper;

    /**
     * 定时修改超时订单状态和商品库存数量(普通订单,活动订单)
     */
    @Override
    @Transactional
    @Scheduled(cron = "0 */5 * * * ?")
    public void updategoodskuNumNotAct() {
        /*查询出超时订单24h*/
        List<OrderInfo> listinfo=orderInfoMapper.getNotPaylist(new OrderInfoInVo());
        if (listinfo==null||listinfo.size()<1){
            return;
        }
        /*修改订单状态*/
        Integer order=orderInfoMapper.updateByRollback(listinfo);
        if (order < 1){
            logger.info(listinfo.toString());
            throw new RuntimeException("修改订单失败!");
        }
        /*添加订单日志*/
        for (OrderInfo orderInfo:listinfo){
            OrderLog log=this.createLog(orderInfo);
            Integer orderlog=orderLogMapper.insert(log);
            if (orderlog<1){
                throw new RuntimeException("订单日志添加失败!");
            }
        }

        for (OrderInfo info:listinfo){
            /*查询出订单中的商品详情*/
            OrderItem orderItem= new OrderItem();
            orderItem.setOrderCode(info.getOrderCode());
            List<OrderItem> listitem=orderItemMapper.getForCodelist(orderItem);
            List<GoodsSkuInVo> listsku=new ArrayList<GoodsSkuInVo>();
            List<ActGoodsSkuInVo> listskuAct=new ArrayList<ActGoodsSkuInVo>();
            for (OrderItem item:listitem){
                GoodsSkuInVo inVo = new GoodsSkuInVo();
                ActGoodsSkuInVo actGoodsSkuInVo = new ActGoodsSkuInVo();
                ActOrderOut actOrder = actOrderMapper.selectFirstDistributionByOrderId(info.getId());
                if (actOrder!=null){
                    actGoodsSkuInVo.setStockNum(item.getGoodsNum());
                    actGoodsSkuInVo.setId(actOrder.getActGoodsSkuId());
                    listskuAct.add(actGoodsSkuInVo);
                    actOrderMapper.updateOrderIsDeleted(info.getId());
                }else {
                    inVo.setStockNum(item.getGoodsNum());
                    inVo.setId(item.getGoodsSkuId());
                    listsku.add(inVo);
                }

            }
            if (listsku!=null&&listsku.size()>0){
                Integer sku=goodsSkuMapper.updateByRollback(listsku);
                if (sku<1){
                    for (GoodsSkuInVo item:listsku){
                        logger.info(item.toString());
                    }
                /*throw new RuntimeException("修改库存失败!");*/
                }
            }
            if (listskuAct!=null&&listskuAct.size()>0){
                Integer skuNum=actGoodsSkuMapper.updateByRollback(listskuAct);
                if (skuNum<1){
                    for (ActGoodsSkuInVo item:listskuAct){
                        logger.info(item.toString());
                    }
                }
            }

        }
    }

    /**
     * 定时给商品库存不足的商家推送提醒(推送到APP)
     * 每天上午10点和下午4点执行一次
     */
    @Override
    @Scheduled(cron = "0 0 10,16 * * ?")
    public void pushWaringStockForApp() {
        //活动商品
        List<ActGoodsSku> listForActGoodsSku= actGoodsSkuMapper.selectStocklist();
        //原价商品
        List<GoodsSku> listForGoodsSku= goodsSkuMapper.selectStocklist();
        //进行推送活动商品
        for (ActGoodsSku actGoodsSku:listForActGoodsSku){
            ActInfo actInfo = actInfoMapper.selectByPrimaryKey(actGoodsSku.getActId());
            GoodsSku goodsSku=goodsSkuMapper.selectByPrimaryKey(actGoodsSku.getSkuId());
            if (actGoodsSku.getShopId()!=null&&actGoodsSku.getShopId()>0){
                UserInVo userInVo=new UserInVo();
                userInVo.setShopId(actGoodsSku.getShopId());
                List<User> user=userService.listForShopId(userInVo);
                for (int i =0;i<user.size();i++){
                    PushMsg pushMsg= new PushMsg();
                    pushMsg.setAlias(user.get(i).getId().toString());
                    pushMsg.setTitle("商品" +goodsSku.getSkuName()+":库存预警通知");
                    pushMsg.setMessageInfo("您的"+actInfo.getActName()+"商品" + ":"+goodsSku.getSkuName()+"-库存不足" + actGoodsSku.getWaringStock()+"（份）");
                    pushMsg.setIos("2,进入商品详情页面");
                    pushMsg.setType("android");
                    pushMsg.setBadge("1");
                    GtPush.sendMessage(pushMsg);
                }
            }
        }
        //进行推送原价商品
        for (GoodsSku goodsSku:listForGoodsSku){
            if (goodsSku.getShopId()!=null&&goodsSku.getShopId()>0){
                UserInVo userInVo=new UserInVo();
                userInVo.setShopId(goodsSku.getShopId());
                List<User> user=userService.listForShopId(userInVo);
                for (int i =0;i<user.size();i++){
                    PushMsg pushMsg= new PushMsg();
                    pushMsg.setAlias(user.get(i).getId().toString());
                    pushMsg.setTitle("商品" +goodsSku.getSkuName()+":库存预警通知");
                    pushMsg.setMessageInfo("您的商品" + ":"+goodsSku.getSkuName()+"-库存不足" + goodsSku.getWaringStock() +"（份）");
                    pushMsg.setIos("2,进入商品详情页面");
                    pushMsg.setType("android");
                    pushMsg.setBadge("1");
                    GtPush.sendMessage(pushMsg);
                }
            }
        }
    }






    /*创建订单日志*/
    private OrderLog createLog(OrderInfo info){
        OrderLog log=new OrderLog();
        /*获取用户信息*/
        User user=userMapper.selectByPrimaryKey(info.getUserId());
        if (user!=null){
            /*写入信息*/
            log.setUserId(user.getId());
            log.setUserIp(user.getUserIp());
            log.setUserName(user.getUserName());
        }
        /*-----------------------------*/
        log.setOrderCode(info.getOrderCode());
        log.setOperateType(OrderInfo.STATUS_QX);
        return log;
    }

    /**
     * 定时检查拼团活动的开团情况
     * 满足开团条件则开团成功，修改开团成功用户的票卷状态，给上级和上上级用户增加奖励金，给不满足开团条件的用户退款
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void distribution(){
        List<ActGoodsSku> actGoodsSkuList=actGoodsSkuMapper.selectDistributionList();//查询拼团活动中已到期的团
        if(actGoodsSkuList!=null&&actGoodsSkuList.size()>0){
            for(ActGoodsSku actGoodsSku : actGoodsSkuList){
                 if (actGoodsSku.getState()==ActGoodsSku.STATE_WAIT&&actGoodsSku.getCurrentNum()<actGoodsSku.getPeopleNum()){
                    actGoodsSku.setState(ActGoodsSku.STATE_FAIL);
                }
                ActOrder actOrder=new ActOrder();
                actOrder.setActGoodsSkuId(actGoodsSku.getId());
                //修改超时团状态
                actGoodsSkuMapper.updateOverTimeActStatus(actGoodsSku);
                //修改用户状态
                actOrderMapper.updateOverTimeByStatus(actOrder);
                if (actGoodsSku.getState()==ActGoodsSku.STATE_FAIL){
                    actGoodsSkuService.failDistribution(actGoodsSku);
                }
            }
        }
    }

    /**
     * 同步订单数据(方便统计)
     * 每天晚上11点
     */
    @Scheduled(cron = "0 00 23 * * ?")
    public void  syncOrderData(){
        /* 核心代码 */
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.set(Calendar.SECOND, 0);
        beforeTime.add(Calendar.DAY_OF_MONTH, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime=sdf.format(beforeTime.getTime());// 5分钟之前的时间


        Calendar toTime = Calendar.getInstance();
        toTime.set(Calendar.SECOND, 0);
        toTime.add(Calendar.SECOND, -1);
        String  endTime=sdf.format(toTime.getTime());  // 当前时间前一秒

        //商户微信收款记录
        shopAccountLogMapper.wxPayByTime(startTime,endTime);
        //微信退款记录
        shopAccountLogMapper.wxRefundByTime(startTime,endTime);
        //商户余额支付记录
        shopAccountLogMapper.yuePayByTime(startTime,endTime);
        //商户余额退款明细
        shopAccountLogMapper.yueRefundByTime(startTime,endTime);

        //平台用户微信收款记录
        shopAccountLogMapper.wxUserPayByTime(startTime,endTime);
        //平台用户微信退款记录
        shopAccountLogMapper.wxUserRefundByTime(startTime,endTime);
        //平台用户余额支付记录
        shopAccountLogMapper.yueUserPayByTime(startTime,endTime);
        //平台用户余额退款明细
        shopAccountLogMapper.yueUserRefundByTime(startTime,endTime);
    }
    public static void main(String[] args){
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.set(Calendar.SECOND, 0);
        beforeTime.add(Calendar.DAY_OF_MONTH, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime=sdf.format(beforeTime.getTime());// 5分钟之前的时间


        Calendar toTime = Calendar.getInstance();
        toTime.set(Calendar.SECOND, 0);
        toTime.add(Calendar.SECOND, -1);
        String  endTime=sdf.format(toTime.getTime());  // 当前时间前一秒
        System.out.print( startTime +"  "+ endTime);
    }
}
