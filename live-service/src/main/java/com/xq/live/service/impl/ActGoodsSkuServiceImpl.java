package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.config.GoldConfig;
import com.xq.live.config.WeiXinTeamplateMsg;
import com.xq.live.dao.*;
import com.xq.live.model.*;
import com.xq.live.service.*;
import com.xq.live.vo.in.*;
import com.xq.live.vo.out.*;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by ss on 2018/11/3.
 */
@Service
public class ActGoodsSkuServiceImpl implements ActGoodsSkuService {

    @Autowired
    private ActGoodsSkuMapper actGoodsSkuMapper;

    @Autowired
    private PullUserMapper pullUserMapper;

    @Autowired
    private ActLotteryCategoryMapper actLotteryCategoryMapper;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ActInfoMapper actInfoMapper;

    @Autowired
    private GoldConfig goldConfig;

    @Autowired
    private PullUserService pullUserService;

    @Autowired
    private ActOrderMapper actOrderMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountLogMapper accountLogMapper;

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private GoodsSkuMapper goodsSkuMapper;

    @Autowired
    private ActLotteryMapper actLotteryMapper;

    @Autowired
    private GoodsPromotionRulesMapper goodsPromotionRulesMapper;

    @Autowired
    private WeiXinPushService weiXinPushService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private KafkaService kafkaService;

    //一级用户分得的金额
    @Value("${distribution.first}")
    private BigDecimal firstDistribution;

    //二级用户分得的金额
    @Value("${distribution.second}")
    private BigDecimal secondDistribution;

    private Logger logger = Logger.getLogger(ActGoodsSkuServiceImpl.class);

    /**
     * 通过活动id查询报名参加该活动的商品类目列表
     * @param inVo
     * @return
     */
    @Override
    public List<ActGoodsSkuOut> selectByGroupSpu(ActGoodsSkuInVo inVo) {
        List<ActGoodsSkuOut> list =actGoodsSkuMapper.selectCategoryByActId(inVo);
        return list;
    }

    @Override
    public ActGoodsSkuOut lottery(ActGoodsSkuInVo inVo,Long userId,String userName,String userIp,PullUser pullUser) throws ParseException {
        List<ActGoodsSkuOut> list =actGoodsSkuMapper.selectLotteryList(inVo);
        if(list==null&&list.size()==0){
            return  null;
        }
        //总的百分比
        double  ratio=0;
        //总的权重
        int  weight=0;
        ActGoodsSkuOut result = new ActGoodsSkuOut();

        //计算总的百分比和权重，有百分比的优先计算百分比，没有百分比的计算权重，权重默认为1
        for(ActGoodsSkuOut actGoodsSkuOut:list){
            if(actGoodsSkuOut.getRatio()!=null){
                ratio+=actGoodsSkuOut.getRatio().doubleValue();
            }else{
                if(actGoodsSkuOut.getWeight()==null||actGoodsSkuOut.getWeight()==0){
                    weight+=1;//权重默认为1
                }else{
                    weight+=actGoodsSkuOut.getWeight();
                }
            }
        }

        //产生随机数
        double randomNumber;
        randomNumber = Math.random();

        //根据随机数在所有奖品分布的区域并确定所抽奖品
        double d1 = 0;
        double d2 = 0;
        for(int i=0;i<list.size();i++){
            if(list.get(i).getRatio()!=null){
                d1=d2;
                d2+=list.get(i).getRatio().doubleValue();
            }else{
                d1=d2;
                if(list.get(i).getWeight()==null||list.get(i).getWeight()==0){
                    d2+=(1-ratio)*1/weight;
                }else{
                    d2+=(1-ratio)*list.get(i).getWeight()/weight;
                }
            }
            if(randomNumber>d1&&randomNumber<=d2&&d1!=d2){
                result=list.get(i);
                break;
            }
        }

        if(result==null||result.getId()==null||result.getGoodsSkuOut()==null||result.getGoodsSkuOut().size()<1){
            return null;
        }

        Long shoId=result.getGoodsSkuOut().get(0).getShopId();
        Long goodsSkuId=result.getGoodsSkuOut().get(0).getId();
        Long goodsSpuId=result.getGoodsSkuOut().get(0).getSpuId();

        OrderItem orderItem=new OrderItem();
        List<OrderItem> orderItemList=new ArrayList<>();
        orderItem.setGoodsSkuId(goodsSkuId);
        orderItem.setGoodsSpuId(goodsSpuId);
        orderItem.setGoodsNum(1);
        orderItem.setOrderItemShopId(shoId);
        orderItemList.add(orderItem);

        OrderInfoInVo orderInfo=new OrderInfoInVo();
        orderInfo.setUserId(userId);
        orderInfo.setUserIp(userIp);
        orderInfo.setUserName(userName);
        orderInfo.setShopId(shoId);
        orderInfo.setSourceType(OrderInfo.SOURCE_TYPE_PT);
        orderInfo.setPayType(OrderInfo.PAY_TYPE_MFZS);
        orderInfo.setSingleType(OrderInfo.SINGLE_TYPE_ZD);
        orderInfo.setFlagType(OrderInfo.FLAG_TYPE_PT);
        orderInfo.setOrderItemList(orderItemList);
        Long id = orderInfoService.createNewForFree(orderInfo);//生成订单(免费赠送)
        if(id>0){
            actGoodsSkuMapper.updateStockNum(result);//抽中之后活动商品关联表中的参与活动奖品数量减少1
            pullUser.setPullNum(pullUser.getPullNum()-goldConfig.getLynum());
            Integer integer = pullUserService.updatePullNumsPx(pullUser);
            return result;
        }else{
            return null;
        }
    }

