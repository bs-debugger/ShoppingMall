package com.xq.live.aop.annotation;

import java.lang.annotation.*;

/**
 * @package: com.xq.live.common.aop.annotation
 * @description: 系统访问日志记录
 * @author: zhangpeng32
 * @date: 2018/3/18 15:50
 * @version: 1.0
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InvokeLog {
    /**
     * 操作类型,新增用户?删除用户 ?调用xx服务?使用接口?...
     *
     * @return
     */
    public String operation();


    /**
     * 业务类型
     * @return
     */
    public BizType type();

    /**
     * 日志级别
     *
     * @return
     */
    public LogType level() default LogType.INFO;
}
