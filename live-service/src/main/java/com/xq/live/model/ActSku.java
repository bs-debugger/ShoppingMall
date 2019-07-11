package com.xq.live.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 活动推荐菜实体类
 */
public class ActSku {
    /**
     * 参与活动申请审批状态 0 待审批 1 审批通过 2 审批不通过
     */
    public final static int ACT_SKU_APPLY_STATUS_WAIT_APPLIED = 0;

    public final static int ACT_SKU_APPLY_STATUS_APPLIED = 1;

    public final static int ACT_SKU_APPLY_STATUS_REFUSED = 2;

    /**
     * 判断商家是否已经参加该活动 0 未报名 1已报名
     */
    public final static int ACT_SKU_IS_SIGN = 1;

    public final static int ACT_SKU_NO_SIGN = 0;

    private Long id;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    private Long shopId;

    @NotNull(message = "actId必填")
    private Long actId;

    @NotNull(message = "skuId必填")
    private Long skuId;

    private String skuCode;

    private Long prId;//推荐菜对应的使用规则id

    private Byte applyStatus;

    private Byte isLuoxuan;

    private Integer voteNum;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode == null ? null : skuCode.trim();
    }

    public Long getPrId() {
        return prId;
    }

    public void setPrId(Long prId) {
        this.prId = prId;
    }

    public Byte getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Byte applyStatus) {
        this.applyStatus = applyStatus;
    }

    public Byte getIsLuoxuan() {
        return isLuoxuan;
    }

    public void setIsLuoxuan(Byte isLuoxuan) {
        this.isLuoxuan = isLuoxuan;
    }

    public Integer getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(Integer voteNum) {
        this.voteNum = voteNum;
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
