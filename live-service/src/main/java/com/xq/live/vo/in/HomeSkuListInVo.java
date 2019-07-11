package com.xq.live.vo.in;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ss on 2018/11/13.
 * 首页类目列表入参
 */
public class HomeSkuListInVo{


    private Integer rows = 20; //总行数
    private Integer page = 1;//当前页
    private BigDecimal locationX;
    private BigDecimal locationY;
    private String city;//城市

    List<HomeObjectInVo> listStart;//搜索行

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<HomeObjectInVo> getListStart() {
        return listStart;
    }

    public void setListStart(List<HomeObjectInVo> listStart) {
        this.listStart = listStart;
    }

    public BigDecimal getLocationX() {
        return locationX;
    }

    public void setLocationX(BigDecimal locationX) {
        this.locationX = locationX;
    }

    public BigDecimal getLocationY() {
        return locationY;
    }

    public void setLocationY(BigDecimal locationY) {
        this.locationY = locationY;
    }


    @Override
    public String toString() {
        return "HomeSkuListInVo{" +
                "rows=" + rows +
                ", page=" + page +
                ", locationX=" + locationX +
                ", locationY=" + locationY +
                ", city='" + city + '\'' +
                ", listStart=" + listStart +
                '}';
    }
}
