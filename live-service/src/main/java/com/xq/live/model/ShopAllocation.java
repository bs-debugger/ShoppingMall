package com.xq.live.model;

import java.util.Date;

public class ShopAllocation {

    /**
     * paymentMethod  1 商家自收  2 平台代收
     */
    public final static int SHOP_ALLOCATION_ZS = 1;

    public final static int SHOP_ALLOCATION_DS = 2;

    /**
     * 是否删除  0 未删除  1 删除
     */
    public final static int SHOP_ALLOCATION_NO_DELETED = 0;

    public final static int SHOP_ALLOCATION_IS_DELETED = 1;


    private Long id;

    private Long shopId;

    private Long operatorId;

    private Integer isDelete;

    private Date createTime;

    private Date updateTime;

    private Integer paymentMethod;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
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

    public Integer getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
