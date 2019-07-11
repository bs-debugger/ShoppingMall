package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 商家运费模板出参
 * Created by lipeng on 2018/12/26.
 */
public class DeliveryTemplateShopOut {
    private Long id;

    private Long deliveryTemplateId;

    private Long shopId;

    private Integer isDeleted;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private DeliveryTemplateOut deliveryTemplateOut;//运费模板

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeliveryTemplateId() {
        return deliveryTemplateId;
    }

    public void setDeliveryTemplateId(Long deliveryTemplateId) {
        this.deliveryTemplateId = deliveryTemplateId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
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

    public DeliveryTemplateOut getDeliveryTemplateOut() {
        return deliveryTemplateOut;
    }

    public void setDeliveryTemplateOut(DeliveryTemplateOut deliveryTemplateOut) {
        this.deliveryTemplateOut = deliveryTemplateOut;
    }
}
