package com.xq.live.service.impl;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.xq.live.common.Pager;
import com.xq.live.common.PaymentConfig;
import com.xq.live.dao.*;
import com.xq.live.model.*;
import com.xq.live.service.AccountService;
import com.xq.live.service.MessageService;
import com.xq.live.service.OrderInfoService;
import com.xq.live.service.PayRefundService;
import com.xq.live.vo.in.OrderInfoInVo;
import com.xq.live.vo.in.PayRefundInVo;
import com.xq.live.vo.in.PayRefundReasonInVo;
import com.xq.live.vo.in.UserAccountInVo;
import com.xq.live.vo.out.OrderInfoOut;
import com.xq.live.vo.out.OrderItemOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2018/10/19.
 */
@Service
public class PayRefundServiceImpl implements PayRefundService {

    @Autowired
    private OrderCouponMapper orderCouponMapper;

    @Autowired
    private PayRefundApplicationMapper payRefundApplicationMapper;

    @Autowired
    private PayRefundReasonMapper payRefundReasonMapper;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private PayRefundLogMapper payRefundLogMapper;

    @Autowired
    private MessageService messageService;

    @Autowired
    private AccountService accountService;

    @Value("${weixin.payrefund.enabled}")
    private Boolean payrefundEnable;

    private WXPay wxpay;
    private PaymentConfig config;

    public PayRefundServiceImpl() throws Exception {
        config = PaymentConfig.getInstance();
        wxpay = new WXPay(config, WXPayConstants.SignType.MD5);
    }

    @Override
    public Boolean isPaidUser(String out_trade_no) {
        Boolean result=false;
        OrderCoupon orderCoupon=orderCouponMapper.getByCouponCode(out_trade_no);
        if(orderCoupon.getOwnId()==orderCoupon.getUserId()){
            result=true;
        }
        return result;
    }

    @Override
    public Long addPayRefund(PayRefundApplication payRefundApplication) {
        int i = payRefundApplicationMapper.insert(payRefundApplication);
        if (i < 1) {
            return null;
        }
        return payRefundApplication.getId();
    }

    @Override
    public Pager<PayRefundApplication> listPayRefundApplication(PayRefundInVo payRefundInVo) {
        Pager<PayRefundApplication> result = new Pager<PayRefundApplication>();
        int listTotal =payRefundApplicationMapper.ListTotalPayRefundApplication(payRefundInVo);
        result.setTotal(listTotal);
        if (listTotal > 0) {
            List<PayRefundApplication> list=payRefundApplicationMapper.listPayRefundApplication(payRefundInVo);
            result.setList(list);
        }
        result.setPage(payRefundInVo.getPage());
        return result;
    }

    @Override
    public int ListTotalPayRefundApplication(PayRefundInVo payRefundInVo) {
        int total=payRefundApplicationMapper.ListTotalPayRefundApplication(payRefundInVo);
        return total;
    }

