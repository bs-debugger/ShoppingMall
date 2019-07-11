package com.xq.live.common;

import com.github.wxpay.sdk.WXPayConfig;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 微信支付常量配置
 *
 * @author zhangpeng32
 * @date 2018-03-09 11:06
 **/
public class PaymentForAppConfig implements WXPayConfig {

    //商家端APP的appid
    public static final String APPID = "wx1724a3abf5520458";
    //微信支付的商户id
    public static final String MCH_ID = "1505760121";
    //微信支付的商户密钥
    public static final String API_KEY = "4795238fa643dd76b7141be750d00331";

    public static final String APP_SECRET = "da45246f2c299d42a92c3ee7482862dd";

    /**
     * 服务商模式下的关键信息
     */
    public static final String FW_APP_ID = "";//服务商的appId

    public static final String FW_MCH_ID = "";//服务商的mchId(服务商的商户id)

    //支付成功后的服务器回调url
    public static final String WX_NOTIFY_URL = "https://www.xiang7.net/wxpayNotify/wxNotify";

    //商家订单支付成功后的服务器回调url
    public static final String WX_NOTIFY_SHOP_URL = "https://www.xiang7.net/wxpayNotify/wxNotifyForShop";

    //活动订单支付成功后的服务器回调url
    public static final String WX_NOTIFY_ACT_URL = "https://www.xiang7.net/wxpayNotify/wxNotifyForAct";

    //商家端服务费支付成功后的服务器回调url
    public static final String WX_NOTIFY_SHOP_APP_URL = "https://www.xiang7.net/app/wxpayNotify/wxNotifyForShopApp";

    //商家端服务费支付成功后的服务器回调url
    public static final String WX_NOTIFY_SHOP_APP_URL_NEW = "https://www.xiang7.net/app/wxpayNotify/wxNotifyForShopAppNew";

    //商家端服务费支付成功后的服务器回调url
    public static final String WX_NOTIFY_SHOP_APP_URL_V1 = "https://www.xiang7.net/app/wxpayNotify/wxNotifyForShopAppv1";

    public static final String GRANT_TYPE = "authorization_code";

    //交易类型，小程序支付的固定值为JSAPI
    public static final String TRADE_TYPE = "JSAPI";

    //交易类型，商家端App支付的固定值为APP
    public static final String TRADE_TYPE_APP = "APP";

    //微信统一下单接口地址
    public static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    public static final String GET_OPEN_ID_URL = "https://api.weixin.qq.com/sns/jscode2session";

    //微信获取access_token地址
    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";

    //微信发送模板消息地址
    public static final String SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send";

    //微信获取帐号下已存在的模板列表
    public static final String TEMPLATE_LIST_URL = "https://api.weixin.qq.com/cgi-bin/wxopen/template/list";
    private static PaymentForAppConfig INSTANCE;
    private byte[] certData;

    private PaymentForAppConfig() throws Exception {
        //        String certPath = "D://CERT/common/apiclient_cert.p12";
        //        File file = new File(certPath);
        //        InputStream certStream = new FileInputStream(file);
        //        this.certData = new byte[(int) file.length()];
        //        certStream.read(this.certData);
        //        certStream.close();
    }

    public static PaymentForAppConfig getInstance() throws Exception {
        if (INSTANCE == null) {
            synchronized (PaymentForAppConfig.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PaymentForAppConfig();
                }
            }
        }
        return INSTANCE;
    }

    public static String getFW_APP_ID() {
        return FW_APP_ID;
    }

    public static String getFW_MCH_ID() {
        return FW_MCH_ID;
    }

    public String getAppID() {
        return APPID;
    }

    public String getMchID() {
        return MCH_ID;
    }

    public String getKey() {
        return API_KEY;
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis;
        certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }


    public int getHttpConnectTimeoutMs() {
        return 2000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }


    public String getPrimaryDomain() {
        return "api.mch.weixin.qq.com";
    }

    public String getAlternateDomain() {
        return "api2.mch.weixin.qq.com";
    }
}
