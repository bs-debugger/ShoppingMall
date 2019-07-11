package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xq.live.model.GoodsSpec;
import com.xq.live.model.GoodsSpuDesc;

import java.util.Date;
import java.util.List;

/**
 * 商城系统Spu的入参
 * Created by lipeng on 2018/8/29.
 */
public class GoodsSpuOut {
    private Long id;

    private String spuName;

    private String spuCode;

    private Integer spuType;//产品类型

    private Long categoryId;

    private Long brandId;

    private Integer isDeleted;//是否删除 0未删除 1已删除

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private GoodsSpuDesc goodsSpuDesc;//spu文描详情

    private List<GoodsSpec> goodsSpecs;//spu对应规格的列表

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName == null ? null : spuName.trim();
    }

    public String getSpuCode() {
        return spuCode;
    }

    public void setSpuCode(String spuCode) {
        this.spuCode = spuCode == null ? null : spuCode.trim();
    }

    public Integer getSpuType() {
        return spuType;
    }

    public void setSpuType(Integer spuType) {
        this.spuType = spuType;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
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

    public GoodsSpuDesc getGoodsSpuDesc() {
        return goodsSpuDesc;
    }

    public void setGoodsSpuDesc(GoodsSpuDesc goodsSpuDesc) {
        this.goodsSpuDesc = goodsSpuDesc;
    }

    public List<GoodsSpec> getGoodsSpecs() {
        return goodsSpecs;
    }

    public void setGoodsSpecs(List<GoodsSpec> goodsSpecs) {
        this.goodsSpecs = goodsSpecs;
    }
}
