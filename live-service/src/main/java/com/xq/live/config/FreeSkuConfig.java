package com.xq.live.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 免费领取卷的配置
 * Created by lipeng on 2018/4/14.
 */
@Component
@ConfigurationProperties(prefix = "freesku")
public class FreeSkuConfig {
    /**
     * 免费领取的id
     */
    private Long skuId;

    /**
     * 免费领取的goodsSkuId
     */
    private Long goodsSkuId;

    /**
     * 免费领取的goodsSpuId
     */
    private Long goodsSpuId;

    /**
     * #免费领取的数量(默认为1)
     */
    private Integer skuNum;

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getGoodsSkuId() {
        return goodsSkuId;
    }

    public void setGoodsSkuId(Long goodsSkuId) {
        this.goodsSkuId = goodsSkuId;
    }

    public Long getGoodsSpuId() {
        return goodsSpuId;
    }

    public void setGoodsSpuId(Long goodsSpuId) {
        this.goodsSpuId = goodsSpuId;
    }

    public Integer getSkuNum() {
        return skuNum;
    }

    public void setSkuNum(Integer skuNum) {
        this.skuNum = skuNum;
    }
}
