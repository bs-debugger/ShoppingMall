package com.xq.live.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 砍菜人数配置
 * Created by ss on 2018/8/10.
 */
@Component
@ConfigurationProperties(prefix = "gold")
public class GoldConfig {
    /**
     * 金币小组人数6人
     */
    private Integer num;

    /**
     * 金币池的金币上限
     */
    private Integer max;

    /**
     * 金币池的金币下限
     */
    private Integer min;

    /**
     * 抢购螃蟹小组人数
     */
    private Integer qgnum;

    /**
     * 换购螃蟹需要人数
     */
    private Integer dhnum;

    /**
     * 旅游景点拉新人数
     */
    private Integer lynum;

    public Integer getDhnum() {
        return dhnum;
    }

    public void setDhnum(Integer dhnum) {
        this.dhnum = dhnum;
    }

    public Integer getQgnum() {
        return qgnum;
    }

    public void setQgnum(Integer qgnum) {
        this.qgnum = qgnum;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getLynum() {
        return lynum;
    }

    public void setLynum(Integer lynum) {
        this.lynum = lynum;
    }

}
