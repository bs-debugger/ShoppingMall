package com.xq.live.web.controller;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.xq.live.common.PaymentConfig;
import com.xq.live.common.PaymentForAppConfig;
import com.xq.live.service.PayRefundService;
import com.xq.live.web.utils.PayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by admin on 2018/12/01.
 */
@RestController
@RequestMapping("/wxRefundNotify")
public class WxRefundNotifyController {

    @Autowired
    private PayRefundService payRefundService;

    private WXPay wxpay;
    private PaymentConfig config;

    private WXPay wxPayForApp;
    private PaymentForAppConfig configForApp;

    public WxRefundNotifyController() throws Exception {
        config = PaymentConfig.getInstance();
        wxpay = new WXPay(config, WXPayConstants.SignType.MD5);
        configForApp = PaymentForAppConfig.getInstance();
        wxPayForApp = new WXPay(configForApp, WXPayConstants.SignType.MD5);
    }

    @RequestMapping("/refund")
    public void NotifyRefund(HttpServletRequest request, HttpServletResponse response) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        //sb为微信返回的xml
        String notifyData = sb.toString();  //支付结果通知的xml格式数据
        System.out.println("退款结果通知的xml格式数据：" + notifyData);
        Map notifyMap = WXPayUtil.xmlToMap(notifyData);
        if ("SUCCESS".equals(notifyMap.get("return_code"))) {
            String req_info = notifyMap.get("req_info").toString();
            String mch_id=notifyMap.get("mch_id").toString();

            String req_info_decrypt = null;
            if(mch_id.equals(configForApp.MCH_ID)){
                req_info_decrypt = PayUtils.getRefundDecrypt(req_info, configForApp.API_KEY);
            }else if (mch_id.equals(config.MCH_ID)){
                req_info_decrypt = PayUtils.getRefundDecrypt(req_info, config.API_KEY);
            }
            System.out.println("微信退款结果通知解密后请求数据xml:" + req_info_decrypt);
            if(!StringUtils.isEmpty(req_info_decrypt)) {
                Map refundnotifyDecryptMap=WXPayUtil.xmlToMap(req_info_decrypt);
                payRefundService.saveRefungResult(refundnotifyDecryptMap);//保存退款结果
            }else{
                System.out.println("微信退款结果通知解密失败");
            }
        }
    }

}
