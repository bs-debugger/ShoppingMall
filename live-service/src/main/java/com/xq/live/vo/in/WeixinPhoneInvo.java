package com.xq.live.vo.in;

import javax.validation.constraints.NotNull;

/**
 * Created by lipeng on 2018/3/29.
 */
public class WeixinPhoneInvo {

    @NotNull(message = "sessionKey必填")
    private String sessionKey;
    @NotNull(message = "ivData必填")
    private String ivData;
    @NotNull(message = "encrypData必填")
    private String encrypData;

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getIvData() {
        return ivData;
    }

    public void setIvData(String ivData) {
        this.ivData = ivData;
    }

    public String getEncrypData() {
        return encrypData;
    }

    public void setEncrypData(String encrypData) {
        this.encrypData = encrypData;
    }
}
