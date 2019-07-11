package com.xq.live.vo.out;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by admin on 2019/2/18.
 */
public class OrderWriteOffResultOut {

    private String shopZoneName;

    private String shopName;

    private String skuNmae;

    private String userName;

    private String userNickName;

    private Date createTime;

    private Date startTime;

    private Date endTime;

    private String usedTime;

    private Long shopZoneItemId;

    private String state;

    private BigDecimal realUnitPrice;

    public String getShopZoneName() {
        return shopZoneName;
    }

    public void setShopZoneName(String shopZoneName) {
        this.shopZoneName = shopZoneName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getSkuNmae() {
        return skuNmae;
    }

    public void setSkuNmae(String skuNmae) {
        this.skuNmae = skuNmae;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getShopZoneItemId() {
        return shopZoneItemId;
    }

    public void setShopZoneItemId(Long shopZoneItemId) {
        this.shopZoneItemId = shopZoneItemId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(String usedTime) {
        this.usedTime = usedTime;
    }

    public BigDecimal getRealUnitPrice() {
        return realUnitPrice;
    }

    public void setRealUnitPrice(BigDecimal realUnitPrice) {
        this.realUnitPrice = realUnitPrice;
    }
}
