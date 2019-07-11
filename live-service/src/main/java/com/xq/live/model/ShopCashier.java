package com.xq.live.model;

import java.util.Date;

public class ShopCashier {
    /**
     * is_deleted 0 未删除  1 已删除
     */
    public final static byte SHOP_CASHIER_NO_DELETED = 0;

    public final static byte SHOP_CASHIER_IS_DELETED = 1;

    /**
     * is_admin 0 不是管理员  1 是管理员
     */
    public final static byte SHOP_CASHIER_NO_ADMIN = 0;

    public final static byte SHOP_CASHIER_IS_ADMIN = 1;


    private Long id;

    private Long cashierId;

    private String cashierName;

    private String password;

    private Long shopId;

    private Long parentId;

    private Byte isAdmin;

    private Byte isDeleted;

    private Date createTime;

    private Long creatorId;

    private Long updatorId;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCashierId() {
        return cashierId;
    }

    public void setCashierId(Long cashierId) {
        this.cashierId = cashierId;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName == null ? null : cashierName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Byte getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Byte isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
