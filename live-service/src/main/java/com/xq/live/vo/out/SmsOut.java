package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by lipeng on 2018/3/29.
 */
public class SmsOut {
    private String verifyId;//注册验证码

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date veridyTime; //注册码发送的时间


    public String getVerifyId() {
        return verifyId;
    }

    public void setVerifyId(String verifyId) {
        this.verifyId = verifyId;
    }

    public Date getVeridyTime() {
        return veridyTime;
    }

    public void setVeridyTime(Date veridyTime) {
        this.veridyTime = veridyTime;
    }


}


