package com.xq.live.model;

import java.util.Date;

public class PayRefundConfig {
    /**
     * 类型  1活动   2类目  3商品
     */
    public final static int TYPE_ACT = 1;   //活动

    public final static int TYPE_CATEGORY = 2;   //类目

    public final static int TYPE_GOODS_SKU = 3;   //商品

    private Long id;

    private Long refId;

    private Integer type;

    private Integer isDeleted;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
