package com.xq.live.vo.in;

import io.swagger.annotations.ApiModelProperty;

public class PayRefundApplicationInVO extends BaseInVo{

    /**
     * status 审核状态 0 待审批 1 审批通过 2审批不通过
     */
    public final  static int PAY_REFUND_STATUS_DSP=0;//待审批

    public final  static int PAY_REFUND_STATUS_SPTG=1;//审批通过

    public final  static int PAY_REFUND_STATUS_SPBTG=2;//审批不通过

    @ApiModelProperty(value = "开始时间")
    private String beginTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;//结束时间

    @ApiModelProperty(value = "查询条件")
    private String keywords;

    @ApiModelProperty(value = "是否导出 false查询 true为导出")
    private Boolean excle;

    @ApiModelProperty(value = "订单号")
    private String outTradeNo;//订单号

    @ApiModelProperty(value = "是否发送短信: 1 是 0 否")
    private Integer isSend;

    @ApiModelProperty(value = "退款单号")
    private String outRefundNo;

    @ApiModelProperty(value = "审核状态 0 待审批 1 审批通过 2审批不通过")
    private Integer status;

    @ApiModelProperty(value = "id数组")
    private String ids;

    @ApiModelProperty(value = "商品名称")
    private String shopName;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "短信内容")
    private String content;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    private String type;

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Boolean getExcle() {
        return excle;
    }

    public void setExcle(Boolean excle) {
        this.excle = excle;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getIsSend() {
        return isSend;
    }

    public void setIsSend(Integer isSend) {
        this.isSend = isSend;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
