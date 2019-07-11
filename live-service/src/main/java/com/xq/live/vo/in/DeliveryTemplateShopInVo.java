package com.xq.live.vo.in;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 商家运费模板入参
 * Created by lipeng on 2018/12/26.
 */
public class DeliveryTemplateShopInVo extends BaseInVo{
    private Long id;

    @NotNull(message = "deliveryTemplateId不能为空")
    private Long deliveryTemplateId;
    @NotNull(message = "shopId不能为空")
    private Long shopId;

    private Integer isDeleted;

    private Date createTime;

    private Date updateTime;

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
}
