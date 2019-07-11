package com.xq.live.model;

import java.util.Date;

public class GoodsSkuRejectLog {
    private Long id;

    private Long skuId;

    private String rejectResult;

    private String rejectPic;

    private Integer smsSend;

    private Integer isDeleted;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getRejectResult() {
        return rejectResult;
    }

    public void setRejectResult(String rejectResult) {
        this.rejectResult = rejectResult == null ? null : rejectResult.trim();
    }

    public String getRejectPic() {
        return rejectPic;
    }

    public void setRejectPic(String rejectPic) {
        this.rejectPic = rejectPic == null ? null : rejectPic.trim();
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

    public Integer getSmsSend() {
        return smsSend;
    }

    public void setSmsSend(Integer smsSend) {
        this.smsSend = smsSend;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}