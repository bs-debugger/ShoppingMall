package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by ss on 2018/8/11.
 */
public class BargainLogOut {

    private Long id;

    private Long shopId;

    private Long userId;

    private Long skuId;

    private Long parentId;

    private BigDecimal skuAmount;

    private Byte isDelete;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private Integer groupId;

    private List<BigDecimal> amountList;//砍菜金额列表

    private Integer peoplenum;//砍菜人数

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;//开始时间

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;//截止时间

    private BigDecimal skuMoneyNow;//菜的现价

    private BigDecimal skuMoneyOut;//菜的原价

    private BigDecimal skuMoneyMin;//菜的底价

    private String nickName;//昵称

    private String userName;//用户名称

    private String iconUrl;//头像

    private Integer goldAmount;//获得的金币

    private String skuName;

    private String picUrl;

    private Integer skuType;//菜的类型

    public Integer getSkuType() {
        return skuType;
    }

    public void setSkuType(Integer skuType) {
        this.skuType = skuType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Integer getGoldAmount() {
        return goldAmount;
    }

    public void setGoldAmount(Integer goldAmount) {
        this.goldAmount = goldAmount;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public BigDecimal getSkuMoneyMin() {
        return skuMoneyMin;
    }

    public void setSkuMoneyMin(BigDecimal skuMoneyMin) {
        this.skuMoneyMin = skuMoneyMin;
    }

    public BigDecimal getSkuMoneyOut() {
        return skuMoneyOut;
    }

    public void setSkuMoneyOut(BigDecimal skuMoneyOut) {
        this.skuMoneyOut = skuMoneyOut;
    }

    public BigDecimal getSkuMoneyNow() {
        return skuMoneyNow;
    }

    public void setSkuMoneyNow(BigDecimal skuMoneyNow) {
        this.skuMoneyNow = skuMoneyNow;
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

    public Integer getPeoplenum() {
        return peoplenum;
    }

    public void setPeoplenum(Integer peoplenum) {
        this.peoplenum = peoplenum;
    }

    public List<BigDecimal> getAmountList() {
        return amountList;
    }

    public void setAmountList(List<BigDecimal> amountList) {
        this.amountList = amountList;
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

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public BigDecimal getSkuAmount() {
        return skuAmount;
    }

    public void setSkuAmount(BigDecimal skuAmount) {
        this.skuAmount = skuAmount;
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
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

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
