package com.xq.live.model;

import java.util.Date;

public class ActivityLayout {

    private Integer id;

    private Integer type; // 活动类型（0：banner推荐；1：钜惠推荐；2：一元秒杀；3：精选商家；99：其它活动）

    private String position; // 布局位置（xcx-home：小程序首页，xcx-category-1：正餐，xcx-category-48：轻餐饮）

    private String city; // 城市名

    private Integer sort; // 排序

    private Boolean status; // 状态（0：不显示，1：显示）

    private Boolean isDeleted; // 是否删除（0：否，1：是）

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
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