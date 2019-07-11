package com.xq.live.vo.out;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Table;
import java.util.Date;

@Table(name = "order_write_off")
public class PaymentTypeOut {

    @ApiModelProperty(value = "支付金额")
    private String realUnitPrice;

    @ApiModelProperty(value = "支付类型")
    private String payType;

    @ApiModelProperty(value = "用户电话")
    private String mobile;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "支付时间")
    private Date createTime;

    public String getRealUnitPrice() {
        return realUnitPrice;
    }

    public void setRealUnitPrice(String realUnitPrice) {
        this.realUnitPrice = realUnitPrice;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
