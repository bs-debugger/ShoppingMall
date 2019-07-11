package com.xq.live.model;

import java.util.Date;

/**
 * 街道
 */
public class DictArea {
    private Long id;

    private Long provinceId;

    private Long cityId;

    private Long countyId;

    private String areaEname;

    private String areaCname;

    private Integer enabled;

    private Integer isDeleted;

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

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getCountyId() {
        return countyId;
    }

    public void setCountyId(Long countyId) {
        this.countyId = countyId;
    }

    public String getAreaEname() {
        return areaEname;
    }

    public void setAreaEname(String areaEname) {
        this.areaEname = areaEname == null ? null : areaEname.trim();
    }

    public String getAreaCname() {
        return areaCname;
    }

    public void setAreaCname(String areaCname) {
        this.areaCname = areaCname == null ? null : areaCname.trim();
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
