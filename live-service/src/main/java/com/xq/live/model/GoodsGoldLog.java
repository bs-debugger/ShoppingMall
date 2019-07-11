package com.xq.live.model;

import java.util.Date;

/**
 *用户领取金币
 * Created by ss on 2018/8/6.
 */
public class GoodsGoldLog {
    /*删除状态*/
    public final static Byte IS_DELETE_NO = 0; //0未删除
    public final static Byte IS_DELETE_YES = 1; //1已删除
    /*金币到账状态*/
    public final static Byte STATE_TYPE_NO=0;//0未到金币
    public final static Byte STATE_TYPE_YES=1;//1已到金币
    /*获取类型状态*/
    public final static Byte GOLD_TYPE=4;//类型(4砍价)


    private Long id;

    private Long shopId;

    private Long userId;

    private Long refId;

    private Byte type;

    private Long parentId;

    private Integer goldAmount;

    private Byte stateType;

    private Byte isDelete;

    private Date paidTime;

    private Date createTime;

    private Date updateTime;

    private Integer groupId;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getGoldAmount() {
        return goldAmount;
    }

    public void setGoldAmount(Integer goldAmount) {
        this.goldAmount = goldAmount;
    }

    public Byte getStateType() {
        return stateType;
    }

    public void setStateType(Byte stateType) {
        this.stateType = stateType;
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    public Date getPaidTime() {
        return paidTime;
    }

    public void setPaidTime(Date paidTime) {
        this.paidTime = paidTime;
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

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}