    /**
     * 专区抽奖
     * @param inVo
     * @return
     */
    @Override
    public ActGoodsSkuOut zoneLottery(ActGoodsSkuInVo inVo, Long userId, String userName, String userIp) throws ParseException {
        ActLotteryInVo lotteryInVo = new ActLotteryInVo();
        lotteryInVo.setActId(new Long(45));
        lotteryInVo.setUserId(userId);
        ActLottery actLottery= actLotteryMapper.selectUserLottery(lotteryInVo);
        if (actLottery!=null&&actLottery.getId()!=null){
            if (actLottery.getTotalNumber()>=1){
                List<ActGoodsSkuOut> list =actGoodsSkuMapper.selectLotteryList(inVo);
                if(list==null&&list.size()==0){
                    return  null;
                }
                //总的百分比
                double  ratio=0;
                //总的权重
                int  weight=0;
                ActGoodsSkuOut result = new ActGoodsSkuOut();

                //计算总的百分比和权重，有百分比的优先计算百分比，没有百分比的计算权重，权重默认为1
                for(ActGoodsSkuOut actGoodsSkuOut:list){
                    if(actGoodsSkuOut.getRatio()!=null){
                        ratio+=actGoodsSkuOut.getRatio().doubleValue();
                    }else{
                        if(actGoodsSkuOut.getWeight()==null||actGoodsSkuOut.getWeight()==0){
                            weight+=1;//权重默认为1
                        }else{
                            weight+=actGoodsSkuOut.getWeight();
                        }
                    }
                }
                //产生随机数
                double randomNumber;
                randomNumber = Math.random();
                //根据随机数在所有奖品分布的区域并确定所抽奖品
                double d1 = 0;
                double d2 = 0;
                for(int i=0;i<list.size();i++){
                    if(list.get(i).getRatio()!=null){
                        d1=d2;
                        d2+=list.get(i).getRatio().doubleValue();
                    }else{
                        d1=d2;
                        if(list.get(i).getWeight()==null||list.get(i).getWeight()==0){
                            d2+=(1-ratio)*1/weight;
                        }else{
                            d2+=(1-ratio)*list.get(i).getWeight()/weight;
                        }
                    }
                    if(randomNumber>d1&&randomNumber<=d2&&d1!=d2){
                        result=list.get(i);
                        break;
                    }
                }
                if(result==null||result.getId()==null||result.getGoodsSkuOut()==null||result.getGoodsSkuOut().size()<1){
                    return null;
                }
                if (result.getGoodsSkuOut().get(0).getId().toString().equals(new Long(8053).toString())){
                    actLotteryMapper.updateDownTotalNumber(lotteryInVo);
                    return result;
                }
                Long shoId=result.getGoodsSkuOut().get(0).getShopId();
                Long goodsSkuId=result.getGoodsSkuOut().get(0).getId();
                Long goodsSpuId=result.getGoodsSkuOut().get(0).getSpuId();

                OrderItem orderItem=new OrderItem();
                List<OrderItem> orderItemList=new ArrayList<>();
                orderItem.setGoodsSkuId(goodsSkuId);
                orderItem.setGoodsSpuId(goodsSpuId);
                orderItem.setGoodsNum(1);
                orderItem.setOrderItemShopId(shoId);
                orderItemList.add(orderItem);

                OrderInfoInVo orderInfo=new OrderInfoInVo();
                orderInfo.setUserId(userId);
                orderInfo.setUserIp(userIp);
                orderInfo.setUserName(userName);
                orderInfo.setShopId(shoId);
                orderInfo.setActId(inVo.getActId());
                orderInfo.setSourceType(OrderInfo.SOURCE_TYPE_PT);
                orderInfo.setPayType(OrderInfo.PAY_TYPE_MFZS);
                orderInfo.setSingleType(result.getGoodsSkuOut().get(0).getSingleType());
                orderInfo.setFlagType(OrderInfo.FLAG_TYPE_CG);
                orderInfo.setOrderItemList(orderItemList);
                Long id = orderInfoService.createNewForFree(orderInfo);//生成订单(免费赠送)
                if(id>0){
                    actGoodsSkuMapper.updateStockNum(result);//抽中之后活动商品关联表中的参与活动奖品数量减少1
                    actLotteryMapper.updateDownTotalNumber(lotteryInVo);
                    return result;
                }else{
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * 长期抽奖
     * @param inVo
     * @return
     */
    @Override
    public ActGoodsSkuOut aloneLottery(ActGoodsSkuInVo inVo, Long userId, String userName, String userIp) throws ParseException {
        ActLotteryInVo lotteryInVo = new ActLotteryInVo();
        lotteryInVo.setActId(new Long(46));
        lotteryInVo.setUserId(userId);
        ActLottery actLottery= actLotteryMapper.selectUserLottery(lotteryInVo);
        if (actLottery!=null&&actLottery.getId()!=null){
            if (actLottery.getTotalNumber()>=1){
                List<ActGoodsSkuOut> list =actGoodsSkuMapper.selectLotteryList(inVo);
                if(list==null&&list.size()==0){
                    return  null;
                }
                //总的百分比
                double  ratio=0;
                //总的权重
                int  weight=0;
                ActGoodsSkuOut result = new ActGoodsSkuOut();

                //计算总的百分比和权重，有百分比的优先计算百分比，没有百分比的计算权重，权重默认为1
                for(ActGoodsSkuOut actGoodsSkuOut:list){
                    if(actGoodsSkuOut.getRatio()!=null){
                        ratio+=actGoodsSkuOut.getRatio().doubleValue();
                    }else{
                        if(actGoodsSkuOut.getWeight()==null||actGoodsSkuOut.getWeight()==0){
                            weight+=1;//权重默认为1
                        }else{
                            weight+=actGoodsSkuOut.getWeight();
                        }
                    }
                }
                //产生随机数
                double randomNumber;
                randomNumber = Math.random();
                //根据随机数在所有奖品分布的区域并确定所抽奖品
                double d1 = 0;
                double d2 = 0;
                for(int i=0;i<list.size();i++){
                    if(list.get(i).getRatio()!=null){
                        d1=d2;
                        d2+=list.get(i).getRatio().doubleValue();
                    }else{
                        d1=d2;
                        if(list.get(i).getWeight()==null||list.get(i).getWeight()==0){
                            d2+=(1-ratio)*1/weight;
                        }else{
                            d2+=(1-ratio)*list.get(i).getWeight()/weight;
                        }
                    }
                    if(randomNumber>d1&&randomNumber<=d2&&d1!=d2){
                        result=list.get(i);
                        break;
                    }
                }
                if(result==null||result.getId()==null||result.getGoodsSkuOut()==null||result.getGoodsSkuOut().size()<1){
                    return null;
                }
                if (result.getGoodsSkuOut().get(0).getId().toString().equals(new Long(8053).toString())){
                    actLotteryMapper.updateDownTotalNumber(lotteryInVo);
                    return result;
                }
                Long shoId=result.getGoodsSkuOut().get(0).getShopId();
                Long goodsSkuId=result.getGoodsSkuOut().get(0).getId();
                Long goodsSpuId=result.getGoodsSkuOut().get(0).getSpuId();

                OrderItem orderItem=new OrderItem();
                List<OrderItem> orderItemList=new ArrayList<>();
                orderItem.setGoodsSkuId(goodsSkuId);
                orderItem.setGoodsSpuId(goodsSpuId);
                orderItem.setGoodsNum(1);
                orderItem.setOrderItemShopId(shoId);
                orderItemList.add(orderItem);

                OrderInfoInVo orderInfo=new OrderInfoInVo();
                orderInfo.setDefaultUsed(OrderInfo.SDEFAULT_USED_YES);
                orderInfo.setUserId(userId);
                orderInfo.setUserIp(userIp);
                orderInfo.setUserName(userName);
                orderInfo.setShopId(shoId);
                orderInfo.setSourceType(OrderInfo.SOURCE_TYPE_PT);
                orderInfo.setActId(inVo.getActId());
                orderInfo.setPayType(OrderInfo.PAY_TYPE_MFZS);
                orderInfo.setSingleType(result.getGoodsSkuOut().get(0).getSingleType());
                orderInfo.setFlagType(OrderInfo.FLAG_TYPE_CG);
                orderInfo.setOrderItemList(orderItemList);
                Long id = orderInfoService.createNewForFree(orderInfo);//生成订单(免费赠送)
                if(id>0){
                    actGoodsSkuMapper.updateStockNum(result);//抽中之后活动商品关联表中的参与活动奖品数量减少1
                    actLotteryMapper.updateDownTotalNumber(lotteryInVo);
                    return result;
                }else{
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public void distribution(OrderInfo orderInfo) {
        OrderInfoOut detail = orderInfoMapper.getDetail(orderInfo.getId());//查询订单详情
        List<OrderItemOut> orderItemOuts = detail.getOrderItemOuts();//查询订单项详情

        ActOrderOut actOrder= actOrderMapper.selectFirstDistributionByOrderId(orderInfo.getId());
        if(actOrder!=null&&actOrder.getParentId()!=null){
            UserAccountInVo userAccountInVo=new UserAccountInVo();
            userAccountInVo.setUserId(actOrder.getParentId());
            userAccountInVo.setOccurAmount(firstDistribution);
            userAccountInVo.setType(AccountLog.TYPE_USER_REVIEW);
            accountService.updateIncome(userAccountInVo, "参与 "+orderItemOuts.get(0).getGoodsSkuName()+" 待开团",null,actOrder.getOrderId());

            ActOrder actOrder1=actOrderMapper.selectSecondDistributionByOrderId(orderInfo.getId());
            if(actOrder1!=null&&actOrder1.getParentId()!=null){
                UserAccountInVo userAccountInVo1=new UserAccountInVo();
                userAccountInVo1.setUserId(actOrder1.getParentId());
                userAccountInVo1.setOccurAmount(secondDistribution);
                userAccountInVo1.setType(AccountLog.TYPE_USER_REVIEW);
                accountService.updateIncome(userAccountInVo1, "参与 "+orderItemOuts.get(0).getGoodsSkuName()+" 待开团",null,actOrder.getOrderId());
            }
        }
    }

    /**
     * 通过订单查询参加活动的团
     * @param orderInfo
     * @return
     */
    @Override
    public ActGoodsSkuOut getActSkuListAndOrder(OrderInfo orderInfo) {
        Date time = new Date();
        ActGoodsSkuOut actGoodsSkuOut = new ActGoodsSkuOut();
        ActOrderOut actOrder=actOrderMapper.selectFirstDistributionByOrderId(orderInfo.getId());
        if (actOrder==null||actOrder.getId()==null){
            return actGoodsSkuOut;
        }
        ActInfo actInfo = actInfoMapper.selectByPrimaryKey(actOrder.getActGoodsSku().getActId());
        if (actOrder==null||actOrder.getActGoodsSkuId()==null){
            return actGoodsSkuOut;
        }
        actGoodsSkuOut.setActInfo(actInfo);
        ActOrder orderInVo = new ActOrder();
        orderInVo.setParentId(actOrder.getUserId());
        orderInVo.setActGoodsSkuId(actOrder.getActGoodsSkuId());
        List<ActOrder> selectSubordinate=actOrderMapper.selectSubordinate(orderInVo);
        ActGoodsSku actGoodsSku=actGoodsSkuMapper.selectByPrimaryKey(actOrder.getActGoodsSkuId());
        if (actGoodsSku.getState() == ActGoodsSku.STATE_WAIT&&actInfo.getType()==OrderInfo.FLAG_TYPE_TG){
            //判断活动是否结束
            if (time.getTime()>=actGoodsSku.getDueTime().getTime()){
                if (actGoodsSku.getCurrentNum()<actGoodsSku.getPeopleNum()){
                    actGoodsSku.setState(ActGoodsSku.STATE_FAIL);
                    failDistribution(actGoodsSku);//失败修改该团产生的相关奖励金
                }
                //修改超时团状态
                actGoodsSkuMapper.updateOverTimeActStatus(actGoodsSku);
                //修改用户状态
                ActOrder actOrderInVo = new ActOrder();
                BeanUtils.copyProperties(actOrder, actOrderInVo);
                actOrderMapper.updateOverTimeByStatus(actOrderInVo);
            }
            //将修改过后的数据返回
            ActGoodsSku actGoodsSkus=actGoodsSkuMapper.selectByPrimaryKey(actOrder.getActGoodsSkuId());
            BeanUtils.copyProperties(actGoodsSkus, actGoodsSkuOut);
            List<User> userList= new ArrayList<User>();
            for (ActOrder it :selectSubordinate){
                userList.add(userMapper.selectByPrimaryKey(it.getUserId()));
            }
            actGoodsSkuOut.setUsers(userList);
            ActOrderOut actOrders=actOrderMapper.selectFirstDistributionByOrderId(orderInfo.getId());
            actGoodsSkuOut.setActOrder(actOrders);
            return actGoodsSkuOut;
        }
        ActGoodsSku actGoodsSkus=actGoodsSkuMapper.selectByPrimaryKey(actOrder.getActGoodsSkuId());
        BeanUtils.copyProperties(actGoodsSkus, actGoodsSkuOut);
        List<User> userList= new ArrayList<User>();
        for (ActOrder it :selectSubordinate){
            userList.add(userMapper.selectByPrimaryKey(it.getUserId()));
        }
        actGoodsSkuOut.setUsers(userList);
        ActOrderOut actOrders=actOrderMapper.selectFirstDistributionByOrderId(orderInfo.getId());
        actGoodsSkuOut.setActOrder(actOrders);
        return actGoodsSkuOut;
    }

    /**
     *二级分销开团成功
     *修改成功用户订单所产生的奖励金到已获得，并在钱包增加余额,发送微信消息通知
     * 修改失败用户订单所产生的奖励金到已失败，
     * 成功用户发送微信消息和小程序通知
     * 失败用户退款,在退款回调中发送微信消息和小程序通知
     * @param actGoodsSku
     */
    @Override
    public void succesDistribution(ActGoodsSku actGoodsSku) {
        AccountLogInVo accountLogInVo=new AccountLogInVo();
        accountLogInVo.setActGoodsSkuId(actGoodsSku.getId());
        accountLogInVo.setType(AccountLog.TYPE_USER_REVIEW);//待审核的奖励金
        List<AccountLogOut> accountLogOutList=accountLogMapper.list(accountLogInVo);//查询审核中的奖励金
        //修改成功用户订单所产生的奖励金到已获得，并在钱包增加余额,发送微信消息通知,  修改失败用户订单所产生的奖励金到已失败
        if(accountLogOutList.size()>0){
            for(AccountLogOut accountLogOut:accountLogOutList){
                if(accountLogOut.getOrderId()!=null&&accountLogOut.getActGoodsSkuState()==ActGoodsSku.STATE_SUCCESS
                        &&accountLogOut.getActOrderState()==ActOrder.ACT_ORDER_GROUP_STATE_TUXEDO_SUCCESS){//参团成功的用户订单日志

                    //增加用户余额
                    OrderInfoOut detail = orderInfoMapper.getDetail(accountLogOut.getOrderId());//查询订单详情
                    List<OrderItemOut> orderItemOuts = detail.getOrderItemOuts();//查询订单项详情
                    UserAccountInVo userAccountInVo=new UserAccountInVo();
                    userAccountInVo.setUserId(accountLogOut.getUserId());
                    userAccountInVo.setOccurAmount(accountLogOut.getOperateAmount());
                    userAccountInVo.setType(AccountLog.TYPE_USER);
                    accountService.updateIncome(userAccountInVo, "邀请参团奖励",null,accountLogOut.getOrderId());

                    //查询用户账户信息
                    UserAccount userAccount =  userAccountMapper.findAccountByUserId(userAccountInVo.getUserId());
                    userAccountInVo.setVersionNo(userAccount.getVersionNo());  //版本号，作为更新的乐观锁条件

                    //增加已获得奖励金余额
                    userAccountInVo.setType(AccountLog.TYPE_USER_PASSED);
                    userAccountMapper.updateIncome(userAccountInVo);

                    //减少审核中的奖励金余额
                    userAccount =  userAccountMapper.findAccountByUserId(userAccountInVo.getUserId());
                    userAccountInVo.setVersionNo(userAccount.getVersionNo());  //版本号，作为更新的乐观锁条件
                    userAccountInVo.setType(AccountLog.TYPE_USER_REVIEW);
                    userAccountMapper.updatePayout(userAccountInVo);

                    //修改待审核奖励金日志为已获得
                    AccountLog accountLog=new AccountLog();
                    accountLog.setType(AccountLog.TYPE_USER_PASSED);
                    accountLog.setId(accountLogOut.getId());
                    accountLog.setRemark("参与 "+orderItemOuts.get(0).getGoodsSkuName()+" 开团成功");
                    accountLogMapper.updateByPrimaryKeySelective(accountLog);
                }else if(accountLogOut.getOrderId()!=null&&accountLogOut.getActGoodsSkuState()==ActGoodsSku.STATE_SUCCESS&&
                        (accountLogOut.getActOrderState()==ActOrder.ACT_ORDER_GROUP_STATE_TUXEDO_JOINING||accountLogOut.getActOrderState()==ActOrder.ACT_ORDER_GROUP_STATE_TUXEDO_END)){//参团失败的用户订单日志
                    OrderInfoOut detail = orderInfoMapper.getDetail(accountLogOut.getOrderId());//查询订单详情
                    List<OrderItemOut> orderItemOuts = detail.getOrderItemOuts();//查询订单项详情

                    UserAccountInVo userAccountInVo=new UserAccountInVo();
                    userAccountInVo.setUserId(accountLogOut.getUserId());
                    userAccountInVo.setOccurAmount(accountLogOut.getOperateAmount());
                    //查询用户账户信息
                    UserAccount userAccount =  userAccountMapper.findAccountByUserId(userAccountInVo.getUserId());
                    userAccountInVo.setVersionNo(userAccount.getVersionNo());  //版本号，作为更新的乐观锁条件

                    //增加获取失败的奖励金余额
                    userAccountInVo.setType(AccountLog.TYPE_USER_FAIL);
                    userAccountMapper.updateIncome(userAccountInVo);

                    //减少审核中的奖励金余额
                    userAccount =  userAccountMapper.findAccountByUserId(userAccountInVo.getUserId());
                    userAccountInVo.setVersionNo(userAccount.getVersionNo());  //版本号，作为更新的乐观锁条件
                    userAccountInVo.setType(AccountLog.TYPE_USER_REVIEW);
                    userAccountMapper.updatePayout(userAccountInVo);

                    //修改待审核奖励金日志为已获得
                    AccountLog accountLog=new AccountLog();
                    accountLog.setType(AccountLog.TYPE_USER_FAIL);
                    accountLog.setId(accountLogOut.getId());
                    accountLog.setRemark("参与 "+orderItemOuts.get(0).getGoodsSkuName()+" 开团失败");
                    accountLogMapper.updateByPrimaryKeySelective(accountLog);
                }
            }
        }

        //成功用户发送微信消息和小程序通知,失败用户退款,在退款回调中发送微信消息和小程序通知
        List<ActOrderOut> actOrderOutList=actOrderMapper.selectActOrderByActGoodsSkuId(actGoodsSku.getId());
        if(actOrderOutList.size()>0){
            for(ActOrderOut actOrderOut:actOrderOutList){
                if(actOrderOut.getOrderId()!=null&&actOrderOut.getState()==ActOrder.ACT_ORDER_GROUP_STATE_TUXEDO_SUCCESS){//参团成功
                    OrderInfoOut detail = orderInfoMapper.getDetail(actOrderOut.getOrderId());//查询订单详情
                    List<OrderItemOut> orderItemOuts = detail.getOrderItemOuts();//查询订单项详情
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date=new Date();
                    String str=sdf.format(date);
                    String keyWords="享七拼购,"+orderItemOuts.get(0).getGoodsSkuName()+","+str+","+"拼团成功啦，可在小程序我的票卷中查看";
                    try{
                        Integer res =weiXinPushService.pushByUserId(WeiXinTeamplateMsg.templateId_TYPE_INTEGRATE_PURCHASE, "",keyWords,actOrderOut.getUserId() );//活动名称+商户名称+成团时间+友情提示 此处友情提示的逗号为中文逗号
                    }catch (Exception e){

                    }
                    //完成之后发送消息到小程序的消息列表
                    messageService.addMessage("拼购成功", "您的 "+orderItemOuts.get(0).getGoodsSkuName()+" 拼购成功。", MessageText.MESSAGE_TEXT_TYPE_PRIVATE, actOrderOut.getUserId(), actOrderOut.getUserId());

                }else if(actOrderOut.getOrderId()!=null&&(actOrderOut.getState()==ActOrder.ACT_ORDER_GROUP_STATE_TUXEDO_JOINING||
                        actOrderOut.getState()==ActOrder.ACT_ORDER_GROUP_STATE_TUXEDO_END)&&
                        actOrderOut.getOrderInfo().getStatus()== OrderInfo.STATUS_IS_SUCCESS){//参团失败,而且订单为已完成状态
                    //退款发kafka消息，kafka中做退款处理
                    Boolean topic = kafkaService.sendDataToTopic("payRefundTopic",  "payRefundTopic", String.valueOf(actOrderOut.getOrderId()));
                }
            }
        }
    }

    /**
     *二级分销开团失败
     * 修改失败用户订单所产生的奖励金到已失败
     * @param actGoodsSku
     */
    @Override
    public void failDistribution(ActGoodsSku actGoodsSku) {
        AccountLogInVo accountLogInVo=new AccountLogInVo();
        accountLogInVo.setActGoodsSkuId(actGoodsSku.getId());
        accountLogInVo.setType(AccountLog.TYPE_USER_REVIEW);//待审核的奖励金
        List<AccountLogOut> accountLogOutList=accountLogMapper.list(accountLogInVo);//查询审核中的奖励金
        if(accountLogOutList.size()>0){
            for(AccountLogOut accountLogOut:accountLogOutList){
                if(accountLogOut.getOrderId()!=null){
                    OrderInfoOut detail = orderInfoMapper.getDetail(accountLogOut.getOrderId());//查询订单详情
                    List<OrderItemOut> orderItemOuts = detail.getOrderItemOuts();//查询订单项详情

                    UserAccountInVo userAccountInVo=new UserAccountInVo();
                    userAccountInVo.setUserId(accountLogOut.getUserId());
                    userAccountInVo.setOccurAmount(accountLogOut.getOperateAmount());
                    //查询用户账户信息
                    UserAccount userAccount =  userAccountMapper.findAccountByUserId(userAccountInVo.getUserId());
                    userAccountInVo.setVersionNo(userAccount.getVersionNo());  //版本号，作为更新的乐观锁条件

                    //减少审核中的奖励金余额
                    userAccountInVo.setType(AccountLog.TYPE_USER_REVIEW);
                    userAccountMapper.updatePayout(userAccountInVo);

                    //增加获取失败的奖励金余额
                    userAccount =  userAccountMapper.findAccountByUserId(userAccountInVo.getUserId());
                    userAccountInVo.setVersionNo(userAccount.getVersionNo());  //版本号，作为更新的乐观锁条件
                    userAccountInVo.setType(AccountLog.TYPE_USER_FAIL);
                    userAccountMapper.updateIncome(userAccountInVo);

                    //修改待审核奖励金日志为已获得
                    AccountLog accountLog=new AccountLog();
                    accountLog.setType(AccountLog.TYPE_USER_FAIL);
                    accountLog.setId(accountLogOut.getId());
                    accountLog.setRemark("参与 "+orderItemOuts.get(0).getGoodsSkuName()+" 开团失败");
                    accountLogMapper.updateByPrimaryKeySelective(accountLog);
                }
            }
        }

        //失败用户退款,在退款回调中发送微信消息和小程序通知
        List<ActOrderOut> actOrderOutList=actOrderMapper.selectActOrderByActGoodsSkuId(actGoodsSku.getId());
        if(actOrderOutList.size()>0){
            for(ActOrderOut actOrderOut:actOrderOutList){
                if(actOrderOut.getOrderId()!=null&&(actOrderOut.getState()==ActOrder.ACT_ORDER_GROUP_STATE_TUXEDO_JOINING||
                        actOrderOut.getState()==ActOrder.ACT_ORDER_GROUP_STATE_TUXEDO_END||
                        actOrderOut.getState()==ActOrder.ACT_ORDER_GROUP_STATE_TUXEDO_SUCCESS)&&
                        actOrderOut.getOrderInfo().getStatus()== OrderInfo.STATUS_IS_SUCCESS){//参团失败,而且订单为已完成状态

                    //退款发kafka消息，kafka中做退款处理
                    Boolean topic = kafkaService.sendDataToTopic("payRefundTopic",  "payRefundTopic", String.valueOf(actOrderOut.getOrderId()));
                }
            }
        }
    }

    @Override
    public Integer updateSku() {
        List<ActGoodsSku> actGoodsSkus = actGoodsSkuMapper.selectList();
        Random random = new Random();
        for (ActGoodsSku actGoodsSku : actGoodsSkus) {
            actGoodsSku.setSortNum(random.nextInt(100));
            actGoodsSkuMapper.updateByPrimaryKeySelective(actGoodsSku);
        }
        return null;
    }

    @Override
    public Long add(ActGoodsSkuInVo inVo) {
        GoodsSkuOut goodsSkuOut = goodsSkuMapper.selectDetailBySkuId(inVo.getSkuId());
        if(goodsSkuOut==null){
            return null;
        }
        ActInfoOut actInfoOut = actInfoMapper.findActInfoById(inVo.getActId());
        if(actInfoOut==null){
            return null;
        }

        int index = actGoodsSkuMapper.countByActId(inVo.getActId());
        /*DecimalFormat mFormat = new DecimalFormat("000");//确定格式，把1转换为001
        String s = mFormat.format(index+1);*/
        String goodsSkuNumber =(index + 1)+"";
        GoodsPromotionRules goodsPromotionRules = inVo.getGoodsPromotionRules();
        //添加goodsPromotionRules,返回规则id
        Long goodsPromotionRulesId = this.insertGoodsPromotionRules(goodsPromotionRules, goodsSkuOut, actInfoOut);
        ActGoodsSku actGoodsSku = new ActGoodsSku();
        BeanUtils.copyProperties(inVo, actGoodsSku);
        actGoodsSku.setSkuId(goodsSkuOut.getId());
        actGoodsSku.setSkuCode(goodsSkuOut.getSkuCode());
        actGoodsSku.setCategoryId(goodsSkuOut.getCategoryId());
        actGoodsSku.setGoodsPrId(goodsPromotionRulesId);
        actGoodsSku.setApplyStatus(ActGoodsSku.APPLY_STATUS_SUCCESS);
        actGoodsSku.setState(ActGoodsSku.STATE_WAIT);
        actGoodsSku.setSortNum(1);
        actGoodsSku.setPeopleNum(actInfoOut.getPeopleNum());
        actGoodsSku.setState(ActGoodsSku.STATE_WAIT);
        actGoodsSku.setGoodsSkuNumber(goodsSkuNumber);
        int actGoodsSkuInsert = actGoodsSkuMapper.insert(actGoodsSku);
        if (actGoodsSkuInsert < 1) {
            logger.error("添加act_goods_sku失败");
            throw new RuntimeException("添加act_goods_sku失败");
        }
        return actGoodsSku.getId();
    }

    /**
     * 插入goodsPromotionRules参数
     * @param goodsPromotionRules
     * @param goodsSkuOut
     * @param actInfoOut
     */
    private Long insertGoodsPromotionRules(GoodsPromotionRules goodsPromotionRules,GoodsSkuOut goodsSkuOut,ActInfoOut actInfoOut){
        goodsPromotionRules.setGoodsSkuId(goodsSkuOut.getId());
        goodsPromotionRules.setGoodsSkuCode(goodsSkuOut.getSkuCode());
        goodsPromotionRules.setGoodsSkuName(goodsSkuOut.getSkuName());
        goodsPromotionRules.setRuleType(actInfoOut.getType());
        if(actInfoOut.getType()==GoodsPromotionRules.RULE_TYPE_KJ){
            goodsPromotionRules.setRuleDesc("邀请好友砍价");
        }else if(actInfoOut.getType()==GoodsPromotionRules.RULE_TYPE_MS){
            goodsPromotionRules.setRuleDesc("邀请好友秒杀");
        }else if(actInfoOut.getType()==GoodsPromotionRules.RULE_TYPE_TG){
            goodsPromotionRules.setRuleDesc("邀请好友拼团");
        }
        int goodsPromotionRulesInsert = goodsPromotionRulesMapper.insert(goodsPromotionRules);
        if(goodsPromotionRulesInsert<1){
            logger.error("添加goods_promotion_rules失败");
            throw new RuntimeException("添加goods_promotion_rules失败");
        }
        return goodsPromotionRules.getId();
    }

    @Override
    public ActGoodsSku selectByPrimaryKey(Long id) {
        return actGoodsSkuMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateVoteNumUp(ActGoodsSkuInVo inVo) {
        return actGoodsSkuMapper.updateVoteNumUp(inVo);
    }

    @Override
    public Pager<ActLotteryCategory> ActLotteryList(ActLotteryCategoryInVo inVo) {
        Pager<ActLotteryCategory> result = new Pager<ActLotteryCategory>();
        Integer listTotal = actLotteryCategoryMapper.listTotal(inVo);
        result.setTotal(listTotal);
        if (listTotal > 0) {
            List<ActLotteryCategory> list = actLotteryCategoryMapper.list(inVo);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public List<ActGoodsSkuOut> selectActByGoodsSkuId(ActGoodsSkuInVo actGoodsSkuInVo){
        List<ActGoodsSkuOut> actGoodsSkuOuts = actGoodsSkuMapper.selectActInfoListByGoodsSkuId(actGoodsSkuInVo);
        return actGoodsSkuOuts;
    }

    @Override
    public List<ActGoodsSkuRecommendOut> selectActInfo(Integer actId, Integer goodsSkuId) {
        return actGoodsSkuMapper.selectActInfo(actId, goodsSkuId);
    }

    @Override
    public Pager<ActGoodsSkuOut> selectGoodsList(ActGoodsSkuInVo actGoodsSkuInVo) {
        Pager<ActGoodsSkuOut> result = new Pager<ActGoodsSkuOut>();
        Integer listTotal = actGoodsSkuMapper.selectGoodsTotal(actGoodsSkuInVo);
        result.setTotal(listTotal);
        if (listTotal > 0) {
            List<ActGoodsSkuOut> list = actGoodsSkuMapper.selectGoodsList(actGoodsSkuInVo);
            result.setList(list);
        }
        result.setPage(actGoodsSkuInVo.getPage());
        return result;
    }

    @Override
    public Integer addStockNum(ActGoodsSkuInVo actGoodsSkuInVo) {
        actGoodsSkuInVo.setStockNumType(ActGoodsSku.STOCK_NUM_TYPE_ADD);
        Integer re= actGoodsSkuMapper.updateCutDownStockNum(actGoodsSkuInVo);
        return re;
    }
}
