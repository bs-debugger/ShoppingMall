package com.xq.live.web.controller;

import com.alibaba.fastjson.JSON;
import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.config.WeiXinTeamplateMsg;
import com.xq.live.model.*;
import com.xq.live.service.*;
import com.xq.live.vo.in.OrderCouponInVo;
import com.xq.live.vo.out.OrderCouponHxOut;
import com.xq.live.vo.out.OrderCouponOut;
import com.xq.live.web.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;

/**
 * 商城系统订单票券接口(票券属于虚拟订单,没有购物车的概念,只能一次买一种商品)
 *
 * @author lipeng
 * @date 2018-09-14 21:32
 **/
@Api(tags = "商城系统订单票券接口")
@RestController
@RequestMapping(value = "/orderCoupon")
public class OrderCouponController {
    @Autowired
    private OrderCouponService orderCouponService;

    @Autowired
    private OrderWriteOffService orderWriteOffService;

    @Autowired
    private PromotionRulesService promotionRulesService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private KafkaService kafkaService;

    @Autowired
    private UserService  userService;

    @Autowired
    private SalePointService salePointService;

    @Autowired
    private ActLotteryService actLotteryService;

    @Autowired
    private WeiXinPushService weiXinPushService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private VoteService voteService;
    /**
     * 根据id查一条记录 ------老接口 ,这个接口要删除
     * @param id
     * @return
     * localhost:8080/orderCoupon/get/10
     */
    @ApiOperation(value = "根据id查一条记录")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<OrderCouponOut> get(@PathVariable("id") Long id){
        OrderCouponOut cp = orderCouponService.selectById(id);
        return new BaseResp<OrderCouponOut>(ResultStatus.SUCCESS, cp);
    }


