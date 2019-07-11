package com.xq.live.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 免费领取折扣券配置
 * Created by lipeng on 2018/4/14.
 */
@Component
@ConfigurationProperties(prefix = "agio")
public class AgioSkuConfig {
    /**
     * 免费领取的id
     */
    private Long skuId;

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

    public Integer getSkuNum() {
        return skuNum;
    }

    public void setSkuNum(Integer skuNum) {
        this.skuNum = skuNum;
    }
}
