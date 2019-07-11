package com.xq.live.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.xq.live.common.*;
import com.xq.live.config.ConstantsConfig;
import com.xq.live.config.CostWeightConfig;
import com.xq.live.config.WeiXinTeamplateMsg;
import com.xq.live.dao.*;
import com.xq.live.model.*;
import com.xq.live.service.*;
import com.xq.live.vo.in.*;
import com.xq.live.vo.out.*;
import com.xq.live.web.utils.CutOutTimeUtils;
import com.xq.live.web.utils.GtPush;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.javatuples.Triplet;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 商城系统订单相关serviceImpl
 * Created by lipeng on 2018/9/5.
 */
@Service
public class OrderInfoServiceImpl implements OrderInfoService{
    private Logger logger = Logger.getLogger(OrderInfoServiceImpl.class);

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private ShopService shopService;

    @Autowired
    private OrderDeliveryMapper orderDeliveryMapper;

    @Autowired
    private ActGoodsSkuMapper actGoodsSkuMapper;

    @Autowired
    private OrderAddressMapper orderAddressMapper;

    @Autowired
    private GoodsPromotionRulesMapper goodsPromotionRulesMapper;

    @Autowired
    private ActInfoMapper actInfoMapper;

    @Autowired
    private GoodsSkuMapper goodsSkuMapper;

    @Autowired
    private ActOrderMapper actOrderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderLogMapper orderLogMapper;

    @Autowired
    private PaidLogMapper paidLogMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderCouponMapper orderCouponMapper;

    @Autowired
    private CostWeightConfig costWeightConfig;

    @Autowired
    private DeliveryCostService deliveryCostService;

    @Autowired
    private SalePointMapper salePointMapper;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private ConstantsConfig constantsConfig;

    @Autowired
    private GoodsGoldLogService goodsGoldLogService;

    @Autowired
    private GoodsSpuMapper goodsSpuMapper;

    @Autowired
    private ServiceConfigMapper serviceConfigMapper;

    @Autowired
    private OrderInvoiceConfigMapper orderInvoiceConfigMapper;

    @Autowired
    private PayRefundConfigMapper payRefundConfigMapper;

    @Autowired
    private OrderCouponExpiryConfigMapper orderCouponExpiryConfigMapper;

    @Autowired
    private ShopAllocationMapper shopAllocationMapper;

    @Autowired
    private ShopCashierMapper shopCashierMapper;

    @Autowired
    private OrderWriteOffMapper orderWriteOffMapper;

    @Autowired
    private OrderInvoiceMapper orderInvoiceMapper;

    @Autowired
    private SalePointGoodsMapper salePointGoodsMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private KafkaService kafkaService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ActGoodsSkuService actGoodsSkuService;

    @Autowired
    private WeiXinPushService weiXinPushService;

    @Autowired
    private PullUserService pullUserService;

    @Autowired
    private OrderCouponService orderCouponService;

    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;

    private WXPay wxpay;
    private PaymentConfig config;

    public OrderInfoServiceImpl() throws Exception {
        config = PaymentConfig.getInstance();
        wxpay = new WXPay(config, WXPayConstants.SignType.MD5);
    }

    @Override
    public OrderInfo get(Long orderId) {
        return orderInfoMapper.selectByPrimaryKey(orderId);
    }

    @Override
    public OrderInfoOut getDetail(Long id) {
        OrderInfoOut detail = orderInfoMapper.getDetail(id);
        if(detail==null){
            return null;
        }
        this.setOrderAddressAndExpress(detail);//设置收货信息和快递单号
        this.setShopName(detail);//设置商家名字
        this.setSalepoint(detail);//设置销售点
        this.setActOrderOut(detail);//设置订单与活动关联的详情
        //添加运费
        //detail.setDeliveryFee(costWeightConfig.getDeliveryFee());
        //拆单了的父订单里面的sendType为null,不拆单的生成的父订单里面的sendType不为null
        /*//查询拆单之后,父订单对应的子订单
        if(detail.getSendType()==null){
            //查询父订单里面所有子订单
            List<OrderInfoOut> orderInfoOuts = this.getSonOrderList(detail);
            detail.setOrderInfoOuts(orderInfoOuts);
        }*/
        return detail;
    }

    @Override
    public OrderInfoOut getDetailNew(Long id) {
        OrderInfoOut detail = orderInfoMapper.getDetail(id);
        if(detail==null){
            return null;
        }

        if(detail.getStatus()== OrderInfo.STATUS_WAIT_PAID||detail.getStatus()== OrderInfo.STATUS_QX){
            Boolean hasPaid= getPayStatus(detail);//手动调用微信接口查询一次订单状态，
            if(hasPaid){//更新支付状态之后重新查询
                detail = orderInfoMapper.getDetail(id);
            }
        }

        List<OrderItemOut> orderItemOuts = detail.getOrderItemOuts();//查询订单项详情
        //后期此地方有购物车之后要改进
        OrderItemOut orderItemOut = orderItemOuts.get(0);//查询第一个订单项详情,虚拟订单没有购物车概念
        //2、查询商品信息
        GoodsSku sku = goodsSkuMapper.selectByPrimaryKey(orderItemOut.getGoodsSkuId());
        GoodsSpu goodsSpu = goodsSpuMapper.selectByPrimaryKey(sku.getSpuId());//查询category_id

        this.setOrderAddressAndExpress(detail);//设置收货信息和快递单号
        this.setShopName(detail);//设置商家名字
        this.setSalepoint(detail);//设置销售点
        this.setActOrderOut(detail);//设置订单与活动关联的详情
        this.setIsOrderInvoice(detail, goodsSpu, sku);//设置是否能够申请开发票
        this.setIsPayRefund(detail, goodsSpu, sku);//设置是否能够申请退款
        this.setOrderCouponIds(detail);//设置订单所对应的票券id列表
        //添加运费
        //detail.setDeliveryFee(costWeightConfig.getDeliveryFee());
        //拆单了的父订单里面的sendType为null,不拆单的生成的父订单里面的sendType不为null
        /*//查询拆单之后,父订单对应的子订单
        if(detail.getSendType()==null){
            //查询父订单里面所有子订单
            List<OrderInfoOut> orderInfoOuts = this.getSonOrderList(detail);
            detail.setOrderInfoOuts(orderInfoOuts);
        }*/
        return detail;
    }

    /**
     * 设置订单所对应的票券id列表
     * @param detail
     */
    public void setOrderCouponIds(OrderInfoOut detail){
        List<OrderCoupon> orderCoupons = orderCouponMapper.listByOrderId(detail.getId());
        detail.setOrderCoupons(orderCoupons);
    }

    /**
     * 设置是否能够开发票
     * @param detail
     */
    public void setIsOrderInvoice(OrderInfoOut detail,GoodsSpu goodsSpu,GoodsSku sku){
        OrderInvoice inVo = new OrderInvoice();
        inVo.setOrderCode(detail.getOrderCode());
        OrderInvoice orderInvoice = orderInvoiceMapper.selectByOrderCode(inVo);
        List<OrderInvoiceConfig> orderInvoiceConfigs = this.getOrderInvoiceConfigs(detail, goodsSpu, sku);
        List<OrderInvoiceConfig> listServiceConfig = new ArrayList<OrderInvoiceConfig>();
        if(orderInvoiceConfigs!=null&&orderInvoiceConfigs.size()>0) {
            //查询开发票配置
            listServiceConfig = orderInvoiceConfigMapper.listByRefIdAndType(orderInvoiceConfigs);
        }
        if((listServiceConfig!=null&&listServiceConfig.size()>0)
                ||detail.getPayType()==OrderInfo.PAY_TYPE_MFZS){
            detail.setIsOrderInvoice(1);//不能开发票
            return;
        }
        if(detail.getStatus()!=null&&detail.getStatus()!=OrderInfo.STATUS_WAIT_SH
                &&detail.getStatus()!=OrderInfo.STATUS_IS_SUCCESS){
            detail.setIsOrderInvoice(1);//不能开发票
            return;
        }
        if(orderInvoice==null){
            detail.setIsOrderInvoice(0);//能开发票
            return;
        }
        Integer applyStatus = orderInvoice.getApplyStatus();
        if(applyStatus==OrderInvoice.APPLY_STATUS_FAIL){
            detail.setIsOrderInvoice(0);
            return;
        }
        detail.setIsOrderInvoice(1);//不能开发票
        return;
    }

    /**
     * 设置是否能退款
     * @param detail
     */
    public void setIsPayRefund(OrderInfoOut detail,GoodsSpu goodsSpu,GoodsSku sku){
        Integer isPayRefund=0;//默认可以退款
        //待收货和已完成才能退款
        if(detail.getStatus()!=OrderInfo.STATUS_WAIT_SH&&detail.getStatus()!=OrderInfo.STATUS_IS_SUCCESS){
            isPayRefund=1;
        }

        List<PayRefundConfig> payRefundConfigs = this.getPayRefundConfigs(detail, goodsSpu, sku);
        List<PayRefundConfig> listServiceConfig = new ArrayList<PayRefundConfig>();
        if(payRefundConfigs!=null&&payRefundConfigs.size()>0) {
            //查询退款配置
            listServiceConfig = payRefundConfigMapper.listByRefIdAndType(payRefundConfigs);
        }
        if((listServiceConfig!=null&&listServiceConfig.size()>0)
                ||detail.getPayType()==OrderInfo.PAY_TYPE_MFZS){
                isPayRefund=1;
        }


        List<OrderCoupon> orderCouponlist=orderCouponMapper.listOrderCouponByOrdercode(detail.getOrderCode());
        if(orderCouponlist!=null&&orderCouponlist.size()>0){
            for(OrderCoupon orderCoupon :orderCouponlist){
                //判断是否有票卷在退款中或者已退款
                if(orderCoupon.getStatus()!=null&&(orderCoupon.getStatus()==OrderCoupon.STATUS_REFUND_APPLICATION||orderCoupon.getStatus()==OrderCoupon.STATUS_REFUND)){
                    isPayRefund=1;
                    break;
                }

                //有票卷已使用不允许退款
                if(orderCoupon.getIsUsed()==OrderCoupon.ORDER_COUPON_IS_USED_YES){
                    isPayRefund=1;
                    break;
                }

                //有票卷被赠送了之后不允许退款
                if(!String.valueOf(orderCoupon.getUserId()).equals(String.valueOf(orderCoupon.getOwnId()))){
                    isPayRefund=1;
                    break;
                }

                if(orderCoupon.getExpiryDate().before(new Date())){
                    isPayRefund=1;
                    break;
                }
            }
        }
        detail.setIsPayRefund(isPayRefund);
    }

