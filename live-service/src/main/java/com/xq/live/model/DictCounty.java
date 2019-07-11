package com.xq.live.model;

import java.util.Date;

/**
 * 县区
 */
public class DictCounty {
    private Long id;

    private Long cityId;

    private String countyCname;

    private String countyEname;

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

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getCountyCname() {
        return countyCname;
    }

    public void setCountyCname(String countyCname) {
        this.countyCname = countyCname == null ? null : countyCname.trim();
    }

    public String getCountyEname() {
        return countyEname;
    }

    public void setCountyEname(String countyEname) {
        this.countyEname = countyEname == null ? null : countyEname.trim();
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
