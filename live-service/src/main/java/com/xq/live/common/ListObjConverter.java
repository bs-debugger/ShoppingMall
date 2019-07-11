
package com.xq.live.common;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * List转换
 */
public class ListObjConverter {
    /**
     * 日志
     */
    private final static Logger logger = LoggerFactory.getLogger(ListObjConverter.class);

    /**
     * 转换
     *
     * @param source      被转换对象
     * @param clazz       转换目标类
     * @param forceMatchs
     * @param <T>
     * @return
     */
    public static <T> List<T> convert(List<?> source, Class<T> clazz, ObjConverter.ForceMatch... forceMatchs) {
        List<T> target = new ArrayList<T>();
        if (source != null) {
            try {
                for (Object sourceObj : source) {
                    target.add(ObjConverter.convert(sourceObj, clazz, forceMatchs));
                }
            } catch (Exception e) {
                logger.warn("对象集合转换异常");
            }
        }
        return target;
    }
}
