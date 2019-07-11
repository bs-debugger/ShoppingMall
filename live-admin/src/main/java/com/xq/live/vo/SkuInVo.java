package com.xq.live.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-12 14:18
 * @copyright:hbxq
 **/
public class SkuInVo extends BaseVo {
    private Long id;

    private String skuCode;

    private String skuName;

    private Integer skuType;

    private BigDecimal sellPrice;

    private BigDecimal inPrice;

    private Integer stockNum;

    private Integer isDeleted;

    private Date createTime;

    private Date updateTime;

    private Long opreatorId;

    private String opreatorName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Integer getSkuType() {
        return skuType;
    }

    public void setSkuType(Integer skuType) {
        this.skuType = skuType;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public BigDecimal getInPrice() {
        return inPrice;
    }

    public void setInPrice(BigDecimal inPrice) {
        this.inPrice = inPrice;
    }

    public Integer getStockNum() {
        return stockNum;
    }

    public void setStockNum(Integer stockNum) {
        this.stockNum = stockNum;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
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

    public Long getOpreatorId() {
        return opreatorId;
    }

    public void setOpreatorId(Long opreatorId) {
        this.opreatorId = opreatorId;
    }

    public String getOpreatorName() {
        return opreatorName;
    }

    public void setOpreatorName(String opreatorName) {
        this.opreatorName = opreatorName;
    }
}
