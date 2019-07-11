package com.xq.live.service;

import com.xq.live.model.User;
import com.xq.live.model.WeiXinOauth;
import com.xq.live.vo.out.UserOut;

/**
 * Created by ss on 2019/1/18.
 * 获取微信用户信息 用于微信WEB端登陆注册
 */
public interface WeiXinUserLoginService {

    /**
     * 通过用户code获取用户access_token
     * @param code   填写第一步获取的code参数
     * https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
     * @return
     */
    public WeiXinOauth getAccessToken(String code);


    /**
     * 获取用户个人信息（UnionID机制）
     * @param accessToken   调用凭证
     * @param openid   普通用户的标识,对当前开发者帐号唯一
     * @param language   国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语，默认为zh-CN
     * https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
     * @return
     */
    public UserOut getUserInfo(String accessToken,String openid,String language);



}
