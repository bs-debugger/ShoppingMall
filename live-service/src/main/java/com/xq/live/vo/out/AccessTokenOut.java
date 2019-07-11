package com.xq.live.vo.out;

/**
 * Created by lipeng on 2018/4/8.
 */
public class AccessTokenOut {
    private String accessToken;

    private Integer errcode;

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "AccessTokenOut{" +
                "accessToken='" + accessToken + '\'' +
                ", errcode=" + errcode +
                '}';
    }
}
