package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xq.live.model.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by ss on 2018/11/1.
 * 活动商品关联
 */
public class ActGoodsSkuOut implements Serializable {

    private static final long serialVersionUID = 8572739373150294261L;

    private Long id;

    private Long actId;//活动id

    private Long skuId;//商品sku_id

    private String skuCode;//商品编号(是参与活动生成的编号)

    private Long shopId;//商品对应的商家id

    private long categoryId;//活动类目

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

    private GoodsCategory goodsCategory;//类目详情

    private GoodsPromotionRules goodsPromotionRules;//活动商品对应的促销规则

    private List<GoodsSkuOut> goodsSkuOut;//商品

    private ActInfo actInfo;//活动

    private Integer peopleNum;//此商品的参与人数

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;//商品活动结束时间

    private  String ruleDesc;//商品活动规则描述

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;//商品活动开始时间

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dueTime;//商品活动截至时间

    private Long createUserId;//创建者id

    private Integer state;//商品活动状态：1，进行中，2，成功 3，失败

    private Integer currentNum;//当前参与人数

    private ActOrderOut actOrder;

    private List<User> users;//邀请的用户列表

    private String goodsSkuNumber;//编号

    private Integer voteNum;//投票数目

    private Integer waringStock;//预警库存

    private Integer autoAddStock;//是否自动补充库存 0 否 1是

    private GoodsSku goodsSku;//商品基本信息

    private String shopName;//商家名称

    private String position; // 活动布局

    private Integer activityLayoutId;  // 活动布局ID

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expiryDate;//有效期时间

    private  BigDecimal discount;//折扣

    private Integer weekendUsable;//是否周末通用(0,不通用,1 通用)

    private  Integer timeUsable;//是否限制时段(0,不限制,1 限制)

    private List<ActTimeRules> actTimeRules;//活动时间规则

    public Integer getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(Integer peopleNum) {
        this.peopleNum = peopleNum;
    }

    public ActInfo getActInfo() {
        return actInfo;
    }

    public void setActInfo(ActInfo actInfo) {
        this.actInfo = actInfo;
    }

    public GoodsCategory getGoodsCategory() {
        return goodsCategory;
    }

    public void setGoodsCategory(GoodsCategory goodsCategory) {
        this.goodsCategory = goodsCategory;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public List<GoodsSkuOut> getGoodsSkuOut() {
        return goodsSkuOut;
    }

    public void setGoodsSkuOut(List<GoodsSkuOut> goodsSkuOut) {
        this.goodsSkuOut = goodsSkuOut;
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

    public GoodsPromotionRules getGoodsPromotionRules() {
        return goodsPromotionRules;
    }

    public void setGoodsPromotionRules(GoodsPromotionRules goodsPromotionRules) {
        this.goodsPromotionRules = goodsPromotionRules;
    }

    public ActOrderOut getActOrder() {
        return actOrder;
    }

    public void setActOrder(ActOrderOut actOrder) {
        this.actOrder = actOrder;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
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

    public GoodsSku getGoodsSku() {
        return goodsSku;
    }

    public void setGoodsSku(GoodsSku goodsSku) {
        this.goodsSku = goodsSku;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getActivityLayoutId() {
        return activityLayoutId;
    }

    public void setActivityLayoutId(Integer activityLayoutId) {
        this.activityLayoutId = activityLayoutId;
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

    public List<ActTimeRules> getActTimeRules() {
        return actTimeRules;
    }

    public void setActTimeRules(List<ActTimeRules> actTimeRules) {
        this.actTimeRules = actTimeRules;
    }
}
