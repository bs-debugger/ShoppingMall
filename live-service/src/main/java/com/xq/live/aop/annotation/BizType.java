package com.xq.live.aop.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @package: com.xq.live.common.aop.annotation
 * @description: 日志业务类型
 * @author: zhangpeng32
 * @date: 2018/3/18 16:27
 * @version: 1.0
 */
public enum BizType {
    SHOP_VIEW(1, "浏览商家"),
    ACT_VIEW(2, "浏览活动");

    private static final Logger LOGGER = LoggerFactory.getLogger(BizType.class);
    private int code;
    private String description;

    BizType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
