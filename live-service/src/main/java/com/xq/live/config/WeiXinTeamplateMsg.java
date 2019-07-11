package com.xq.live.config;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;

/**
 * Created by ss on 2018/10/9.
 * 微信推送模板
 */
public class WeiXinTeamplateMsg {
    /*购买成功的模板id*/
    public static final String templateId_TYPE = "Wzxu3aDcDcdIuYP0N63hot2QKxopSy2FYln1X50pTa4";
    /*注册成功*/
    public static final String templateId_TYPE_INSERT = "niQoBmhgnpm3NtZHOYcQ33S7SxpMLUUuWsB9IChVCg8";
    /*核销成功*/
    public static final String templateId_TYPE_HX = "nN6M3S7T9y4PAqyHc8Ajml187Q2odAuFrnU0y-2HVXs";
    /*砍价人数已满成功*/
    public static final String templateId_TYPE_ACT_BARGAIN = "SkYn8eqfh72mJNwabuBGLsdltBMzxfR0wrUlW2Az_Iw";
    /*支付成功*/
    public static final String templateId_TYPE_PAY = "7y5zQo2hdw0fZym6JxeqjLJlIh91N8QkcYtP24mm3d8";
    /*开团成功*/
    public static final String templateId_TYPE_INTEGRATE_PURCHASE = "jPxeFFIKSGQVfe7mvFtUDvtKf7rRnrd0UVlAGBTu6Mg";



    public final static String KEYWORD = "keyword";//单个内容

    private String touser; //接收者的openId
    private String template_id; //模板id
    private String form_id;
    private TemplateItem data; //数据
    private String page; //跳转链接
    public String getPage() {
        return page;
    }
    public void setPage(String page) {
        this.page = page;
    }
    public static WeiXinTeamplateMsg New() {
        return new WeiXinTeamplateMsg();
    }
    private WeiXinTeamplateMsg() {
        this.data = new TemplateItem();
    }
    public String getTouser() {
        return touser;
    }
    public WeiXinTeamplateMsg setTouser(String touser) {
        this.touser = touser;
        return this;
    }
    public String getTemplate_id() {
        return template_id;
    }
    public WeiXinTeamplateMsg setTemplate_id(String template_id) {
        this.template_id = template_id;
        return this;
    }
    public String getForm_id() {
        return form_id;
    }

    public WeiXinTeamplateMsg setForm_id(String url) {
        this.form_id = url;
        return this;
    }
    public TemplateItem getData() {
        return data;
    }
    public WeiXinTeamplateMsg add(String key, String value, String color){
        data.put(key, new Item(value, color));
        return this;
    }
    public WeiXinTeamplateMsg add(String key, String value){
        data.put(key, new Item(value));
        return this;
    }
    public String build() {
        return JSON.toJSONString(this);
        /*return JsonUtils.toJson(this);*/
    }
    public class TemplateItem extends HashMap<String, Item> {
        public TemplateItem() {}
        public TemplateItem(String key, Item item) {
            this.put(key, item);
        }
    }
    public class Item {
        private Object value;
        private String color;
        public Object getValue() {
            return value;
        }
        public void setValue(Object value) {
            this.value = value;
        }
        public String getColor() {
            return color;
        }
        public void setColor(String color) {
            this.color = color;
        }
        public Item(Object value) {
            this(value, "#999");
        }
        public Item(Object value, String color) {
            this.value = value;
            this.color = color;
        }
    }
}
