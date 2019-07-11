package com.xq.live.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ss on 2018/11/1.
 *活动商品关联
 */
public class ActGoodsSku {
    /**
     * 审批状态 0 审批中  1 审批通过  2审批不通过
     */
    public final static Byte APPLY_STATUS_WAIT = 0;

    public final static Byte APPLY_STATUS_SUCCESS = 1;

    public final static Byte APPLY_STATUS_FAIL =2;

    /**
     * 是否删除 0 未删除  1 已删除
     */
    public final static int IS_DELETED_NO = 0;

    public final static int IS_DELETED_YES = 1;

    /**
     * 商品活动状态：1，进行中，2，成功 3，失败
     */
    public final static int STATE_WAIT = 1;

    public final static int STATE_SUCCESS = 2;

    public final static int STATE_FAIL =3;

    /**
     * 库存条件
     */
    public final static int STOCK_NUM_TYPE_DOWN =1;//为库存减少

    public final static int STOCK_NUM_TYPE_ADD =2;//为库存增加

    /**
     * 是否自动补充库存 0 否 1是
     */
    public final static int AUTO_ADD_STOCK_NO = 0;

    public final static int AUTO_ADD_STOCK_YES = 1;

    private Long id;

    private Long actId;//活动id

    private Long skuId;//商品sku_id

    private String skuCode;//商品编号(是参与活动生成的编号)

    private Long shopId;//商品对应的商家id

    private Long categoryId;//活动类目

    private Long goodsPrId;//商品所对应的规则id

    private Byte applyStatus;//审核状态 0 待审批 1 审批通过 2审批不通过

    private Integer sortNum;//排序顺序

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//创建时间

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;//更新时间

    private Integer isDeleted;//是否删除 0 否 1是

    private Integer weight;//权重 ,该商品抽奖时的权重

    private BigDecimal ratio;//概率，抽奖时的固定概率，有值时不计算权重，例：0.2表示固定20%概率

    private Integer stockNum;//库存，剩余可抽奖数量，不为0才能参与抽奖

    private Integer peopleNum;//此商品的参与人数

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;//商品活动结束时间

    private String ruleDesc;//商品活动规则描述

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;//商品活动开始时间

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dueTime;//商品活动截至时间

    private Long createUserId;//创建者id

    private Integer state;//商品活动状态：1，进行中，2，成功 3，失败

    private Integer currentNum;//当前参与人数

    private String goodsSkuNumber;//编号

    private Integer voteNum;//投票数目

    private Integer waringStock;//预警库存

    private Integer autoAddStock;//是否自动补充库存 0 否 1是

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expiryDate;//有效期时间

    private  BigDecimal discount;//折扣

    private Integer weekendUsable;//是否周末通用(0,不通用,1 通用)

    private  Integer timeUsable;//是否限制时段(0,不限制,1 限制)

    public Integer getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(Integer peopleNum) {
        this.peopleNum = peopleNum;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

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

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getGoodsPrId() {
        return goodsPrId;
    }

    public void setGoodsPrId(Long goodsPrId) {
        this.goodsPrId = goodsPrId;
    }

    public Byte getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Byte applyStatus) {
        this.applyStatus = applyStatus;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
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

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    public Integer getStockNum() {
        return stockNum;
    }

    public void setStockNum(Integer stockNum) {
        this.stockNum = stockNum;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getRuleDesc() {
        return ruleDesc;
    }

    public void setRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getDueTime() {
        return dueTime;
    }

    public void setDueTime(Date dueTime) {
        this.dueTime = dueTime;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(Integer currentNum) {
        this.currentNum = currentNum;
    }

    public String getGoodsSkuNumber() {
        return goodsSkuNumber;
    }

    public void setGoodsSkuNumber(String goodsSkuNumber) {
        this.goodsSkuNumber = goodsSkuNumber;
    }

    public Integer getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(Integer voteNum) {
        this.voteNum = voteNum;
    }

    public Integer getWaringStock() {
        return waringStock;
    }

    public void setWaringStock(Integer waringStock) {
        this.waringStock = waringStock;
    }

    public Integer getAutoAddStock() {
        return autoAddStock;
    }

    public void setAutoAddStock(Integer autoAddStock) {
        this.autoAddStock = autoAddStock;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Integer getWeekendUsable() {
        return weekendUsable;
    }

    public void setWeekendUsable(Integer weekendUsable) {
        this.weekendUsable = weekendUsable;
    }

    public Integer getTimeUsable() {
        return timeUsable;
    }

    public void setTimeUsable(Integer timeUsable) {
        this.timeUsable = timeUsable;
    }
}
