package com.xq.live.web.controllerForApp;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.xq.live.common.PaymentConfig;
import com.xq.live.common.PaymentForAppConfig;
import com.xq.live.model.AccountLog;
import com.xq.live.model.CashApply;
import com.xq.live.service.*;
import com.xq.live.vo.in.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 微信回调
 * Created by ss on 2018/10/20.
 */
@RestController
@RequestMapping("/app/wxpayNotify")
public class WxpayNotifyForAppController {

    private Logger logger = Logger.getLogger(WxpayNotifyForAppController.class);

    @Autowired
    private SoService soService;

    @Autowired
    private com.xq.live.service.ShopService ShopService;

    @Autowired
    private ServiceAmountService serviceAmountService;

    @Autowired
    private SoWriteOffService soWriteOffService;

    @Autowired
    private OrderWriteOffService orderWriteOffService;

    @Autowired
    private CashApplyService cashApplyService;

    @Autowired
    private AccountService accountService;

    private WXPay wxpay;
    private PaymentConfig config;

    private WXPay wxPayForApp;
    private PaymentForAppConfig configForApp;

    public WxpayNotifyForAppController() throws Exception {
        config = PaymentConfig.getInstance();
        wxpay = new WXPay(config, WXPayConstants.SignType.MD5);
        configForApp = PaymentForAppConfig.getInstance();
        wxPayForApp = new WXPay(configForApp, WXPayConstants.SignType.MD5);
    }


