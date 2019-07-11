package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xq.live.model.DeliveryMethod;

import java.util.Date;
import java.util.List;

/**
 * 运费模板出参
 * Created by lipeng on 2018/12/26.
 */
public class DeliveryTemplateOut {

    /**
     * 是否包邮 0不包邮 1包邮
     */
    public final static int DELIVERY_TEMPLATE_NO_FREE = 0;//不包邮

    public final static int DELIVERY_TEMPLATE_IS_FREE = 1;//包邮

    private Long id;

    private String name;

    private String shippingAddress;

    private Integer isFree;

    private Integer calculateType;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private List<DeliveryMethod> deliveryMethods;//运费模板详细内容

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress == null ? null : shippingAddress.trim();
    }

    public Integer getIsFree() {
        return isFree;
    }

    public void setIsFree(Integer isFree) {
        this.isFree = isFree;
    }

    public Integer getCalculateType() {
        return calculateType;
    }

    public void setCalculateType(Integer calculateType) {
        this.calculateType = calculateType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<DeliveryMethod> getDeliveryMethods() {
        return deliveryMethods;
    }

    public void setDeliveryMethods(List<DeliveryMethod> deliveryMethods) {
        this.deliveryMethods = deliveryMethods;
    }
}
