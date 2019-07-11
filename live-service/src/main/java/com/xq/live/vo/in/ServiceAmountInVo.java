package com.xq.live.vo.in;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 缴纳服务费入参
 * Created by lipeng on 2018/7/27.
 */
public class ServiceAmountInVo extends BaseInVo{

    private Long id;

    @NotNull(message = "paidUserId必填")
    private Long paidUserId;

    @NotNull(message = "shopId必填")
    private Long shopId;

    @NotNull(message = "servicePrice必填")
    private BigDecimal servicePrice;

    private Date beginTime;

    private Date endTime;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPaidUserId() {
        return paidUserId;
    }

    public void setPaidUserId(Long paidUserId) {
        this.paidUserId = paidUserId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public BigDecimal getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(BigDecimal servicePrice) {
        this.servicePrice = servicePrice;
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
