package com.xq.live.vo.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Table(name = "shop")
public class MerchantDetailsOut {

    @ApiModelProperty(value = "主键ID")
    @Excel(name="ID",orderNum = "0")
    private Long id;

    @ApiModelProperty(value = "商家名称")
    @Excel(name="商家名称",orderNum = "1")
    private String shopName;

    @ApiModelProperty(value = "所在城市")
    @Excel(name="所在城市",orderNum = "2")
    private String city;

    @ApiModelProperty(value = "所在专区")
    @Transient
    private String prefectureName;

    @ApiModelProperty(value = "订单数量")
    @Excel(name="订单数量",orderNum = "3")
    @Transient
    private String orderCount;

    @ApiModelProperty(value = "订单金额")
    @Excel(name="订单金额",orderNum = "4")
    @Transient
    private String orderSum;

    @ApiModelProperty(value = "非订单数量")
    @Excel(name="非订单数量",orderNum = "5")
    @Transient
    private String orderQuantityCount;

    @ApiModelProperty(value = "非订单的金额")
    @Excel(name="非订单的金额",orderNum = "6")
    @Transient
    private String orderQuantitySum;

    @ApiModelProperty(value = "商家电话")
    @Excel(name="商家电话",orderNum = "7")
    private String mobile;

    @ApiModelProperty(value = "订单号")
    private String orderCode;

    @ApiModelProperty(value = "商品名称")
    private String skuName;

    @ApiModelProperty(value = "单笔服务费")
    private String serviceAmount;

    @ApiModelProperty(value = "订单类型:1普通型  4砍价型  5秒杀型 6抽奖型 7团购型")
    private String flag_type;

    @ApiModelProperty(value = "实际付款金额")
    private String realUnitPrice;

    @ApiModelProperty(value = "商家实际到账金额 ")
    private String realShopUnitPrice;

    @ApiModelProperty(value = "创建时间")
    @Excel(name="创建时间",orderNum = "8",format = "yyyy-mm-dd hh:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "变更方式 1 支出 2 收入")
    private String operateType;

    @ApiModelProperty(value = "变更金额")
    private String operateAmount;

    @ApiModelProperty(value = "变更内容")
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPrefectureName() {
        return prefectureName;
    }

    public void setPrefectureName(String prefectureName) {
        this.prefectureName = prefectureName;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getOperateAmount() {
        return operateAmount;
    }

    public void setOperateAmount(String operateAmount) {
        this.operateAmount = operateAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(String orderCount) {
        this.orderCount = orderCount;
    }

    public String getOrderSum() {
        return orderSum;
    }

    public void setOrderSum(String orderSum) {
        this.orderSum = orderSum;
    }

    public String getOrderQuantityCount() {
        return orderQuantityCount;
    }

    public void setOrderQuantityCount(String orderQuantityCount) {
        this.orderQuantityCount = orderQuantityCount;
    }

    public String getOrderQuantitySum() {
        return orderQuantitySum;
    }

    public void setOrderQuantitySum(String orderQuantitySum) {
        this.orderQuantitySum = orderQuantitySum;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getServiceAmount() {
        return serviceAmount;
    }

    public void setServiceAmount(String serviceAmount) {
        this.serviceAmount = serviceAmount;
    }

    public String getFlag_type() {
        return flag_type;
    }

    public void setFlag_type(String flag_type) {
        this.flag_type = flag_type;
    }

    public String getRealUnitPrice() {
        return realUnitPrice;
    }

    public void setRealUnitPrice(String realUnitPrice) {
        this.realUnitPrice = realUnitPrice;
    }

    public String getRealShopUnitPrice() {
        return realShopUnitPrice;
    }

    public void setRealShopUnitPrice(String realShopUnitPrice) {
        this.realShopUnitPrice = realShopUnitPrice;
    }
}
