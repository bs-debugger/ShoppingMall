package com.xq.live.model;

import java.util.Date;

/**
 * 订单活动关联
 */
public class ActOrder {
    /**
     * 参团状态 1未参团 2参团成功 3参团申请中 4参团结束(用于拼团活动)
     */
    public final static int ACT_ORDER_GROUP_STATE_NOT_TUXEDO = 1;
    public final static int ACT_ORDER_GROUP_STATE_TUXEDO_SUCCESS = 2;
    public final static int ACT_ORDER_GROUP_STATE_TUXEDO_JOINING = 3;
    public final static int ACT_ORDER_GROUP_STATE_TUXEDO_END = 4;

    /*
    * 是否被删除
    * */
    public final static int ACT_STATE_IS_DELETED = 1;//删除

    public final static int ACT_STATE_IS_NOT_DELETED = 0;//未删除



    private Long id;

    private Long orderId;

    private Long actGoodsSkuId;

    private Integer state;

    private Long userId;

    private Long parentId;

    private Integer peopleNum;

    private Integer isDeleted;

    private Date createTime;

    private Date updateTime;

    @Override
    public String toString() {
        return "ActOrder{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", actGoodsSkuId=" + actGoodsSkuId +
                ", state=" + state +
                ", userId=" + userId +
                ", parentId=" + parentId +
                ", peopleNum=" + peopleNum +
                ", isDeleted=" + isDeleted +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getActGoodsSkuId() {
        return actGoodsSkuId;
    }

    public void setActGoodsSkuId(Long actGoodsSkuId) {
        this.actGoodsSkuId = actGoodsSkuId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(Integer peopleNum) {
        this.peopleNum = peopleNum;
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