    /**
     * 主动查询订单的支付状态
     * 后面要将此方法迁到微信的统一service中
     * @param detail
     * @return
     */
    public Boolean getPayStatus(OrderInfoOut detail){
        Boolean result=false;
        String nonce_str = WXPayUtil.generateNonceStr();
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("appid",config.getAppID());
        data.put("mch_id",config.getMchID());
        data.put("nonce_str",nonce_str);
        data.put("out_trade_no", detail.getOrderCode());
        try{
            Map<String, String> rMap=new HashMap<String, String>();

            String sign = WXPayUtil.generateSignature(data, config.API_KEY);
            data.put("sign",sign);
            rMap = wxpay.orderQuery(data);
            String return_code = rMap.get("return_code");//返回状态码

            if ("SUCCESS".equals(return_code)){
                String result_code = rMap.get("result_code");//业务结果
                if( "SUCCESS".equals(result_code)){
                    String trade_state=rMap.get("trade_state");//交易状态
                    if("SUCCESS".equals(trade_state)){
                        String orderCode = (String) rMap.get("out_trade_no"); //商户订单号
                        String attach = (String) rMap.get("attach");//商家订单中对应的attach，其中包含有couponId和shopId，可以通过此来完成对账
                        String total_fee = (String) rMap.get("total_fee");
                        WeixinInVo attachInVo = JSON.parseObject(attach, WeixinInVo.class);//将附带参数读取出来
                        OrderInfo orderOut = getByCode(orderCode);
                        if (OrderInfo.STATUS_WAIT_PAID == orderOut.getStatus()||OrderInfo.STATUS_QX== orderOut.getStatus()) {//支付的状态判断
                            //订单状态的修改。根据实际业务逻辑执行
                            OrderInfoInVo inVo = new OrderInfoInVo();
                            BeanUtils.copyProperties(orderOut, inVo);
                            User userById = userService.getUserById(inVo.getUserId());
                            if(userById!=null){
                                inVo.setUserName(userById.getUserName());
                            }
                            int ret = paidCouponNew(inVo, attachInVo);
                            if( ret>0){
                                pushOrderInfo(orderOut,total_fee);//支付成功之后给用户发送微信消息
                                actGoodsSkuService.distribution(orderOut);//二级分销给上级用户加钱
                            }
                            result= true;
                        }
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return  result;
    }

    /**
     * 订单支付成功之后微信消息推送
     * @param orderOut
     */
    public void pushOrderInfo(OrderInfo orderOut,String totalFee)throws Exception{
        OrderInfoOut detail = orderInfoMapper.getDetail(orderOut.getId());//查询订单详情
        List<OrderItemOut> orderItemOuts = detail.getOrderItemOuts();//查询订单项详情
        Long shopId= orderItemOuts.get(0).getShopId();
        ShopOut shopOut=shopMapper.findShopOutById(shopId);
        String shopName="享七自营";
        if(shopOut!=null&&shopOut.getShopName()!=null){
            shopName=shopOut.getShopName();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date=new java.util.Date();
        String str=sdf.format(date);
        String keyWords=shopName+","+new BigDecimal(totalFee).divide(new BigDecimal(100))+","+str+","+orderOut.getOrderCode()+",你购买的商品已支付成功，查看详情了解更多信息";//商户名称+支付金额+支付时间+交易单号+说明 此处说明的逗号为中文逗号
        Integer res =weiXinPushService.pushByUserId(WeiXinTeamplateMsg.templateId_TYPE_PAY, "",keyWords,orderOut.getUserId() );

        //完成之后发送消息到小程序的消息列表
        messageService.addMessage("付款成功", "您的 "+orderItemOuts.get(0).getGoodsSkuName()+" 购买成功。", MessageText.MESSAGE_TEXT_TYPE_PRIVATE, orderOut.getUserId(), orderOut.getUserId());
    }

    @Override
    public OrderInfoOut getDetailByOrderCode(String orderCode) {
        OrderInfoOut detail = orderInfoMapper.getDetailByOrderCode(orderCode);
        this.setSalepoint(detail);//设置销售点
        //拆单了的父订单里面的sendType为null,不拆单的生成的父订单里面的sendType不为null
        /*//查询拆单之后,父订单对应的子订单
        if(detail.getSendType()==null){
            //查询父订单里面所有子订单
            List<OrderInfoOut> orderInfoOuts = this.getSonOrderList(detail);
            detail.setOrderInfoOuts(orderInfoOuts);
        }*/
        return detail;
    }


    public void setActOrderOut(OrderInfoOut detail){
        ActOrderOut actOrderOut = actOrderMapper.selectFirstDistributionByOrderId(detail.getId());
        detail.setActOrderOut(actOrderOut);
    }

    private void setOrderAddressAndExpress(OrderInfoOut detail) {
        OrderDelivery orderDelivery = orderDeliveryMapper.selectByOrderCode(detail.getOrderCode());//查询快递单号
        OrderAddressOut address = orderAddressMapper.getAddress(detail.getOrderAddressId());//查询收货的信息
        if(orderDelivery!=null){
            detail.setExpressCode(orderDelivery.getExpressCode());//设置快递单号
        }
        detail.setOrderAddressOut(address);//设置收货的信息
    }

    private void setSalepoint(OrderInfoOut detail) {
        SalePoint salePoint = salePointMapper.selectByPrimaryKey(detail.getSalepointId());
        detail.setSalePoint(salePoint);
    }

    public void setShopName(OrderInfoOut detail){
        if(detail.getShopId()==null){
            detail.setShopName("享七商城");
        }else if(detail.getShopId().toString().equals("0")){
            detail.setShopName("享七自营");
        }else{
            Shop shop = shopMapper.selectByPrimaryKey(detail.getShopId());
            if(shop!=null){
                detail.setShopName(shop.getShopName());
            }
        }
    }

    @Override
    public Pager<OrderInfoOut> list(OrderInfoInVo inVo) {
        Pager<OrderInfoOut> result = new Pager<OrderInfoOut>();
        int listTotal = orderInfoMapper.listTotal(inVo);
        if(listTotal > 0){
            List<OrderInfoOut> list = orderInfoMapper.list(inVo);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        result.setTotal(listTotal);
        return result;
    }

    @Override
    public Pager<OrderInfoOut> listFree(OrderInfoInVo inVo) {
        Pager<OrderInfoOut> result = new Pager<OrderInfoOut>();
        int listTotal = orderInfoMapper.listTotalFree(inVo);
        if(listTotal > 0){
            List<OrderInfoOut> list = orderInfoMapper.listFree(inVo);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        result.setTotal(listTotal);
        return result;
    }

    @Override
    public Pager<OrderInfoOut> listAll(OrderInfoInVo inVo) {
        Pager<OrderInfoOut> result = new Pager<OrderInfoOut>();
        int listTotal = orderInfoMapper.listTotal(inVo);
        if(listTotal > 0){
            List<OrderInfoOut> list = orderInfoMapper.list(inVo);
            //拆单了的父订单里面的sendType为null,不拆单的生成的父订单里面的sendType不为null
            //查询拆单之后,父订单对应的子订单
            for (OrderInfoOut orderInfoOut : list) {
               if(orderInfoOut.getSendType()==null){
                   //查询父订单里面所有子订单
                   List<OrderInfoOut> orderInfoOuts = this.getSonOrderList(orderInfoOut);
                   orderInfoOut.setOrderInfoOuts(orderInfoOuts);
               }
            }
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        result.setTotal(listTotal);
        return result;
    }

    @Override
    @Transactional
    public Long create(OrderInfoInVo inVo) {
        List<OrderItem> orderItemList = inVo.getOrderItemList();//获取订单里面的详细内容

        BigDecimal orderAmount = BigDecimal.ZERO;//实付款金额
        BigDecimal skuAllAmount = BigDecimal.ZERO;//商品总额(单纯的数量*单价)
        BigDecimal sendAllAmount = BigDecimal.ZERO;//总运费
        for (OrderItem orderItem : orderItemList) {
            GoodsSkuOut goodsSku = goodsSkuMapper.selectDetailBySkuId(orderItem.getGoodsSkuId());//查询商品详情
            //查询商品是否存在，是否下架
            if(goodsSku==null||goodsSku.getIsDeleted()==GoodsSku.GOODS_SKU_IS_DELETED
                    ||goodsSku.getStatus()==GoodsSku.STATUS_XJ){
                return null;
            }
            //判断实际库存数量是否小于购买的数量
            if(goodsSku.getStockNum()<orderItem.getGoodsNum()){
                return null;
            }
            //判断购买的数量是否大于最小要求购买量
            if(goodsSku.getMiniNum()>orderItem.getGoodsNum()){
                return null;
            }
            BigDecimal goodsAmount = BigDecimal.ZERO;
            //计算用户某个订单项的商品总额
            goodsAmount = this.subGoodsSkuAmount(inVo,goodsSku,orderItem);

            skuAllAmount = skuAllAmount.add(goodsAmount);
            //目前这种模式没办法考虑同种规则类型，不同规则定义的标准(比如满10盒送一盒同时又满20盒送3盒)
            List<GoodsPromotionRules> goodsPromotionRules = goodsSku.getGoodsPromotionRules();
            Collections.sort(goodsPromotionRules);//按照ruleType从小到大排序
            OrderItem item = new OrderItem();
            for (GoodsPromotionRules goodsPromotionRule : goodsPromotionRules) {
                if(goodsPromotionRule.getRuleType()==GoodsPromotionRules.RULE_TYPE_ADD){
                    double floor = Math.floor((double)orderItem.getGoodsNum() / goodsPromotionRule.getManNum())
                            *goodsPromotionRule.getGiftNum();

                    item.setGiftNum((int) floor);
                }
                if(goodsPromotionRule.getRuleType()==GoodsPromotionRules.RULE_TYPE_PACKING){
                    int giftNum = item.getGiftNum() == null ? 0 : item.getGiftNum();
                    double amount = Math.ceil((double)(orderItem.getGoodsNum()+giftNum) / goodsPromotionRule.getManNum());
                    double floor = amount*goodsPromotionRule.getGiftNum();
                    BigDecimal bigDecimal = BigDecimal.valueOf(floor).setScale(2);
                    goodsAmount = goodsAmount.add(bigDecimal);
                }
            }
            BigDecimal sendAmount = BigDecimal.ZERO;
            //通过传不传地址，来判断是否计算运费
            if(inVo.getOrderAddressId()!=null&&inVo.getSendType()==OrderInfo.SEND_TYPE_PTYG) {
                int giftNum = item.getGiftNum() == null ? 0 : item.getGiftNum();
                //计算理论重量
                Double weight = (orderItem.getGoodsNum() + giftNum) * goodsSku.getRealWeight().doubleValue();
                //计算实际重量
                BigDecimal realWeight = this.subRealWeight(weight);

                //查询收货地址信息
                OrderAddressOut address = orderAddressMapper.getAddress(inVo.getOrderAddressId());
                if(address==null){
                    throw new RuntimeException("地址信息有误,请联系客服!");
                }
                OrderAddressInVo orderAddressInVo = new OrderAddressInVo();
                orderAddressInVo.setDictProvinceId(address.getDictProvinceId());
                orderAddressInVo.setDictCityId(address.getDictCityId());
                //计算单个订单项的运费
                 sendAmount = deliveryCostService
                         .calculateCost(orderAddressInVo, realWeight,1,BigDecimal.ZERO,goodsSku.getDeliveryTemplateId(),0);
            }
            sendAllAmount = sendAllAmount.add(sendAmount);
            //计算订单金额
            orderAmount = orderAmount.add(goodsAmount).add(sendAmount);
        }

        //1.保存订单信息
        inVo.setRealAmount(orderAmount);//实际支付金额
        inVo.setSkuAmount(skuAllAmount);//商品总额
        inVo.setSendAmount(sendAllAmount.setScale(0, BigDecimal.ROUND_UP));//总运费
        inVo.setOrderCode(RandomStringUtil.getRandomCode(11, 0));
        inVo.setStatus(OrderInfo.STATUS_WAIT_PAID);
        int ret = orderInfoMapper.insert(inVo);
        if (ret < 1) {
            logger.error("保存订单失败,userId : "+inVo.getUserId()+" orderItemList : "+inVo.getOrderItemList());
            return null;
        }
        Long id = inVo.getId();
        //2、保存订单明细信息和修改商品的库存和已售数量
        inVo.setId(id);
        this.saveOrderItem(inVo, orderItemList);

        //3、保存订单日志
        this.saveOrderLog(inVo, OrderLog.OPT_WAIT_PAID);



        /*//6、判断支付方式，如果是享七支付，则直接扣减账户余额，并修改订单状态，如果是微信支付，则只生成待支付的订单
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
        }*/

        return id;
    }

    @Override
    @Transactional
    public Long createNew(OrderInfoInVo inVo) {
        List<OrderItem> orderItemList = inVo.getOrderItemList();//获取订单里面的详细内容

        BigDecimal orderAmount = BigDecimal.ZERO;//实付款金额
        BigDecimal skuAllAmount = BigDecimal.ZERO;//商品总额(单纯的数量*单价)
        BigDecimal sendAllAmount = BigDecimal.ZERO;//总运费
        for (OrderItem orderItem : orderItemList) {
            GoodsSkuOut goodsSku = goodsSkuMapper.selectDetailBySkuId(orderItem.getGoodsSkuId());//查询商品详情
            //查询商品是否存在，是否下架
            if(goodsSku==null||goodsSku.getIsDeleted()==GoodsSku.GOODS_SKU_IS_DELETED
                    ||goodsSku.getStatus()==GoodsSku.STATUS_XJ){
                throw new RuntimeException("商品已下架");
            }
            orderItem.setGoodsSpuId(goodsSku.getSpuId());

            //如果活动id不为空和购买类型判断活动库存
            if(inVo.getActId()!=null&&inVo.getFlagType()!=OrderInfo.FLAG_TYPE_PT){
                Date time = new Date();
                ActGoodsSkuInVo actGoodsSkuInVo=new ActGoodsSkuInVo();
                actGoodsSkuInVo.setActId(inVo.getActId());
                actGoodsSkuInVo.setSkuId(orderItem.getGoodsSkuId());
                ActGoodsSkuOut actGoodsSkuOut=actGoodsSkuMapper.selectByActId(actGoodsSkuInVo);
                if (inVo.getFlagType()==OrderInfo.FLAG_TYPE_TG){
                    if (actGoodsSkuOut.getCurrentNum()>=actGoodsSkuOut.getPeopleNum()){
                        throw new RuntimeException("活动人数已达上限");
                    }
                    if(actGoodsSkuOut.getDueTime().getTime()<time.getTime()){
                        throw new RuntimeException("活动已过期");
                    }
                    ActOrder actOrder = new ActOrder();
                    actOrder.setUserId(inVo.getUserId());
                    actOrder.setActGoodsSkuId(actGoodsSkuOut.getId());
                    List<ActOrder> order=actOrderMapper.selectByActGoodsSkuId(actOrder);
                    if (order.get(0).getOrderId()!=null){
                        OrderInfo updateInfo = orderInfoMapper.selectByPrimaryKey(order.get(0).getOrderId());

                        //目前只有微信改余额
                        if (updateInfo.getPayType()!=inVo.getPayType()){
                            OrderInfoInVo invoInfo = new OrderInfoInVo();
                            BeanUtils.copyProperties(updateInfo, invoInfo);
                            //改变支付方式
                            invoInfo.setPayType(OrderInfo.PAY_TYPE_XQYE);

                            inVo.setPayType(OrderInfo.PAY_TYPE_XQYE);
                            inVo.setId(order.get(0).getOrderId());
                            //修改订单支付方式
                            orderInfoMapper.updateByPrimaryKeySelective(inVo);
                            //全部使用余额支付(余额支付相关流程)
                            payRemainingSum(updateInfo.getRealAmount(),invoInfo);
                        }
                        return order.get(0).getOrderId();
                    }
                }
                //判断活动库存数量是否小于购买的数量
                if(actGoodsSkuOut.getStockNum()<orderItem.getGoodsNum()){
                    throw new RuntimeException("库存不足");
                }
            }else {
                //判断实际库存数量是否小于购买的数量
                if(goodsSku.getStockNum()<orderItem.getGoodsNum()){
                    throw new RuntimeException("库存不足");
                }
                //判断购买的数量是否大于最小要求购买量
                if(goodsSku.getMiniNum()>orderItem.getGoodsNum()){
                    throw new RuntimeException("购买数量不足");
                }
            }

            BigDecimal goodsAmount = BigDecimal.ZERO;
            //计算用户某个订单项的商品总额
            goodsAmount = this.subGoodsSkuAmount(inVo,goodsSku,orderItem);

            skuAllAmount = skuAllAmount.add(goodsAmount);
            //目前这种模式没办法考虑同种规则类型，不同规则定义的标准(比如满10盒送一盒同时又满20盒送3盒)
            List<GoodsPromotionRules> goodsPromotionRules = goodsSku.getGoodsPromotionRules();
            Collections.sort(goodsPromotionRules);//按照ruleType从小到大排序
            OrderItem item = new OrderItem();
            for (GoodsPromotionRules goodsPromotionRule : goodsPromotionRules) {
                if(goodsPromotionRule.getRuleType()==GoodsPromotionRules.RULE_TYPE_ADD){
                    double floor = Math.floor((double)orderItem.getGoodsNum() / goodsPromotionRule.getManNum())
                            *goodsPromotionRule.getGiftNum();

                    item.setGiftNum((int) floor);
                }
                if(goodsPromotionRule.getRuleType()==GoodsPromotionRules.RULE_TYPE_PACKING){
                    int giftNum = item.getGiftNum() == null ? 0 : item.getGiftNum();
                    double amount = Math.ceil((double)(orderItem.getGoodsNum()+giftNum) / goodsPromotionRule.getManNum());
                    double floor = amount*goodsPromotionRule.getGiftNum();
                    BigDecimal bigDecimal = BigDecimal.valueOf(floor).setScale(2);
                    goodsAmount = goodsAmount.add(bigDecimal);
                }
            }
            BigDecimal sendAmount = BigDecimal.ZERO;
            sendAllAmount = sendAllAmount.add(sendAmount);
            //计算订单金额
            orderAmount = orderAmount.add(goodsAmount).add(sendAmount);
        }

        //1.保存订单信息
        if (inVo.getFlagType()==OrderInfo.FLAG_TYPE_CG){
            inVo.setRealAmount(BigDecimal.ZERO);//实际支付金额
        }else {
            inVo.setRealAmount(orderAmount);//实际支付金额
        }
        BigDecimal accountAmount=BigDecimal.ZERO;
        UserAccount account= accountService.findAccountByUserId(inVo.getUserId());
        if(inVo.getUseAccount()!=null&&inVo.getUseAccount()==OrderInfo.USE_ACCOUNT_YES&&account.getUserAmount()!=null&&account.getUserAmount().compareTo(BigDecimal.ZERO)==1){//选了余额支付且用户余额大于0
            if(account.getUserAmount().compareTo(inVo.getRealAmount())!=-1){//用户余额大于实际支付金额，取实际支付金额
                accountAmount=inVo.getRealAmount();
                inVo.setPayType(OrderInfo.PAY_TYPE_XQYE);
            }
        }
        inVo.setAccountAmount(accountAmount);
        inVo.setSkuAmount(skuAllAmount);//商品总额
        inVo.setSendAmount(sendAllAmount.setScale(0, BigDecimal.ROUND_UP));//总运费OrderInfo
        inVo.setOrderCode(RandomStringUtil.getRandomCode(11, 0));
        inVo.setStatus(OrderInfo.STATUS_WAIT_PAID);
        int ret = orderInfoMapper.insert(inVo);
        if (ret < 1) {
            logger.error("保存订单失败,userId : "+inVo.getUserId()+" orderItemList : "+inVo.getOrderItemList());
            throw new RuntimeException("订单保存失败");
        }
        Long id = inVo.getId();
        //2、保存订单明细信息和修改商品的库存和已售数量
        inVo.setId(id);
        if (inVo.getActId()!=null&&inVo.getFlagType()==OrderInfo.FLAG_TYPE_TG){
            //保存邀请人和订单的关系
            int actOrder=this.insertActOrder(inVo);
            if (actOrder < 1) {
                logger.error("团购结束保存订单失败,userId : "+inVo.getUserId()+" orderItemList : "+inVo.getOrderItemList());
                throw new RuntimeException("团购结束保存订单失败");
            }
            //修改活动库存
            this.saveOrderItemForAct(inVo, orderItemList);
            //判断是否向商家推送库存通知
            this.waringStock(inVo);
        }else if(inVo.getActId()!=null&&inVo.getFlagType()!=OrderInfo.FLAG_TYPE_PT){
            //保存活动和订单的关系
            int insertOrder= this.insertActOrderNotTG(inVo);
            if (insertOrder < 1) {
                logger.error("保存活动订单关联失败,userId : "+inVo.getUserId()+"order:"+inVo.getId());
                throw new RuntimeException("保存活动订单关联失败");
            }
            //修改活动库存this.saveOrderItemForAct(inVo, orderItemList);
            this.saveOrderItemForAct(inVo, orderItemList);
            //判断是否向商家推送库存通知
            this.waringStock(inVo);
        }else {
            //2、保存订单明细信息和修改商品的库存和已售数量(原价购买的商品)
            this.saveOrderItem(inVo, orderItemList);
            //判断是否向商家推送库存通知
            this.waringStock(inVo);
        }

        //3、保存订单日志
        this.saveOrderLog(inVo, OrderLog.OPT_WAIT_PAID);

        //全部使用余额支付(余额支付相关流程)
        payRemainingSum(accountAmount,inVo);
        return id;
    }


    /*余额支付相关流程*/
    private void payRemainingSum(BigDecimal accountAmount,OrderInfoInVo inVo){
        if( accountAmount.compareTo(inVo.getRealAmount())==0){
            OrderInfo info = orderInfoMapper.selectByPrimaryKey(inVo.getId());
            OrderInfoInVo invoInfo = new OrderInfoInVo();
            BeanUtils.copyProperties(info, invoInfo);
            invoInfo.setUserIp(inVo.getUserIp());
            invoInfo.setUserName(inVo.getUserName());

            /*订单类型判断 生成回调所需参数*/
            WeixinInVo weixinInVo = this.setOrderFlagType(inVo);

            //减用户余额
            this.payUserAccount(accountAmount,inVo);
            //先生成票券
            this.paidCouponNew(invoInfo, weixinInVo);
            //判断是否开团和票券显示
            if (inVo.getFlagType()==WeixinInVo.WEI_XIN_TYPE_SHOP_TG){
                PullUser pullUser = new PullUser();
                pullUser.setType(PullUser.PULL_TYPE_PT);
                pullUser.setParentId(inVo.getUserId());
                Integer pull=pullUserService.updateActGroupNumUp(pullUser,inVo.getId());
            }

            /**
             * 订单支付成功之后微信消息推送
             */
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date date=new java.util.Date();
            String str=sdf.format(date);
            OrderInfoOut detail = orderInfoMapper.getDetail(info.getId());//查询订单详情
            List<OrderItemOut> orderItemOuts = detail.getOrderItemOuts();//查询订单项详情
            Long shopId= orderItemOuts.get(0).getShopId();
            ShopOut shopOut=shopMapper.findShopOutById(shopId);
            String shopName="享七自营";
            if(shopOut!=null&&shopOut.getShopName()!=null){
                shopName=shopOut.getShopName();
            }
            String keyWords=shopName+","+inVo.getRealAmount()+","+str+","+info.getOrderCode()+",你购买的商品已支付成功，查看详情了解更多信息";//商户名称+支付金额+支付时间+交易单号+说明 此处说明的逗号为中文逗号
            Integer res =weiXinPushService.pushByUserId(WeiXinTeamplateMsg.templateId_TYPE_PAY, "",keyWords,info.getUserId() );

            /**
             * 更新用户账户信息，
             * 二级分销给上级和上上级增加奖励金
             */
            actGoodsSkuService.distribution(info);
            //完成之后发送消息到小程序的消息列表
            messageService.addMessage("付款成功", "您的 "+orderItemOuts.get(0).getGoodsSkuName()+" 购买成功。", MessageText.MESSAGE_TEXT_TYPE_PRIVATE, info.getUserId(), info.getUserId());
        }
    }

    /*订单类型判断 生成回调所需参数*/
    private WeixinInVo setOrderFlagType(OrderInfoInVo inVo){
        WeixinInVo weixinInVo = new WeixinInVo();
        if(inVo.getGroupId()!=null){
            weixinInVo.setGroupId(inVo.getGroupId().intValue());
        }
        switch (inVo.getFlagType()){
            case OrderInfo.FLAG_TYPE_PT:
                weixinInVo.setType(WeixinInVo.WEI_XIN_TYPE_YJ);
            break;
            case OrderInfo.FLAG_TYPE_CG:
                weixinInVo.setType(WeixinInVo.WEI_XIN_TYPE_SHOP_CJ);
                break;
            case OrderInfo.FLAG_TYPE_KJ:
                weixinInVo.setType(WeixinInVo.WEI_XIN_TYPE_SHOP_KJ);
                break;
            case OrderInfo.FLAG_TYPE_MS:
                weixinInVo.setType(WeixinInVo.WEI_XIN_TYPE_SHOP_MS);
                break;
            case OrderInfo.FLAG_TYPE_TG:
                weixinInVo.setType(WeixinInVo.WEI_XIN_TYPE_SHOP_TG);
                break;
            default:
                weixinInVo.setType(WeixinInVo.WEI_XIN_TYPE_YJ);
                break;
        }
        return weixinInVo;
    }

    /*余额购买,减用户余额*/
    private void payUserAccount(BigDecimal accountAmount,OrderInfoInVo inVo){
        UserAccountInVo userAccountInVo=new UserAccountInVo();
        userAccountInVo.setUserId(inVo.getUserId());
        userAccountInVo.setOccurAmount(accountAmount);
        userAccountInVo.setType(AccountLog.TYPE_USER);
        accountService.updatePayout(userAccountInVo, "购买商品",null,inVo.getId());
    }

    @Override
    @Transactional
    public Long createForShop(OrderInfoInVo inVo) {
        List<OrderItem> orderItemList = inVo.getOrderItemList();//获取订单里面的详细内容
        //1.保存订单信息
        inVo.setRealAmount(inVo.getRealAmount());//实际支付金额
        inVo.setSkuAmount(inVo.getSkuAmount());//商品总额
        inVo.setSendAmount(BigDecimal.ZERO);//总运费
        inVo.setOrderCode(RandomStringUtil.getRandomCode(11, 0));
        inVo.setStatus(OrderInfo.STATUS_WAIT_PAID);
        int ret = orderInfoMapper.insert(inVo);
        Long id = inVo.getId();
        inVo.setId(id);
        //2、保存订单明细信息和修改商品的库存和已售数量(原价购买的商品)
        this.saveOrderItemForShop(inVo, orderItemList);
        //3、保存订单日志
        this.saveOrderLog(inVo, OrderLog.OPT_WAIT_PAID);
        return id;
    }

    private void saveOrderItemForShop(OrderInfoInVo inVo, List<OrderItem> orderItemList) {
        OrderItem orderItem = orderItemList.get(0);
        GoodsSkuOut goodsSku = goodsSkuMapper.selectDetailBySkuId(orderItem.getGoodsSkuId());
        //保存订单详情
        OrderItem item = new OrderItem();
        item.setOrderCode(inVo.getOrderCode());
        item.setGoodsSkuId(orderItem.getGoodsSkuId());
        item.setGoodsSpuId(orderItem.getGoodsSpuId());
        item.setShopId(orderItem.getOrderItemShopId());
        item.setGoodsSkuName(goodsSku.getSkuName());
        item.setGoodsNum(orderItem.getGoodsNum());
        item.setGoodsPrice(goodsSku.getSellPrice());
        item.setRemark(orderItem.getRemark());
        item.setRealUnitPrice(goodsSku.getSellPrice());
        item.setGiftNum(0);
        orderItemMapper.insert(item);
    }

    /**
     * 免费创建订单和领取券
     * @param inVo
     * @return
     */
    @Override
    @Transactional
    public Long createNewForFree(OrderInfoInVo inVo){
        List<OrderItem> orderItemList = inVo.getOrderItemList();//获取订单里面的详细内容

        BigDecimal orderAmount = BigDecimal.ZERO;//实付款金额
        BigDecimal skuAllAmount = BigDecimal.ZERO;//商品总额(单纯的数量*单价)
        BigDecimal sendAllAmount = BigDecimal.ZERO;//总运费
        for (OrderItem orderItem : orderItemList) {
            GoodsSkuOut goodsSku = goodsSkuMapper.selectDetailBySkuId(orderItem.getGoodsSkuId());//查询商品详情
            //查询商品是否存在，是否下架
            if(goodsSku==null||goodsSku.getIsDeleted()==GoodsSku.GOODS_SKU_IS_DELETED
                    ||goodsSku.getStatus()==GoodsSku.STATUS_XJ){
                return null;
            }
            //如果活动id不为空和购买类型判断活动库存
            if(inVo.getActId()!=null&&inVo.getFlagType()!=OrderInfo.FLAG_TYPE_PT){
                Date time = new Date();
                ActGoodsSkuInVo actGoodsSkuInVo=new ActGoodsSkuInVo();
                actGoodsSkuInVo.setActId(inVo.getActId());
                actGoodsSkuInVo.setSkuId(orderItem.getGoodsSkuId());
                ActGoodsSkuOut actGoodsSkuOut=actGoodsSkuMapper.selectByActId(actGoodsSkuInVo);
                if (inVo.getFlagType()==OrderInfo.FLAG_TYPE_TG){
                    if (actGoodsSkuOut.getCurrentNum()>=actGoodsSkuOut.getPeopleNum()){
                        return null;
                    }
                    if(actGoodsSkuOut.getDueTime().getTime()<time.getTime()){
                        return null;
                    }
                    ActOrder actOrder = new ActOrder();
                    actOrder.setUserId(inVo.getUserId());
                    actOrder.setActGoodsSkuId(actGoodsSkuOut.getId());
                    List<ActOrder> order=actOrderMapper.selectByActGoodsSkuId(actOrder);
                    if (order.get(0).getOrderId()!=null){
                        return order.get(0).getOrderId();
                    }
                }
                //判断活动库存数量是否小于购买的数量
                if(actGoodsSkuOut.getStockNum()<orderItem.getGoodsNum()){
                    return null;
                }
            }else {
                //判断实际库存数量是否小于购买的数量
                if(goodsSku.getStockNum()<orderItem.getGoodsNum()){
                    return null;
                }
                //判断购买的数量是否大于最小要求购买量
                if(goodsSku.getMiniNum()>orderItem.getGoodsNum()){
                    return null;
                }
            }
            orderItem.setGoodsSpuId(goodsSku.getSpuId());
            BigDecimal goodsAmount = BigDecimal.ZERO;
            //计算用户某个订单项的商品总额
            goodsAmount = this.subGoodsSkuAmount(inVo,goodsSku,orderItem);

            skuAllAmount = skuAllAmount.add(goodsAmount);
            //目前这种模式没办法考虑同种规则类型，不同规则定义的标准(比如满10盒送一盒同时又满20盒送3盒)
            List<GoodsPromotionRules> goodsPromotionRules = goodsSku.getGoodsPromotionRules();
            Collections.sort(goodsPromotionRules);//按照ruleType从小到大排序
            OrderItem item = new OrderItem();
            for (GoodsPromotionRules goodsPromotionRule : goodsPromotionRules) {
                if(goodsPromotionRule.getRuleType()==GoodsPromotionRules.RULE_TYPE_ADD){
                    double floor = Math.floor((double)orderItem.getGoodsNum() / goodsPromotionRule.getManNum())
                            *goodsPromotionRule.getGiftNum();

                    item.setGiftNum((int) floor);
                }
                if(goodsPromotionRule.getRuleType()==GoodsPromotionRules.RULE_TYPE_PACKING){
                    int giftNum = item.getGiftNum() == null ? 0 : item.getGiftNum();
                    double amount = Math.ceil((double)(orderItem.getGoodsNum()+giftNum) / goodsPromotionRule.getManNum());
                    double floor = amount*goodsPromotionRule.getGiftNum();
                    BigDecimal bigDecimal = BigDecimal.valueOf(floor).setScale(2);
                    goodsAmount = goodsAmount.add(bigDecimal);
                }
            }
            BigDecimal sendAmount = BigDecimal.ZERO;
            sendAllAmount = sendAllAmount.add(sendAmount);
            //计算订单金额
            orderAmount = orderAmount.add(goodsAmount).add(sendAmount);
        }
        //1.保存订单信息
        if (inVo.getFlagType()==OrderInfo.FLAG_TYPE_CG){
            inVo.setRealAmount(BigDecimal.ZERO);//实际支付金额
        }else {
            inVo.setRealAmount(orderAmount);//实际支付金额
        }
        inVo.setSkuAmount(skuAllAmount);//商品总额
        inVo.setSendAmount(sendAllAmount.setScale(0, BigDecimal.ROUND_UP));//总运费
        inVo.setOrderCode(RandomStringUtil.getRandomCode(11, 0));
        inVo.setStatus(OrderInfo.STATUS_WAIT_PAID);
        int ret = orderInfoMapper.insert(inVo);
        if (ret < 1) {
            logger.error("保存订单失败,userId : "+inVo.getUserId()+" orderItemList : "+inVo.getOrderItemList());
            return null;
        }
        Long id = inVo.getId();
        //2、保存订单明细信息和修改商品的库存和已售数量
        inVo.setId(id);
        if (inVo.getActId()!=null&&inVo.getFlagType()==OrderInfo.FLAG_TYPE_TG){
            //保存邀请人和订单的关系
            int actOrder=this.insertActOrder(inVo);
            if (actOrder < 1) {
                logger.error("团购结束保存订单失败,userId : "+inVo.getUserId()+" orderItemList : "+inVo.getOrderItemList());
                return null;
            }
            //修改活动库存
            this.saveOrderItemForAct(inVo, orderItemList);
        }else if(inVo.getActId()!=null&&inVo.getFlagType()!=OrderInfo.FLAG_TYPE_PT){
            //保存活动和订单的关系
            int insertOrder= this.insertActOrderNotTG(inVo);
            if (insertOrder < 1) {
                logger.error("保存活动订单关联失败,userId : "+inVo.getUserId()+"order:"+inVo.getId());
                return null;
            }
            //修改活动库存this.saveOrderItemForAct(inVo, orderItemList);
            this.saveOrderItemForAct(inVo, orderItemList);
        }else {
            //2、保存订单明细信息和修改商品的库存和已售数量(原价购买的商品)
            this.saveOrderItem(inVo, orderItemList);
        }
       /* this.saveOrderItem(inVo, orderItemList);*/

        //3、保存订单日志
        this.saveOrderLog(inVo, OrderLog.OPT_WAIT_PAID);

        //4.生成券并且改订单日志
        OrderInfo info = orderInfoMapper.selectByPrimaryKey(id);
        OrderInfoInVo invoInfo = new OrderInfoInVo();
        BeanUtils.copyProperties(info, invoInfo);
        invoInfo.setUserIp(inVo.getUserIp());
        invoInfo.setUserName(inVo.getUserName());
        if (inVo.getDefaultUsed()!=null && inVo.getDefaultUsed()==OrderInfo.SDEFAULT_USED_YES){
            invoInfo.setDefaultUsed(OrderInfo.SDEFAULT_USED_YES);
        }else if(inVo.getDefaultUsed()!=null && inVo.getDefaultUsed()==OrderInfo.SDEFAULT_USED_NO){
            invoInfo.setDefaultUsed(OrderInfo.SDEFAULT_USED_NO);
        }
        WeixinInVo weixinInVo = new WeixinInVo();
        if(inVo.getFlagType()==OrderInfo.FLAG_TYPE_PT) {
            weixinInVo.setType(WeixinInVo.WEI_XIN_TYPE_YJ);
        }else if(inVo.getFlagType()==OrderInfo.FLAG_TYPE_CG){
            weixinInVo.setType(WeixinInVo.WEI_XIN_TYPE_SHOP_CJ);
        }
        this.paidCouponNew(invoInfo, weixinInVo);
        return id;
    }

    @Override
    public Long batchCreateForXq(OrderInfoInVo inVo) {
        List<OrderItem> orderItemList = inVo.getOrderItemList();//获取订单里面的详细内容

        BigDecimal orderAmount = BigDecimal.ZERO;//实付款金额
        BigDecimal skuAllAmount = BigDecimal.ZERO;//商品总额(单纯的数量*单价)
        BigDecimal sendAllAmount = BigDecimal.ZERO;//总运费
        for (OrderItem orderItem : orderItemList) {
            GoodsSkuOut goodsSku = goodsSkuMapper.selectDetailBySkuId(orderItem.getGoodsSkuId());//查询商品详情
            //查询商品是否存在，是否下架
            if(goodsSku==null||goodsSku.getIsDeleted()==GoodsSku.GOODS_SKU_IS_DELETED
                    ||goodsSku.getStatus()==GoodsSku.STATUS_XJ){
                return null;
            }
            BigDecimal goodsAmount = BigDecimal.ZERO;
            //计算用户某个订单项的商品总额
            goodsAmount = this.batchSubGoodsSkuAmount(inVo, goodsSku, orderItem);

            skuAllAmount = skuAllAmount.add(goodsAmount);
            BigDecimal sendAmount = BigDecimal.ZERO;
            sendAllAmount = sendAllAmount.add(sendAmount);
            //计算订单金额
            orderAmount = orderAmount.add(goodsAmount).add(sendAmount);
        }

        //1.保存订单信息
        inVo.setRealAmount(orderAmount);//实际支付金额
        inVo.setSkuAmount(skuAllAmount);//商品总额
        inVo.setSendAmount(sendAllAmount.setScale(0, BigDecimal.ROUND_UP));//总运费
        inVo.setOrderCode(RandomStringUtil.getRandomCode(11, 0));
        if(inVo.getBatchOrderInVo().getOrderStatus()==BatchOrderInVo.ORDER_STATUS_QUIT){
            inVo.setStatus(OrderInfo.STATUS_QX);
            int ret = orderInfoMapper.batchInsert(inVo);
            if (ret < 1) {
                logger.error("保存订单失败,userId : "+inVo.getUserId()+" orderItemList : "+inVo.getOrderItemList());
                return null;
            }
            Long id = inVo.getId();
            //2、保存订单明细信息和修改商品的库存和已售数量
            inVo.setId(id);
            if (inVo.getActId()!=null){
                //保存活动和订单的关系
                int insertOrder= this.batchInsertActOrderNotTG(inVo);
                if (insertOrder < 1) {
                    logger.error("保存活动订单关联失败,userId : "+inVo.getUserId()+"order:"+inVo.getId());
                    return null;
                }
                this.batchSaveOrderItemForAct(inVo, orderItemList);
            }else {
                this.batchSaveOrderItem(inVo, orderItemList);
            }

            //3、保存订单日志
            this.batchSaveOrderLog(inVo, OrderLog.OPT_QX);
            return id;
        }
        inVo.setStatus(OrderInfo.STATUS_WAIT_PAID);
        int ret = orderInfoMapper.batchInsert(inVo);
        if (ret < 1) {
            logger.error("保存订单失败,userId : "+inVo.getUserId()+" orderItemList : "+inVo.getOrderItemList());
            return null;
        }
        Long id = inVo.getId();
        //2、保存订单明细信息和修改商品的库存和已售数量
        inVo.setId(id);
        if (inVo.getActId()!=null){
            //保存活动和订单的关系
            int insertOrder= this.batchInsertActOrderNotTG(inVo);
            if (insertOrder < 1) {
                logger.error("保存活动订单关联失败,userId : "+inVo.getUserId()+"order:"+inVo.getId());
                return null;
            }
            this.batchSaveOrderItemForAct(inVo, orderItemList);
        }else {
            this.batchSaveOrderItem(inVo, orderItemList);
        }

        //3、保存订单日志
        this.batchSaveOrderLog(inVo, OrderLog.OPT_WAIT_PAID);

        //4.生成券并且改订单日志
        OrderInfo info = orderInfoMapper.selectByPrimaryKey(id);
        OrderInfoInVo invoInfo = new OrderInfoInVo();
        BeanUtils.copyProperties(info, invoInfo);
        invoInfo.setUserIp(inVo.getUserIp());
        invoInfo.setUserName(inVo.getUserName());
        invoInfo.setCreateTime(inVo.getCreateTime());
        invoInfo.setBatchOrderInVo(inVo.getBatchOrderInVo());
        WeixinInVo weixinInVo = new WeixinInVo();
        weixinInVo.setType(WeixinInVo.WEI_XIN_TYPE_YJ);
        this.batchPaidCoupon(invoInfo, weixinInVo);
        return id;
    }

    /**
     * 拼团订单和用户写入记录
     * @param inVo 用户下单初始入参
     * @return
     */
    public int insertActOrder(OrderInfoInVo inVo){
        ActGoodsSkuInVo actGoodsSkuInVo = new ActGoodsSkuInVo();
        actGoodsSkuInVo.setActId(inVo.getActId());
        actGoodsSkuInVo.setSkuId(inVo.getOrderItemList().get(0).getGoodsSkuId());
        ActGoodsSkuOut actGoodsSkuOut=actGoodsSkuMapper.selectByActId(actGoodsSkuInVo);
        if (actGoodsSkuOut==null||actGoodsSkuOut.getId()==null){
            return -1;
        }
        ActOrder actOrder=new ActOrder();
        actOrder.setUserId(inVo.getUserId());
        actOrder.setActGoodsSkuId(actGoodsSkuOut.getId());
        actOrder.setState(ActOrder.ACT_ORDER_GROUP_STATE_TUXEDO_END);
        List<ActOrder> order=actOrderMapper.selectByActGoodsSkuId(actOrder);
        if (order==null||order.size()<=0){
            return -1;
        }
        order.get(0).setOrderId(inVo.getId());
        int update=actOrderMapper.updateByPrimaryKeySelective(order.get(0));
        return update;
    }

    /**
     * 活动订单和用户写入记录
     * @param inVo 用户下单初始入参
     * @return
     */
    public int insertActOrderNotTG(OrderInfoInVo inVo){
        ActGoodsSkuInVo actGoodsSkuInVo = new ActGoodsSkuInVo();
        actGoodsSkuInVo.setActId(inVo.getActId());
        actGoodsSkuInVo.setSkuId(inVo.getOrderItemList().get(0).getGoodsSkuId());
        ActGoodsSkuOut actGoodsSkuOut=actGoodsSkuMapper.selectByActId(actGoodsSkuInVo);
        if (actGoodsSkuOut==null||actGoodsSkuOut.getId()==null){
            return -1;
        }
        //保存活动订单关系
        ActOrder actOrder = new ActOrder();
        actOrder.setActGoodsSkuId(actGoodsSkuOut.getId());
        actOrder.setOrderId(inVo.getId());
        actOrder.setUserId(inVo.getUserId());
        return actOrderMapper.insertOrderLog(actOrder);
    }

    /**
     * 批量活动订单和用户写入记录
     * @param inVo 用户下单初始入参
     * @return
     */
    public int batchInsertActOrderNotTG(OrderInfoInVo inVo){
        ActGoodsSkuInVo actGoodsSkuInVo = new ActGoodsSkuInVo();
        actGoodsSkuInVo.setActId(inVo.getActId());
        actGoodsSkuInVo.setSkuId(inVo.getOrderItemList().get(0).getGoodsSkuId());
        ActGoodsSkuOut actGoodsSkuOut=actGoodsSkuMapper.selectByActId(actGoodsSkuInVo);
        if (actGoodsSkuOut==null||actGoodsSkuOut.getId()==null){
            return -1;
        }
        //保存活动订单关系
        ActOrder actOrder = new ActOrder();
        actOrder.setActGoodsSkuId(actGoodsSkuOut.getId());
        actOrder.setOrderId(inVo.getId());
        actOrder.setUserId(inVo.getUserId());
        actOrder.setCreateTime(inVo.getCreateTime());
        return actOrderMapper.batchInsertOrderLog(actOrder);
    }

    /**
     * 计算用户某个订单项的商品总额
     * @param inVo 用户下单初始入参
     * @param goodsSku  商品详情
     * @param orderItem 单户下单订单项详情
     * @return
     */
    public BigDecimal subGoodsSkuAmount(OrderInfoInVo inVo,GoodsSkuOut goodsSku,OrderItem orderItem){
        BigDecimal goodsAmount = BigDecimal.ZERO;

        if(inVo.getFlagType()==null||(inVo.getFlagType()==OrderInfo.FLAG_TYPE_PT&&inVo.getPayType()!=OrderInfo.PAY_TYPE_MFZS)){
            BigDecimal price=goodsSku.getSellPrice();
            //查询商品的类目信息
            GoodsCategory goodsCategory = goodsCategoryMapper.selectByPrimaryKey(goodsSku.getCategoryId());
            if(goodsCategory!=null&&goodsCategory.getParentId()==87){//轻餐的原价购买取singlyPrice
                if(goodsSku.getSinglyPrice()!=null&&goodsSku.getSinglyPrice().compareTo(BigDecimal.ZERO)>0){
                    price=goodsSku.getSinglyPrice();
                }
            }
            orderItem.setRealUnitPrice(price);
            goodsAmount = BigDecimal.valueOf(orderItem.getGoodsNum()).multiply(price);
        }else if (inVo.getFlagType()==OrderInfo.FLAG_TYPE_CG||inVo.getPayType()==OrderInfo.PAY_TYPE_MFZS) {
            orderItem.setRealUnitPrice(BigDecimal.ZERO);
            goodsAmount = BigDecimal.valueOf(orderItem.getGoodsNum()).multiply(goodsSku.getSellPrice());
        }else if(inVo.getFlagType()==OrderInfo.FLAG_TYPE_KJ){
                List<GoodsPromotionRules> goodsPromotionRules = goodsSku.getGoodsPromotionRules();
                for (GoodsPromotionRules goodsPromotionRule : goodsPromotionRules) {
                    if(goodsPromotionRule.getRuleType()==GoodsPromotionRules.RULE_TYPE_KJ){
                        String hgetBargain = (String)redisCache.hget(inVo.getTotalKey(),inVo.getValueKey());
                        GoodsBargainLogOut goodsBargainLogOut = JSON.parseObject(hgetBargain, GoodsBargainLogOut.class);
                        orderItem.setRealUnitPrice(goodsBargainLogOut.getSkuMoneyNow());
                        //获取小组id余额用
                        inVo.setGroupId(new Long(goodsBargainLogOut.getGroupId()));
                        goodsAmount = BigDecimal.valueOf(orderItem.getGoodsNum()).multiply(goodsBargainLogOut.getSkuMoneyNow());
                        break;
                    }
                }
            }else if (inVo.getFlagType()==OrderInfo.FLAG_TYPE_TG){
                //获取促销规则
                ActGoodsSkuInVo actGoodsSkuInVo = new ActGoodsSkuInVo();
                actGoodsSkuInVo.setActId(inVo.getActId());
                actGoodsSkuInVo.setSkuId(goodsSku.getId());
                ActGoodsSkuOut actGoodsSkuOut=actGoodsSkuMapper.selectByActId(actGoodsSkuInVo);
                GoodsPromotionRules goodsPromotionRules=goodsPromotionRulesMapper.selectByPrimaryKey(actGoodsSkuOut.getGoodsPrId());
                orderItem.setRealUnitPrice(goodsPromotionRules.getActAmount());
                goodsAmount = BigDecimal.valueOf(orderItem.getGoodsNum()).multiply(goodsPromotionRules.getActAmount());
            }if (inVo.getFlagType()==OrderInfo.FLAG_TYPE_MS){
            //获取促销规则
            ActGoodsSkuInVo actGoodsSkuInVo = new ActGoodsSkuInVo();
            actGoodsSkuInVo.setActId(inVo.getActId());
            actGoodsSkuInVo.setSkuId(goodsSku.getId());
            ActGoodsSkuOut actGoodsSkuOut=actGoodsSkuMapper.selectByActId(actGoodsSkuInVo);
            GoodsPromotionRules goodsPromotionRules=goodsPromotionRulesMapper.selectByPrimaryKey(actGoodsSkuOut.getGoodsPrId());
            orderItem.setRealUnitPrice(goodsPromotionRules.getActAmount());
            goodsAmount = BigDecimal.valueOf(orderItem.getGoodsNum()).multiply(goodsPromotionRules.getActAmount());
            }
        return goodsAmount;
    }

    /**
     * 计算用户某个订单项的商品总额(批量插入用户)
     * @param inVo 用户下单初始入参
     * @param goodsSku  商品详情
     * @param orderItem 单户下单订单项详情
     * @return
     */
    public BigDecimal batchSubGoodsSkuAmount(OrderInfoInVo inVo,GoodsSkuOut goodsSku,OrderItem orderItem){
        BigDecimal goodsAmount = BigDecimal.ZERO;
        if(inVo.getFlagType()==null||inVo.getFlagType()==OrderInfo.FLAG_TYPE_PT){
            BigDecimal price=goodsSku.getSellPrice();
            //查询商品的类目信息
            GoodsCategory goodsCategory = goodsCategoryMapper.selectByPrimaryKey(goodsSku.getCategoryId());
            if(goodsCategory!=null&&goodsCategory.getParentId()==87){//轻餐的原价购买取singlyPrice
                if(goodsSku.getSinglyPrice()!=null&&goodsSku.getSinglyPrice().compareTo(BigDecimal.ZERO)>0){
                    price=goodsSku.getSinglyPrice();
                }
            }
            goodsAmount = BigDecimal.valueOf(orderItem.getGoodsNum()).multiply(price);
            orderItem.setRealUnitPrice(price);
        }else if (inVo.getFlagType()==OrderInfo.FLAG_TYPE_CG) {
            orderItem.setRealUnitPrice(BigDecimal.ZERO);
            goodsAmount = BigDecimal.valueOf(orderItem.getGoodsNum()).multiply(goodsSku.getSellPrice());
        }else if(inVo.getFlagType()==OrderInfo.FLAG_TYPE_KJ||inVo.getFlagType()==OrderInfo.FLAG_TYPE_TG
                ||inVo.getFlagType()==OrderInfo.FLAG_TYPE_MS){
            //获取促销规则
            ActGoodsSkuInVo actGoodsSkuInVo = new ActGoodsSkuInVo();
            actGoodsSkuInVo.setActId(inVo.getActId());
            actGoodsSkuInVo.setSkuId(goodsSku.getId());
            ActGoodsSkuOut actGoodsSkuOut=actGoodsSkuMapper.selectByActId(actGoodsSkuInVo);
            GoodsPromotionRules goodsPromotionRules=goodsPromotionRulesMapper.selectByPrimaryKey(actGoodsSkuOut.getGoodsPrId());
            orderItem.setRealUnitPrice(goodsPromotionRules.getActAmount());
            goodsAmount = BigDecimal.valueOf(orderItem.getGoodsNum()).multiply(goodsPromotionRules.getActAmount());
        }
        return goodsAmount;
    }


    @Override
    @Transactional
    public Long createRo(OrderInfoInVo inVo) {
        List<OrderItem> orderItemList = inVo.getOrderItemList();//获取订单里面的详细内容
        //https://blog.csdn.net/my_precious/article/details/53010232
        Set<OrderItem> orderItemSet = new HashSet<OrderItem>(orderItemList);//通过set集合直接去重,来判断拆单
        List<OrderItem> items = new ArrayList<>(orderItemSet);
        List<List<OrderItem>>  orderItemDifList = new ArrayList<List<OrderItem>>();

        //将订单拆分,如果只有父订单orderItemDifList.size()==1
        for (OrderItem item : items) {
            List<OrderItem> orderItems = new ArrayList<OrderItem>();
            for (OrderItem orderItem : orderItemList) {
                if(orderItem.equals(item)){
                    orderItems.add(orderItem);
                }
            }
            orderItemDifList.add(orderItems);
        }

        //订单的所有集合(每个OrderInfoInVo都代表一个订单,如果该集合内只有一个OrderInfoInVo,那么他就是一个父订单)
        List<OrderInfoInVo> orderInfoInVoList = this.getAllOrderInfoInVoList(orderItemDifList, inVo);
        if(orderInfoInVoList==null||orderInfoInVoList.size()==0){
            logger.error("创建订单失败,请开发人员检查错误");
            return null;
        }
        if(orderInfoInVoList.size()==1){
            OrderInfoInVo infoInVo = new OrderInfoInVo();
            infoInVo = orderInfoInVoList.get(0);//里面的订单编号已经存在
            infoInVo.setParentOrderId(0L);
            infoInVo.setIsParent(OrderInfo.IS_PARENT_YES);
            //保存实物订单的订单信息,保存订单明细信息和修改商品的库存和已售数量,保存订单日志
            Long parentId = this.saveRoOrder(infoInVo);
            return parentId;
        }

        //保存父订单信息
        OrderInfoInVo parentInVo = new OrderInfoInVo();//父订单入参
        BigDecimal parentOrderAmount = BigDecimal.ZERO;//父订单实付款金额
        BigDecimal parentSkuAllAmount = BigDecimal.ZERO;//商品总额(单纯的数量*单价)
        BigDecimal parentSendAllAmount = BigDecimal.ZERO;//总运费
        for (OrderInfoInVo infoInVo : orderInfoInVoList) {
            parentOrderAmount = parentOrderAmount.add(infoInVo.getRealAmount());
            parentSkuAllAmount = parentSkuAllAmount.add(infoInVo.getSkuAmount());
            parentSendAllAmount = parentSendAllAmount.add(infoInVo.getSendAmount());
        }
        BeanUtils.copyProperties(inVo,parentInVo);
        parentInVo.setRealAmount(parentOrderAmount);
        parentInVo.setSkuAmount(parentSkuAllAmount);
        parentInVo.setSendAmount(parentSendAllAmount);
        parentInVo.setOrderCode(RandomStringUtil.getRandomCode(11, 0));
        parentInVo.setStatus(OrderInfo.STATUS_WAIT_PAID);
        parentInVo.setParentOrderId(0L);
        parentInVo.setIsParent(OrderInfo.IS_PARENT_YES);

        int ret = orderInfoMapper.insert(parentInVo);
        if (ret < 1) {
            throw new RuntimeException("保存父订单失败,userId : "+parentInVo.getUserId()+" orderItemList : "+parentInVo.getOrderItemList());
        }
        Long id = parentInVo.getId();//父订单id,把这个id当作子订单的orderParentId
        parentInVo.setId(id);
        //保存父订单的订单日志
        this.saveOrderLog(parentInVo, OrderLog.OPT_WAIT_PAID);

        //保存各个子订单的信息
        for (OrderInfoInVo infoInVo : orderInfoInVoList) {
            infoInVo.setParentOrderId(id);//保存父订单id
            infoInVo.setIsParent(OrderInfo.IS_PARENT_NO);
            //保存子订单的订单信息,保存订单明细信息和修改商品的库存和已售数量,保存订单日志
            Long sonId = this.saveRoOrder(infoInVo);
        }

        return id;
    }

    @Override
    public Long createVo(OrderInfoInVo inVo) {
        List<OrderItem> orderItemList = inVo.getOrderItemList();//获取订单里面的详细内容
        //https://blog.csdn.net/my_precious/article/details/53010232
        Set<OrderItem> orderItemSet = new HashSet<OrderItem>(orderItemList);//通过set集合直接去重,来判断拆单
        List<OrderItem> items = new ArrayList<>(orderItemSet);
        List<List<OrderItem>>  orderItemDifList = new ArrayList<List<OrderItem>>();

        //将订单拆分,如果只有父订单orderItemDifList.size()==1
        for (OrderItem item : items) {
            List<OrderItem> orderItems = new ArrayList<OrderItem>();
            for (OrderItem orderItem : orderItemList) {
                if(orderItem.equals(item)){
                    orderItems.add(orderItem);
                }
            }
            orderItemDifList.add(orderItems);
        }

        //订单的所有集合(每个OrderInfoInVo都代表一个订单,如果该集合内只有一个OrderInfoInVo,那么他就是一个父订单)
        List<OrderInfoInVo> orderInfoInVoList = this.getAllOrderInfoInVoList(orderItemDifList, inVo);
        if(orderInfoInVoList==null||orderInfoInVoList.size()==0){
            logger.error("创建订单失败,请开发人员检查错误");
            return null;
        }
        Long parentId = null;
        if(orderInfoInVoList.size()==1){
            OrderInfoInVo infoInVo = new OrderInfoInVo();
            infoInVo = orderInfoInVoList.get(0);//里面的订单编号已经存在
            infoInVo.setParentOrderId(0L);
            infoInVo.setIsParent(OrderInfo.IS_PARENT_YES);
            //保存实物订单的订单信息,保存订单明细信息和修改商品的库存和已售数量,保存订单日志
            parentId = this.saveRoOrder(infoInVo);
            return parentId;
        }

        return parentId;
    }

    /**
     * 保存实物订单的订单信息,保存订单明细信息和修改商品的库存和已售数量,保存订单日志
     * @param infoInVo
     * @return
     */
    public Long saveRoOrder(OrderInfoInVo infoInVo){
        //1.保存订单信息
        int ret = orderInfoMapper.insert(infoInVo);
        if (ret < 1) {
            throw new RuntimeException("保存订单失败,userId : "+infoInVo.getUserId()+" orderItemList : "+infoInVo.getOrderItemList());
        }
        Long id = infoInVo.getId();
        //2、保存订单明细信息和修改商品的库存和已售数量
        infoInVo.setId(id);
        this.saveOrderItem(infoInVo, infoInVo.getOrderItemList());
        //3、保存订单日志
        this.saveOrderLog(infoInVo, OrderLog.OPT_WAIT_PAID);
        return id;
    }

    /**
     * 订单的所有集合(每个OrderInfoInVo都代表一个订单,如果该集合内只有一个OrderInfoInVo,那么他就是一个父订单)
     * @param orderItemDifList 拆分过后,每个订单项的集合
     * @param inVo 订单的初始入参(每个子订单和父订单的初始入参都相同)
     * @return  订单的所有集合
     */
    public List<OrderInfoInVo> getAllOrderInfoInVoList(List<List<OrderItem>>  orderItemDifList,OrderInfoInVo inVo){
        //订单的所有集合(每个OrderInfoInVo都代表一个订单,如果该集合内只有一个OrderInfoInVo,那么他就是一个父订单)
        List<OrderInfoInVo> orderInfoInVoList = new ArrayList<OrderInfoInVo>();
        for (List<OrderItem> orderItems : orderItemDifList) {
            //创建一个新的入参,用来存储每个子订单的详情
            OrderInfoInVo infoInVo = new OrderInfoInVo();
            BeanUtils.copyProperties(inVo, infoInVo);
            //传递子订单的orderItem内的信息到入参
            infoInVo.setSendType(orderItems.get(0).getSendType());
            infoInVo.setSendTime(orderItems.get(0).getSendTime());
            infoInVo.setOrderAddressId(orderItems.get(0).getOrderAddressId());
            infoInVo.setSalepointId(orderItems.get(0).getSalepointId());
            infoInVo.setOrderItemList(orderItems);//传递每个订单的订单项集合

            BigDecimal orderAmount = BigDecimal.ZERO;//实付款金额
            BigDecimal skuAllAmount = BigDecimal.ZERO;//商品总额(单纯的数量*单价)
            BigDecimal sendAllAmount = BigDecimal.ZERO;//总运费
            BigDecimal packingAllAmount = BigDecimal.ZERO;//总包装费

            //计算每个订单的实际付款金额,商品总额,总运费
            Map<String, BigDecimal> map = this.subPriceAmount(orderItems);
            orderAmount = map.get("orderAmount");
            skuAllAmount = map.get("skuAllAmount");
            sendAllAmount = map.get("sendAllAmount");
            packingAllAmount = map.get("packingAllAmount");

            //1.保存订单信息
            infoInVo.setRealAmount(orderAmount);//实际支付金额
            infoInVo.setSkuAmount(skuAllAmount);//商品总额
            infoInVo.setSendAmount(sendAllAmount.setScale(0, BigDecimal.ROUND_UP));//总运费
            infoInVo.setPackingAmount(packingAllAmount);//包装费
            infoInVo.setOrderCode(RandomStringUtil.getRandomCode(11, 0));
            infoInVo.setStatus(OrderInfo.STATUS_WAIT_PAID);

            orderInfoInVoList.add(infoInVo);
        }
        return orderInfoInVoList;
    }

    /**
     * 计算每个订单的实际付款金额,商品总额,总运费
     * @param orderItems
     * @return
     */
    public Map<String,BigDecimal> subPriceAmount(List<OrderItem> orderItems){
        BigDecimal orderAmount = BigDecimal.ZERO;//实付款金额
        BigDecimal skuAllAmount = BigDecimal.ZERO;//商品总额(单纯的数量*单价)
        BigDecimal sendAllAmount = BigDecimal.ZERO;//总运费
        BigDecimal packingAllAmount = BigDecimal.ZERO;//总包装费
        for (OrderItem orderItem : orderItems) {
            GoodsSkuOut goodsSku = goodsSkuMapper.selectDetailBySkuId(orderItem.getGoodsSkuId());//查询商品详情
            //查询商品是否存在，是否下架
            if(goodsSku==null||goodsSku.getIsDeleted()==GoodsSku.GOODS_SKU_IS_DELETED
                    ||goodsSku.getStatus()==GoodsSku.STATUS_XJ){
                return null;
            }
            //判断实际库存数量是否小于购买的数量
            if(goodsSku.getStockNum()<orderItem.getGoodsNum()){
                return null;
            }
            //判断购买的数量是否大于最小要求购买量
            if(goodsSku.getMiniNum()>orderItem.getGoodsNum()){
                return null;
            }
            BigDecimal goodsAmount = BigDecimal.valueOf(orderItem.getGoodsNum()).multiply(goodsSku.getSellPrice());//计算每样商品的商品总额

            skuAllAmount = skuAllAmount.add(goodsAmount);
            //目前这种模式没办法考虑同种规则类型，不同规则定义的标准(比如满10盒送一盒同时又满20盒送3盒)
            List<GoodsPromotionRules> goodsPromotionRules = goodsSku.getGoodsPromotionRules();
            Collections.sort(goodsPromotionRules);//按照ruleType从小到大排序
            OrderItem item = new OrderItem();
            for (GoodsPromotionRules goodsPromotionRule : goodsPromotionRules) {
                if(goodsPromotionRule.getRuleType()==GoodsPromotionRules.RULE_TYPE_ADD){
                    double floor = Math.floor((double)orderItem.getGoodsNum() / goodsPromotionRule.getManNum())
                            *goodsPromotionRule.getGiftNum();

                    item.setGiftNum((int) floor);
                }
                if(goodsPromotionRule.getRuleType()==GoodsPromotionRules.RULE_TYPE_PACKING){
                    int giftNum = item.getGiftNum() == null ? 0 : item.getGiftNum();
                    double amount = Math.ceil((double)(orderItem.getGoodsNum()+giftNum) / goodsPromotionRule.getManNum());
                    double floor = amount*goodsPromotionRule.getGiftNum();
                    BigDecimal bigDecimal = BigDecimal.valueOf(floor).setScale(2);
                    packingAllAmount = packingAllAmount.add(bigDecimal);//计算包装费
                    goodsAmount = goodsAmount.add(bigDecimal);//计算含有包装费的商品总额
                }
            }
            //通过传不传地址，来判断是否计算运费
            BigDecimal sendAmount = this.subSendAmount(orderItem,item,goodsSku);//计算每样商品的运费
            sendAllAmount = sendAllAmount.add(sendAmount);
            //计算订单金额
            orderAmount = orderAmount.add(goodsAmount).add(sendAmount);
        }
        Map<String,BigDecimal> map = new HashMap<String,BigDecimal>();
        map.put("orderAmount",orderAmount);
        map.put("skuAllAmount", skuAllAmount);
        map.put("sendAllAmount", sendAllAmount);
        map.put("packingAllAmount", packingAllAmount);
        return map;
    }

    /**
     * 通过传不传地址，来判断是否计算运费
     * @param orderItem 订单项的信息
     * @param item 商品的赠送数量
     * @param goodsSku 商品的参数信息
     * @return
     */
    public BigDecimal subSendAmount(OrderItem orderItem,OrderItem item,GoodsSkuOut goodsSku){
        BigDecimal sendAmount = BigDecimal.ZERO;
        if(orderItem.getOrderAddressId()!=null&&orderItem.getSendType()==OrderInfo.SEND_TYPE_PTYG) {
            int giftNum = item.getGiftNum() == null ? 0 : item.getGiftNum();
            //计算理论重量
            Double weight = (orderItem.getGoodsNum() + giftNum) * goodsSku.getRealWeight().doubleValue();
            //计算实际重量
            BigDecimal realWeight = this.subRealWeight(weight);

            //查询收货地址信息
            OrderAddressOut address = orderAddressMapper.getAddress(orderItem.getOrderAddressId());
            if(address==null){
                throw new RuntimeException("地址信息有误,请联系客服!");
            }
            OrderAddressInVo orderAddressInVo = new OrderAddressInVo();
            orderAddressInVo.setDictProvinceId(address.getDictProvinceId());
            orderAddressInVo.setDictCityId(address.getDictCityId());
            //计算单个订单项的运费 默认传了一个件数和0体积
            sendAmount = deliveryCostService
                    .calculateCost(orderAddressInVo, realWeight,new Integer(1),BigDecimal.ZERO, goodsSku.getDeliveryTemplateId(),0);
        }
        return sendAmount;
    }

    /**
     * 计算实际重量
     * @param weight
     * @return
     */
    public BigDecimal subRealWeight(Double weight){
        Double[] area = costWeightConfig.getArea();
        Double[] costWeight = costWeightConfig.getWeight();
        BigDecimal realWeight = BigDecimal.ZERO;
        if(weight==0){
            realWeight = BigDecimal.ZERO;
        }else if(weight>0&&weight<=area[0]){
            realWeight = new BigDecimal(2.5);
        }else if(weight>area[0]&&weight<=area[1]){
            realWeight = BigDecimal.valueOf(weight).multiply(BigDecimal.valueOf(costWeight[0]));
        }else if(weight>area[1]&&weight<=area[2]){
            realWeight = BigDecimal.valueOf(weight).multiply(BigDecimal.valueOf(costWeight[1]));
        }else if(weight>area[2]){
            realWeight = BigDecimal.valueOf(weight).multiply(BigDecimal.valueOf(costWeight[2]));
        }
        return realWeight.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 保存订单明细信息和修改商品的库存和已售数量
     * @param inVo
     * @param orderItemList
     */
    private void saveOrderItem(OrderInfoInVo inVo, List<OrderItem> orderItemList) {
        for (OrderItem orderItem : orderItemList) {
            GoodsSkuOut goodsSku = goodsSkuMapper.selectDetailBySkuId(orderItem.getGoodsSkuId());
            //保存订单详情
            OrderItem item = new OrderItem();
            item.setOrderCode(inVo.getOrderCode());
            item.setGoodsSkuId(orderItem.getGoodsSkuId());
            item.setGoodsSpuId(orderItem.getGoodsSpuId());
            item.setShopId(orderItem.getOrderItemShopId());
            item.setGoodsSkuName(goodsSku.getSkuName());
            item.setGoodsNum(orderItem.getGoodsNum());
            item.setGoodsPrice(goodsSku.getSellPrice());
            item.setRemark(orderItem.getRemark());
            item.setRealUnitPrice(orderItem.getRealUnitPrice());
            List<GoodsPromotionRules> goodsPromotionRules = goodsSku.getGoodsPromotionRules();
            Collections.sort(goodsPromotionRules);//按照ruleType从小到大排序
            //目前这种模式没办法考虑同种规则类型，不同规则定义的标准(比如满10盒送一盒同时又满20盒送3盒)
            for (GoodsPromotionRules goodsPromotionRule : goodsPromotionRules) {
                if(goodsPromotionRule.getRuleType()==GoodsPromotionRules.RULE_TYPE_ADD){
                    double floor = Math.floor((double)orderItem.getGoodsNum() / goodsPromotionRule.getManNum())
                            *goodsPromotionRule.getGiftNum();

                    item.setGiftNum((int) floor);
                }
                if(goodsPromotionRule.getRuleType()==GoodsPromotionRules.RULE_TYPE_PACKING){
                    int giftNum = item.getGiftNum() == null ? 0 : item.getGiftNum();
                    double amount = Math.ceil((double)(orderItem.getGoodsNum()+giftNum) / goodsPromotionRule.getManNum());
                    double floor = amount*goodsPromotionRule.getGiftNum();

                    item.setPackingPrice(BigDecimal.valueOf(floor).setScale(2));
                }
            }
            orderItemMapper.insert(item);

            //修改库存和已售数量
            if(null!=goodsSku.getAutoAddStock()&&goodsSku.getAutoAddStock()==GoodsSku.AUTO_ADD_STOCK_YES&&goodsSku.getStockNum() - orderItem.getGoodsNum()==0){//活动商品设置了自动补充库存，且购买后库存会降为0时，库存自动补充到15份
                goodsSku.setStockNum(15);
            }else{
                goodsSku.setStockNum(goodsSku.getStockNum() - orderItem.getGoodsNum());
            }
            goodsSku.setSellNum(goodsSku.getSellNum() + orderItem.getGoodsNum());
            GoodsSkuInVo goodsSkuInVo = new GoodsSkuInVo();
            BeanUtils.copyProperties(goodsSku, goodsSkuInVo);
            goodsSkuMapper.updateByPrimaryKeySelective(goodsSkuInVo);
        }
    }

    /**
     * 保存订单明细信息和修改商品的库存和已售数量
     * @param inVo
     * @param orderItemList
     */
    private void batchSaveOrderItem(OrderInfoInVo inVo, List<OrderItem> orderItemList) {
        for (OrderItem orderItem : orderItemList) {
            GoodsSkuOut goodsSku = goodsSkuMapper.selectDetailBySkuId(orderItem.getGoodsSkuId());
            //保存订单详情
            OrderItem item = new OrderItem();
            item.setOrderCode(inVo.getOrderCode());
            item.setGoodsSkuId(orderItem.getGoodsSkuId());
            item.setGoodsSpuId(orderItem.getGoodsSpuId());
            item.setShopId(orderItem.getOrderItemShopId());
            item.setGoodsSkuName(goodsSku.getSkuName());
            item.setGoodsNum(orderItem.getGoodsNum());
            item.setGoodsPrice(goodsSku.getSellPrice());
            item.setRemark(orderItem.getRemark());
            item.setRealUnitPrice(orderItem.getRealUnitPrice());
            item.setCreateTime(inVo.getCreateTime());
            orderItemMapper.insert(item);
        }
    }


    /**
     * 保存订单明细信息和修改商品的活动库存
     * @param inVo
     * @param orderItemList
     */
    private void saveOrderItemForAct(OrderInfoInVo inVo, List<OrderItem> orderItemList) {
        for (OrderItem orderItem : orderItemList) {
            GoodsSkuOut goodsSku = goodsSkuMapper.selectDetailBySkuId(orderItem.getGoodsSkuId());
            //保存订单详情
            OrderItem item = new OrderItem();
            item.setOrderCode(inVo.getOrderCode());
            item.setGoodsSkuId(orderItem.getGoodsSkuId());
            item.setGoodsSpuId(orderItem.getGoodsSpuId());
            item.setShopId(orderItem.getOrderItemShopId());
            item.setGoodsSkuName(goodsSku.getSkuName());
            if (inVo.getFlagType()==OrderInfo.FLAG_TYPE_MS){
                item.setRealUnitPrice(BigDecimal.ZERO);
            }else {
                item.setRealUnitPrice(orderItem.getRealUnitPrice());
            }

            item.setGoodsNum(orderItem.getGoodsNum());
            item.setGoodsPrice(goodsSku.getSellPrice());
            item.setRemark(orderItem.getRemark());
            List<GoodsPromotionRules> goodsPromotionRules = goodsSku.getGoodsPromotionRules();
            Collections.sort(goodsPromotionRules);//按照ruleType从小到大排序
            //目前这种模式没办法考虑同种规则类型，不同规则定义的标准(比如满10盒送一盒同时又满20盒送3盒)
            for (GoodsPromotionRules goodsPromotionRule : goodsPromotionRules) {
                if(goodsPromotionRule.getRuleType()==GoodsPromotionRules.RULE_TYPE_ADD){
                    double floor = Math.floor((double)orderItem.getGoodsNum() / goodsPromotionRule.getManNum())
                            *goodsPromotionRule.getGiftNum();

                    item.setGiftNum((int) floor);
                }
                if(goodsPromotionRule.getRuleType()==GoodsPromotionRules.RULE_TYPE_PACKING){
                    int giftNum = item.getGiftNum() == null ? 0 : item.getGiftNum();
                    double amount = Math.ceil((double)(orderItem.getGoodsNum()+giftNum) / goodsPromotionRule.getManNum());
                    double floor = amount*goodsPromotionRule.getGiftNum();

                    item.setPackingPrice(BigDecimal.valueOf(floor).setScale(2));
                }
            }
            orderItemMapper.insert(item);

            //修改已售数量
            goodsSku.setSellNum(goodsSku.getSellNum() + orderItem.getGoodsNum());
            GoodsSkuInVo goodsSkuInVo = new GoodsSkuInVo();
            BeanUtils.copyProperties(goodsSku, goodsSkuInVo);
            goodsSkuMapper.updateByPrimaryKeySelective(goodsSkuInVo);

            //修改活动库存
            ActGoodsSkuInVo actGoodsSkuInVo = new ActGoodsSkuInVo();
            actGoodsSkuInVo.setActId(inVo.getActId());
            actGoodsSkuInVo.setSkuId(inVo.getOrderItemList().get(0).getGoodsSkuId());
            ActGoodsSkuOut actGoodsSkuOut=actGoodsSkuMapper.selectByActId(actGoodsSkuInVo);
            actGoodsSkuInVo.setId(actGoodsSkuOut.getId());
            actGoodsSkuInVo.setStockNumType(ActGoodsSku.STOCK_NUM_TYPE_DOWN);
            actGoodsSkuInVo.setStockNum(orderItem.getGoodsNum());
            //活动商品设置了自动补充库存，预警库存不为空，且购买后库存会降为0时，库存自动补充到15份
            if(null!=actGoodsSkuOut.getAutoAddStock()&&actGoodsSkuOut.getAutoAddStock()==ActGoodsSku.AUTO_ADD_STOCK_YES&&actGoodsSkuOut.getStockNum()-orderItem.getGoodsNum()==0){//
                ActGoodsSku actGoodsSku=new ActGoodsSku();
                actGoodsSku.setId(actGoodsSkuOut.getId());
                actGoodsSku.setStockNum(15);
                actGoodsSkuMapper.updateByPrimaryKeySelective(actGoodsSku);
            }else{
                actGoodsSkuMapper.updateCutDownStockNum(actGoodsSkuInVo);
            }
        }
    }

    /**
     * 判断是否向商家推送库存通知
     * @param inVo
     */
    private void waringStock(OrderInfoInVo inVo) {
        List<OrderItem> orderItemList = inVo.getOrderItemList();//获取订单里面的详细内容
        for (OrderItem orderItem : orderItemList) {
            GoodsSkuOut goodsSku = goodsSkuMapper.selectDetailBySkuId(orderItem.getGoodsSkuId());
            //如果活动id不为空和购买类型判断活动库存
            if(inVo.getActId()!=null&&inVo.getFlagType()!=OrderInfo.FLAG_TYPE_PT){
                Date time = new Date();
                ActGoodsSkuInVo actGoodsSkuInVo=new ActGoodsSkuInVo();
                actGoodsSkuInVo.setActId(inVo.getActId());
                actGoodsSkuInVo.setSkuId(orderItem.getGoodsSkuId());
                ActGoodsSkuOut actGoodsSkuOut=actGoodsSkuMapper.selectByActId(actGoodsSkuInVo);
                if (actGoodsSkuOut.getWaringStock()!=null&&actGoodsSkuOut.getAutoAddStock()== ActGoodsSku.AUTO_ADD_STOCK_NO){//活动商品设置了自动补充库存不推送消息
                    ActInfo actInfo = actInfoMapper.selectByPrimaryKey(inVo.getActId());
                    //判断活动库存数量是否小于购买的数量
                    if(actGoodsSkuOut.getStockNum()-orderItem.getGoodsNum()<=actGoodsSkuOut.getWaringStock()){
                        if (inVo.getShopId()!=null&&inVo.getShopId()>0){
                            UserInVo userInVo=new UserInVo();
                            userInVo.setShopId(inVo.getShopId());
                            List<User> user=userService.listForShopId(userInVo);
                            for (int i =0;i<user.size();i++){
                                PushMsg pushMsg= new PushMsg();
                                pushMsg.setAlias(user.get(i).getId().toString());
                                pushMsg.setTitle("商品" +goodsSku.getSkuName()+":库存预警通知");
                                pushMsg.setMessageInfo("您的"+actInfo.getActName()+"商品" + ":"+goodsSku.getSkuName()+"-库存不足" + actGoodsSkuOut.getStockNum()+"（份）");
                                pushMsg.setIos("2,进入商品详情页面");
                                pushMsg.setType("android");
                                pushMsg.setBadge("1");
                                GtPush.sendMessage(pushMsg);
                            }
                        }
                    }
                }else {
                    //判断实际库存数量是否小于购买的数量
                    if (goodsSku.getWaringStock()!=null&&goodsSku.getAutoAddStock()==GoodsSku.AUTO_ADD_STOCK_NO){//商品设置了自动补充库存不推送消息
                        if(goodsSku.getStockNum()-orderItem.getGoodsNum()<=goodsSku.getWaringStock()){
                            if (inVo.getShopId()!=null&&inVo.getShopId()>0){
                                UserInVo userInVo=new UserInVo();
                                userInVo.setShopId(inVo.getShopId());
                                List<User> user=userService.listForShopId(userInVo);
                                for (int i =0;i<user.size();i++){
                                    PushMsg pushMsg= new PushMsg();
                                    pushMsg.setAlias(user.get(i).getId().toString());
                                    pushMsg.setTitle("商品" +goodsSku.getSkuName()+":库存预警通知");
                                    pushMsg.setMessageInfo("您的商品" + ":"+goodsSku.getSkuName()+"-库存不足" + goodsSku.getStockNum() +"（份）");
                                    pushMsg.setIos("2,进入商品详情页面");
                                    pushMsg.setType("android");
                                    pushMsg.setBadge("1");
                                    GtPush.sendMessage(pushMsg);
                                }
                            }
                        }
                    }

                }
                }
        }
    }

    /**
     * 批量造单保存订单明细信息和修改商品的活动库存
     * @param inVo
     * @param orderItemList
     */
    private void batchSaveOrderItemForAct(OrderInfoInVo inVo, List<OrderItem> orderItemList) {
        for (OrderItem orderItem : orderItemList) {
            GoodsSkuOut goodsSku = goodsSkuMapper.selectDetailBySkuId(orderItem.getGoodsSkuId());
            //保存订单详情
            OrderItem item = new OrderItem();
            item.setOrderCode(inVo.getOrderCode());
            item.setGoodsSkuId(orderItem.getGoodsSkuId());
            item.setGoodsSpuId(orderItem.getGoodsSpuId());
            item.setShopId(orderItem.getOrderItemShopId());
            item.setGoodsSkuName(goodsSku.getSkuName());
            item.setRealUnitPrice(orderItem.getRealUnitPrice());
            item.setGoodsNum(orderItem.getGoodsNum());
            item.setGoodsPrice(goodsSku.getSellPrice());
            item.setRemark(orderItem.getRemark());
            item.setCreateTime(inVo.getCreateTime());
            orderItemMapper.batchInsert(item);
        }
    }

    private void saveOrderLog(OrderInfoInVo inVo, int optWaitPaid) {
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderCode(inVo.getOrderCode());
        orderLog.setOperateType(optWaitPaid);
        orderLog.setUserId(inVo.getUserId());
        orderLog.setUserName(inVo.getUserName());
        orderLog.setUserIp(inVo.getUserIp());
        orderLogMapper.insert(orderLog);
    }

    private void batchSaveOrderLog(OrderInfoInVo inVo, int optWaitPaid) {
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderCode(inVo.getOrderCode());
        orderLog.setOperateType(optWaitPaid);
        orderLog.setUserId(inVo.getUserId());
        orderLog.setUserName(inVo.getUserName());
        orderLog.setUserIp(inVo.getUserIp());
        orderLog.setCreateTime(inVo.getCreateTime());
        orderLogMapper.batchInsert(orderLog);
    }

    @Override
    @Transactional
    public int paid(OrderInfoInVo inVo) {
        //1、更新订单状态
        inVo.setStatus(OrderInfo.STATUS_WAIT_SH);
        int ret = orderInfoMapper.paid(inVo);
        if (ret > 0) {
            //2、支付日志
            this.savePayLog(inVo);
            //3、订单日志
            this.saveOrderLog(inVo, OrderLog.OPT_WAIT_SH);
        }
        return ret;
    }

    @Override
    @Transactional
    public int paidCoupon(OrderInfoInVo inVo) {
        //1、更新订单状态
        inVo.setStatus(OrderInfo.STATUS_IS_SUCCESS);
        int ret = orderInfoMapper.paid(inVo);
        if (ret > 0) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, 3);     //有效时间为3个月
            //2、支付成功生成抵用券
            this.createCoupon(inVo);
            //3、支付日志
            this.savePayLog(inVo);
            //4、订单日志
            this.saveOrderLog(inVo, OrderLog.OPT_WAIT_SH);
        }
        return ret;
    }

    @Override
    @Transactional
    public int paidCouponNew(OrderInfoInVo inVo, WeixinInVo weixinInVo){
        //1、更新订单状态
        OrderInfo orderInfo=  orderInfoMapper.selectByPrimaryKey(inVo.getId());
        if(orderInfo.getStatus()==OrderInfo.STATUS_IS_SUCCESS)return 0;//支付之前查询一次判断订单状态，避免重复生成票卷

        inVo.setStatus(OrderInfo.STATUS_IS_SUCCESS);
        int ret = orderInfoMapper.paid(inVo);
        if (ret > 0) {
            //2、支付成功生成抵用券
            this.createCouponNew(inVo);
            //3、支付日志
            this.savePayLog(inVo);
            //4、订单日志
            this.saveOrderLog(inVo, OrderLog.OPT_WAIT_SH);
            //5.给用户账户分配金币
            this.updateUserGold(inVo, weixinInVo);
        }
        return ret;
    }


    public int batchPaidCoupon(OrderInfoInVo inVo, WeixinInVo weixinInVo) {
        //1、更新订单状态
        inVo.setStatus(OrderInfo.STATUS_IS_SUCCESS);
        int ret = orderInfoMapper.batchPaid(inVo);
        if (ret > 0) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, 3);     //有效时间为3个月
            //2、支付成功生成抵用券
            this.batchCreateCoupon(inVo);
            //3、支付日志
            this.batchSavePayLog(inVo);
            //4、订单日志
            this.batchSaveOrderLog(inVo, OrderLog.OPT_WAIT_SH);
        }
        return ret;
    }

    @Override
    @Transactional
    public int paidShopOrder(OrderInfoInVo inVo, WeixinInVo attachInVo) {
        //1、更新订单状态
        inVo.setStatus(OrderInfo.STATUS_IS_SUCCESS);
        ShopAllocationInVo shopAllocationInVo = new ShopAllocationInVo();
        shopAllocationInVo.setShopId(inVo.getShopId());
        ShopAllocationOut shopAllocationOut = shopAllocationMapper.selectByPrimaryKey(shopAllocationInVo);
        ShopCashier shopCashier = shopCashierMapper.adminByShopId(inVo.getShopId());
        if(shopAllocationOut.getPaymentMethod()==ShopAllocation.SHOP_ALLOCATION_DS){
            //注意完成对账功能之前，一定要保证该券先调用核销
            //平台代收，要收取服务费，并且还要完成对账操作(把核销之后的券的对账改成已对账状态)
            UserAccountInVo accountInVo = new UserAccountInVo();
            accountInVo.setUserId(shopCashier.getCashierId());//商家管理员的用户id
            accountInVo.setOccurAmount(inVo.getRealAmount());
            accountService.income(accountInVo, "用户买单，订单号：" + inVo.getOrderCode());

        }else if(shopAllocationOut.getPaymentMethod()==ShopAllocation.SHOP_ALLOCATION_ZS){
            //商家自收，服务费另外算，不完成对账操作,此代码需后期补充
           /* UserAccountInVo accountInVo = new UserAccountInVo();
            accountInVo.setUserId(inVo.getUserId());
            accountInVo.setOccurAmount(inVo.getSoAmount());
            accountService.income(accountInVo, "用户买单，订单号：" + inVo.getId());*/
        }
        int ret = orderInfoMapper.paid(inVo);
        if (ret > 0) {

            //3、支付日志
            this.savePayLog(inVo);
            //4、订单日志
            this.saveOrderLog(inVo, OrderLog.OPT_WAIT_SH);

        }
        return ret;
    }


    @Override
    @Transactional
    public int paidMDZT(OrderInfoInVo inVo) {
        //1、更新订单状态
        inVo.setStatus(OrderInfo.STATUS_WAIT_SH);
        int ret = orderInfoMapper.paid(inVo);
        if (ret > 0) {
            //2、支付成功生成二维码加入订单中
            this.createQrcodeUrl(inVo);
            //3、支付日志
            this.savePayLog(inVo);
            //4、订单日志
            this.saveOrderLog(inVo, OrderLog.OPT_WAIT_SH);
        }
        return ret;
    }

    @Override
    @Transactional
    public int paidRo(OrderInfoInVo inVo) {
        /**  更新子订单状态  */
        //拆单了的父订单里面的sendType为null,不拆单的生成的父订单里面的sendType不为null
        //查询拆单之后,父订单对应的子订单
        if(inVo.getSendType()==null){
            //查询父订单里面所有子订单
            OrderInfoOut orderInfoOut = new OrderInfoOut();
            orderInfoOut.setUserId(orderInfoOut.getUserId());
            orderInfoOut.setParentOrderId(orderInfoOut.getId());
            List<OrderInfoOut> orderInfoOuts = this.getSonOrderList(orderInfoOut);
            for (OrderInfoOut infoOut : orderInfoOuts) {
                OrderInfoInVo infoInVo = new OrderInfoInVo();
                BeanUtils.copyProperties(infoOut,infoInVo);
                //1、更新子订单状态
                infoInVo.setStatus(OrderInfo.STATUS_WAIT_SH);
                int ret = orderInfoMapper.paid(infoInVo);
                if (ret > 0) {
                    //2、如果是sendType==2为门店自提,那么要生成对应的二维码
                    if(infoInVo.getSendType()==OrderInfo.SEND_TYPE_MDZT) {
                        this.createQrcodeUrl(infoInVo);
                    }
                    //3、支付日志
                    this.savePayLog(infoInVo);
                    //4、订单日志
                    this.saveOrderLog(infoInVo, OrderLog.OPT_WAIT_SH);
                }
            }
        }
        /**  更新父订单状态  */
        //1、更新父订单状态
        inVo.setStatus(OrderInfo.STATUS_WAIT_SH);
        int ret = orderInfoMapper.paid(inVo);
        if (ret > 0) {
            //2、支付成功生成二维码加入订单中
            this.createQrcodeUrl(inVo);
            //3、支付日志
            this.savePayLog(inVo);
            //4、订单日志
            this.saveOrderLog(inVo, OrderLog.OPT_WAIT_SH);
        }
        return ret;
    }

    @Override
    @Transactional
    public Integer useOrderInfo(OrderInfoInVo inVo) {
        //1、更新订单状态
        inVo.setStatus(OrderInfo.STATUS_IS_SUCCESS);
        int ret = orderInfoMapper.paid(inVo);
        if (ret > 0) {
            //2、订单日志
            this.saveOrderLog(inVo, OrderLog.OPT_IS_SUCCESS);
        }
        return ret;
    }

    @Override
    public List<OrderInfoInVo> getAllOrderList(OrderInfoInVo inVo) {
        List<OrderItem> orderItemList = inVo.getOrderItemList();//获取订单里面的详细内容
        //https://blog.csdn.net/my_precious/article/details/53010232
        Set<OrderItem> orderItemSet = new HashSet<OrderItem>(orderItemList);//通过set集合直接去重,来判断拆单
        List<OrderItem> items = new ArrayList<>(orderItemSet);
        List<List<OrderItem>>  orderItemDifList = new ArrayList<List<OrderItem>>();

        //将订单拆分,如果只有父订单orderItemDifList.size()==1
        for (OrderItem item : items) {
            List<OrderItem> orderItems = new ArrayList<OrderItem>();
            for (OrderItem orderItem : orderItemList) {
                if(orderItem.equals(item)){
                    orderItems.add(orderItem);
                }
            }
            orderItemDifList.add(orderItems);
        }

        //订单的所有集合(每个OrderInfoInVo都代表一个订单,如果该集合内只有一个OrderInfoInVo,那么他就是一个父订单)
        List<OrderInfoInVo> orderInfoInVoList = this.getAllOrderInfoInVoList(orderItemDifList, inVo);
        return orderInfoInVoList;
    }

    /**
     * 查询父订单里面所有子订单
     * @param orderInfoOut
     * @return
     */
    @Override
    public List<OrderInfoOut> getSonOrderList(OrderInfoOut orderInfoOut) {
        OrderInfoInVo infoInVo = new OrderInfoInVo();
        infoInVo.setUserId(orderInfoOut.getUserId());
        infoInVo.setParentOrderId(orderInfoOut.getId());
        infoInVo.setIsParent(OrderInfo.IS_PARENT_NO);
        List<OrderInfoOut> orderInfoOuts = orderInfoMapper.list(infoInVo);
        return orderInfoOuts;
    }

    @Override
    public Map<String, String> queryShopTurnover(OrderInfoInVo inVo) {
        OrderInfoOut orderInfoOut = orderInfoMapper.queryCashAmount(inVo);
        OrderInfoOut shopTurnover = orderInfoMapper.queryShopTurnover(inVo);
        OrderWriteOffInVo orderWriteOffInVo = new OrderWriteOffInVo();
        orderWriteOffInVo.setShopId(inVo.getShopId());
        orderWriteOffInVo.setBeginTime(inVo.getBeginTime());
        orderWriteOffInVo.setEndTime(inVo.getEndTime());
        Integer orderWriteOffTotal = orderWriteOffMapper.listTotal(orderWriteOffInVo);
        Map<String,String> map = new HashMap<String,String>();
        map.put("duiPrice",orderInfoOut.getDuiPrice().toString());//对账的所有实际营业额
        map.put("noDuiPrice", orderInfoOut.getNoDuiPrice().toString());//未对账的所有实际营业额
        map.put("allPrice", orderInfoOut.getAllPrice().toString());//所有实际营业额
        map.put("shopTurnover", shopTurnover.getAllPrice().toString());//所有理论的营业额
        map.put("orderTotal", shopTurnover.getItemTotal().toString());//订单的数目
        map.put("orderWriteOffTotal", orderWriteOffTotal.toString());//核销票券的数目
        return map;
    }

    @Override
    public Map<String, String> queryShopTurnoverV1(OrderInfoInVo inVo) {
        Date beginTime = inVo.getBeginTime();
        Date endTime = inVo.getEndTime();
        //获取日期前一天
        Date beginTimeBefore = this.getSpecifiedDay(beginTime, -1);
        Date endTimeBefore = this.getSpecifiedDay(endTime, -1);
        OrderInfoInVo beforeInVo = new OrderInfoInVo();
        beforeInVo.setShopId(inVo.getShopId());
        beforeInVo.setBeginTime(beginTimeBefore);
        beforeInVo.setEndTime(endTimeBefore);
        //查询商家端商家订单营业额
        OrderInfoOut shopOrderTurnover = orderInfoMapper.queryShopOrderTurnover(inVo);
        //查询商家端商家昨日订单营业额
        OrderInfoOut beforeShopOrderTurnover = orderInfoMapper.queryShopOrderTurnover(beforeInVo);
        OrderWriteOffInVo orderWriteOffInVo = new OrderWriteOffInVo();
        orderWriteOffInVo.setShopId(inVo.getShopId());
        orderWriteOffInVo.setBeginTime(beginTime);
        orderWriteOffInVo.setEndTime(endTime);
        OrderWriteOffInVo orderWriteOffBeforeInVo = new OrderWriteOffInVo();
        orderWriteOffBeforeInVo.setShopId(inVo.getShopId());
        orderWriteOffBeforeInVo.setBeginTime(beginTimeBefore);
        orderWriteOffBeforeInVo.setEndTime(endTimeBefore);
        //查询商家端用户订单营业额和核销数目
        List<OrderWriteOffOut> orderWriteOffOuts = orderWriteOffMapper.selectTotalAmount(orderWriteOffInVo);
        //查询商家端用户昨日订单营业额和核销数目
        List<OrderWriteOffOut> orderWriteOffOutsBefore = orderWriteOffMapper.selectTotalAmount(orderWriteOffBeforeInVo);
        Map<String,String> map = new HashMap<String,String>();
        map.put("allShopOrderPrice",shopOrderTurnover.getAllShopOrderPrice().toString());//商家端商家订单总营业额
        map.put("beforeAllShopOrderPrice",beforeShopOrderTurnover.getAllShopOrderPrice().toString());//昨日商家端商家订单总营业额
        if(orderWriteOffOuts!=null&&orderWriteOffOuts.size()>0) {
            OrderWriteOffOut orderWriteOffOut = orderWriteOffOuts.get(0);
            map.put("orderWriteOffTotal", orderWriteOffOut.getTotalItem().toString());//核销票券的数目
            map.put("allRealShopUnitPrice",orderWriteOffOut.getTotalShopPrice().toString());//商家端用户订单总营业额
            map.put("allShopTurnover",orderWriteOffOut.getTotalShopPrice().
                    add(shopOrderTurnover.getAllShopOrderPrice()).toString());//商家总营业额
            OrderWriteOffOut beforeOrderWriteOffOut = orderWriteOffOutsBefore.get(0);
            map.put("beforeOrderWriteOffTotal", beforeOrderWriteOffOut.getTotalItem().toString());//昨日核销票券的数目
            map.put("beforeAllRealShopUnitPrice",beforeOrderWriteOffOut.getTotalShopPrice().toString());//昨日商家端用户订单总营业额
            map.put("beforeAllShopTurnover",beforeOrderWriteOffOut.getTotalShopPrice().
                    add(beforeShopOrderTurnover.getAllShopOrderPrice()).toString());//昨日商家总营业额
        }
        return map;
    }


    /**
     * 获取指定日期的前(后)n天
     * @param date
     * @return
     */
    private Date getSpecifiedDay(Date date,int n){
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(date);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, n);  //设置为前(后)n天
        return calendar.getTime();   //得到前一天的时间
    }


    private void createQrcodeUrl(OrderInfoInVo inVo) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);     //有效时间为3个月
        inVo.setExpiryDate(cal.getTime());
        //上传二维码图片到腾讯COS服务器
        String qrcodeUrl = uploadQRCodeToCos(inVo.getOrderCode());
        inVo.setQrcodeUrl(qrcodeUrl);
        orderInfoMapper.createQrcodeUrl(inVo);
    }


    private int savePayLog(OrderInfoInVo inVo) {
        int res = 0;
        PaidLog paidLog = new PaidLog();
        paidLog.setOrderCode(inVo.getOrderCode());
        paidLog.setPaidType(So.SO_PAY_TYPE_WX);
        paidLog.setUserId(inVo.getUserId());
        paidLog.setUserName(inVo.getUserName());
        try {
            res = paidLogMapper.insert(paidLog);
        } catch (Exception e) {
            logger.error("支付保存订单日志失败,orderId : " + inVo.getId());
        }
        return res;
    }

    private int batchSavePayLog(OrderInfoInVo inVo) {
        int res = 0;
        PaidLog paidLog = new PaidLog();
        paidLog.setOrderCode(inVo.getOrderCode());
        paidLog.setPaidType(So.SO_PAY_TYPE_WX);
        paidLog.setUserId(inVo.getUserId());
        paidLog.setUserName(inVo.getUserName());
        paidLog.setCreateTime(inVo.getCreateTime());
        try {
            res = paidLogMapper.batchInsert(paidLog);
        } catch (Exception e) {
            logger.error("支付保存订单日志失败,orderId : " + inVo.getId());
        }
        return res;
    }

    /**
     * 生成券二维码图片并上传到腾讯云服务器
     * @param orderCode
     * @return
     */
    private String uploadQRCodeToCos(String orderCode) {
        String imagePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "static" + File.separator + "images" + File.separator + "logo.jpg";
        String destPath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "upload" + File.separator + orderCode + ".jpg";
        String text = constantsConfig.getDomainXqUrl() + "/orderInfo/getDetailByOrderCode/"+orderCode;

        //生成logo图片到destPath
        try {
            QRCodeUtil.encode(text, imagePath, destPath, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //上传文件到腾讯云cos--缩放0.8
        String imgUrl = uploadService.uploadFileToCos(destPath, "orderInfo");
        int i=0;
        do {
            i++;
            if (imgUrl==null){
                imgUrl=uploadService.uploadFileToCos(destPath, "orderInfo");
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
     * 生成券二维码图片并上传到腾讯云服务器
     * @param couponCode
     * @return
     */
    private String uploadOrderCouponQRCodeToCos(String couponCode) {
        String imagePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "static" + File.separator + "images" + File.separator + "logo.jpg";
        String destPath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "upload" + File.separator + couponCode + ".jpg";
        String text = constantsConfig.getDomainXqUrl() + "/orderCoupon/getByCode/"+couponCode;

        //生成logo图片到destPath
        try {
            QRCodeUtil.encode(text, imagePath, destPath, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //上传文件到腾讯云cos--缩放0.8
        String imgUrl = uploadService.uploadFileToCos(destPath, "orderCoupon");
        int i=0;
        do {
            i++;
            if (imgUrl==null){
                imgUrl=uploadService.uploadFileToCos(destPath, "orderCoupon");
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

    @Override
    @Transactional
    public Integer confirmCeceipt(Long id) {
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(id);
        orderInfo.setStatus(OrderInfo.STATUS_IS_SUCCESS);
        OrderInfoInVo infoInVo = new OrderInfoInVo();
        BeanUtils.copyProperties(orderInfo, infoInVo);
        User userById = userMapper.selectByPrimaryKey(infoInVo.getUserId());
        if(userById!=null){
            infoInVo.setUserName(userById.getUserName());
        }
        int i = orderInfoMapper.updateByPrimaryKeySelective(infoInVo);
        if(i>1){
            this.saveOrderLog(infoInVo, OrderLog.OPT_IS_SUCCESS);
        }

        return i;
    }

    @Override
    public OrderInfo getByCode(String orderCode) {
        return orderInfoMapper.selectByCode(orderCode);
    }

    /**
     * 根据订单信息生成代金券(老)-----以后删除
     * @param inVo
     * @return
     */
    private int createCoupon(OrderInfoInVo inVo){
        OrderInfoOut detail = orderInfoMapper.getDetail(inVo.getId());//查询订单详情
        List<OrderItemOut> orderItemOuts = detail.getOrderItemOuts();//查询订单项详情
        OrderItemOut orderItemOut = orderItemOuts.get(0);//查询第一个订单项详情,虚拟订单没有购物车概念
        int res = 0;
        //2、查询商品信息
        GoodsSku sku = goodsSkuMapper.selectByPrimaryKey(orderItemOut.getGoodsSkuId());

        //根据订单生成相应的票券
        res = this.insertCoupon(inVo, detail, orderItemOut, sku);
        return res;
    }

    /**
     * 根据订单信息生成代金券(新)
     * @param inVo
     * @return
     */
    private int createCouponNew(OrderInfoInVo inVo) {
        OrderInfoOut detail = orderInfoMapper.getDetail(inVo.getId());//查询订单详情
        List<OrderItemOut> orderItemOuts = detail.getOrderItemOuts();//查询订单项详情
        OrderItemOut orderItemOut = orderItemOuts.get(0);//查询第一个订单项详情,虚拟订单没有购物车概念
        int res = 0;
        //2、查询商品信息
        GoodsSku sku = goodsSkuMapper.selectByPrimaryKey(orderItemOut.getGoodsSkuId());

        //根据订单生成相应的票券
        res = this.insertCouponNew(inVo, detail, orderItemOut, sku);

        //修改用户活动订单用户状态
        //this.updateActOrderStatus(inVo);
        return res;
    }

    /**
     * 批量造单根据订单信息生成代金券(新)
     * @param inVo
     * @return
     */
    private int batchCreateCoupon(OrderInfoInVo inVo){
        OrderInfoOut detail = orderInfoMapper.getDetail(inVo.getId());//查询订单详情
        List<OrderItemOut> orderItemOuts = detail.getOrderItemOuts();//查询订单项详情
        OrderItemOut orderItemOut = orderItemOuts.get(0);//查询第一个订单项详情,虚拟订单没有购物车概念
        int res = 0;
        //2、查询商品信息
        GoodsSku sku = goodsSkuMapper.selectByPrimaryKey(orderItemOut.getGoodsSkuId());

        //根据订单生成相应的票券
        res = this.batchInsertCoupon(inVo, detail, orderItemOut, sku);
        return res;
    }

    /**
     * 修改用户活动订单用户状态
     * @param inVo
     * @return
     */
    public int updateActOrderStatus(OrderInfoInVo inVo){
        ActOrderOut actOrder=actOrderMapper.selectFirstDistributionByOrderId(inVo.getId());
        if (actOrder==null||actOrder.getId()==null){
            return 0;
        }
        ActInfo actInfo = actInfoMapper.selectByPrimaryKey(actOrder.getActGoodsSku().getActId());
        if (actInfo.getType()==OrderInfo.FLAG_TYPE_TG){
            actOrder.setState(ActOrder.ACT_ORDER_GROUP_STATE_TUXEDO_JOINING);
            ActOrder actOrderInVo = new ActOrder();
            BeanUtils.copyProperties(actOrder, actOrderInVo);
            return actOrderMapper.updateUserState(actOrderInVo);
        }
        return 0;
    }



    /**
     * 根据订单的基本信息来生成相应的票券(老)----以后要删除
     * @param inVo
     * @param detail
     * @param orderItemOut
     * @param sku
     * @return
     */
    public int insertCoupon(OrderInfoInVo inVo,OrderInfoOut detail,OrderItemOut orderItemOut,GoodsSku sku){
        int res = 0;

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 2);     //有效时间为两年
        if(detail.getSingleType()==null) {//为线上兼容做的处理
            if (detail.getSendType() == OrderInfo.SEND_TYPE_PTYG) {
                //3、支付成功生成抵用券
                for (int i = 0; i < orderItemOut.getGoodsNum() + orderItemOut.getGiftNum(); i++) {
                    OrderCoupon coupon = new OrderCoupon();
                    coupon.setOrderId(inVo.getId());
                    coupon.setCouponCode(RandomStringUtil.getRandomCode(10, 0));
                    coupon.setGoodsSkuId(sku.getId());
                    coupon.setGoodsSkuCode(sku.getSkuCode());
                    coupon.setGoodsSkuName(sku.getSkuName());
                    coupon.setCouponAmount(sku.getSellPrice());
                    //平台邮购
                    coupon.setType(OrderCoupon.TYPE_LPQ);//后面可以在goods_sku或goods_spu里面加入一个type字段来判断类型
                    coupon.setSingleType(OrderCoupon.SINGLE_TYPE_FD);//分单
                    coupon.setFlagType(detail.getFlagType());
                    coupon.setUserId(inVo.getUserId());
                    coupon.setUserName(inVo.getUserName());
                    //上传二维码图片到腾讯COS服务器
                    String qrcodeUrl = uploadOrderCouponQRCodeToCos(coupon.getCouponCode());
                    coupon.setQrcodeUrl(qrcodeUrl);
                    coupon.setExpiryDate(cal.getTime());
                    coupon.setShopId(inVo.getShopId());
                    coupon.setOwnId(inVo.getUserId());
                    int insert = orderCouponMapper.insert(coupon);
                    if (insert < 1) {
                        throw new RuntimeException("生成票券失败，请联系客服!");
                    }
                    res++;
                }
            }else if (detail.getSendType() == OrderInfo.SEND_TYPE_MDZT) {
                OrderCoupon coupon = new OrderCoupon();
                coupon.setOrderId(inVo.getId());
                coupon.setCouponCode(RandomStringUtil.getRandomCode(10, 0));
                coupon.setGoodsSkuId(sku.getId());
                coupon.setGoodsSkuCode(sku.getSkuCode());
                coupon.setGoodsSkuName(sku.getSkuName());
                coupon.setCouponAmount(sku.getSellPrice());
                //门店自提
                coupon.setType(OrderCoupon.TYPE_MDZTQ);//后面可以在goods_sku或goods_spu里面加入一个type字段来判断类型
                coupon.setSingleType(OrderCoupon.SINGLE_TYPE_FD);//整单
                coupon.setFlagType(detail.getFlagType());
                coupon.setUserId(inVo.getUserId());
                coupon.setUserName(inVo.getUserName());
                //上传二维码图片到腾讯COS服务器
                String qrcodeUrl = uploadOrderCouponQRCodeToCos(coupon.getCouponCode());
                coupon.setQrcodeUrl(qrcodeUrl);
                coupon.setExpiryDate(cal.getTime());
                coupon.setShopId(inVo.getShopId());
                coupon.setOwnId(inVo.getUserId());
                int insert = orderCouponMapper.insert(coupon);
                if (insert < 1) {
                    throw new RuntimeException("生成票券失败，请联系客服!");
                }
                res++;
            }
            return res;
        }
        //分单
        if(detail.getSingleType()==OrderInfo.SINGLE_TYPE_FD){
            for (int i = 0; i < orderItemOut.getGoodsNum() + orderItemOut.getGiftNum(); i++) {
                OrderCoupon coupon = new OrderCoupon();
                coupon.setOrderId(inVo.getId());
                coupon.setCouponCode(RandomStringUtil.getRandomCode(10, 0));
                coupon.setGoodsSkuId(sku.getId());
                coupon.setGoodsSkuCode(sku.getSkuCode());
                coupon.setGoodsSkuName(sku.getSkuName());
                coupon.setCouponAmount(sku.getSellPrice());
                if(detail.getSendType()==OrderInfo.SEND_TYPE_PTYG) {
                    coupon.setType(OrderCoupon.TYPE_LPQ);//后面可以在goods_sku或goods_spu里面加入一个type字段来判断类型
                }else if(detail.getSendType()==OrderInfo.SEND_TYPE_MDZT){
                    coupon.setType(OrderCoupon.TYPE_MDZTQ);//后面可以在goods_sku或goods_spu里面加入一个type字段来判断类型
                }
                coupon.setSingleType(OrderCoupon.SINGLE_TYPE_FD);//分单
                coupon.setFlagType(detail.getFlagType());
                coupon.setUserId(inVo.getUserId());
                coupon.setUserName(inVo.getUserName());
                //上传二维码图片到腾讯COS服务器
                String qrcodeUrl = uploadOrderCouponQRCodeToCos(coupon.getCouponCode());
                coupon.setQrcodeUrl(qrcodeUrl);
                coupon.setExpiryDate(cal.getTime());
                coupon.setShopId(inVo.getShopId());
                coupon.setOwnId(inVo.getUserId());
                int insert = orderCouponMapper.insert(coupon);
                if (insert < 1) {
                    throw new RuntimeException("生成票券失败，请联系客服!");
                }
                res++;
            }
        }else if(detail.getSingleType()==OrderInfo.SINGLE_TYPE_ZD){//整单
            OrderCoupon coupon = new OrderCoupon();
            coupon.setOrderId(inVo.getId());
            coupon.setCouponCode(RandomStringUtil.getRandomCode(10, 0));
            coupon.setGoodsSkuId(sku.getId());
            coupon.setGoodsSkuCode(sku.getSkuCode());
            coupon.setGoodsSkuName(sku.getSkuName());
            coupon.setCouponAmount(sku.getSellPrice());
            //门店自提
            if(detail.getSendType()==OrderInfo.SEND_TYPE_PTYG) {
                coupon.setType(OrderCoupon.TYPE_LPQ);//后面可以在goods_sku或goods_spu里面加入一个type字段来判断类型
            }else if(detail.getSendType()==OrderInfo.SEND_TYPE_MDZT){
                coupon.setType(OrderCoupon.TYPE_MDZTQ);//后面可以在goods_sku或goods_spu里面加入一个type字段来判断类型
            }
            coupon.setSingleType(OrderCoupon.SINGLE_TYPE_ZD);//整单
            coupon.setFlagType(detail.getFlagType());
            coupon.setUserId(inVo.getUserId());
            coupon.setUserName(inVo.getUserName());
            //上传二维码图片到腾讯COS服务器
            String qrcodeUrl = uploadOrderCouponQRCodeToCos(coupon.getCouponCode());
            coupon.setQrcodeUrl(qrcodeUrl);
            coupon.setExpiryDate(cal.getTime());
            coupon.setShopId(inVo.getShopId());
            coupon.setOwnId(inVo.getUserId());
            int insert = orderCouponMapper.insert(coupon);
            if (insert < 1) {
                throw new RuntimeException("生成票券失败，请联系客服!");
            }
            res++;
        }

        return res;
    }

    /**
     * 根据订单的基本信息来生成相应的票券(新)
     * @param inVo
     * @param detail
     * @param orderItemOut
     * @param sku
     * @return
     */
    public int insertCouponNew(OrderInfoInVo inVo,OrderInfoOut detail,OrderItemOut orderItemOut,GoodsSku sku){
        int res = 0;
        GoodsSpu goodsSpu = goodsSpuMapper.selectByPrimaryKey(sku.getSpuId());//查询category_id
        Map<String,BigDecimal> map = this.subServiceAmount(detail, sku,goodsSpu,orderItemOut);//查询总服务费
        //分单
        if(detail.getSingleType()==OrderInfo.SINGLE_TYPE_FD){
            for (int i = 0; i < orderItemOut.getGoodsNum() + orderItemOut.getGiftNum(); i++) {
                inVo.setGoodsSkuId(sku.getId());
                OrderCoupon coupon = new OrderCoupon();
                coupon.setOrderId(inVo.getId());
                coupon.setCouponCode(RandomStringUtil.getRandomCode(10, 0));
                coupon.setGoodsSkuId(sku.getId());
                coupon.setGoodsSkuCode(sku.getSkuCode());
                coupon.setGoodsSkuName(sku.getSkuName());
                coupon.setCouponAmount(sku.getSellPrice());
                coupon.setSingleType(OrderCoupon.SINGLE_TYPE_FD);//分单
                coupon.setFlagType(detail.getFlagType());
                coupon.setUserId(inVo.getUserId());
                coupon.setStatus(0);
                coupon.setUserName(inVo.getUserName());
                //上传二维码图片到腾讯COS服务器
                String qrcodeUrl = uploadOrderCouponQRCodeToCos(coupon.getCouponCode());
                coupon.setQrcodeUrl(qrcodeUrl);
                this.setExpiryDate(inVo, coupon, sku);//设置过期时间
                coupon.setShopId(inVo.getShopId());
                coupon.setOwnId(inVo.getUserId());
                if(i<orderItemOut.getGiftNum()) {
                    coupon.setRealUnitPrice(BigDecimal.ZERO);//赠品的实际支付单价为0
                }else{
                    coupon.setRealUnitPrice(orderItemOut.getRealUnitPrice());//分单票券里面的实际支付单价
                }
                //判断是否是参团订单生成的票券
                ActOrderOut actOrder=actOrderMapper.selectFirstDistributionByOrderId(inVo.getId());
                if (actOrder==null||actOrder.getId()==null){
                    coupon.setIsShow(OrderCoupon.IS_SHOW);
                }else {
                    inVo.setActId(actOrder.getActGoodsSku().getActId());
                    ActInfo actInfo = actInfoMapper.selectByPrimaryKey(actOrder.getActGoodsSku().getActId());
                    if (actInfo.getType()==OrderInfo.FLAG_TYPE_TG){
                        coupon.setIsShow(OrderCoupon.IS_NOT_SHOW);
                    }else {
                        coupon.setIsShow(OrderCoupon.IS_SHOW);
                    }
                }
                coupon.setServiceAmount(map.get("serviceAmount").divide(BigDecimal.valueOf(orderItemOut.getGoodsNum())).setScale(2, BigDecimal.ROUND_HALF_UP));
                coupon.setShopServiceAmount(map.get("shopServiceAmount").divide(BigDecimal.valueOf(orderItemOut.getGoodsNum())).setScale(2, BigDecimal.ROUND_HALF_UP));
                coupon.setUserServiceAmount(map.get("userServiceAmount").divide(BigDecimal.valueOf(orderItemOut.getGoodsNum())).setScale(2, BigDecimal.ROUND_HALF_UP));
                this.setRealShopUnitPrice(inVo, coupon, goodsSpu, orderItemOut);//设置实际给商家的钱
                int insert = orderCouponMapper.insert(coupon);
                if (inVo.getActId()!=null&&inVo.getUserId()!=null&&inVo.getGoodsSkuId()!=null){
                    List<OrderInfo> list = orderInfoMapper.onePeopleNotPaylist(inVo);
                    if (list!=null&&list.size()>0){
                        orderInfoMapper.updateByRollback(list);
                    }
                }
                this.updateShowCode(coupon);//更新showCode
                if (inVo.getDefaultUsed()!=null && inVo.getDefaultUsed()==OrderInfo.SDEFAULT_USED_YES){
                    OrderCouponInVo couponInVo = new OrderCouponInVo();
                    couponInVo.setId(coupon.getId());
                    couponInVo.setChangerId(inVo.getUserId());
                    couponInVo.setChangerName(inVo.getUserName());
                    couponInVo.setShopId(inVo.getShopId());
                    couponInVo.setRemark("恭喜发财");
                    //设置商家名字
                    if(new Long(0).toString().equals(inVo.getShopId().toString())){
                        couponInVo.setShopName("享七自营");
                    }else{
                        Shop shopById = shopService.getShopById(inVo.getShopId());
                        couponInVo.setShopName(shopById.getShopName());
                    }
                    orderCouponService.useCouponForRedPacket(couponInVo);
                }
                //this.updateShowCode(coupon);//更新showCode
                if (insert < 1) {
                    throw new RuntimeException("生成票券失败，请联系客服!");
                }
                res++;
            }
        }else if(detail.getSingleType()==OrderInfo.SINGLE_TYPE_ZD){//整单
            OrderCoupon coupon = new OrderCoupon();
            coupon.setOrderId(inVo.getId());
            coupon.setCouponCode(RandomStringUtil.getRandomCode(10, 0));
            coupon.setGoodsSkuId(sku.getId());
            coupon.setGoodsSkuCode(sku.getSkuCode());
            coupon.setGoodsSkuName(sku.getSkuName());
            coupon.setCouponAmount(sku.getSellPrice());
            coupon.setSingleType(OrderCoupon.SINGLE_TYPE_ZD);//整单
            coupon.setFlagType(detail.getFlagType());
            coupon.setUserId(inVo.getUserId());
            coupon.setUserName(inVo.getUserName());
            //上传二维码图片到腾讯COS服务器
            String qrcodeUrl = uploadOrderCouponQRCodeToCos(coupon.getCouponCode());
            coupon.setQrcodeUrl(qrcodeUrl);
            this.setExpiryDate(inVo, coupon, sku);//设置过期时间
            coupon.setShopId(inVo.getShopId());
            coupon.setOwnId(inVo.getUserId());
            //整单的票券实际支付单价=商品实际支付单价*商品理论购买数量
            coupon.setRealUnitPrice(orderItemOut.getRealUnitPrice().multiply(BigDecimal.valueOf(orderItemOut.getGoodsNum())));
            //判断是否是参团订单生成的票券
            ActOrderOut actOrder=actOrderMapper.selectFirstDistributionByOrderId(inVo.getId());
            if (actOrder==null||actOrder.getId()==null){
                coupon.setIsShow(OrderCoupon.IS_SHOW);
            }else {
                ActInfo actInfo = actInfoMapper.selectByPrimaryKey(actOrder.getActGoodsSku().getActId());
                if (actInfo.getType()==OrderInfo.FLAG_TYPE_TG){
                    coupon.setIsShow(OrderCoupon.IS_NOT_SHOW);
                }else {
                    coupon.setIsShow(OrderCoupon.IS_SHOW);
                }
            }
            coupon.setServiceAmount(map.get("serviceAmount"));
            coupon.setShopServiceAmount(map.get("shopServiceAmount"));
            coupon.setUserServiceAmount(map.get("userServiceAmount"));
            this.setRealShopUnitPrice(inVo, coupon, goodsSpu,orderItemOut);//设置实际给商家的钱
            int insert = orderCouponMapper.insert(coupon);
            this.updateShowCode(coupon);//更新showCode
            if (inVo.getDefaultUsed()!=null && inVo.getDefaultUsed()==OrderInfo.SDEFAULT_USED_YES){
                OrderCouponInVo couponInVo = new OrderCouponInVo();
                couponInVo.setId(coupon.getId());
                couponInVo.setChangerId(inVo.getUserId());
                couponInVo.setChangerName(inVo.getUserName());
                couponInVo.setShopId(inVo.getShopId());
                //设置商家名字
                if(new Long(0).toString().equals(inVo.getShopId().toString())){
                    couponInVo.setShopName("享七自营");
                }else{
                    Shop shopById = shopService.getShopById(inVo.getShopId());
                    couponInVo.setShopName(shopById.getShopName());
                }
                orderCouponService.useCouponForRedPacket(couponInVo);
            }
            //this.updateShowCode(coupon);//更新showCode
            if (insert < 1) {
                throw new RuntimeException("生成票券失败，请联系客服!");
            }
            res++;
        }
        return res;
    }

    /*
     * 设置实际给商家的钱*/
    void setRealShopUnitPrice(OrderInfoInVo inVo,OrderCoupon coupon,GoodsSpu goodsSpu,OrderItemOut orderItemOut){
        BigDecimal realShopUnitPrice = BigDecimal.ZERO;
        if(goodsSpu.getCategoryId().longValue()==new Long(12).longValue()){
            realShopUnitPrice = BigDecimal.ZERO.subtract(coupon.getServiceAmount());
        }else{
            //这是为了将给商家的钱降到最少，原价也给活动价
            //BEGIN 随时切换
            ActOrderOut actOrder=actOrderMapper.selectFirstDistributionByOrderId(inVo.getId());
            ActGoodsSkuInVo actGoodsSkuInVo = new ActGoodsSkuInVo();
            ActGoodsSkuOut actGoodsSkuOut = new ActGoodsSkuOut();
            actGoodsSkuInVo.setSkuId(coupon.getGoodsSkuId());
            switch (coupon.getFlagType()){
                case OrderInfo.FLAG_TYPE_PT:
                    actGoodsSkuInVo.setActId(new Long(41));
                    actGoodsSkuOut =actGoodsSkuMapper.selectByActId(actGoodsSkuInVo);
                    if (actGoodsSkuOut!=null&&actGoodsSkuOut.getId()!=null){
                        realShopUnitPrice = actGoodsSkuOut.getGoodsPromotionRules().getActAmount().subtract(coupon.getServiceAmount());
                    }else {
                        realShopUnitPrice = coupon.getRealUnitPrice().subtract(coupon.getServiceAmount());
                    }
                    break;
                case OrderInfo.FLAG_TYPE_KJ:
                    if (actOrder!=null){
                        actGoodsSkuInVo.setActId(actOrder.getActGoodsSku().getActId());
                        actGoodsSkuOut =actGoodsSkuMapper.selectByActId(actGoodsSkuInVo);
                        realShopUnitPrice = actGoodsSkuOut.getGoodsPromotionRules().getActAmount().subtract(coupon.getServiceAmount());
                    }else {
                        realShopUnitPrice = coupon.getRealUnitPrice().subtract(coupon.getServiceAmount());
                    }
                    break;
                default:
                    realShopUnitPrice = coupon.getRealUnitPrice().subtract(coupon.getServiceAmount());
                    break;
            }
            //END
            /*realShopUnitPrice = coupon.getRealUnitPrice().subtract(coupon.getServiceAmount());*/
        }
        coupon.setRealShopUnitPrice(realShopUnitPrice);
    }

    /*
    * 过期时间*/
    void setExpiryDate(OrderInfoInVo inVo,OrderCoupon coupon,GoodsSku sku){
        Calendar cal = Calendar.getInstance();
        ActOrderOut actOrder = actOrderMapper.selectFirstDistributionByOrderId(inVo.getId());
        List<SalePointGoods> salePointGoods = salePointGoodsMapper.selectBySkuId(sku.getId());
        List<OrderCouponExpiryConfig> orderCouponExpiryConfigs = this.getOrderCouponExpiryConfigs(actOrder, salePointGoods);
        List<OrderCouponExpiryConfig> list = new ArrayList<OrderCouponExpiryConfig>();
        if(orderCouponExpiryConfigs!=null&&orderCouponExpiryConfigs.size()>0) {
            list = orderCouponExpiryConfigMapper.listByRefIdAndType(orderCouponExpiryConfigs);
        }
        if(list!=null&&list.size()>0){
            OrderCouponExpiryConfig orderCouponExpiryConfig = list.get(0);//取第一条记录来设置有效期
            if(orderCouponExpiryConfig.getExpiryDate()!=null){
                coupon.setExpiryDate(orderCouponExpiryConfig.getExpiryDate());
            }else{
                cal.add(Calendar.DATE, orderCouponExpiryConfig.getExpiryDateDays());
                coupon.setExpiryDate(cal.getTime());
            }
        }else{
            cal.add(Calendar.DATE, sku.getExpiryDate());//设置有效期
            coupon.setExpiryDate(cal.getTime());
        }
    }



    /**
     * 批量造单根据订单的基本信息来生成相应的票券
     * @param inVo
     * @param detail
     * @param orderItemOut
     * @param sku
     * @return
     */
    public int batchInsertCoupon(OrderInfoInVo inVo,OrderInfoOut detail,OrderItemOut orderItemOut,GoodsSku sku){
        int res = 0;
        GoodsSpu goodsSpu = goodsSpuMapper.selectByPrimaryKey(sku.getSpuId());//查询category_id
        Map<String,BigDecimal> map = this.subServiceAmount(detail, sku,goodsSpu,orderItemOut);//查询总服务费
        //分单
        if(detail.getSingleType()==OrderInfo.SINGLE_TYPE_FD){
            for (int i = 0; i < orderItemOut.getGoodsNum() + orderItemOut.getGiftNum(); i++) {
                OrderCoupon coupon = new OrderCoupon();
                coupon.setOrderId(inVo.getId());
                coupon.setCouponCode(RandomStringUtil.getRandomCode(10, 0));
                coupon.setGoodsSkuId(sku.getId());
                coupon.setGoodsSkuCode(sku.getSkuCode());
                coupon.setGoodsSkuName(sku.getSkuName());
                coupon.setCouponAmount(sku.getSellPrice());
                coupon.setSingleType(OrderCoupon.SINGLE_TYPE_FD);//分单
                coupon.setFlagType(detail.getFlagType());
                coupon.setUserId(inVo.getUserId());
                coupon.setUserName(inVo.getUserName());
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, sku.getExpiryDate());//设置有效期
                coupon.setExpiryDate(cal.getTime());
                coupon.setShopId(inVo.getShopId());
                coupon.setOwnId(inVo.getUserId());
                coupon.setCreateTime(inVo.getCreateTime());
                if(i<orderItemOut.getGiftNum()) {
                    coupon.setRealUnitPrice(BigDecimal.ZERO);//赠品的实际支付单价为0
                }else{
                    coupon.setRealUnitPrice(orderItemOut.getRealUnitPrice());//分单票券里面的实际支付单价
                }
                coupon.setIsShow(OrderCoupon.IS_SHOW);
                coupon.setServiceAmount(map.get("serviceAmount").divide(BigDecimal.valueOf(orderItemOut.getGoodsNum())).setScale(2, BigDecimal.ROUND_HALF_UP));
                int insert = orderCouponMapper.batchInsert(coupon);
                if (insert < 1) {
                    throw new RuntimeException("生成票券失败，请联系客服!");
                }
                OrderCouponInVo orderCouponInVo = new OrderCouponInVo();
                orderCouponInVo.setId(coupon.getId());
                orderCouponInVo.setShopId(inVo.getBatchOrderInVo().getHxShopId());
                orderCouponInVo.setShopName(inVo.getBatchOrderInVo().getHxShopName());
                ShopCashier shopCashier = shopCashierMapper.adminByShopId(orderCouponInVo.getShopId());
                orderCouponInVo.setHxUserId(shopCashier.getCashierId());
                orderCouponInVo.setHxUserName(shopCashier.getCashierName());
                Long time =inVo.getCreateTime().getTime()+(2*60*60+11*60+22)*1000;
                Date date=new Date(time);
                orderCouponInVo.setCreateTime(date);
                this.batchHxCoupon(orderCouponInVo);
                /*String s = JSON.toJSONString(orderCouponInVo);
                kafkaService.sendDataToTopic("batch.order.topic",s,s);*/
                res++;
            }
        }else if(detail.getSingleType()==OrderInfo.SINGLE_TYPE_ZD){//整单
            OrderCoupon coupon = new OrderCoupon();
            coupon.setOrderId(inVo.getId());
            coupon.setCouponCode(RandomStringUtil.getRandomCode(10, 0));
            coupon.setGoodsSkuId(sku.getId());
            coupon.setGoodsSkuCode(sku.getSkuCode());
            coupon.setGoodsSkuName(sku.getSkuName());
            coupon.setCouponAmount(sku.getSellPrice());
            coupon.setSingleType(OrderCoupon.SINGLE_TYPE_ZD);//整单
            coupon.setFlagType(detail.getFlagType());
            coupon.setUserId(inVo.getUserId());
            coupon.setUserName(inVo.getUserName());
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, sku.getExpiryDate());//设置有效期
            coupon.setExpiryDate(cal.getTime());
            coupon.setShopId(inVo.getShopId());
            coupon.setOwnId(inVo.getUserId());
            coupon.setCreateTime(inVo.getCreateTime());
            //整单的票券实际支付单价=商品实际支付单价*商品理论购买数量
            coupon.setRealUnitPrice(orderItemOut.getRealUnitPrice().multiply(BigDecimal.valueOf(orderItemOut.getGoodsNum())));
            coupon.setIsShow(OrderCoupon.IS_SHOW);
            coupon.setServiceAmount(map.get("serviceAmount"));
            int insert = orderCouponMapper.batchInsert(coupon);
            if (insert < 1) {
                throw new RuntimeException("生成票券失败，请联系客服!");
            }
            OrderCouponInVo orderCouponInVo = new OrderCouponInVo();
            orderCouponInVo.setId(coupon.getId());
            orderCouponInVo.setShopId(inVo.getBatchOrderInVo().getHxShopId());
            orderCouponInVo.setShopName(inVo.getBatchOrderInVo().getHxShopName());
            ShopCashier shopCashier = shopCashierMapper.adminByShopId(orderCouponInVo.getShopId());
            orderCouponInVo.setHxUserId(shopCashier.getCashierId());
            orderCouponInVo.setHxUserName(shopCashier.getCashierName());
            Long time =inVo.getCreateTime().getTime()+(2*60*60+11*60+22)*1000;
            Date date=new Date(time);
            orderCouponInVo.setCreateTime(date);
            this.batchHxCoupon(orderCouponInVo);
            /*String s = JSON.toJSONString(orderCouponInVo);
            kafkaService.sendDataToTopic("batch.order.topic", s,s);*/
            res++;
        }

        return res;
    }

    public int batchHxCoupon(OrderCouponInVo inVo) {
        OrderCouponOut cp = this.selectById(inVo.getId());

        OrderCoupon orderCoupon = new OrderCoupon();
        orderCoupon.setId(inVo.getId());
        orderCoupon.setIsUsed(OrderCoupon.ORDER_COUPON_IS_USED_YES);
        orderCoupon.setChangerId(cp.getOwnId());
        orderCoupon.setUsedTime(inVo.getCreateTime());
        orderCoupon.setOwnId(cp.getOwnId());
        orderCoupon.setUpdateTime(inVo.getCreateTime());
        orderCoupon.setType(OrderCoupon.TYPE_MDZTQ);//核销券都是门店自提
        orderCoupon.setCouponSalepointId(inVo.getSalepointId());
        int result = orderCouponMapper.updateByPrimaryKeySelective(orderCoupon);

        OrderWriteOff orderWriteOff = new OrderWriteOff();
        orderWriteOff.setOrderId(cp.getOrderId());
        orderWriteOff.setShopId(inVo.getShopId());
        orderWriteOff.setShopName(inVo.getShopName());
        orderWriteOff.setCouponAmount(cp.getCouponAmount());//票券所对应商品的理论售价
        orderWriteOff.setOrderCouponId(cp.getId());
        orderWriteOff.setOrderCouponCode(cp.getCouponCode());
        orderWriteOff.setGoodsSkuId(cp.getGoodsSkuId());
        orderWriteOff.setUserId(cp.getUserId());//票券购买人
        orderWriteOff.setUserName(cp.getUserName());
        orderWriteOff.setChangerId(inVo.getHxUserId());//票券核销人
        orderWriteOff.setChangerName(inVo.getHxUserName());
        orderWriteOff.setSalepointId(inVo.getSalepointId());
        orderWriteOff.setServiceAmount(cp.getServiceAmount());
        orderWriteOff.setRealUnitPrice(cp.getRealUnitPrice());
        orderWriteOff.setCreateTime(inVo.getCreateTime());

        int r = orderWriteOffMapper.batchInsert(orderWriteOff);

        //给商家钱包加入余额
        if(!cp.getShopId().equals(0)){
            OrderCouponInVo orderCouponInVo = new OrderCouponInVo();
            BeanUtils.copyProperties(cp, orderCouponInVo);
            int re = this.addUserAccountToShop(orderCouponInVo);
        }

        return result;
    }

    @Override
    @Transactional
    public int batchHxCouponToKafka(OrderCouponInVo inVo) {
        OrderCouponOut cp = this.selectById(inVo.getId());

        OrderCoupon orderCoupon = new OrderCoupon();
        orderCoupon.setId(inVo.getId());
        orderCoupon.setIsUsed(OrderCoupon.ORDER_COUPON_IS_USED_YES);
        orderCoupon.setChangerId(cp.getOwnId());
        orderCoupon.setUsedTime(inVo.getCreateTime());
        orderCoupon.setOwnId(cp.getOwnId());
        orderCoupon.setUpdateTime(inVo.getCreateTime());
        orderCoupon.setType(OrderCoupon.TYPE_MDZTQ);//核销券都是门店自提
        orderCoupon.setCouponSalepointId(inVo.getSalepointId());
        int result = orderCouponMapper.updateByPrimaryKeySelective(orderCoupon);

        OrderWriteOff orderWriteOff = new OrderWriteOff();
        orderWriteOff.setOrderId(cp.getOrderId());
        orderWriteOff.setShopId(inVo.getShopId());
        orderWriteOff.setShopName(inVo.getShopName());
        orderWriteOff.setCouponAmount(cp.getCouponAmount());//票券所对应商品的理论售价
        orderWriteOff.setOrderCouponId(cp.getId());
        orderWriteOff.setOrderCouponCode(cp.getCouponCode());
        orderWriteOff.setGoodsSkuId(cp.getGoodsSkuId());
        orderWriteOff.setUserId(cp.getUserId());//票券购买人
        orderWriteOff.setUserName(cp.getUserName());
        orderWriteOff.setChangerId(inVo.getHxUserId());//票券核销人
        orderWriteOff.setChangerName(inVo.getHxUserName());
        orderWriteOff.setSalepointId(inVo.getSalepointId());
        orderWriteOff.setServiceAmount(cp.getServiceAmount());
        orderWriteOff.setRealUnitPrice(cp.getRealUnitPrice());
        orderWriteOff.setCreateTime(inVo.getCreateTime());

        int r = orderWriteOffMapper.batchInsert(orderWriteOff);

        //给商家钱包加入余额
        if(!cp.getShopId().equals(0)){
            OrderCouponInVo orderCouponInVo = new OrderCouponInVo();
            BeanUtils.copyProperties(cp, orderCouponInVo);
            int re = this.addUserAccountToShop(orderCouponInVo);
        }

        return result;
    }

    @Override
    public Integer isNewUser(Long userId) {
        int i = orderInfoMapper.selectByUserIdTotal(userId);
        return i;
    }

    public int addUserAccountToShop(OrderCouponInVo orderCouponInVo) {
        ShopCashier shopCashier = shopCashierMapper.adminByShopId(orderCouponInVo.getShopId());
        if(shopCashier!=null && shopCashier.getCashierId()!=null) {
            //1. 账户日志
            UserAccountInVo accountInVo = new UserAccountInVo();
            accountInVo.setUserId(shopCashier.getCashierId());
            accountInVo.setOccurAmount(orderCouponInVo.getRealUnitPrice());
            accountService.income(accountInVo, "用户核销，票券单号：" + orderCouponInVo.getCouponCode());
        }
        return 1;
    }

    public OrderCouponOut selectById(Long id) {
        OrderCouponOut orderCouponOut = orderCouponMapper.selectDetailByPrimaryKey(id);
        return orderCouponOut;
    }

    /**
     * 通过订单详情和商品详情去查这个订单的总服务费
     * @param detail
     * @param sku
     * @return
     */
    public Map<String,BigDecimal> subServiceAmount(OrderInfoOut detail,GoodsSku sku,GoodsSpu goodsSpu,OrderItemOut orderItemOut){
        List<ServiceConfig> serviceConfigs = this.getServiceConfigs(detail, goodsSpu, sku);//构造服务费配置查询参数
        List<ServiceConfig> listServiceConfig = serviceConfigMapper.listByRefIdAndType(serviceConfigs);//查询服务费配置
        BigDecimal serviceAmount = BigDecimal.ZERO;
        BigDecimal shopServiceAmount = BigDecimal.ZERO;
        BigDecimal userServiceAmount = BigDecimal.ZERO;
        if(listServiceConfig!=null&&listServiceConfig.size()>0) {
            ServiceConfig serviceConfig = listServiceConfig.get(0);
            if(detail.getPayType()==OrderInfo.PAY_TYPE_MFZS){
                //商家服务费
                shopServiceAmount = sku.getSellPrice().multiply(serviceConfig.getServiceRate()).add(serviceConfig.getServiceWeight()).multiply(new BigDecimal(orderItemOut.getGoodsNum()));
                //用户服务费
                userServiceAmount = sku.getSellPrice().multiply(serviceConfig.getUserServiceRate()).add(serviceConfig.getUserServiceWeight()).multiply(new BigDecimal(orderItemOut.getGoodsNum()));
                //总服务费
                serviceAmount = shopServiceAmount.add(userServiceAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
            }else {
                //商家服务费
                shopServiceAmount = detail.getRealAmount().multiply(serviceConfig.getServiceRate()).add((serviceConfig.getServiceWeight()).multiply(new BigDecimal(orderItemOut.getGoodsNum())));
                //用户服务费
                userServiceAmount = detail.getRealAmount().multiply(serviceConfig.getUserServiceRate()).add((serviceConfig.getUserServiceWeight()).multiply(new BigDecimal(orderItemOut.getGoodsNum())));
                //总服务费
                serviceAmount = shopServiceAmount.add(userServiceAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
            }
        }
        Map<String,BigDecimal> map = new HashMap<String, BigDecimal>();
        map.put("serviceAmount",serviceAmount);
        map.put("shopServiceAmount",shopServiceAmount);
        map.put("userServiceAmount",userServiceAmount);
        return map;
    }

    /**
     * 通过actId,categoryId,goodsSkuId去查询相应的服务费配置
     * @param detail
     * @param goodsSpu
     * @param sku
     * @return
     */
    public List<ServiceConfig> getServiceConfigs(OrderInfoOut detail,GoodsSpu goodsSpu,GoodsSku sku){
        List<ServiceConfig> serviceConfigs = new ArrayList<ServiceConfig>();
        ServiceConfig serviceConfig = new ServiceConfig();
        //获取活动id
        ActOrderOut actOrderOut = actOrderMapper.selectFirstDistributionByOrderId(detail.getId());
        if(actOrderOut!=null&&actOrderOut.getActGoodsSku()!=null&&actOrderOut.getActGoodsSku().getActId()!=null){
            serviceConfig.setRefId(actOrderOut.getActGoodsSku().getActId());
            serviceConfig.setType(ServiceConfig.TYPE_ACT);
            serviceConfigs.add(serviceConfig);
        }
        ServiceConfig serviceConfig1 = new ServiceConfig();
        serviceConfig1.setRefId(goodsSpu.getCategoryId());
        serviceConfig1.setType(ServiceConfig.TYPE_CATEGORY);
        serviceConfigs.add(serviceConfig1);
        ServiceConfig serviceConfig2 = new ServiceConfig();
        serviceConfig2.setRefId(sku.getId());
        serviceConfig2.setType(ServiceConfig.TYPE_GOODS_SKU);
        serviceConfigs.add(serviceConfig2);
        ServiceConfig serviceConfig3 = new ServiceConfig();
        serviceConfig3.setRefId(new Long(0));
        serviceConfig3.setType(0);
        serviceConfigs.add(serviceConfig3);
        return serviceConfigs;
    }

    /**
     * 通过actId,categoryId,goodsSkuId去查询相应的开发票配置
     * @param detail
     * @param goodsSpu
     * @param sku
     * @return
     */
    public List<OrderInvoiceConfig> getOrderInvoiceConfigs(OrderInfoOut detail,GoodsSpu goodsSpu,GoodsSku sku){
        List<OrderInvoiceConfig> serviceConfigs = new ArrayList<OrderInvoiceConfig>();
        OrderInvoiceConfig serviceConfig = new OrderInvoiceConfig();
        //获取活动id
        ActOrderOut actOrderOut = actOrderMapper.selectFirstDistributionByOrderId(detail.getId());
        if(actOrderOut!=null&&actOrderOut.getActGoodsSku()!=null&&actOrderOut.getActGoodsSku().getActId()!=null){
            serviceConfig.setRefId(actOrderOut.getActGoodsSku().getActId());
            serviceConfig.setType(OrderInvoiceConfig.TYPE_ACT);
            serviceConfigs.add(serviceConfig);
        }
        OrderInvoiceConfig serviceConfig1 = new OrderInvoiceConfig();
        serviceConfig1.setRefId(goodsSpu.getCategoryId());
        serviceConfig1.setType(OrderInvoiceConfig.TYPE_CATEGORY);
        serviceConfigs.add(serviceConfig1);
        OrderInvoiceConfig serviceConfig2 = new OrderInvoiceConfig();
        serviceConfig2.setRefId(sku.getId());
        serviceConfig2.setType(OrderInvoiceConfig.TYPE_GOODS_SKU);
        serviceConfigs.add(serviceConfig2);
        return serviceConfigs;
    }

    /**
     * 通过actId,categoryId,goodsSkuId去查询相应的退款配置
     * @param detail
     * @param goodsSpu
     * @param sku
     * @return
     */
    public List<PayRefundConfig> getPayRefundConfigs(OrderInfoOut detail,GoodsSpu goodsSpu,GoodsSku sku){
        List<PayRefundConfig> serviceConfigs = new ArrayList<PayRefundConfig>();
        PayRefundConfig serviceConfig = new PayRefundConfig();
        //获取活动id
        ActOrderOut actOrderOut = actOrderMapper.selectFirstDistributionByOrderId(detail.getId());
        if(actOrderOut!=null&&actOrderOut.getActGoodsSku()!=null&&actOrderOut.getActGoodsSku().getActId()!=null){
            serviceConfig.setRefId(actOrderOut.getActGoodsSku().getActId());
            serviceConfig.setType(PayRefundConfig.TYPE_ACT);
            serviceConfigs.add(serviceConfig);
        }
        PayRefundConfig serviceConfig1 = new PayRefundConfig();
        serviceConfig1.setRefId(goodsSpu.getCategoryId());
        serviceConfig1.setType(PayRefundConfig.TYPE_CATEGORY);
        serviceConfigs.add(serviceConfig1);
        PayRefundConfig serviceConfig2 = new PayRefundConfig();
        serviceConfig2.setRefId(sku.getId());
        serviceConfig2.setType(PayRefundConfig.TYPE_GOODS_SKU);
        serviceConfigs.add(serviceConfig2);
        return serviceConfigs;
    }

    /**
     * 通过actId,salepointId去查询相应的票券有效期配置
     * @param actOrderOut
     * @param salePointGoods
     * @return
     */
    public List<OrderCouponExpiryConfig> getOrderCouponExpiryConfigs(ActOrderOut actOrderOut,List<SalePointGoods> salePointGoods){
        List<OrderCouponExpiryConfig> serviceConfigs = new ArrayList<OrderCouponExpiryConfig>();
        OrderCouponExpiryConfig serviceConfig = new OrderCouponExpiryConfig();
        //获取活动id
        if(actOrderOut!=null&&actOrderOut.getActGoodsSku()!=null&&actOrderOut.getActGoodsSku().getActId()!=null){
            serviceConfig.setRefId(actOrderOut.getActGoodsSku().getActId());
            serviceConfig.setType(OrderCouponExpiryConfig.TYPE_ACT);
            serviceConfigs.add(serviceConfig);
        }
        for (SalePointGoods sa : salePointGoods) {
            OrderCouponExpiryConfig se = new OrderCouponExpiryConfig();
            serviceConfig.setRefId(sa.getSalepointId());
            serviceConfig.setType(OrderCouponExpiryConfig.TYPE_SALEPOINT);
            serviceConfigs.add(se);
        }
        return serviceConfigs;
    }

    /**
     * 更新用户的金币及删除缓存
     * @param inVo
     * @param weixinInVo
     */
    public void updateUserGold(OrderInfoInVo inVo,WeixinInVo weixinInVo){
        if(weixinInVo.getType()== WeixinInVo.WEI_XIN_TYPE_SHOP_KJ) {
            OrderInfoOut detail = orderInfoMapper.getDetail(inVo.getId());//查询订单详情
            List<OrderItemOut> orderItemOuts = detail.getOrderItemOuts();//查询订单项详情
            OrderItemOut orderItemOut = orderItemOuts.get(0);//查询第一个订单项详情,虚拟订单没有购物车概念
            //5. 金币变动的账户日志(要判断是否是直接用原价购买的，如果是，则不需要变动金币)
            GoodsGoldLogInVo goodsGoldLogInVo = new GoodsGoldLogInVo();
            goodsGoldLogInVo.setGroupId(weixinInVo.getGroupId());
            goodsGoldLogInVo.setRefId(orderItemOut.getGoodsSkuId());
            goodsGoldLogInVo.setParentId(inVo.getUserId());
            //获取活动id
            ActOrderOut actOrderOut = actOrderMapper.selectFirstDistributionByOrderId(inVo.getId());
            goodsGoldLogInVo.setActId(actOrderOut.getActGoodsSku().getActId());
            Integer integer = goodsGoldLogService.changeState(goodsGoldLogInVo);
            logger.info("userid" + inVo.getUserId() + "skuid=" + orderItemOut.getGoodsSkuId() + "groupid=" + weixinInVo.getGroupId() + "actId=" + actOrderOut.getActGoodsSku().getActId());
        }else if (weixinInVo.getType()== WeixinInVo.WEI_XIN_TYPE_SHOP_MS){
            OrderInfoOut detail = orderInfoMapper.getDetail(inVo.getId());//查询订单详情
            List<OrderItemOut> orderItemOuts = detail.getOrderItemOuts();//查询订单项详情
            OrderItemOut orderItemOut = orderItemOuts.get(0);//查询第一个订单项详情,虚拟订单没有购物车概念
            //获取活动id
            ActOrderOut actOrderOut = actOrderMapper.selectFirstDistributionByOrderId(inVo.getId());
            String valueKey="ActId_"+actOrderOut.getActGoodsSku().getActId()+"MsGoodsSku_"+"sku_id_"+orderItemOut.getGoodsSkuId()+"parent_id_"+inVo.getUserId();
            String totleKey="ActId_"+actOrderOut.getActGoodsSku().getActId()+"MsGoodsSku_"+"parent_id_"+inVo.getUserId();
            redisCache.hdel(totleKey, valueKey);
        }
    }

    /**
     * 更新showCode
     * @param coupon
     */
    public void updateShowCode(OrderCoupon coupon){
        SimpleDateFormat df = new SimpleDateFormat("MMdd");
        OrderCouponOut orderCouponOut = orderCouponMapper.selectDetailByPrimaryKey(coupon.getId());
        String date = df.format(orderCouponOut.getCreateTime());
        String s = CutOutTimeUtils.addZeroForNum(orderCouponOut.getId().toString(), 6);
        String showCode = date+s;
        OrderCoupon inVo = new OrderCoupon();
        inVo.setId(orderCouponOut.getId());
        inVo.setShowCode(showCode);
        orderCouponMapper.updateByPrimaryKeySelective(inVo);
    }

    @Override
    public BigDecimal totalAmount(OrderWriteOffInVo orderWriteOffInVo) {
        OrderWriteOffOut orderWriteOffOut= orderWriteOffMapper.selectTotalPrice(orderWriteOffInVo);
        BigDecimal a = BigDecimal.ZERO;
        if(orderWriteOffOut!=null){
            a=orderWriteOffOut.getTotalPrice();
        }
        return a;
    }

    @Override
    public Integer notPayList(OrderInfoInVo inVo) {
        List<OrderInfo> list = orderInfoMapper.onePeopleNotPaylist(inVo);
        if (list!=null&&list.size()>0){
            return orderInfoMapper.updateByRollback(list);
        }
        return null;
    }
}
