package com.xq.live.vo.out;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by ss on 2018/12/29.
 * 分店数据
 */
public class SubbranchOut {

    private Long shopId;
    private String shopName;
    private BigDecimal duiPrice;//对账的所有实际营业额
    private BigDecimal noDuiPrice;//未对账的所有实际营业额
    private BigDecimal allPrice;//所有实际营业额
    private BigDecimal shopTurnover;//所有理论的营业额
    private Integer orderTotal;//订单的数目
    private Integer orderWriteOffTotal;//核销票券的数目
    private Integer allOrderTotle;//总订单数
    private Integer allOrderWriteTotle;//总核销数
    private BigDecimal allOrderAmount;//总营业额

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public BigDecimal getDuiPrice() {
        return duiPrice;
    }

    public void setDuiPrice(BigDecimal duiPrice) {
        this.duiPrice = duiPrice;
    }

    public BigDecimal getNoDuiPrice() {
        return noDuiPrice;
    }

    public void setNoDuiPrice(BigDecimal noDuiPrice) {
        this.noDuiPrice = noDuiPrice;
    }

    public BigDecimal getAllPrice() {
        return allPrice;
    }

    public void setAllPrice(BigDecimal allPrice) {
        this.allPrice = allPrice;
    }

    public BigDecimal getShopTurnover() {
        return shopTurnover;
    }

    public void setShopTurnover(BigDecimal shopTurnover) {
        this.shopTurnover = shopTurnover;
    }

    public Integer getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(Integer orderTotal) {
        this.orderTotal = orderTotal;
    }

    public Integer getOrderWriteOffTotal() {
        return orderWriteOffTotal;
    }

    public void setOrderWriteOffTotal(Integer orderWriteOffTotal) {
        this.orderWriteOffTotal = orderWriteOffTotal;
    }

    public Integer getAllOrderTotle() {
        return allOrderTotle;
    }

    public void setAllOrderTotle(Integer allOrderTotle) {
        this.allOrderTotle = allOrderTotle;
    }

    public Integer getAllOrderWriteTotle() {
        return allOrderWriteTotle;
    }

    public void setAllOrderWriteTotle(Integer allOrderWriteTotle) {
        this.allOrderWriteTotle = allOrderWriteTotle;
    }

    public BigDecimal getAllOrderAmount() {
        return allOrderAmount;
    }

    public void setAllOrderAmount(BigDecimal allOrderAmount) {
        this.allOrderAmount = allOrderAmount;
    }
}
