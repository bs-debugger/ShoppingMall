package com.xq.live.vo.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class PlatformReconciliationOut {

    @ApiModelProperty(value = "主键id")
    @Excel(name="ID",orderNum = "0")
    private Integer id;

    @ApiModelProperty(value = "订单金额")
    @Excel(name="订单金额",orderNum = "1")
    private String realUnitPrice;

    @Excel(name="订单类型",orderNum = "2",replace = { "普通型_1", "砍价型_4", "秒杀型_5","抽奖型_6","团购型_7" })
    @ApiModelProperty(value = "订单类型")
    private String flagType;

    @Excel(name="下单电话",orderNum = "3")
    @ApiModelProperty(value = "下单电话")
    private String mobile;

    @Excel(name="下单昵称",orderNum = "4")
    @ApiModelProperty(value = "下单昵称")
    private String nickName;

    @Excel(name="支付方式",orderNum = "5",replace = { "享七金币支付_1", "微信支付_2", "支付宝支付_3","余额支付_4" })
    @ApiModelProperty(value = "支付方式")
    private String payType;

    @Excel(name="下单时间",orderNum = "6",format = "yyyy-mm-dd hh:mm:ss")
    @ApiModelProperty(value = "下单时间")
    private Date createTime;

    @Excel(name="使用时间",orderNum = "7",format = "yyyy-mm-dd hh:mm:ss")
    @ApiModelProperty(value = "使用时间")
    private Date payTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRealUnitPrice() {
        return realUnitPrice;
    }

    public void setRealUnitPrice(String realUnitPrice) {
        this.realUnitPrice = realUnitPrice;
    }

    public String getFlagType() {
        return flagType;
    }

    public void setFlagType(String flagType) {
        this.flagType = flagType;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }
}
