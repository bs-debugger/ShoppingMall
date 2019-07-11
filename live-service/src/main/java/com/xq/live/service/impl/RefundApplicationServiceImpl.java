package com.xq.live.service.impl;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.xq.live.common.*;
import com.xq.live.dao.*;
import com.xq.live.model.AccountLog;
import com.xq.live.model.OrderInfo;
import com.xq.live.model.PayRefundApplication;
import com.xq.live.model.So;
import com.xq.live.service.AccountService;
import com.xq.live.service.RefundApplicationService;
import com.xq.live.vo.in.*;
import com.xq.live.vo.out.OrderInfoBoOut;
import com.xq.live.vo.out.PayRefundApplicationOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RefundApplicationServiceImpl implements RefundApplicationService {

    @Autowired
    private RefundApplicationMapper refundApplicationMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderCouponMapper orderCouponMapper;

    @Autowired
    private SoMapper soMapper;

    @Autowired
    private SmsSendMapper smsSendMapper;

    private PaymentConfig config;

    private WXPay wxpay;

    private PaymentForAppConfig configForApp;

    private WXPay wxPayForApp;



    /**
     * 退款申请列表
     */
    @Override
    public Pager<PayRefundApplicationOut> getList(PayRefundApplicationInVO payRefundApplicationVO) {
        //创建分页对象
        Pager<PayRefundApplicationOut> result = new Pager<>();
        //查询列表
        List<PayRefundApplicationOut> list = refundApplicationMapper.getList(payRefundApplicationVO);
        //转为输出实例
        List<PayRefundApplicationOut> accountOuts = ListObjConverter.convert(list, PayRefundApplicationOut.class);
        result.setTotal(refundApplicationMapper.getListCount(payRefundApplicationVO));
        result.setPage(payRefundApplicationVO.getPage());
        result.setList(accountOuts);
        return result;
    }

    /**
     * 退款申请图表
     */
    @Override
    public BaseResp getTableList() {
        //封装数据对象
        BaseResp bs = new BaseResp();
        try {
            bs.setCode(1);
            bs.setData(refundApplicationMapper.getTableList());
            bs.setMessage("查询退款申请图表成功");
        } catch (Exception e) {
            bs.setCode(2);
            bs.setMessage("查询退款申请图表失败");
            e.printStackTrace();
        }
        return bs;
    }

    /**
     * 批量同意申请退款
     */
    @Override
    @Transactional
    public BaseResp agreeRefund(PayRefundApplicationInVO payRefundApplicationVO) {
        //封装数据对象
        BaseResp bs = new BaseResp();
        try {
            //根据id获取退款信息
            List<PayRefundApplicationOut> payRefundApplicationOuts = refundApplicationMapper.selectById(payRefundApplicationVO);
            if (payRefundApplicationVO.getIds().split(",").length > payRefundApplicationOuts.size()) {
                bs.setCode(2);
                bs.setMessage("批量申请中有不存在退款單");
                return bs;
            }
            //遍历审核状态
            for (PayRefundApplicationOut payRefundApplicationOut : payRefundApplicationOuts) {
                //批量更新
                payRefundApplicationOut.setStatus(PayRefundApplication.PAY_REFUND_STATUS_SPTG);
                Integer i = refundApplicationMapper.updateById(payRefundApplicationOut);
                if (i == null || i < 0) {
                    bs.setCode(2);
                    bs.setMessage("批量申请失敗");
                    return bs;
                }
                OrderInfoBoOut orderInfoBoOut = refundApplicationMapper.selectByOrderCode(payRefundApplicationOut.getOutTradeNo());
                //余额支付退款到用户余额
                if (orderInfoBoOut != null && orderInfoBoOut.getPayType() == OrderInfo.PAY_TYPE_XQYE
                        && orderInfoBoOut.getAccountAmount() != null && orderInfoBoOut.getAccountAmount().compareTo(BigDecimal.ZERO) == 1) {
                    String rsult = refundYE(orderInfoBoOut);
                    bs.setMessage(rsult);
                } else {//微信支付发送微信退款申请
                    int total_fee = payRefundApplicationOut.getTotalFee().multiply(new BigDecimal(100)).intValue();
                    int refund_fee = payRefundApplicationOut.getRefundFee().multiply(new BigDecimal(100)).intValue();
                    String nonce_str = WXPayUtil.generateNonceStr();
                    HashMap<String, String> data = new HashMap<String, String>();
                    data.put("appid", config.getAppID());
                    data.put("mch_id", config.getMchID());
                    data.put("nonce_str", nonce_str);
                    data.put("out_trade_no", payRefundApplicationOut.getOutTradeNo());
                    data.put("out_refund_no", payRefundApplicationOut.getOutRefundNo());
                    data.put("total_fee", String.valueOf(total_fee));
                    data.put("refund_fee", String.valueOf(refund_fee));
                    data.put("refund_desc", "退款通知");

                    if (payRefundApplicationOut.getType() == PayRefundApplication.PAY_REFUND_TYPE_APP) {//app
                        data.put("appid", configForApp.getAppID());
                        data.put("mch_id", configForApp.getMchID());
                    } else if (payRefundApplicationOut.getType() == PayRefundApplication.PAY_REFUND_TYPE_MINI) {//小程序
                        data.put("appid", config.getAppID());
                        data.put("mch_id", config.getMchID());
                    } else {
                        //需指定是app上的支付还是小程序的支付退款
                        bs.setMessage("该申请未指定来源（app或者小程序）");
                        return bs;
                    }

                    //小程序和app用同一个结果通知url，在结果通知中根据MchID判断是小程序还是app
                    data.put("notify_url", config.NOTIFY_PAY_REFUND_URL);
                    Map<String, String> rMap = new HashMap<String, String>();
                    if (payRefundApplicationOut.getType() == PayRefundApplication.PAY_REFUND_TYPE_APP) {//app
                        String sign = WXPayUtil.generateSignature(data, configForApp.API_KEY);
                        data.put("sign", sign);

                        rMap = wxPayForApp.refund(data);
                    } else if (payRefundApplicationOut.getType() == PayRefundApplication.PAY_REFUND_TYPE_MINI) {//小程序
                        String sign = WXPayUtil.generateSignature(data, config.API_KEY);
                        data.put("sign", sign);
                        rMap = wxpay.refund(data);
                    }
                    System.out.println("退款接口返回: " + rMap);
                    String return_code = rMap.get("return_code");//返回状态码
                    String result_code = rMap.get("result_code");//业务结果
                    if ("SUCCESS".equals(return_code) && return_code.equals(result_code)) {
                        bs.setMessage("退款成功");
                    } else {
                        String err_code_des = rMap.get("err_code_des");//
                        bs.setMessage("退款失败:" + err_code_des);
                        return bs;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            bs.setMessage("批量退款失败");
            return bs;
        }
        bs.setMessage("批量退款成功");
        return bs;
    }

    /**
     * 批量驳回退款申请
     * */
    @Override
    @Transactional
    public BaseResp refuseRefund(PayRefundApplicationInVO payRefundApplicationVO) {
        //数据封装
        BaseResp bs = new BaseResp();
        try {
            //根据id获取退款信息
            List<PayRefundApplicationOut> payRefundApplicationOuts = refundApplicationMapper.selectById(payRefundApplicationVO);
            if (payRefundApplicationVO.getIds().split(",").length > payRefundApplicationOuts.size()) {
                bs.setCode(2);
                bs.setMessage("批量申请中有不存在退款單");
                return bs;
            }
        //遍历修改状态
        for(PayRefundApplicationOut payRefundApplicationOut : payRefundApplicationOuts) {
            payRefundApplicationOut.setStatus(PayRefundApplication.PAY_REFUND_STATUS_SPBTG);
            Integer i = refundApplicationMapper.updateById(payRefundApplicationOut);
            if(payRefundApplicationOut.getOrderType().equals(PayRefundApplication.PAY_REFUND_ORDER_TYPE_SO)){//so订单
                Long soId=Long.valueOf(payRefundApplicationOut.getOutTradeNo());
                SoConditionInVO so=new SoConditionInVO();
                so.setId(soId);
                so.setSoStatus(So.SO_STATUS_APPLIED);
                soMapper.UpdateById(so);//订单状态标记为已退款
                CouponConditionInVo couponConditionVo=new CouponConditionInVo();
                couponConditionVo.setSoId(soId);
                couponConditionVo.setStatus(CouponConditionInVo.STATUS_DEFAULT);
                soMapper.UpdateCouponBySoId(couponConditionVo);//标记订单下的票卷为退款状态
                //发短信
                if(payRefundApplicationVO.getIsSend() == 1) {
                    sendSms(payRefundApplicationVO.getContent(), payRefundApplicationOut.getUserName());
                }
            }else if (payRefundApplicationOut.getOrderType().equals(PayRefundApplication.PAY_REFUND_ORDER_TYPE_ORDER)) {//order商城订单
                OrderInfoConditionInVO orderInfo=new OrderInfoConditionInVO();
                orderInfo.setStatus(OrderInfo.STATUS_IS_SUCCESS);
                orderInfo.setOrderCode(payRefundApplicationOut.getOutTradeNo());
                orderInfoMapper.UpdateByOrderCode(orderInfo);//订单状态标记为已退款
                OrderCouponConditionInVO orderCouponConditionVO=new OrderCouponConditionInVO();
                orderCouponConditionVO.setStatus(OrderCouponConditionInVO.STATUS_DEFAULT);
                orderCouponConditionVO.setOrderCode(payRefundApplicationOut.getOutTradeNo());
                orderCouponMapper.updateStatus(orderCouponConditionVO);//标记订单下的票卷为退款状态
                if(payRefundApplicationVO.getIsSend() == 1) {
                    //发短信
                    sendSms(payRefundApplicationVO.getContent(), payRefundApplicationOut.getUserName());
                }
            }
            if(i==0){
                bs.setMessage("批量驳回失败");
                return  bs;
            }
        }
        } catch (Exception e) {
            e.printStackTrace();
            bs.setMessage("批量驳回成功");
            return bs;
            }
        bs.setMessage("批量驳回成功");
        return bs;
    }

    /**
     *批量退款
     **/
    private String refundYE(OrderInfoBoOut orderInfoBo) {

        //增加户余额
        UserAccountInVo userAccountInVo=new UserAccountInVo();
        userAccountInVo.setUserId(orderInfoBo.getUserId());
        userAccountInVo.setOccurAmount(orderInfoBo.getAccountAmount());
        userAccountInVo.setType(AccountLog.TYPE_USER);
        accountService.updateIncome(userAccountInVo, "商品退款",null,orderInfoBo.getId());


        //保存退款申请相关信息
        PayRefundApplicationOut payRefundApplication=refundApplicationMapper.getByOutRefundNo(orderInfoBo.getOrderCode());
        payRefundApplication.setRefundStatus(PayRefundApplication.PAY_REFUND__REFUND_STATUS_SUCCESS);//退款成功
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        payRefundApplication.setRefundTime(new Date());
        payRefundApplication.setSettlementRefundFee(orderInfoBo.getAccountAmount());
        refundApplicationMapper.updateById(payRefundApplication);

        //订单和票卷标记为退款状态
        OrderInfoConditionInVO orderInfo=new OrderInfoConditionInVO();
        orderInfo.setStatus(OrderInfo.STATUS_REFUND);
        orderInfo.setOrderCode(orderInfoBo.getOrderCode());
        orderInfoMapper.UpdateByOrderCode(orderInfo);//订单状态标记为已退款
        OrderCouponConditionInVO orderCouponConditionVO=new OrderCouponConditionInVO();
        orderCouponConditionVO.setStatus(OrderCouponConditionInVO.STATUS_REFUND);
        orderCouponConditionVO.setOrderCode(orderInfoBo.getOrderCode());
        orderCouponMapper.updateStatus(orderCouponConditionVO);//标记订单下的票卷为退款状态

        return "退款成功";
    }

    public RefundApplicationServiceImpl() throws Exception {
        config = PaymentConfig.getInstance();
        wxpay = new WXPay(config, WXPayConstants.SignType.MD5);
        configForApp = PaymentForAppConfig.getInstance();
        wxPayForApp = new WXPay(configForApp, WXPayConstants.SignType.MD5);
    }
    public boolean sendSms(String tradename,String Telephone){
        //发送短信
        String message = MessageFormat.format(Constants.REFUND_APPLICATION,tradename);
        boolean isSend= SmsUtils.send(Telephone, message);
        //记录短信信息
        SmsSendInVo smsSendInVo = new SmsSendInVo();
        smsSendInVo.setSmsType(3);//提现驳回
        smsSendInVo.setSmsContent(message);
        smsSendInVo.setShopMobile(null);
        smsSendInVo.setShopId(null);
        smsSendInVo.setShopName(null);
        smsSendInVo.setUserName(Telephone);
        smsSendInVo.setSendStatus(isSend==true?1:0);
        smsSendInVo.setRemark("用户退款申請駁回");
        smsSendInVo.setCreateTime(new Date());
        smsSendMapper.create(smsSendInVo);
        return isSend;
    }
    }
