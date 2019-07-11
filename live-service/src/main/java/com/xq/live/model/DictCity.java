package com.xq.live.model;

import java.util.Date;

/**
 * 城市
 */
public class DictCity {
    private Long id;

    private Long provinceId;

    private String cityCname;

    private String cityEname;

    private Integer enabled;

    private Integer isDeleted;

    private String cityPinyin;

    private String cityCnameEfirst;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityCname() {
        return cityCname;
    }

    public void setCityCname(String cityCname) {
        this.cityCname = cityCname == null ? null : cityCname.trim();
    }

    public String getCityEname() {
        return cityEname;
    }

    public void setCityEname(String cityEname) {
        this.cityEname = cityEname == null ? null : cityEname.trim();
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCityPinyin() {
        return cityPinyin;
    }

    public void setCityPinyin(String cityPinyin) {
        this.cityPinyin = cityPinyin == null ? null : cityPinyin.trim();
    }

    public String getCityCnameEfirst() {
        return cityCnameEfirst;
    }

    public void setCityCnameEfirst(String cityCnameEfirst) {
        this.cityCnameEfirst = cityCnameEfirst == null ? null : cityCnameEfirst.trim();
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
