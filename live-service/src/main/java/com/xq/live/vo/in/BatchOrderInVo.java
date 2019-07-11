package com.xq.live.vo.in;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 批量制造订单入参
 * Created by lipeng on 2018/12/7.
 */
public class BatchOrderInVo {
    /**
     * 订单状态  1取消  2完成
     */
    public final static int ORDER_STATUS_QUIT = 1;//取消

    public final static int ORDER_STATUS_SUCCESS = 2;//完成

    /**
     * 订单类型   1享七  2活动
     */
    public final static int ORDER_TYPE_XQ = 1;//享七

    public final static int ORDER_TYPE_ACT = 2;//活动



    private Integer batchSize;//批量制造的数据

    private Integer xqNum;//享七券所占比例 6

    private Integer kjNum;//砍价券所占比例 8

    private Integer msNum;//秒杀券所占比例 10

    private Integer quitOrderNum;//取消订单所占比例 6

    private Integer successOrderNum;//已完成订单所占比例 10

    private Integer shopNum;//商家数量

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;//制造数据的开始时间

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;//制造数据的结束时间

    private Integer orderStatus;//订单状态  1取消  2完成

    private Integer orderType;//订单类型   1享七  2活动

    private Long hxShopId;//核销的shopId

    private String hxShopName;//核销的shopName

    private Integer type;//1,享七五元，2，享七十元，3，享七十五元，4，享七二十元，5，秒杀，6 砍价拼菜，7大闸蟹,8,食碘卷

    private Integer userStart1;//用户区间1开始id

    private Integer userEnd1;//用户区间1结束id

    private Integer userSum1;//区间1用户总数

    private Integer userStart2;//用户区间2开始id

    private Integer userEnd2;//用户区间2结束id

    private Integer userSum2;//区间2用户总数

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public Integer getXqNum() {
        return xqNum;
    }

    public void setXqNum(Integer xqNum) {
        this.xqNum = xqNum;
    }

    public Integer getKjNum() {
        return kjNum;
    }

    public void setKjNum(Integer kjNum) {
        this.kjNum = kjNum;
    }

    public Integer getMsNum() {
        return msNum;
    }

    public void setMsNum(Integer msNum) {
        this.msNum = msNum;
    }

    public Integer getQuitOrderNum() {
        return quitOrderNum;
    }

    public void setQuitOrderNum(Integer quitOrderNum) {
        this.quitOrderNum = quitOrderNum;
    }

    public Integer getSuccessOrderNum() {
        return successOrderNum;
    }

    public void setSuccessOrderNum(Integer successOrderNum) {
        this.successOrderNum = successOrderNum;
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

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Long getHxShopId() {
        return hxShopId;
    }

    public void setHxShopId(Long hxShopId) {
        this.hxShopId = hxShopId;
    }

    public String getHxShopName() {
        return hxShopName;
    }

    public void setHxShopName(String hxShopName) {
        this.hxShopName = hxShopName;
    }

    public Integer getShopNum() {
        return shopNum;
    }

    public void setShopNum(Integer shopNum) {
        this.shopNum = shopNum;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getUserStart1() {
        return userStart1;
    }

    public void setUserStart1(Integer userStart1) {
        this.userStart1 = userStart1;
    }

    public Integer getUserEnd1() {
        return userEnd1;
    }

    public void setUserEnd1(Integer userEnd1) {
        this.userEnd1 = userEnd1;
    }

    public Integer getUserStart2() {
        return userStart2;
    }

    public void setUserStart2(Integer userStart2) {
        this.userStart2 = userStart2;
    }

    public Integer getUserEnd2() {
        return userEnd2;
    }

    public void setUserEnd2(Integer userEnd2) {
        this.userEnd2 = userEnd2;
    }

    public Integer getUserSum1() {
        return userSum1;
    }

    public void setUserSum1(Integer userSum1) {
        this.userSum1 = userSum1;
    }

    public Integer getUserSum2() {
        return userSum2;
    }

    public void setUserSum2(Integer userSum2) {
        this.userSum2 = userSum2;
    }

}
