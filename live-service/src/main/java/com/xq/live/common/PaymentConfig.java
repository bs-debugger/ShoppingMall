package com.xq.live.common;

import com.github.wxpay.sdk.WXPayConfig;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 微信支付常量配置
 *
 * @author zhangpeng32
 * @date 2018-03-09 11:06
 **/
public class PaymentConfig implements WXPayConfig {

    //小程序appid
    public static final String APPID = "wxf91e2a026658e78e";
    //公众号-享七美食公众号appid
    public static final String APPID_GZ = "wx08238cc1074764be";
    //公众号-享七美食公众号secret密钥
    public static final String WEB_SECRET = "6b1edba46f8bdc5c4ee09cd27c2a7e3c";

    //微信支付的商户id
    public static final String MCH_ID = "1499658152";
    //微信支付的商户密钥
    public static final String API_KEY = "4921641fc679dd76a7141ba750d88204";
    //小程序app secret
    public static final String APP_SECRET = "4921641fc679dd76a7141ba750d88204";

    /**
     * 服务商模式下的关键信息
     */
    public static final String FW_APP_ID = "";//服务商的appId

    public static final String FW_MCH_ID = "";//服务商的mchId(服务商的商户id)

    //支付成功后的服务器回调url
    public static final String WX_NOTIFY_URL = "https://xiang7.net/wxpayNotify/wxNotify";

    //商家订单支付成功后的服务器回调url
    public static final String WX_NOTIFY_SHOP_URL = "https://xiang7.net/wxpayNotify/wxNotifyForShop";

    //活动订单支付成功后的服务器回调url
    public static final String WX_NOTIFY_ACT_URL = "https://xiang7.net/wxpayNotify/wxNotifyForAct";

    //砍价菜订单支付成功后的服务器回调url
    public static final String WX_NOTIFY_KJ_URL = "https://xiang7.net/wxpayNotify/wxNotifyForKjq";

    //抢购菜订单支付成功后的服务器回调url
    public static final String WX_NOTIFY_QG_URL = "https://xiang7.net/wxpayNotify/wxNotifyForQgq";

    //兑换菜订单支付成功后的服务器回调url
    public static final String WX_NOTIFY_DH_URL = "https://xiang7.net/wxpayNotify/wxNotifyForDhq";

    //商城系统订单支付成功后的服务器回调url
    public static final String WX_NOTIFY_SM_URL = "https://xiang7.net/wxpayNotify/wxNotifyForShoppingMall";

    //商城系统票券订单支付成功后的服务器回调url(老)
    public static final String WX_NOTIFY_SMC_URL = "https://xiang7.net/wxpayNotify/wxNotifyForShoppingMallCoupon";

    //商城系统票券订单支付成功后的服务器回调url(新)
    public static final String WX_NOTIFY_SMC_NEW_URL = "https://xiang7.net/wxpayNotify/wxNotifyForShoppingMallCouponNew";

    //商城系统票券运费支付成功后的服务器回调url
    public static final String WX_NOTIFY_CSA_URL = "https://xiang7.net/wxpayNotify/wxNotifyForCouponSendAmount";

    //商城系统票券运费支付成功后的服务器回调url新
    public static final String WX_NOTIFY_CSA_URLV1 = "https://xiang7.net/wxpayNotify/wxNotifyForCouponSendAmountV1";

    //商城系统门店自提订单成功后的服务器回调url
    public static final String WX_NOTIFY_MDZT_URL = "https://xiang7.net/wxpayNotify/wxNotifyForShoppingMallMDZT";

    //商城系统实物订单成功后的服务器回调url
    public static final String WX_NOTIFY_RO_URL = "https://xiang7.net/wxpayNotify/wxNotifyForShoppingMallRo";

    //商城系统商家订单支付成功后的服务器回调url
    public static final String WX_NOTIFY_SHOP_ORDER_URL = "https://xiang7.net/wxpayNotify/wxNotifyForShopOrder";

    //商家端服务费支付成功后的服务器回调url
    public static final String WX_NOTIFY_SHOP_APP_URL = "https://xiang7.net/app/wxpayNotify/wxNotifyForShopApp";

    //微信支付退款后的回调地址
    public static final String NOTIFY_PAY_REFUND_URL = "https://xiang7.net/wxRefundNotify/refund";

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
    private static PaymentConfig INSTANCE;
    private byte[] certData;

    private PaymentConfig() throws Exception {
        String certPath = this.getClass().getClassLoader().getResource("").getPath() + "cert" + File.separator + "apiclient_cert1499658152.p12";
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    public static PaymentConfig getInstance() throws Exception {
        if (INSTANCE == null) {
            synchronized (PaymentConfig.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PaymentConfig();
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
