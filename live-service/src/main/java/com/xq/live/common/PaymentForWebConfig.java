package com.xq.live.common;

/**
 * Created by ss on 2019/1/18.
 * WEB端
 */
public class PaymentForWebConfig {

    /**
     * WEB端微信用
     */
    public static final String APPID = "wxbb3b60fdb2724986";//微信网页appid，应用唯一标识，在微信开放平台提交应用审核通过后获得

    public static final String APPSECRET = "d9334600a5377f42f2e52823594bb4fd";//应用密钥AppSecret，在微信开放平台提交应用审核通过后获得

    //通过code获取access_token url
    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?grant_type=authorization_code";

    //获取用户个人信息（UnionID机制）
    public static final String UNION_ID_URL = "https://api.weixin.qq.com/sns/userinfo?connect_redirect=1";

}
