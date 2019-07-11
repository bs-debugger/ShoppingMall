package com.xq.live.model;

import java.util.Date;

/**
 * 省份Dao
 */
public class DictProvince {
    private Long id;

    private String provinceCname;

    private String provinceEname;

    private String pinyin;

    private Integer enabled;

    private Integer isDeleted;

    private String provinceCnameEfirst;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProvinceCname() {
        return provinceCname;
    }

    public void setProvinceCname(String provinceCname) {
        this.provinceCname = provinceCname == null ? null : provinceCname.trim();
    }

    public String getProvinceEname() {
        return provinceEname;
    }

    public void setProvinceEname(String provinceEname) {
        this.provinceEname = provinceEname == null ? null : provinceEname.trim();
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin == null ? null : pinyin.trim();
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

    public String getProvinceCnameEfirst() {
        return provinceCnameEfirst;
    }

    public void setProvinceCnameEfirst(String provinceCnameEfirst) {
        this.provinceCnameEfirst = provinceCnameEfirst == null ? null : provinceCnameEfirst.trim();
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
