package com.xq.live.vo.out;


import java.math.BigDecimal;

/**
 * Created by ss on 2018/8/3.
 * 分店营业数据
 */
public class ShopForSubOut {

    private Long id;
    private String shopName;
    private Integer isSub;//是否是分店

    private BigDecimal soAmount;//单个分店总营业额

    private Integer solist;//单个分店订单总数

    private Integer soWriteoffList;//单个分店核销总数

    private Integer allsolist;//所有分店订单总数

    private Integer allsoWritelist;//所有分店核销总数

    private BigDecimal allsoAmount;//所有分店总营业额

    public BigDecimal getAllsoAmount() {
        return allsoAmount;
    }

    public void setAllsoAmount(BigDecimal allsoAmount) {
        this.allsoAmount = allsoAmount;
    }

    public Integer getAllsolist() {
        return allsolist;
    }

    public void setAllsolist(Integer allsolist) {
        this.allsolist = allsolist;
    }

    public Integer getAllsoWritelist() {
        return allsoWritelist;
    }

    public void setAllsoWritelist(Integer allsoWritelist) {
        this.allsoWritelist = allsoWritelist;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Integer getIsSub() {
        return isSub;
    }

    public void setIsSub(Integer isSub) {
        this.isSub = isSub;
    }

    public BigDecimal getSoAmount() {
        return soAmount;
    }

    public void setSoAmount(BigDecimal soAmount) {
        this.soAmount = soAmount;
    }

    public Integer getSolist() {
        return solist;
    }

    public void setSolist(Integer solist) {
        this.solist = solist;
    }

    public Integer getSoWriteoffList() {
        return soWriteoffList;
    }

    public void setSoWriteoffList(Integer soWriteoffList) {
        this.soWriteoffList = soWriteoffList;
    }







}
