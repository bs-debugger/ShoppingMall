package com.xq.live.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class OrderAddress {
    //是否是默认地址
    public static final Integer IS_DEFAULT = 1;//已默认

    public static final Integer IS_NOT_DEFAULT = 0;//未默认

    //是否删除
    public static final Integer IS_DELETED = 1;//删除

    public static final Integer IS_NOT_DELETED = 0;//未删除

    private Long id;

    private Long userId;

    private Long dictProvinceId;//省份id

    private Long dictCityId;//城市id

    private Long dictCountyId;//县(区)id

    private Long dictAreaId;//乡镇(街道)id

    private String detailAddress;//详细地址

    private String chatName;//联系人

    private String mobile;//手机号

    private Integer isDefault;//是否是默认地址 0未默认 1已默认

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private Integer isDeleted;//是否删除 0 否 1 是

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDictProvinceId() {
        return dictProvinceId;
    }

    public void setDictProvinceId(Long dictProvinceId) {
        this.dictProvinceId = dictProvinceId;
    }

    public Long getDictCityId() {
        return dictCityId;
    }

    public void setDictCityId(Long dictCityId) {
        this.dictCityId = dictCityId;
    }

    public Long getDictCountyId() {
        return dictCountyId;
    }

    public void setDictCountyId(Long dictCountyId) {
        this.dictCountyId = dictCountyId;
    }

    public Long getDictAreaId() {
        return dictAreaId;
    }

    public void setDictAreaId(Long dictAreaId) {
        this.dictAreaId = dictAreaId;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress == null ? null : detailAddress.trim();
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName == null ? null : chatName.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
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
}