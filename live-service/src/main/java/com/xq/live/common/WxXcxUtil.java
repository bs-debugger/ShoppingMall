package com.xq.live.common;

import com.alibaba.fastjson.JSONObject;
import com.xq.live.web.utils.PayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 微信小程序工具类
 * @author feitao <yyimba@qq.com> 2019/5/6 14:50
 */
@Service
public class WxXcxUtil {

    @Autowired
    private RedisCache redisCache;

    /**
     * 小程序访问token的redis缓存key
     */
    public static final String ACCESS_TOKEN_KEY = "xcx-xq-access-token";

    /**
     * 获取访问token的接口地址
     */
    public static final String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";

    /**
     * 获取访问token的请求参数
     */
    public static final String GET_ACCESS_TOKEN_PARAMS = "grant_type=client_credential&appid=%s&secret=%s";

    /**
     * 获取小程序码图片的接口地址
     */
    public static final String GET_WXACODE_UNLIMIT_URL = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=%s";

    /**
     * 获取小程序访问token
     * @return
     */
    public String getAccessToken() {
        // 先从缓存中获取访问Token
        String redisAccessToken = redisCache.getString(ACCESS_TOKEN_KEY);
        if (redisAccessToken != null) {
            return redisAccessToken;
        }
        //创建请求对象
        String params = String.format(GET_ACCESS_TOKEN_PARAMS, PaymentConfig.APPID, PaymentConfig.APP_SECRET);
        String httpRet = PayUtils.httpRequest(GET_ACCESS_TOKEN_URL, "GET", params);
        JSONObject jsonObject = JSONObject.parseObject(httpRet);
        if (jsonObject == null || jsonObject.getIntValue("errcode ") != 0) {
            return null;
        }
        // 小程序访问Token
        String accessToken = jsonObject.getString("access_token");
        // 把token存入redis缓存中
        redisCache.set(ACCESS_TOKEN_KEY, accessToken, jsonObject.getIntValue("expires_in") - 1, TimeUnit.SECONDS);
        return accessToken;
    }

}