    /**
     * 微信支付结果通知(商家端app)
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/wxNotifyForShopApp")
    public void wxNotifyForShopApp(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //读取参数
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        //sb为微信返回的xml
        String notifyData = sb.toString();  //支付结果通知的xml格式数据
        System.out.println("支付结果通知的xml格式数据：" + notifyData);

        Map notifyMap = WXPayUtil.xmlToMap(notifyData);       // 转换成map
        String resXml = "";
        if (wxPayForApp.isPayResultNotifySignatureValid(notifyMap)) {
            // 签名正确
            if ("SUCCESS".equals(notifyMap.get("result_code"))) {
                //这里是支付成功
                //////////执行自己的业务逻辑////////////////
                String mch_id = (String) notifyMap.get("mch_id"); //商户号
                String out_trade_no = (String) notifyMap.get("out_trade_no"); //商户订单号
                String total_fee = (String) notifyMap.get("total_fee");
                String transaction_id = (String) notifyMap.get("transaction_id"); //微信支付订单号
                String attach = (String) notifyMap.get("attach");
                WeixinInVo weixinInVo = JSON.parseObject(attach, WeixinInVo.class);//将附带参数读取出来

                if (!PaymentForAppConfig.MCH_ID.equals(mch_id)) {
                    logger.info("支付失败,错误信息：" + "参数错误");
                    logger.info("notifyMap信息：" + notifyMap);
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[参数错误]]></return_msg>" + "</xml> ";
                } else {
                    //添加缴费记录begin
                    ServiceAmountInVo serviceAmountInVo = new ServiceAmountInVo();
                    serviceAmountInVo.setPaidUserId(weixinInVo.getUserId());
                    serviceAmountInVo.setShopId(weixinInVo.getShopId());
                    serviceAmountInVo.setServicePrice(weixinInVo.getServicePrice());
                    serviceAmountInVo.setBeginTime(weixinInVo.getBeginTime());
                    serviceAmountInVo.setEndTime(weixinInVo.getEndTime());
                    Long id = serviceAmountService.add(serviceAmountInVo);
                    //添加缴费记录end
                    //将缴费时间段内的核销的票券给结算掉
                    SoWriteOffInVo soWriteOffInVo = new SoWriteOffInVo();
                    soWriteOffInVo.setShopId(weixinInVo.getShopId());
                    soWriteOffInVo.setBegainTime(weixinInVo.getBeginTime());
                    soWriteOffInVo.setEndTime(weixinInVo.getEndTime());
                    int kk = soWriteOffService.updateByShopId(soWriteOffInVo);
                    resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

                }
            } else {
                logger.info("支付失败,错误信息：" + notifyMap.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
        } else {
            // 签名错误，如果数据里没有sign字段，也认为是签名错误
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[通知签名验证失败]]></return_msg>" + "</xml> ";
            logger.info("通知签名验证失败");
        }
        //------------------------------
        //处理业务完毕
        //------------------------------
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

    /**
     * 微信支付结果通知(商家端app)新
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/wxNotifyForShopAppNew")
    public void wxNotifyForShopAppNew(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //读取参数
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        //sb为微信返回的xml
        String notifyData = sb.toString();  //支付结果通知的xml格式数据
        System.out.println("支付结果通知的xml格式数据：" + notifyData);

        String resXml = "";
        Map notifyMap = WXPayUtil.xmlToMap(notifyData);       // 转换成map
        if (wxPayForApp.isPayResultNotifySignatureValid(notifyMap)) {
            // 签名正确
            if ("SUCCESS".equals(notifyMap.get("result_code"))) {
                //这里是支付成功
                //////////执行自己的业务逻辑////////////////
                String mch_id = (String) notifyMap.get("mch_id"); //商户号
                String total_fee = (String) notifyMap.get("total_fee");
                String out_trade_no = (String) notifyMap.get("out_trade_no"); //商户订单号
                String transaction_id = (String) notifyMap.get("transaction_id"); //微信支付订单号
                String attach = (String) notifyMap.get("attach");
                WeixinInVo weixinInVo = JSON.parseObject(attach, WeixinInVo.class);//将附带参数读取出来

                if (!PaymentForAppConfig.MCH_ID.equals(mch_id)) {
                    logger.info("notifyMap信息：" + notifyMap);
                    logger.info("支付失败,错误信息：" + "参数错误");
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[参数错误]]></return_msg>" + "</xml> ";
                } else {
                    //添加缴费记录begin
                    ServiceAmountInVo serviceAmountInVo = new ServiceAmountInVo();
                    serviceAmountInVo.setShopId(weixinInVo.getShopId());
                    serviceAmountInVo.setPaidUserId(weixinInVo.getUserId());
                    serviceAmountInVo.setServicePrice(weixinInVo.getServicePrice());
                    serviceAmountInVo.setBeginTime(weixinInVo.getBeginTime());
                    serviceAmountInVo.setEndTime(weixinInVo.getEndTime());
                    Long id = serviceAmountService.add(serviceAmountInVo);
                    //添加缴费记录end
                    //将缴费时间段内的核销的票券给结算掉
                    OrderWriteOffInVo orderWriteOffInVo = new OrderWriteOffInVo();
                    orderWriteOffInVo.setShopId(weixinInVo.getShopId());
                    orderWriteOffInVo.setBeginTime(weixinInVo.getBeginTime());
                    orderWriteOffInVo.setEndTime(weixinInVo.getEndTime());
                    int kk = orderWriteOffService.updateByShopId(orderWriteOffInVo);
                    resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

                }
            } else {
                logger.info("支付失败,错误信息：" + notifyMap.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
        } else {
            // 签名错误，如果数据里没有sign字段，也认为是签名错误
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[通知签名验证失败]]></return_msg>" + "</xml> ";
            logger.info("通知签名验证失败");
        }
        //------------------------------
        //处理业务完毕
        //------------------------------
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

    /**
     * 微信支付结果通知(商家端app)新
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/wxNotifyForShopAppv1")
    public void wxNotifyForShopAppv1(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //读取参数
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        //sb为微信返回的xml
        String notifyData = sb.toString();  //支付结果通知的xml格式数据
        System.out.println("支付结果通知的xml格式数据：" + notifyData);

        String resXml = "";
        Map notifyMap = WXPayUtil.xmlToMap(notifyData);       // 转换成map
        if (wxPayForApp.isPayResultNotifySignatureValid(notifyMap)) {
            // 签名正确
            if ("SUCCESS".equals(notifyMap.get("result_code"))) {
                //这里是支付成功
                //////////执行自己的业务逻辑////////////////
                String mch_id = (String) notifyMap.get("mch_id"); //商户号
                String total_fee = (String) notifyMap.get("total_fee");
                String out_trade_no = (String) notifyMap.get("out_trade_no"); //商户订单号
                String transaction_id = (String) notifyMap.get("transaction_id"); //微信支付订单号
                String attach = (String) notifyMap.get("attach");
                WeixinInVo weixinInVo = JSON.parseObject(attach, WeixinInVo.class);//将附带参数读取出来

                CashApply cashApply = cashApplyService.get(weixinInVo.getCashApplyId());
                if (!PaymentForAppConfig.MCH_ID.equals(mch_id)) {
                    logger.info("notifyMap信息：" + notifyMap);
                    logger.info("支付失败,错误信息：" + "参数错误");
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[参数错误]]></return_msg>" + "</xml> ";
                } else {
                    if(cashApply.getApplyStatus()==CashApply.CASH_APPLY_STATUS_WAIT){
                        cashApply.setApplyStatus(Byte.valueOf(String.valueOf(CashApply.CASH_APPLY_STATUS_TG)));
                        cashApplyService.paystart(cashApply);//修改申请状态

                        //1. 账户日志
                        UserAccountInVo accountInVo = new UserAccountInVo();
                        accountInVo.setUserId(cashApply.getUserId());
                        accountInVo.setOccurAmount((new BigDecimal(total_fee)).divide(new BigDecimal(100)));
                        accountInVo.setType(AccountLog.TYPE_SHOP);
                        AccountLog accountLogInVo = new AccountLog();
                        accountLogInVo.setRemark("余额充值" );
                        accountService.updateIncomeV1(accountInVo, accountLogInVo);

                        //2.修改对应时间段的对账状态
                        CashApplyInVo cashApplyInVo=new CashApplyInVo();
                        cashApplyInVo.setShopId(weixinInVo.getShopId());
                        cashApplyInVo.setBeginTime(cashApply.getBeginTime());
                        cashApplyInVo.setEndTime(cashApply.getEndTime());
                        cashApplyService.updateOrderCouponList(cashApplyInVo);


                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    }else{
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                        logger.info("订单已处理");
                    }

                }
            } else {
                logger.info("支付失败,错误信息：" + notifyMap.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
        } else {
            // 签名错误，如果数据里没有sign字段，也认为是签名错误
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[通知签名验证失败]]></return_msg>" + "</xml> ";
            logger.info("通知签名验证失败");
        }
        //------------------------------
        //处理业务完毕
        //------------------------------
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

}