    @Override
    public Pager<PayRefundReason> listReason(PayRefundReasonInVo inVo) {
        Pager<PayRefundReason> result = new Pager<PayRefundReason>();
        int listTotal =payRefundReasonMapper.listTotalPayRefundReason(inVo);
        result.setTotal(listTotal);
        if (listTotal > 0) {
            List<PayRefundReason> list=payRefundReasonMapper.listPayRefundReason(inVo);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public void refund(Long orderId) {
        if(!payrefundEnable)return ;
        //根据id获取订单信息
        OrderInfoOut orderInfoOut=orderInfoService.getDetail(orderId);
        List<OrderItemOut> orderItemOuts = orderInfoOut.getOrderItemOuts();//查询订单项详情
        if (orderInfoOut == null) {
            return ;
        }
        //订单已完成
        if (orderInfoOut.getStatus()!=OrderInfo.STATUS_WAIT_SH&&orderInfoOut.getStatus()!=OrderInfo.STATUS_IS_SUCCESS) {
            return ;
        }
        List<OrderCoupon> orderCouponlist=orderCouponMapper.listOrderCouponByOrdercode(orderInfoOut.getOrderCode());
        if(orderCouponlist!=null&&orderCouponlist.size()>0){
            for(OrderCoupon orderCoupon :orderCouponlist){
                //判断是否有票卷在退款中或者已退款
                if(orderCoupon.getStatus()!=null&&(orderCoupon.getStatus()==OrderCoupon.STATUS_REFUND_APPLICATION||orderCoupon.getStatus()==OrderCoupon.STATUS_REFUND)){
                    return ;
                }

                //有票卷已使用不允许退款
                if(orderCoupon.getIsUsed()==OrderCoupon.ORDER_COUPON_IS_USED_YES){
                    return ;
                }

                //有票卷被赠送了之后不允许退款
                if(!String.valueOf(orderCoupon.getUserId()).equals(String.valueOf(orderCoupon.getOwnId()))){
                    return ;
                }
            }
            //更改票卷的状态为退款中
            for(OrderCoupon orderCoupon :orderCouponlist){
                OrderCoupon oc= new OrderCoupon();
                oc.setStatus(OrderCoupon.STATUS_REFUND_APPLICATION);
                oc.setId(orderCoupon.getId());
                oc.setUpdateTime(new Date());
                orderCouponMapper.updateByPrimaryKeySelective(oc);
            }
        }

        OrderInfoInVo orderInfoInVo=new OrderInfoInVo();
        orderInfoInVo.setOrderCode(orderInfoOut.getOrderCode());
        orderInfoInVo.setStatus(OrderInfo.STATUS_REFUND_APPICATION);
        orderInfoMapper.refundApplication(orderInfoInVo);

        //完成之后发送消息到小程序的消息列表
        messageService.addMessage("拼购失败", "您的 "+orderItemOuts.get(0).getGoodsSkuName()+" 拼购失败。", MessageText.MESSAGE_TEXT_TYPE_PRIVATE, orderInfoOut.getUserId(), orderInfoOut.getUserId());

        //余额退款
        if(orderInfoOut.getPayType()==OrderInfo.PAY_TYPE_XQYE&&orderInfoOut.getAccountAmount().compareTo(orderInfoOut.getRealAmount())==0){
            //订单标记为已退款状态
            OrderInfoInVo orderInfo=new OrderInfoInVo();
            orderInfo.setOrderCode(orderInfoOut.getOrderCode());
            orderInfo.setStatus(OrderInfo.STATUS_REFUND);
            orderInfoMapper.refundApplication(orderInfo);

            //票卷标记为已退款状态
            if(orderCouponlist!=null&&orderCouponlist.size()>0){
                for(OrderCoupon orderCoupon :orderCouponlist){
                    OrderCoupon oc= new OrderCoupon();
                    oc.setUpdateTime(new Date());
                    oc.setStatus(OrderCoupon.STATUS_REFUND);
                    oc.setId(orderCoupon.getId());
                    orderCouponMapper.updateByPrimaryKeySelective(oc);
                }
            }

            //增加用户余额
            UserAccountInVo userAccountInVo=new UserAccountInVo();
            userAccountInVo.setUserId(orderInfoOut.getUserId());
            userAccountInVo.setOccurAmount(orderInfoOut.getAccountAmount());
            userAccountInVo.setType(AccountLog.TYPE_USER);
            accountService.updateIncome(userAccountInVo, "拼团失败退款",null,orderInfoOut.getId());

        }else{//微信退款
            int total_fee=orderInfoOut.getRealAmount().multiply(new BigDecimal(100)).intValue();
            int refund_fee=orderInfoOut.getRealAmount().multiply(new BigDecimal(100)).intValue();
            String nonce_str = WXPayUtil.generateNonceStr();
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("appid",config.getAppID());
            data.put("mch_id",config.getMchID());
            data.put("nonce_str",nonce_str);
            data.put("out_trade_no",orderInfoOut.getOrderCode());
            data.put("out_refund_no",orderInfoOut.getOrderCode());
            data.put("total_fee",String.valueOf(total_fee));
            data.put("refund_fee",String.valueOf(refund_fee));
            data.put("refund_desc","参与"+orderItemOuts.get(0).getGoodsSkuName()+"开团失败");
            data.put("notify_url",config.NOTIFY_PAY_REFUND_URL);
            try{
                Map<String, String> rMap=new HashMap<String, String>();

                String sign = WXPayUtil.generateSignature(data, config.API_KEY);
                data.put("sign",sign);
                rMap = wxpay.refund(data);
                System.out.println("退款接口返回: " + rMap);
                String return_code = rMap.get("return_code");//返回状态码
                String result_code = rMap.get("result_code");//业务结果
                if ("SUCCESS".equals(return_code) && return_code.equals(result_code)){
                    return ;
                }else{
                    String err_code_des = rMap.get("err_code_des");//
                    return;
                }
            }catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }


    }

    @Override
    public void saveRefungResult(Map<String, String> refundMap) throws ParseException {
        String transaction_id=(String)refundMap.get("transaction_id");//微信订单号
        String refund_id=(String)refundMap.get("refund_id");//微信退款单号
        String out_trade_no=(String)refundMap.get("out_trade_no");//商户系统内部的订单号
        String out_refund_no=(String)refundMap.get("out_refund_no");//商户退款单号
        String total_fee = (String) refundMap.get("total_fee");//订单总金额
        String refund_fee = (String) refundMap.get("refund_fee");//退款总金额
        String settlement_refund_fee = (String) refundMap.get("settlement_refund_fee");//退款金额=申请退款金额-非充值代金券退款金额
        String refund_status = (String) refundMap.get("refund_status");//退款状态SUCCESS-退款成功 CHANGE-退款异常 REFUNDCLOSE—退款关闭
        String refund_recv_accout = (String) refundMap.get("refund_recv_accout");//退款入账账户
        String success_time = (String) refundMap.get("success_time");//退款成功时间

        //退款日志
        //微信退款会多次调用回调url，根据OutRefundNo判断是更新退款日志还是新增日志
        List<PayRefundLog> payRefundLogList=payRefundLogMapper.listByOutRefundNo(out_refund_no);
        Boolean hasPush=false;//通过回调记录判断是否已经推送过微信消息
        if(payRefundLogList!=null&&payRefundLogList.size()>0){
            hasPush=true;
            for(PayRefundLog payRefundLog:payRefundLogList){
                payRefundLog.setTransactionId(transaction_id);
                payRefundLog.setOutTradeNo(out_trade_no);
                payRefundLog.setOutRefundNo(out_refund_no);
                payRefundLog.setRefundId(refund_id);
                payRefundLog.setTotalFee(new BigDecimal(total_fee).divide(new BigDecimal(100)));
                payRefundLog.setRefundFee(new BigDecimal(refund_fee).divide(new BigDecimal(100)));
                payRefundLog.setSettlementRefundFee(new BigDecimal(settlement_refund_fee).divide(new BigDecimal(100)));
                payRefundLog.setRefundStatus(refund_status);
                payRefundLog.setRefundRecvAccout(refund_recv_accout);
                payRefundLogMapper.updateByPrimaryKeySelective(payRefundLog);
            }
        }else{
            PayRefundLog payRefundLog=new PayRefundLog();
            payRefundLog.setTransactionId(transaction_id);
            payRefundLog.setOutTradeNo(out_trade_no);
            payRefundLog.setOutRefundNo(out_refund_no);
            payRefundLog.setRefundId(refund_id);
            payRefundLog.setTotalFee(new BigDecimal(total_fee).divide(new BigDecimal(100)));
            payRefundLog.setRefundFee(new BigDecimal(refund_fee).divide(new BigDecimal(100)));
            payRefundLog.setSettlementRefundFee(new BigDecimal(settlement_refund_fee).divide(new BigDecimal(100)));
            payRefundLog.setRefundStatus(refund_status);
            payRefundLog.setRefundRecvAccout(refund_recv_accout);
            payRefundLogMapper.insert(payRefundLog);
        }

        if(!hasPush){
            //订单标记为已退款状态
            OrderInfoInVo orderInfoInVo=new OrderInfoInVo();
            orderInfoInVo.setOrderCode(out_refund_no);
            orderInfoInVo.setStatus(OrderInfo.STATUS_REFUND);
            orderInfoMapper.refundApplication(orderInfoInVo);

            //票卷标记为已退款状态
            List<OrderCoupon> orderCouponlist=orderCouponMapper.listOrderCouponByOrdercode(out_refund_no);
            if(orderCouponlist!=null&&orderCouponlist.size()>0){
                for(OrderCoupon orderCoupon :orderCouponlist){
                    OrderCoupon oc= new OrderCoupon();
                    oc.setStatus(OrderCoupon.STATUS_REFUND);
                    oc.setUpdateTime(new Date());
                    oc.setId(orderCoupon.getId());
                    orderCouponMapper.updateByPrimaryKeySelective(oc);
                }
            }
        }
    }
}