    /**
     * 查询票券详情接口(新)
     *
     * 入参:id,locationX,locationY,(orderCouponSendType)
     *
     * @param inVo
     * @return
     */
    @ApiOperation(value = "查询票券详情接口(新)")
    @RequestMapping(value = "/getDetail", method = RequestMethod.GET)
    public BaseResp<OrderCouponOut> getDetail(OrderCouponInVo inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<OrderCouponOut>(ResultStatus.error_param_empty);
        }
        if(inVo.getSendOrReceiveUserId()==null) {
            inVo.setSendOrReceiveUserId(user.getId());
        }
        OrderCouponOut cp = orderCouponService.getDetail(inVo);
        String  girlsDayResult=orderCouponService.girlsDayFirstWriteOff(cp);//3.7,3.8女神节送奖励金结果
        cp.setGirlsDayResult(girlsDayResult);
        return new BaseResp<OrderCouponOut>(ResultStatus.SUCCESS, cp);
    }

    /**
     * 根据券码查一条记录
     *
     * @param couponCode
     * @return
     */
    @ApiOperation(value = "根据券码查一条记录")
        @RequestMapping(value = "/getByCode/{couponCode}", method = RequestMethod.GET)
    public BaseResp<OrderCouponOut> getByCode(@PathVariable("couponCode")String couponCode){
        if(StringUtils.isEmpty(couponCode)){
            return new BaseResp<OrderCouponOut>(ResultStatus.error_para_coupon_code_empty);
        }
            OrderCouponOut cp = orderCouponService.getDetailByCouponCode(couponCode);

        return new BaseResp<OrderCouponOut>(ResultStatus.SUCCESS, cp);
    }

    /**
     * 查询是否拥有核销权限
     * @param id
     * @return
     */
    @ApiOperation(value = "查询是否拥有核销权限")
    @RequestMapping(value = "/hxJurisdiction",method = RequestMethod.GET)
    public BaseResp<OrderCouponHxOut> hxJurisdiction(Long id){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<OrderCouponHxOut>(ResultStatus.error_param_empty);
        }
        OrderCouponHxOut re =  orderCouponService.hxJurisdiction(id,user.getId(),user.getShopId());
        if(re.getErrorCode().equals("0")) {
            return new BaseResp<OrderCouponHxOut>(ResultStatus.SUCCESS);
        }
        return new BaseResp<OrderCouponHxOut>(ResultStatus.FAIL,re);
    }

    /**
     * 分页查询票券信息
     *
     * 注意:
     * 1.券列表:userId,isUsed=0,page,rows  -----------不要了
     * 2.兑换记录列表:changerId,browSort=1,page,rows  ----------这个还需要
     * @param inVo
     * @return
     */
    @ApiOperation(value = "分页查询票券信息")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResp<Pager<OrderCouponOut>> list(OrderCouponInVo inVo){
        Pager<OrderCouponOut> result = orderCouponService.list(inVo);
        return new BaseResp<Pager<OrderCouponOut>>(ResultStatus.SUCCESS, result);
    }


    /**
     * 分页查询票券列表信息
     *
     * 注意:
     * 1.券列表:userId,isUsed,page,rows  这个是最新的
     *
     * userId是当前用户的，要从网关中取
     * @param inVo
     * @return
     */
    @ApiOperation(value = "分页查询票券列表信息")
    @RequestMapping(value = "/listForSendNew", method = RequestMethod.GET)
    public BaseResp<Pager<OrderCouponOut>> listForSendNew(OrderCouponInVo inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Pager<OrderCouponOut>>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        Pager<OrderCouponOut> result = orderCouponService.listForSend(inVo);
        return new BaseResp<Pager<OrderCouponOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 分页查询票券列表信息
     *
     * 注意:
     * 1.赠送列表:sendUserId,page,rows
     * 2.收取列表:receiveUserId,page,rows
     * @param inVo
     * @return
     */
    @ApiOperation(value = "分页查询票券列表信息")
    @RequestMapping(value = "/listCoupon", method = RequestMethod.GET)
    public BaseResp<Pager<OrderCouponOut>> listCoupon(OrderCouponInVo inVo){
        Pager<OrderCouponOut> result = orderCouponService.listCoupon(inVo);
        return new BaseResp<Pager<OrderCouponOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 分页查询能被商家订单使用的享七券列表
     *
     * 注意:
     * 1.券列表:userId,page,rows,isUsed:0,categoryId:48,shopId,finalAmount  这个是最新的
     *
     * userId是当前用户的，要从网关中取
     * @param inVo
     * @return
     */
    @ApiOperation(value = "分页查询能被商家订单使用的享七券列表")
    @RequestMapping(value = "/listCouponForShopUser", method = RequestMethod.GET)
    public BaseResp<Pager<OrderCouponOut>> listCouponForShopUser(OrderCouponInVo inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Pager<OrderCouponOut>>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        Pager<OrderCouponOut> result = orderCouponService.listCouponForShopUser(inVo);
        return new BaseResp<Pager<OrderCouponOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 使用礼品券(支付完运费之后调用此接口)
     *
     * 注:changerId和changerName为当前用户的,要从网关中获取(票券使用人)
     * 还要传shopId和shopName
     *
     * 还要传sendTime和remark
     * @param inVo
     * @return
     */
    @ApiOperation(value = "使用礼品券")
    @RequestMapping(value = "/useCoupon",method = RequestMethod.POST)
    @Transactional
    public BaseResp<Integer> useCoupon(OrderCouponInVo inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        inVo.setChangerId(user.getId());
        inVo.setChangerName(user.getUserName());
        if(inVo==null||inVo.getId()==null||inVo.getCouponAddressId()==null
                ||inVo.getChangerId()==null||inVo.getChangerName()==null||inVo.getSendAmount()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }

        this.setShopName(inVo);//设置商家名字

        //判断票券是否存在或票券是否被使用
        OrderCouponOut cp = orderCouponService.selectById(inVo.getId());
        if(cp==null||cp.getIsUsed()== OrderCoupon.ORDER_COUPON_IS_USED_YES){
            return new BaseResp<Integer>(ResultStatus.error_coupon_is_used);
        }

        //退款申请中和已退款的票卷不能使用
        if(cp.getStatus()!=null&&(cp.getStatus()==OrderCoupon.STATUS_REFUND||cp.getStatus()==OrderCoupon.STATUS_REFUND_APPLICATION)){
            return new BaseResp<Integer>(ResultStatus.error_coupon_refund);
        }

        //判断订单是否存在
        OrderInfo orderInfo = orderInfoService.get(cp.getOrderId());
        if(orderInfo==null){
            return new BaseResp<Integer>(ResultStatus.error_so_not_exist);
        }

        int re = orderCouponService.useCoupon(inVo);

        return new BaseResp<Integer>(ResultStatus.SUCCESS,re);

    }


    /**
     * 使用红包券(
     *
     * 注:changerId和changerName为当前用户的,要从网关中获取(票券使用人)
     *
     * id,remark
     * @param inVo
     * @return
     */
    @ApiOperation(value = "使用红包券")
    @RequestMapping(value = "/useCouponForRedPacket",method = RequestMethod.POST)
    @Transactional
    public BaseResp<Integer> useCouponForRedPacket(OrderCouponInVo inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        inVo.setChangerId(user.getId());
        inVo.setChangerName(user.getUserName());

        if(inVo==null||inVo.getId()==null ||inVo.getChangerId()==null
                ||inVo.getChangerName()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        //判断票券是否存在或票券是否被使用
        OrderCouponOut cp = orderCouponService.selectById(inVo.getId());
        if(cp==null||cp.getIsUsed()== OrderCoupon.ORDER_COUPON_IS_USED_YES){
            return new BaseResp<Integer>(ResultStatus.error_coupon_is_used);
        }
        inVo.setShopId(cp.getShopId());

        this.setShopName(inVo);//设置商家名字



        //退款申请中和已退款的票卷不能使用
        if(cp.getStatus()!=null&&(cp.getStatus()==OrderCoupon.STATUS_REFUND||cp.getStatus()==OrderCoupon.STATUS_REFUND_APPLICATION)){
            return new BaseResp<Integer>(ResultStatus.error_coupon_refund);
        }

        //判断订单是否存在
        OrderInfo orderInfo = orderInfoService.get(cp.getOrderId());
        if(orderInfo==null){
            return new BaseResp<Integer>(ResultStatus.error_so_not_exist);
        }

        int re = orderCouponService.useCouponForRedPacket(inVo);

        return new BaseResp<Integer>(ResultStatus.SUCCESS,re);

    }

    /**
     * 核销券
     *
     * id(票券id)
     * @param inVo
     * @return
     */
    @ApiOperation(value = "核销券")
    @RequestMapping(value = "/hxCouponV1",method = RequestMethod.POST)
    public BaseResp<Integer> hxCouponV1(OrderCouponInVo inVo)throws Exception{
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        OrderCouponHxOut orderCouponHxOut =  orderCouponService.hxJurisdiction(inVo.getId(),user.getId(),user.getShopId());
        if(!orderCouponHxOut.getErrorCode().equals("0")){
            return new BaseResp<Integer>(ResultStatus.FAIL);
        }
        inVo.setHxUserId(user.getId());//票券核销人是当前用户id
        inVo.setHxUserName(user.getUserName());
        inVo.setShopId(orderCouponHxOut.getShopId());
        inVo.setShopName(orderCouponHxOut.getShopName());
        inVo.setSalepointId(orderCouponHxOut.getSalePointId());
        if(inVo==null||inVo.getId()==null||inVo.getShopId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }

        //判断票券是否存在或票券是否被使用
        OrderCouponOut cp = orderCouponService.selectById(inVo.getId());
        //*验证商家是否支持对应满减规则*/
        if (cp.getGoodsSpu().getCategoryId().toString().equals("12")){
            ShopPromotionRules rules = new ShopPromotionRules();
            rules.setShopId(cp.getShopId());
            rules.setGoodsSkuId(cp.getGoodsSkuId());
            ShopPromotionRules shopPromotionRules = promotionRulesService.selectByRules(rules);
            if (shopPromotionRules==null||shopPromotionRules.getId()==null){
                return new BaseResp<Integer>(ResultStatus.error_promotion_rules_not_have);
            }
        }


        //判断订单是否存在
        OrderInfo orderInfo = orderInfoService.get(cp.getOrderId());
        if(orderInfo==null){
            return new BaseResp<Integer>(ResultStatus.error_so_not_exist);
        }

        int re = orderCouponService.hxCouponV2(inVo);

        //核销完成之后推送核销完成信息到微信
        String shopName="享七自营";
        if(inVo.getSalepointId()!=null){
            SalePoint salePoint=salePointService.getSalePointByID(inVo.getSalepointId());
            if(salePoint!=null) {
                shopName = salePoint.getSalepointName();
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date=new java.util.Date();
        String str=sdf.format(date);
        StringBuilder sb = new StringBuilder(user.getMobile());
        sb.replace(3, 7, "****");
        String keyWords=cp.getGoodsSkuName()+","+str+","+shopName+","+sb+",您的订单已核销";//卡卷名称+核销时间+门店+店员昵称+备注
        Integer res =weiXinPushService.pushByUserId(WeiXinTeamplateMsg.templateId_TYPE_HX, "",keyWords,cp.getOwnId() );
        //核销完之后增加票数和抽奖机会
        kafkaService.sendDataToTopic("messageAndActivitys", "messageAndActivitys", JSON.toJSONString(inVo));
        //核销完成之后发送消息到小程序的消息列表
        messageService.addMessage("核销成功", "您的 " + cp.getGoodsSkuName() + " 已核销。", MessageText.MESSAGE_TEXT_TYPE_PRIVATE, cp.getOwnId(), user.getId());
        return new BaseResp<Integer>(ResultStatus.SUCCESS,re);

    }

    /**
     * 核销券v2
     *
     * id(票券id)
     * @param inVo
     * @return
     */
    @ApiOperation(value = "核销券v2")
    @RequestMapping(value = "/hxCouponV2",method = RequestMethod.POST)
    public BaseResp<Integer> hxCouponV2(OrderCouponInVo inVo)throws Exception{
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        OrderCouponHxOut orderCouponHxOut =  orderCouponService.hxJurisdiction(inVo.getId(),user.getId(),user.getShopId());
        if(!orderCouponHxOut.getErrorCode().equals("0")){
            return new BaseResp<Integer>(ResultStatus.FAIL);
        }
        inVo.setHxUserId(user.getId());//票券核销人是当前用户id
        inVo.setHxUserName(user.getUserName());
        inVo.setShopId(orderCouponHxOut.getShopId());
        inVo.setShopName(orderCouponHxOut.getShopName());
        inVo.setSalepointId(orderCouponHxOut.getSalePointId());
        if(inVo==null||inVo.getId()==null||inVo.getShopId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }

        //判断票券是否存在或票券是否被使用
        OrderCouponOut cp = orderCouponService.selectById(inVo.getId());

        //*验证商家是否支持对应满减规则*/
        if (cp.getGoodsSpu().getCategoryId().toString().equals("12")){
            ShopPromotionRules rules = new ShopPromotionRules();
            rules.setShopId(user.getShopId());
            rules.setGoodsSkuId(cp.getGoodsSkuId());
            ShopPromotionRules shopPromotionRules = promotionRulesService.selectByRules(rules);
            if (shopPromotionRules==null||shopPromotionRules.getId()==null){
                return new BaseResp<Integer>(ResultStatus.error_promotion_rules_not_have);
            }
        }

        //判断订单是否存在
        OrderInfo orderInfo = orderInfoService.get(cp.getOrderId());
        if(orderInfo==null){
            return new BaseResp<Integer>(ResultStatus.error_so_not_exist);
        }

        int re = orderCouponService.hxCouponV2(inVo);

        //核销完成之后推送核销完成信息到微信
        String shopName="享七自营";
        if(inVo.getSalepointId()!=null){
            SalePoint salePoint=salePointService.getSalePointByID(inVo.getSalepointId());
            if(salePoint!=null) {
                shopName = salePoint.getSalepointName();
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date=new java.util.Date();
        String str=sdf.format(date);
        StringBuilder sb = new StringBuilder(user.getMobile());
        sb.replace(3, 7, "****");
        String keyWords=cp.getGoodsSkuName()+","+str+","+shopName+","+sb+",您的订单已核销";//卡卷名称+核销时间+门店+店员昵称+备注
        Integer res =weiXinPushService.pushByUserId(WeiXinTeamplateMsg.templateId_TYPE_HX, "",keyWords,cp.getOwnId() );
        //核销完之后增加票数和抽奖机会
        kafkaService.sendDataToTopic("messageAndActivitys", "messageAndActivitys", JSON.toJSONString(inVo));
        //核销完成之后发送消息到小程序的消息列表
        messageService.addMessage("核销成功", "您的 " + cp.getGoodsSkuName() + " 已核销。", MessageText.MESSAGE_TEXT_TYPE_PRIVATE, cp.getOwnId(), user.getId());
        return new BaseResp<Integer>(ResultStatus.SUCCESS,re);

    }


    /**
     * 领取票券(新)
     *
     * receiveUserId是当前用户id,要从网关中获取,该versionNo是页面上面带过来的版本号,而不是查出数据的版本号
     * @param inVo
     * @return
     */
    @ApiOperation(value = "领取票券(新)")
    @RequestMapping(value = "/sendVersionCoupon",method = RequestMethod.POST)
    public BaseResp<Integer> sendVersionCoupon(OrderCouponSend inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        inVo.setReceiveUserId(user.getId());
        if(inVo==null||inVo.getOrderCouponCode()==null
                ||inVo.getSendUserId()==null||inVo.getReceiveUserId()==null||inVo.getVersionNo()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }

        Integer re = orderCouponService.sendVersionCoupon(inVo);

        return new BaseResp<Integer>(ResultStatus.SUCCESS,re);
    }

    /**
     * 赠送票券
     * @param inVo
     * @return
     */
    @ApiOperation(value = "赠送票券")
    @RequestMapping(value = "/complimentaryTickets",method = RequestMethod.POST)
    public BaseResp<Integer> complimentaryTickets(OrderCouponSend inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        inVo.setReceiveUserId(user.getId());
        if(inVo==null||inVo.getOrderCouponCode()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        OrderCoupon orderCoupon = orderCouponService.getOrderCouponInfo(inVo);
        if (orderCoupon.getStatus()!=0){
            return new BaseResp<Integer>(ResultStatus.FAIL);
        }
        Integer re = orderCouponService.complimentaryTickets(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS,re);
    }

    public void setShopName(OrderCouponInVo inVo){
        if(new Long(0).toString().equals(inVo.getShopId().toString())){
            inVo.setShopName("享七自营");
        }else{
            Shop shopById = shopService.getShopById(inVo.getShopId());
            inVo.setShopName(shopById.getShopName());
        }
    }
    /**
     * 确认收货
     * @param inVo
     * @return
     */
    @ApiOperation(value = "确认收货")
    @RequestMapping(value = "/receiving",method = RequestMethod.POST)
    public BaseResp<Integer> Receiving(OrderCouponInVo inVo){
        OrderCoupon orderCoupon=new OrderCoupon();
        orderCoupon.setId(inVo.getId());
        orderCoupon.setExpressState(OrderCoupon.EXPRESSSTATE_FINISH);
        Integer i=orderCouponService.updateCoupon(orderCoupon);
        return new BaseResp<Integer>(ResultStatus.SUCCESS,i);
    }

    /**
     * @param inVo
     * @return
     */
    @ApiOperation(value = "分页查询会员票券信息")
    @RequestMapping(value = "/listForVip", method = RequestMethod.GET)
    public BaseResp<Pager<OrderCouponOut>> listForVip(OrderCouponInVo inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Pager<OrderCouponOut>>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        Pager<OrderCouponOut> result = orderCouponService.listForVip(inVo);
        return new BaseResp<Pager<OrderCouponOut>>(ResultStatus.SUCCESS, result);
    }

}
