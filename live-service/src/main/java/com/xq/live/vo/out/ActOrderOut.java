package com.xq.live.vo.out;

import com.xq.live.model.ActGoodsSku;
import com.xq.live.model.OrderInfo;

import java.io.Serializable;
import java.util.Date;

public class ActOrderOut implements Serializable {

    private static final long serialVersionUID = -3849280944872343992L;

    private Long id;

    private Long orderId;

    private Long actGoodsSkuId;

    private Integer state;

    private Long groupId;

    private Integer isOpend;

    private Long userId;

    private Long parentId;

    private Integer peopleNum;

    private Integer isDeleted;

    private Date createTime;

    private Date updateTime;

    private OrderInfo orderInfo;

    private ActGoodsSku actGoodsSku;//活动商品的关联详情

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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Integer getIsOpend() {
        return isOpend;
    }

    public void setIsOpend(Integer isOpend) {
        this.isOpend = isOpend;
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

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public ActGoodsSku getActGoodsSku() {
        return actGoodsSku;
    }

    public void setActGoodsSku(ActGoodsSku actGoodsSku) {
        this.actGoodsSku = actGoodsSku;
    }
}
