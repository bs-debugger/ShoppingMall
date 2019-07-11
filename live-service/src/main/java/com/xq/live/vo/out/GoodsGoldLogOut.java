package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

/**
 * Created by ss on 2018/11/1.
 * 用户发起砍菜领取金币
 */
public class GoodsGoldLogOut {

    private String totleKey;//缓存主键

    private String valueKey;//内容主键

    private Long id;

    private Long shopId;//商家id

    private Long userId;//参与的用户id

    private Long refId;//目前是商城的sku_id

    private Byte type;//类型(1商品砍价)

    private Long parentId;//发起人的user_id(如果为空的话那就是发起人)

    private Integer goldAmount;//获得金币的数目

    private Byte stateType;//到账状态 0未到金币 1已到金币

    private Byte isDelete;//是否删除(0未删除 1已删除)

    private Date paidTime;//支付时间

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//创建时间

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;//更新时间

    private Integer groupId;//小组id

    private List<Integer> goldGroup;

    private Integer peopleNum;//小组中获得金币的人数

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private Integer goldSum;

    private Long actId; //活动id

    public String getTotleKey() {
        return totleKey;
    }

    public void setTotleKey(String totleKey) {
        this.totleKey = totleKey;
    }

    public String getValueKey() {
        return valueKey;
    }

    public void setValueKey(String valueKey) {
        this.valueKey = valueKey;
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public Integer getGoldSum() {
        return goldSum;
    }

    public void setGoldSum(Integer goldSum) {
        this.goldSum = goldSum;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(Integer peopleNum) {
        this.peopleNum = peopleNum;
    }

    public List<Integer> getGoldGroup() {
        return goldGroup;
    }

    public void setGoldGroup(List<Integer> goldGroup) {
        this.goldGroup = goldGroup;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

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
}
