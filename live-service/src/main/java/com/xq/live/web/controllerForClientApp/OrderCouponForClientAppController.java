package com.xq.live.web.controllerForClientApp;

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
@RestController
@RequestMapping(value = "/clientApp/orderCoupon")
public class OrderCouponForClientAppController {
    @Autowired
    private OrderCouponService orderCouponService;

    @Autowired
    private OrderWriteOffService orderWriteOffService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private UserService  userService;

    @Autowired
    private SalePointService salePointService;

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
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<OrderCouponOut> get(@PathVariable("id") Long id){
        OrderCouponOut cp = orderCouponService.selectById(id);
        return new BaseResp<OrderCouponOut>(ResultStatus.SUCCESS, cp);
    }


    /**
     * 查询票券详情接口(新)
     *
     * 入参:id,locationX,locationY
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/getDetail", method = RequestMethod.GET)
    public BaseResp<OrderCouponOut> getDetail(OrderCouponInVo inVo){
        OrderCouponOut cp = orderCouponService.getDetail(inVo);
        return new BaseResp<OrderCouponOut>(ResultStatus.SUCCESS, cp);
    }

    /**
     * 根据券码查一条记录
     *
     * @param couponCode
     * @return
     */
    @RequestMapping(value = "/getByCode/{couponCode}", method = RequestMethod.GET)
    public BaseResp<OrderCouponOut> getByCode(@PathVariable("couponCode")String couponCode){
        if(StringUtils.isEmpty(couponCode)){
            return new BaseResp<OrderCouponOut>(ResultStatus.error_para_coupon_code_empty);
        }
        OrderCouponOut cp = orderCouponService.getDetailByCouponCode(couponCode);

        return new BaseResp<OrderCouponOut>(ResultStatus.SUCCESS, cp);
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
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResp<Pager<OrderCouponOut>> list(OrderCouponInVo inVo){
        Pager<OrderCouponOut> result = orderCouponService.list(inVo);
        return new BaseResp<Pager<OrderCouponOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 分页查询票券列表信息
     *
     * 注意:
     * 1.券列表:userId,page,rows  这个是最新的
     *
     * userId是当前用户的，要从网关中取
     * @param inVo
     * @return
     */
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
     * 核销券
     *
     * id(票券id), salepointId(销售点id可不传),shopId,shopName
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/hxCoupon",method = RequestMethod.POST)
    @Transactional
    public BaseResp<Integer> hxCoupon(OrderCouponInVo inVo)throws Exception{
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        inVo.setHxUserId(user.getId());//票券核销人是当前用户id
        inVo.setHxUserName(user.getUserName());
        if(inVo==null||inVo.getId()==null||inVo.getShopId()==null){
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

        int re = orderCouponService.hxCoupon(inVo);

        //核销完成后给商家投票
        voteService.addByOrderIdFroAct(orderInfo);

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

        //核销完成之后发送消息到小程序的消息列表
        messageService.addMessage("核销成功", "您的 "+cp.getGoodsSkuName()+" 已核销。", MessageText.MESSAGE_TEXT_TYPE_PRIVATE, cp.getOwnId(), user.getId());

        return new BaseResp<Integer>(ResultStatus.SUCCESS,re);

    }

    /**
     * 核销券
     *
     * id(票券id)
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/hxCouponV1",method = RequestMethod.POST)
    @Transactional
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

        //判断订单是否存在
        OrderInfo orderInfo = orderInfoService.get(cp.getOrderId());
        if(orderInfo==null){
            return new BaseResp<Integer>(ResultStatus.error_so_not_exist);
        }

        int re = orderCouponService.hxCoupon(inVo);

        //核销完成后给商家投票
        voteService.addByOrderIdFroAct(orderInfo);

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

        //核销完成之后发送消息到小程序的消息列表
        messageService.addMessage("核销成功", "您的 "+cp.getGoodsSkuName()+" 已核销。", MessageText.MESSAGE_TEXT_TYPE_PRIVATE, cp.getOwnId(), user.getId());

        return new BaseResp<Integer>(ResultStatus.SUCCESS,re);

    }


    /**
     * 查询是否拥有核销权限
     * @param id
     * @return
     */
    @RequestMapping(value = "/hxJurisdiction",method = RequestMethod.GET)
    public BaseResp<OrderCouponHxOut>   hxJurisdiction(Long id){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<OrderCouponHxOut>(ResultStatus.error_param_empty);
        }
        OrderCouponHxOut re =  orderCouponService.hxJurisdiction(id,user.getId(),user.getShopId());
        if(re.getErrorCode().equals("0")) {
            return new BaseResp<OrderCouponHxOut>(ResultStatus.SUCCESS,re);
        }
        return new BaseResp<OrderCouponHxOut>(ResultStatus.FAIL,re);
    }


    /**
     * 领取票券(新)
     *
     * receiveUserId是当前用户id,要从网关中获取
     * @param inVo
     * @return
     */
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

    public void setShopName(OrderCouponInVo inVo){
        if(new Long(0).equals(inVo.getShopId())){
            inVo.setShopName("享七自营");
        }else{
            Shop shopById = shopService.getShopById(inVo.getShopId());
            inVo.setShopName(shopById.getShopName());
        }
    }
}
