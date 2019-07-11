package com.xq.live.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 螃蟹实际总量划分配置
 * Created by lipeng on 2018/9/19.
 */
@Component
@ConfigurationProperties(prefix = "costWeight")
public class CostWeightConfig {
    /**
     * 划分区间
     */
    private Double[] area;
    /**
     * 划分重量
     */
    private Double[] weight;

    private BigDecimal deliveryFee;

    public Double[] getArea() {
        return area;
    }

    public void setArea(Double[] area) {
        this.area = area;
    }

    public Double[] getWeight() {
        return weight;
    }

    public void setWeight(Double[] weight) {
        this.weight = weight;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }
}
