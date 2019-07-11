package com.xq.live.vo.in;

/**
 * 地址入参
 * Created by lipeng on 2018/3/23.
 */
public class DictInVo extends BaseInVo{
    private Long id;

    private Long provinceId;

    private Long cityId;

    private Long countyId;

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
}
