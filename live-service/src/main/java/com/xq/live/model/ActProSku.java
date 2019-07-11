package com.xq.live.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ss on 2018/6/15.
 */
public class ActProSku {
    /**
     * 促销类型 0 免费 1 满减 2 满赠
     */
    public static final int RULE_TYPE_FREE = 0;

    public static final int RULE_TYPE_DELETE = 1;

    public static final int RULE_TYPE_ADD = 2;
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
    //活动id
    private Long actId;



    private Long shopId;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
    //菜id
    private Long skuId;
    //券id
    private Long skugId;
    //菜品code
    private String skuCode;
    //券code
    private String skugCode;

    public String getSkugCode() {
        return skugCode;
    }

    public void setSkugCode(String skugCode) {
        this.skugCode = skugCode;
    }

    private String skuName;

    private Integer ruleType;

    private String ruleDesc;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private BigDecimal manAmount;

    private BigDecimal jianAmount;

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public Long getId() {
        return id;
    }
    public Long getSkugId() {
        return skugId;
    }

    public void setSkugId(Long skugId) {
        this.skugId = skugId;
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

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode == null ? null : skuCode.trim();
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName == null ? null : skuName.trim();
    }

    public Integer getRuleType() {
        return ruleType;
    }

    public void setRuleType(Integer ruleType) {
        this.ruleType = ruleType;
    }

    public String getRuleDesc() {
        return ruleDesc;
    }

    public void setRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc == null ? null : ruleDesc.trim();
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

    public BigDecimal getManAmount() {
        return manAmount;
    }

    public void setManAmount(BigDecimal manAmount) {
        this.manAmount = manAmount;
    }

    public BigDecimal getJianAmount() {
        return jianAmount;
    }

    public void setJianAmount(BigDecimal jianAmount) {
        this.jianAmount = jianAmount;
    }
}
