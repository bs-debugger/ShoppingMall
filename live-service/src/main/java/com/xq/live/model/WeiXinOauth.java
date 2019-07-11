package com.xq.live.model;

/**
 * Created by ss on 2019/1/18.
 * 微信用户信息
 */
public class WeiXinOauth {
    private String accessToken;//接口调用凭证access_token
    private Integer expiresIn;//接口调用凭证超时时间，单位（秒）
    private String refreshToken;//用户刷新access_token
    private String openid;//授权用户唯一标识
    private String scope;//用户授权的作用域，使用逗号（,）分隔
    private Integer errcode;//错误码

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }
}
