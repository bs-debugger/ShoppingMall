package com.xq.live.vo.in;

import javax.validation.constraints.NotNull;

/**
 * Created by lipeng on 2018/4/6.
 */
public class WeixinSendInvo {
    @NotNull(message = "accessToken必填")
    private String accessToken;//获取到的access_token
    @NotNull(message = "touser必填")
    private String touser;//接收者（用户）的 openid
    @NotNull(message = "templateId必填")
    private String templateId;//所需下发的模板消息的id
    private String page;//点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转
    @NotNull(message = "formId必填")
    private String formId;//表单提交场景下，为 submit 事件带上的 formId；支付场景下，为本次支付的 prepay_id
    @NotNull(message = "data必填")
    private String data;//模板内容，不填则下发空模板
    private String color;//模板内容字体的颜色，不填默认黑色
    private String emphasisKeyword;//模板需要放大的关键词，不填则默认无放大
    private String offset;//offset和count用于分页，表示从offset开始，拉取count条记录，offset从0开始，count最大为20。最后一页的list长度可能小于请求的count
    private String count;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEmphasisKeyword() {
        return emphasisKeyword;
    }

    public void setEmphasisKeyword(String emphasisKeyword) {
        this.emphasisKeyword = emphasisKeyword;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
