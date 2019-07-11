package com.xq.live.web.utils;


import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.ITemplate;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;

import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.xq.live.model.PushMsg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by ss on 2018/6/14.
 */
public class GtPush {
    //定义常量, appId、appKey、masterSecret 采用本文档 "第二步 获取访问凭证 "中获得的应用配置
    private static final String appId = "nAghSHHJBn9BJvWVcdDYj6";
    private static final String appKey = "JNmaMQ6hk18VNJLB1GVRU6";
    private static final String masterSecret = "m0LgDlg3RE6S8eX6yGKay4";
    private static final String url = "http://sdk.open.api.igexin.com/apiex.htm";


    public static void main(String[] args) throws IOException {
        //String clientId="d8e5fbf28277220e8b5c8ae5a6e02f98";
        String alias="78536";
        String msgTitle="《简介》";
        String msgContent="平台代收金额到账20";//兽人永不为奴！有没有收到！
        String badge="1";

        String type="ios";
        //String type="ios";
        String ios="1,进入营业额页面";//穿透消息
        String msgAPNInfo="1,进入营业额页面";   //iOS推送使用该字段,支持VoIPPayload
        PushMsg msg = PushMsg.initial(alias, type, msgTitle, msgContent, badge,ios + "",msgAPNInfo);
       /* for (int i=0;i<10;i++){
            GtPush.sendMessage(msg);
        }*/
        GtPush.sendMessage(msg);
    }
    //"穿透", "进入商家账单页面"
    public static void sendMessage(PushMsg msg) {
        String code="";
        if (msg.getType().equals("android")) {
            //外面推送
            ITemplate notificationTemplate = notificationTemplateDemo(
                    msg.getTitle(), msg.getMessageInfo(), msg.getBadge());
            pushSingleMessage(msg.getAlias(), notificationTemplate, false);
            //穿透消息
            ITemplate iosTransmissionTemplate = iosTransmissionTemplate(
                    msg.getTitle(), msg.getIos(), msg.getBadge());// 带APNPayload
            pushSingleMessage(msg.getAlias(), iosTransmissionTemplate, false);

        } else if (msg.getType().equals("ios")) {
            // ios透传通知 安卓透传
            ITemplate iosTransmissionTemplate = iosTransmissionTemplate(
                    msg.getTitle(), msg.getMessageInfo(), msg.getBadge());// 带APNPayload
            pushSingleMessage(msg.getAlias(), iosTransmissionTemplate, true);
        }
        //return
    }

    // 单个推送
    private static void pushSingleMessage(String alias, ITemplate template,
                                          boolean offline) {
        IGtPush push = new IGtPush(appKey, masterSecret, true);

        SingleMessage message = new SingleMessage();

        message.setOffline(offline);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 3600 * 1000);
        message.setData(template);
        // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
        message.setPushNetWorkType(0);
        Target target = new Target();
        target.setAppId(appId);
        target.setAlias(alias);
        //target.setClientId(Cid);
        IPushResult ret = null;

        try {
            ret = push.pushMessageToSingle(message, target);
        } catch (RequestException e) {
            e.printStackTrace();
            ret = push.pushMessageToSingle(message, target, e.getRequestId());
        }
    }

    //设置推送栏消息
    private static NotificationTemplate notificationTemplateDemo(String title,
                                                                 String messageInfo, String badge) {
        NotificationTemplate template = new NotificationTemplate();
        // 设置APPID与APPKEY
        template.setAppId(appId);
        template.setAppkey(appKey);
        // 设置通知栏标题与内容
       /* template.setTitle(title);
        template.setText(messageInfo);*/
        template.setTitle(title);
        template.setText(messageInfo);
        // 配置通知栏图标
        /*template.setLogo("");*/
        // 配置通知栏网络图标
        /*template.setLogoUrl("");*/
        // 设置通知是否响铃，震动，或者可清除
        template.setIsRing(true);
        template.setIsVibrate(true);
        template.setIsClearable(true);
        //template.setAPNInfo(getApnPayload(title, messageInfo, badge));
        template.setTransmissionType(1);
        //template.setTransmissionContent("notification." + messageInfo);
        return template;
    }

    // ios透传，设置APNPayload参数
    private static TransmissionTemplate iosTransmissionTemplate(String title,
                                                                String messageInfo, String badge) {
        TransmissionTemplate template = transmissionTemplate(messageInfo);
        APNPayload payload = getApnPayload(title, messageInfo, badge);
        // 字典模式使用下者
        // payload.setAlertMsg(getDictionaryAlertMsg());
        template.setAPNInfo(payload);
        return template;
    }

    private static APNPayload getApnPayload(String title, String messageInfo,
                                            String badge) {
        APNPayload payload = new APNPayload();
        // +1在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，显示指定数字
        payload.setAutoBadge(badge);
        payload.addCustomMsg("payload",messageInfo);
//        payload.setContentAvailable(1);
        payload.setSound("123.wav");
        payload.setCategory("$由客户端定义");
        // //简单模式APNPayload.SimpleMsg
        payload.setAlertMsg(new APNPayload.SimpleAlertMsg(messageInfo));
        APNPayload.DictionaryAlertMsg dictionaryAlertMsg = new APNPayload.DictionaryAlertMsg();
        dictionaryAlertMsg.setTitle(title);
        dictionaryAlertMsg.setBody(messageInfo);
        payload.setAlertMsg(dictionaryAlertMsg);
        System.out.println(payload);
        return payload;
    }

    // 透传消息
    private static TransmissionTemplate transmissionTemplate(String messageInfo) {
        System.out.println(messageInfo);
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTransmissionContent(messageInfo);
        template.setTransmissionType(2);
        return template;
    }



    //群发消息
    public static String pushInfoByShop(PushMsg msg){
        IGtPush push = new IGtPush(url, appKey, masterSecret);

        // 定义"点击链接打开通知模板"，并设置标题、内容、链接
        LinkTemplate template = new LinkTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTitle(msg.getTitle());
        template.setText(msg.getMessageInfo());
        template.setUrl(msg.getUrlDown());
        //template.setLogoUrl(msg.getLogUrl());

        List<String> appIds = new ArrayList<String>();
        appIds.add(appId);

        // 定义"AppMessage"类型消息对象，设置消息内容模板、发送的目标App列表、是否支持离线发送、以及离线消息有效期(单位毫秒)
        AppMessage message = new AppMessage();
        message.setData(template);
        message.setAppIdList(appIds);
        message.setOffline(true);
        message.setOfflineExpireTime(1000 * 600);

        IPushResult ret = push.pushMessageToApp(message);
        //String push=ret.getResponse().toString();
        System.out.println(ret.getResponse().toString());
        return ret.getResponse().toString();
    }
}
