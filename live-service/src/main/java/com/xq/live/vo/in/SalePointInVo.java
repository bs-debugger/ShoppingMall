package com.xq.live.vo.in;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by admin on 2018/9/26.
 */
public class SalePointInVo extends BaseInVo {

    private Long id;

    private String salepointName;

    /**
     * 搜索关键字
     */
    private String searcheKey;

    private BigDecimal locationX;

    private BigDecimal locationY;

    private Integer salepointStatus;//销售点状态

    private Integer applyStatus;//审批状态

    private String city;//选择的城市

    private String salepointCode;//销售点编码

    private String salepointHours;//销售点营业时间

    private String otherService;//其他服务

    private Integer sendType;//商品配送类型   1.平台邮购  2门店自提

    private Long goodsSkuId;//商品的id

    private Integer type;//销售点类型(1超市 2酒店 3景点)

    private Integer singleType;//分单类型 1分单 2整单

    private Long shopId;//商家id   0代表享七自营

    private List<Long> salePointIds;//销售点id列表

    private Integer chainType;//联锁店

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSalepointName() {
        return salepointName;
    }

    public void setSalepointName(String salepointName) {
        this.salepointName = salepointName;
    }

    public String getSearcheKey() {
        return searcheKey;
    }

    public void setSearcheKey(String searcheKey) {
        this.searcheKey = searcheKey;
    }

    public BigDecimal getLocationX() {
        return locationX;
    }

    public void setLocationX(BigDecimal locationX) {
        this.locationX = locationX;
    }

    public BigDecimal getLocationY() {
        return locationY;
    }

    public void setLocationY(BigDecimal locationY) {
        this.locationY = locationY;
    }

    public Integer getSalepointStatus() {
        return salepointStatus;
    }

    public void setSalepointStatus(Integer salepointStatus) {
        this.salepointStatus = salepointStatus;
    }

    public Integer getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Integer applyStatus) {
        this.applyStatus = applyStatus;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSalepointCode() {
        return salepointCode;
    }

    public void setSalepointCode(String salepointCode) {
        this.salepointCode = salepointCode;
    }

    public String getSalepointHours() {
        return salepointHours;
    }

    public void setSalepointHours(String salepointHours) {
        this.salepointHours = salepointHours;
    }

    public String getOtherService() {
        return otherService;
    }

    public void setOtherService(String otherService) {
        this.otherService = otherService;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public Long getGoodsSkuId() {
        return goodsSkuId;
    }

    public void setGoodsSkuId(Long goodsSkuId) {
        this.goodsSkuId = goodsSkuId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSingleType() {
        return singleType;
    }

    public void setSingleType(Integer singleType) {
        this.singleType = singleType;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public List<Long> getSalePointIds() {
        return salePointIds;
    }

    public void setSalePointIds(List<Long> salePointIds) {
        this.salePointIds = salePointIds;
    }

    public Integer getChainType() {
        return chainType;
    }

    public void setChainType(Integer chainType) {
        this.chainType = chainType;
    }
}
