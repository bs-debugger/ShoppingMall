package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.dao.CouponMapper;
import com.xq.live.dao.OrderCouponMapper;
import com.xq.live.dao.OrderInfoMapper;
import com.xq.live.dao.SoMapper;
import com.xq.live.model.*;
import com.xq.live.service.OrderCouponService;
import com.xq.live.service.OrderInfoService;
import com.xq.live.service.PayRefundService;
import com.xq.live.service.SoService;
import com.xq.live.vo.in.OrderInfoInVo;
import com.xq.live.vo.in.PayRefundInVo;
import com.xq.live.vo.in.PayRefundReasonInVo;
import com.xq.live.vo.in.SoInVo;
import com.xq.live.vo.out.OrderInfoOut;
import com.xq.live.vo.out.SoOut;
import com.xq.live.web.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 退款申请接口
 * Created by admin on 2018/10/19.
 */
@RestController
@RequestMapping(value = "/payRefund")
public class PayRefundController {

    @Autowired
    private PayRefundService payRefundService;

    @Autowired
    private OrderCouponService orderCouponService;

    @Autowired
    private SoService soService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private SoMapper soMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderCouponMapper orderCouponMapper;

    @Autowired
    private CouponMapper couponMapper;
    /**
     * 添加退款申请
     * OutTradeNo 订单号
     * OrderType 订单类型 1,so订单 2 order商城订单 3 order_coupon商城卷
     * ApplyReason 退款原因
     * UserId 退款人ID
     *
     * 注:userId为当前用户，从网关中获取
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/addApplication", method = RequestMethod.POST)
    public BaseResp<Long> add(PayRefundInVo inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        if(inVo.getOutTradeNo()==null||inVo.getOrderType()==null||(inVo.getApplyReason()==null&&inVo.getOtherReason()==null)||inVo.getUserId()==null){
            return  new BaseResp<Long>(ResultStatus.error_param_empty);
        }

        //订单有待审批和审批通过申请时不能重复提交申请
        inVo.setStatus(PayRefundApplication.PAY_REFUND_STATUS_SPBTG);
        int total=payRefundService.ListTotalPayRefundApplication(inVo);
        if(total>0){
            return  new BaseResp<Long>(ResultStatus.error_refund_has_application);
        }

        PayRefundApplication payRefundApplication=new PayRefundApplication();
        //so订单
        if(inVo.getOrderType()== PayRefundApplication.PAY_REFUND_ORDER_TYPE_SO){
            Long soId=Long.valueOf(inVo.getOutTradeNo());
            SoOut soOut=soService.get(soId);
            if(soOut==null){
                return new BaseResp<Long>(ResultStatus.error_so_not_exist);
            }
            //已支付才能退款
            if(soOut.getSoStatus()!=So.SO_STATUS_PAID){
                return new BaseResp<Long>(ResultStatus.error_not_allow_refund);
            }

            List<Coupon> coupons= couponMapper.listCouponBySoId(soId);
            if(coupons!=null&&coupons.size()>0){
                for(Coupon coupon:coupons){
                    //有票卷已使用不允许退款
                    if(coupon.getIsUsed()==Coupon.COUPON_IS_USED_YES){
                        return new BaseResp<Long>(ResultStatus.error_coupon_is_used);
                    }
                    //判断是否有票卷在退款中或者已退款
                    if(coupon.getStatus()!=null&&(coupon.getStatus()==Coupon.STATUS_REFUND||coupon.getStatus()==Coupon.STATUS_REFUND_APPLICATION)){
                        return new BaseResp<Long>(ResultStatus.error_refund_has_application);
                    }
                }
                //更改票卷的状态为退款中
                for(Coupon coupon:coupons){
                    Coupon cp=new Coupon();
                    cp.setId(coupon.getId());
                    cp.setUpdateTime(new Date());
                    cp.setStatus(Coupon.STATUS_REFUND_APPLICATION);
                    couponMapper.updateByPrimaryKeySelective(cp);
                }
            }

            SoInVo soInVo=new SoInVo();
            soInVo.setId(soId);
            soInVo.setSoStatus(So.SO_STATUS_REFUND_APPLICATION);
            soMapper.refundApplication(soInVo);
            payRefundApplication.setTotalFee(soOut.getSoAmount());
            payRefundApplication.setRefundFee(soOut.getSoAmount());
        }
        //order商城订单
        if(inVo.getOrderType()== PayRefundApplication.PAY_REFUND_ORDER_TYPE_ORDER){
            OrderInfoOut orderInfoOut=orderInfoService.getDetailByOrderCode(inVo.getOutTradeNo());
            if(orderInfoOut==null){
                return new BaseResp<Long>(ResultStatus.error_so_not_exist);
            }

            //待收货和已完成才能退款
            if(orderInfoOut.getStatus()!=OrderInfo.STATUS_WAIT_SH&&orderInfoOut.getStatus()!=OrderInfo.STATUS_IS_SUCCESS){
                return new BaseResp<Long>(ResultStatus.error_not_allow_refund);
            }

            List<OrderCoupon> orderCouponlist=orderCouponMapper.listOrderCouponByOrdercode(inVo.getOutTradeNo());
            if(orderCouponlist!=null&&orderCouponlist.size()>0){
                for(OrderCoupon orderCoupon :orderCouponlist){
                    //判断是否有票卷在退款中或者已退款
                    if(orderCoupon.getStatus()!=null&&(orderCoupon.getStatus()==OrderCoupon.STATUS_REFUND_APPLICATION||orderCoupon.getStatus()==OrderCoupon.STATUS_REFUND)){
                        return new BaseResp<Long>(ResultStatus.error_refund_has_application);
                    }

                    //有票卷已使用不允许退款
                    if(orderCoupon.getIsUsed()==OrderCoupon.ORDER_COUPON_IS_USED_YES){
                        return new BaseResp<Long>(ResultStatus.error_coupon_is_used);
                    }

                    //有票卷被赠送了之后不允许退款
                    if(!String.valueOf(orderCoupon.getUserId()).equals(String.valueOf(orderCoupon.getOwnId()))){
                        return  new BaseResp<Long>(ResultStatus.error_refund_not_paid_user);
                    }

                    if(orderCoupon.getExpiryDate().before(new Date())){
                        return  new BaseResp<Long>(ResultStatus.error_order_over_date);
                    }
                }
                //更改票卷的状态为退款中
                for(OrderCoupon orderCoupon :orderCouponlist){
                    OrderCoupon oc= new OrderCoupon();
                    oc.setId(orderCoupon.getId());
                    oc.setStatus(OrderCoupon.STATUS_REFUND_APPLICATION);
                    oc.setUpdateTime(new Date());
                    orderCouponMapper.updateByPrimaryKeySelective(oc);
                }
            }
            payRefundApplication.setTotalFee(orderInfoOut.getRealAmount());
            payRefundApplication.setRefundFee(orderInfoOut.getRealAmount());
            OrderInfoInVo orderInfoInVo=new OrderInfoInVo();
            orderInfoInVo.setOrderCode(inVo.getOutTradeNo());
            orderInfoInVo.setStatus(OrderInfo.STATUS_REFUND_APPICATION);
            orderInfoMapper.refundApplication(orderInfoInVo);
        }

        //order_coupon商城卷
        if(inVo.getOrderType()== PayRefundApplication.PAY_REFUND_ORDER_TYPE_ORDER_COUPON){
            //判断当前商城卷持有人是否为购买人
            Boolean isPaidUser=payRefundService.isPaidUser(inVo.getOutTradeNo());
            if(!isPaidUser){
                return  new BaseResp<Long>(ResultStatus.error_refund_not_paid_user);
            }
            OrderCoupon orderCoupon=orderCouponService.getByCouponCode(inVo.getOutTradeNo());
            if(orderCoupon==null){
                return new BaseResp<Long>(ResultStatus.error_so_not_exist);
            }
            payRefundApplication.setTotalFee(orderCoupon.getCouponAmount());
            payRefundApplication.setRefundFee(orderCoupon.getCouponAmount());
        }

        payRefundApplication.setOutTradeNo(inVo.getOutTradeNo());
        payRefundApplication.setOutRefundNo(inVo.getOutTradeNo());
        payRefundApplication.setApplyReason(inVo.getApplyReason());
        payRefundApplication.setStatus(PayRefundApplication.PAY_REFUND_STATUS_DSP);//审核状态:0 待审批
        payRefundApplication.setType(PayRefundApplication.PAY_REFUND_TYPE_MINI);//申请来源类型:1,小程序
        payRefundApplication.setOrderType(inVo.getOrderType());//订单类型 1,so订单 2 order商城订单 3 order_coupon商城卷
        payRefundApplication.setUserId(inVo.getUserId());
        payRefundApplication.setOtherReason(inVo.getOtherReason());

        Long id=payRefundService.addPayRefund(payRefundApplication);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 获取退款原因列表
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/listReason", method = RequestMethod.GET)
    public BaseResp<Pager<PayRefundReason>> listReason(PayRefundReasonInVo inVo){
        Pager<PayRefundReason> result=payRefundService.listReason(inVo);
        return new BaseResp<Pager<PayRefundReason>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 获取退款进度
     * OutTradeNo 订单号
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/listApplication", method = RequestMethod.GET)
    public BaseResp<Pager<PayRefundApplication>> list(PayRefundInVo inVo){
        if(inVo.getOutTradeNo()==null){
            return  new BaseResp<Pager<PayRefundApplication>>(ResultStatus.error_param_empty);
        }
        Pager<PayRefundApplication> result=payRefundService.listPayRefundApplication(inVo);
        return new BaseResp<Pager<PayRefundApplication>>(ResultStatus.SUCCESS, result);
    }
}
