package com.xq.live.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class SmsSend {
    public final static int SMS_SEND_STATUS_SUCCESS = 1;

    public final static int SMS_SEND_STATUS_FAIL = 2;

    /*短信类型 1 注册验证码 2 登录验证码 3绑定和修改银行卡 4忘记安全密码 10 收款通知*/
    public final static int SMS_TYPE_VERTIFY = 1;//注册验证码

    public final static int SMS_TYPE_BINDING = 3;//3绑定和修改银行卡

    public final static int SMS_TYPE_FORGET = 4;//4 忘记安全密码

    public final static int SMS_TYPE_GOODS_REJECT = 5;//5 商品驳回

    public final static int SMS_TYPE_FORGET_PASSWORD = 6;//6 忘记密码


    private Long id;

    private Integer smsType;

    private String smsContent;

    private String shopMobile;

    private Long shopId;

    private String shopName;

    private Integer sendStatus;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent == null ? null : smsContent.trim();
    }

    public String getShopMobile() {
        return shopMobile;
    }

    public void setShopMobile(String shopMobile) {
        this.shopMobile = shopMobile;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    public Integer getSmsType() {
        return smsType;
    }

    public void setSmsType(Integer smsType) {
        this.smsType = smsType;
    }

    public Integer getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

}
