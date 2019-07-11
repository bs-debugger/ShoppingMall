package com.xq.live.vo.in;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by ss on 2018/9/4.
 */
public class OrderAddressInVo  extends BaseInVo {

    private Long id;

    private Long userId;

    private Long dictProvinceId;

    private Long dictCityId;

    private Long dictCountyId;

    private Long dictAreaId;

    private String detailAddress;

    private String chatName;

    private String mobile;

    private Integer isDefault;

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
        this.detailAddress = detailAddress;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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
