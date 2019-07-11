package com.xq.live.vo.in;

import com.xq.live.model.GoodsSpuDesc;
import com.xq.live.model.GoodsSpuSpec;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 商城系统Spu的入参
 * Created by lipeng on 2018/8/29.
 */
public class GoodsSpuInVo extends BaseInVo{
    private Long id;

    @NotNull(message = "spuName不能为空")
    private String spuName;

    private String spuCode;

    @NotNull(message = "spuType不能为空")
    private Integer spuType;//产品类型

    @NotNull(message = "categoryId不能为空")
    private Long categoryId;

    @NotNull(message = "brandId不能为空")
    private Long brandId;

    private Integer isDeleted;//是否删除 0未删除 1已删除

    private Date createTime;

    private Date updateTime;

    private GoodsSpuSpec goodsSpuSpec;//spu的规格详情

    private GoodsSpuDesc goodsSpuDesc;//spu文描详情

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

    public GoodsSpuSpec getGoodsSpuSpec() {
        return goodsSpuSpec;
    }

    public void setGoodsSpuSpec(GoodsSpuSpec goodsSpuSpec) {
        this.goodsSpuSpec = goodsSpuSpec;
    }

    public GoodsSpuDesc getGoodsSpuDesc() {
        return goodsSpuDesc;
    }

    public void setGoodsSpuDesc(GoodsSpuDesc goodsSpuDesc) {
        this.goodsSpuDesc = goodsSpuDesc;
    }
}
