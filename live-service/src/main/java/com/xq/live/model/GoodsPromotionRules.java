package com.xq.live.model;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class GoodsPromotionRules implements Comparable<GoodsPromotionRules>, Serializable {

    private static final long serialVersionUID = 3296453885463791779L;

    /**
     * 促销类型 0 免费 1 满减 2 满赠 3包装费规则  4砍价优惠规则  5秒杀优惠规则
     * 7拼团规则
     */
    public static final int RULE_TYPE_FREE = 0;//免费

    public static final int RULE_TYPE_DELETE = 1;//满减

    public static final int RULE_TYPE_ADD = 2;//满赠

    public static final int RULE_TYPE_PACKING = 3;//包装费规则

    public static final int RULE_TYPE_KJ = 4;//砍价优惠规则

    public static final int RULE_TYPE_MS = 5;//秒杀优惠规则

    public static final int RULE_TYPE_TG = 7;//拼团规则

    @ApiModelProperty(value = "活动规则id")
    private Long id;
    @ApiModelProperty(value = "商品skuId")
    private Long goodsSkuId;
    @ApiModelProperty(value = "商品编号")
    private String goodsSkuCode;
    @ApiModelProperty(value = "商品名字")
    private String goodsSkuName;
    @ApiModelProperty(value = "促销类型 0 免费 1 满减 2 满赠 3包装费规则  4砍价规则 5秒杀商品规则  7拼团规则")
    private Integer ruleType;
    @ApiModelProperty(value = "规则描述")
    private String ruleDesc;
    @ApiModelProperty(value = "截止时间(现在还无需考虑，没实际用途)")
    private Date endTime;
    @ApiModelProperty(value = "商家id")
    private Long shopId;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "满的面额")
    private Integer manNum;
    @ApiModelProperty(value = "减的面额")
    private Integer giftNum;
    @ApiModelProperty(value = "活动价")
    private BigDecimal actAmount;//活动价
    @ApiModelProperty(value = "满金额")
    private BigDecimal manAmount;//满金额
    @ApiModelProperty(value = "减金额")
    private BigDecimal jianAmount;//减金额

    public BigDecimal getActAmount() {
        return actAmount;
    }

    public void setActAmount(BigDecimal actAmount) {
        this.actAmount = actAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodsSkuId() {
        return goodsSkuId;
    }

    public void setGoodsSkuId(Long goodsSkuId) {
        this.goodsSkuId = goodsSkuId;
    }

    public String getGoodsSkuCode() {
        return goodsSkuCode;
    }

    public void setGoodsSkuCode(String goodsSkuCode) {
        this.goodsSkuCode = goodsSkuCode == null ? null : goodsSkuCode.trim();
    }

    public String getGoodsSkuName() {
        return goodsSkuName;
    }

    public void setGoodsSkuName(String goodsSkuName) {
        this.goodsSkuName = goodsSkuName == null ? null : goodsSkuName.trim();
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

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
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

    public Integer getManNum() {
        return manNum;
    }

    public void setManNum(Integer manNum) {
        this.manNum = manNum;
    }

    public Integer getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(Integer giftNum) {
        this.giftNum = giftNum;
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

    //由小到大排序
    @Override
    public int compareTo(GoodsPromotionRules o) {
        Integer a = this.getRuleType();
        Integer b = o.getRuleType();
        if(a>b){
            return 1;
        }
        return -1;
    }
}
