package com.xq.live.model;

/**
 * Created by ss on 2018/6/14.
 */
public class PushMsg {
    private String cid;
    private String type;
    private String title;
    private String messageInfo;
    private String badge;
    private String alias;//别名 对应用户id
    private String ios;//穿透消息
    private String urlDown;
    private String logUrl;
    private Long shopId;//商家
    private String APNInfo;//iOS推送使用该字段,支持VoIPPayload

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getLogUrl() {
        return logUrl;
    }

    public void setLogUrl(String logUrl) {
        this.logUrl = logUrl;
    }

    public String getUrlDown() {
        return urlDown;
    }

    public void setUrlDown(String urlDown) {
        this.urlDown = urlDown;
    }

    public String getIos() {
        return ios;
    }

    public void setIos(String ios) {
        this.ios = ios;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessageInfo() {
        return messageInfo;
    }

    public void setMessageInfo(String messageInfo) {
        this.messageInfo = messageInfo;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getAPNInfo() {
        return APNInfo;
    }

    public void setAPNInfo(String APNInfo) {
        this.APNInfo = APNInfo;
    }

    public static PushMsg initial(String alias, String type, String title,
                                  String messageInfo,String badge,String ios) {
        PushMsg msg = new PushMsg();
        //msg.setCid(cid);
        msg.setAlias(alias);
        msg.setType(type);
        msg.setTitle(title);
        msg.setMessageInfo(messageInfo);
        msg.setBadge(badge);
        msg.setIos(ios);
        return msg;
    }

    public static PushMsg initial(String alias, String type, String title,
                                  String messageInfo,String badge,String ios,String apninfo) {
        PushMsg msg = new PushMsg();
        //msg.setCid(cid);
        msg.setAlias(alias);
        msg.setType(type);
        msg.setTitle(title);
        msg.setMessageInfo(messageInfo);
        msg.setBadge(badge);
        msg.setIos(ios);
        msg.setAPNInfo(apninfo);
        return msg;
    }

    public static PushMsg initial(String alias, String title,
                                  String messageInfo,String urlDown) {
        PushMsg msg = new PushMsg();
        msg.setAlias(alias);
        msg.setTitle(title);
        msg.setMessageInfo(messageInfo);
        msg.setUrlDown(urlDown);
        return msg;
    }
    public static PushMsg initial(String alias, String title,
                                    String messageInfo,String urlDown,String logUrl) {
        PushMsg msg = new PushMsg();
        msg.setAlias(alias);
        msg.setTitle(title);
        msg.setMessageInfo(messageInfo);
        msg.setUrlDown(urlDown);
        msg.setLogUrl(logUrl);
        return msg;
    }
}
