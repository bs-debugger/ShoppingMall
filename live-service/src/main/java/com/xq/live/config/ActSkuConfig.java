package com.xq.live.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 免费领取活动配置
 * Created by lipeng on 2018/4/14.
 */
@Component
@ConfigurationProperties(prefix = "actSku")
public class ActSkuConfig {
    /**
     * 免费领取的id(20元活动券)
     */
    private Long skuId;

    /**
     * 免费领取的id(7.7元活动券)
     */
    private Long skuIdOther;

    /**
     * 7.7元活动券对应的活动id
     */
    private Long actId;

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

    public Long getSkuIdOther() {
        return skuIdOther;
    }

    public void setSkuIdOther(Long skuIdOther) {
        this.skuIdOther = skuIdOther;
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public Integer getSkuNum() {
        return skuNum;
    }

    public void setSkuNum(Integer skuNum) {
        this.skuNum = skuNum;
    }
}
