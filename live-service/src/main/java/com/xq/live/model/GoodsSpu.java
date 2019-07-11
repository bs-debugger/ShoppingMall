package com.xq.live.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class GoodsSpu {
    /**
     * is_deleted 0 未删除  1 已删除
     */
    public final static int GOODS_SPU_NO_DELETED = 0;   //未删除

    public final static int GOODS_SPU_IS_DELETED = 1;   //已删除

    /**
     * spu_type   10 小程序代售
     */
    public final static int SPU_TYPE_XCX = 10;   //小程序代售

    private Long id;

    @NotNull(message = "spuName不能为空")
    private String spuName;//产品名字

    private String spuCode;//产品编码

    @NotNull(message = "spuType不能为空")
    private Integer spuType;//产品类型

    @NotNull(message = "categoryId不能为空")
    private Long categoryId;//类目id

    @NotNull(message = "brandId不能为空")
    private Long brandId;//品牌id

    private Integer isDeleted;//是否删除 0未删除 1已删除

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
}
