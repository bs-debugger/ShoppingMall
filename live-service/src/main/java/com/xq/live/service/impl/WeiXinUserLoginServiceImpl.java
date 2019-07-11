package com.xq.live.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xq.live.common.PaymentConfig;
import com.xq.live.common.PaymentForWebConfig;
import com.xq.live.model.User;
import com.xq.live.model.WeiXinOauth;
import com.xq.live.service.WeiXinUserLoginService;
import com.xq.live.vo.out.UserOut;
import com.xq.live.web.utils.PayUtils;
import org.springframework.stereotype.Service;

/**
 * Created by ss on 2019/1/18.
 */
@Service
public class WeiXinUserLoginServiceImpl implements WeiXinUserLoginService {

    /**
     * 通过用户code获取用户access_token
     * @param code   填写第一步获取的code参数
     * https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
     * @return
     */
    @Override
    public WeiXinOauth getAccessToken(String code) {
        //获取access_token
        String param =  "&appid=" + PaymentForWebConfig.APPID + "&secret=" + PaymentForWebConfig.APPSECRET +"&code="+code+"&connect_redirect=1";
        System.out.println(PaymentForWebConfig.ACCESS_TOKEN_URL + param);
        //创建请求对象
        String httpRet = PayUtils.httpRequest(PaymentForWebConfig.ACCESS_TOKEN_URL, "GET", param);
        JSONObject jsonObject = JSONObject.parseObject(httpRet);
        WeiXinOauth weiXinOauth = new WeiXinOauth();
        if (jsonObject != null) {
            Integer errcode = jsonObject.getInteger("errcode");
            if (errcode != null) {
                //返回异常信息
                weiXinOauth.setErrcode(errcode);
                return weiXinOauth;
            }
            weiXinOauth.setAccessToken(jsonObject.getString("access_token"));
            weiXinOauth.setOpenid(jsonObject.getString("openid"));
        }
       return weiXinOauth;
    }


    /**
     * 获取用户个人信息（UnionID机制）
     * @param accessToken   调用凭证
     * @param openid   普通用户的标识,对当前开发者帐号唯一
     * @param language   国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语，默认为zh-CN
     * https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
     * @return
     */
    @Override
    public UserOut getUserInfo(String accessToken, String openid, String language) {
        //获取access_token
        String param ="&access_token="+accessToken+"&openid="+openid;
        System.out.println(PaymentForWebConfig.UNION_ID_URL + param);
        //创建请求对象
        String httpRet = PayUtils.httpRequest(PaymentForWebConfig.UNION_ID_URL, "GET", param);
        JSONObject jsonObject = JSONObject.parseObject(httpRet);
        UserOut user = new UserOut();
        if (jsonObject != null) {
            Integer errcode = jsonObject.getInteger("errcode");
            if (errcode != null) {
                //返回异常信息
                user.setErrcode(errcode);
                return user;
            }
            user.setOpenId(jsonObject.getString("openid"));
            user.setNickName(jsonObject.getString("nickname"));
            user.setIconUrl(jsonObject.getString("headimgurl"));
            user.setSex(jsonObject.getInteger("sex"));
            user.setUnionId(jsonObject.getString("unionid"));
        }
        return user;
    }
}
