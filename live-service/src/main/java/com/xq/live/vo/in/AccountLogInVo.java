package com.xq.live.vo.in;

/**
 * com.xq.live.vo.in
 * 交易流水日志
 * @author zhangpeng32
 * Created on 2018/5/7 上午10:13
 * @Description:
 */
public class AccountLogInVo extends BaseInVo{
    private Long id;

    private Long userId;

    private Long accountId;

    private Integer operateType;//1支出 2收入

    private Integer type;//1，商户余额 ，2 操作金币，3,用户余额,4，用户已获得的奖励金，5，用户审核中的奖励金，6，用户获取失败的奖励金

    private Long actGoodsSkuId;//活动商品关联表id

    private Integer actGoodsSkuState;//团的状态

    private Integer actOrderState;//参团状态

    private String orderCouponCode;//票券编号

    private String beginTime;

    private String endTime;

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getActGoodsSkuId() {
        return actGoodsSkuId;
    }

    public void setActGoodsSkuId(Long actGoodsSkuId) {
        this.actGoodsSkuId = actGoodsSkuId;
    }

    public Integer getActGoodsSkuState() {
        return actGoodsSkuState;
    }

    public void setActGoodsSkuState(Integer actGoodsSkuState) {
        this.actGoodsSkuState = actGoodsSkuState;
    }

    public Integer getActOrderState() {
        return actOrderState;
    }

    public void setActOrderState(Integer actOrderState) {
        this.actOrderState = actOrderState;
    }

    public String getOrderCouponCode() {
        return orderCouponCode;
    }

    public void setOrderCouponCode(String orderCouponCode) {
        this.orderCouponCode = orderCouponCode;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
