package com.xq.live.vo.in;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xq.live.model.ActTimeRules;
import com.xq.live.model.GoodsPromotionRules;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by ss on 2018/11/1.
 * 活动商品关联
 */
public class ActGoodsSkuInVo extends BaseInVo{
    @ApiModelProperty(value = "商品名称")
    private Long id;
    @ApiModelProperty(value = "活动id")
    private Long actId;//活动id
    @ApiModelProperty(value = "商品sku_id")
    private Long skuId;//商品sku_id
    @ApiModelProperty(value = "商品编号(是参与活动生成的编号)")
    private String skuCode;//商品编号(是参与活动生成的编号)
    @ApiModelProperty(value = "商品对应的商家id")
    private Long shopId;//商品对应的商家id
    @ApiModelProperty(value = "活动类目")
    private Long categoryId;//活动类目
    @ApiModelProperty(value = "商品所对应的规则id")
    private Long goodsPrId;//商品所对应的规则id
    @ApiModelProperty(value = "审核状态 0 待审批 1 审批通过 2审批不通过")
    private Byte applyStatus;//审核状态 0 待审批 1 审批通过 2审批不通过
    @ApiModelProperty(value = "排序顺序")
    private Integer sortNum;//排序顺序
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//创建时间
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;//更新时间
    @ApiModelProperty(value = "是否删除 0 否 1是")
    private Integer isDeleted;//是否删除 0 否 1是
    @ApiModelProperty(value = "pulluser类型1:兼职邀请新用户，2:邀请新用户吃螃蟹,3:拉新人送旅游景点门票 5参团邀请新人注册 6邀请新人注册购买秒杀商品")
    private Integer type; //pulluser类型1:兼职邀请新用户，2:邀请新用户吃螃蟹,3:拉新人送旅游景点门票 5参团邀请新人注册 6邀请新人注册购买秒杀商品
    @ApiModelProperty(value = "此商品的参与人数")
    private Integer peopleNum;//此商品的参与人数
    @ApiModelProperty(value = "商品活动结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;//商品活动结束时间
    @ApiModelProperty(value = "商品活动规则描述")
    private  String ruleDesc;//商品活动规则描述
    @ApiModelProperty(value = "商品活动开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;//商品活动开始时间
    @ApiModelProperty(value = "商品活动截至时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dueTime;//商品活动截至时间
    @ApiModelProperty(value = "创建者id")
    private Long createUserId;//创建者id
    @ApiModelProperty(value = "商品活动状态：1，进行中，2，成功 3，失败")
    private Integer state;//商品活动状态：1，进行中，2，成功 3，失败
    @ApiModelProperty(value = "当前参与人数")
    private Integer currentNum;//当前参与人数
    @ApiModelProperty(value = "1 为库存减少 2为库存增加")
    private Integer stockNumType;//1 为库存减少 2为库存增加
    @ApiModelProperty(value = "库存")
    private Integer stockNum;//库存
    @ApiModelProperty(value = "编号")
    private String goodsSkuNumber;//编号
    @ApiModelProperty(value = "投票数目")
    private Integer voteNum;//投票数目
    @ApiModelProperty(value = "城市")
    private String city;//城市
    @ApiModelProperty(value = "权重 ,该商品抽奖时的权重")
    private Integer weight;//权重 ,该商品抽奖时的权重
    @ApiModelProperty(value = "概率，抽奖时的固定概率，有值时不计算权重，例：0.2表示固定20%概率")
    private BigDecimal ratio;//概率，抽奖时的固定概率，有值时不计算权重，例：0.2表示固定20%概率
    @ApiModelProperty(value = "参与活动商品的的促销规则")
    private GoodsPromotionRules goodsPromotionRules;//参与活动商品的的促销规则
    @ApiModelProperty(value = "预警库存")
    private Integer waringStock;//预警库存

    @ApiModelProperty(value = "是否自动补充库存 0 否 1是")
    private Integer autoAddStock;//是否自动补充库存 0 否 1是

    @ApiModelProperty(value = "有效期时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expiryDate;//有效期时间

    @ApiModelProperty(value = "折扣")
    private  BigDecimal discount;//折扣

    @ApiModelProperty(value = "是否周末通用(0,不通用,1 通用)")
    private Integer weekendUsable;//是否周末通用(0,不通用,1 通用)

    @ApiModelProperty(value = "是否限制时段(0,不限制,1 限制)")
    private  Integer timeUsable;//是否限制时段(0,不限制,1 限制)

    @ApiModelProperty(value = "参与活动商品活动时间规则")
    private List<ActTimeRules> actTimeRules;//参与活动商品活动时间规则

    /**
     * 搜索关键字
     */
    private String searcheKey;

    @Override
    public String toString() {
        return "ActGoodsSkuInVo{" +
                "id=" + id +
                ", actId=" + actId +
                ", skuId=" + skuId +
                ", skuCode='" + skuCode + '\'' +
                ", shopId=" + shopId +
                ", categoryId=" + categoryId +
                ", goodsPrId=" + goodsPrId +
                ", applyStatus=" + applyStatus +
                ", sortNum=" + sortNum +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isDeleted=" + isDeleted +
                ", type=" + type +
                ", peopleNum=" + peopleNum +
                ", endTime=" + endTime +
                ", rulDesc='" + ruleDesc + '\'' +
                ", startTime=" + startTime +
                ", dueTime=" + dueTime +
                ", createUserId=" + createUserId +
                ", state=" + state +
                ", currentNum=" + currentNum +
                ", stockNumType=" + stockNumType +
                ", stockNum=" + stockNum +
                ", goodsPromotionRules=" + goodsPromotionRules +
                '}';
    }

    public Integer getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(Integer voteNum) {
        this.voteNum = voteNum;
    }

    public String getGoodsSkuNumber() {
        return goodsSkuNumber;
    }

    public void setGoodsSkuNumber(String goodsSkuNumber) {
        this.goodsSkuNumber = goodsSkuNumber;
    }

    public Integer getStockNum() {
        return stockNum;
    }

    public void setStockNum(Integer stockNum) {
        this.stockNum = stockNum;
    }

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

    public Integer getStockNumType() {
        return stockNumType;
    }

    public void setStockNumType(Integer stockNumType) {
        this.stockNumType = stockNumType;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getSearcheKey() {
        return searcheKey;
    }

    public void setSearcheKey(String searcheKey) {
        this.searcheKey = searcheKey;
    }

    public List<ActTimeRules> getActTimeRules() {
        return actTimeRules;
    }

    public void setActTimeRules(List<ActTimeRules> actTimeRules) {
        this.actTimeRules = actTimeRules;
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
