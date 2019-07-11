
package com.xq.live.common;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Obj转换
 */
public class ObjConverter {

    /**
     * 日志
     */
    private final static Logger logger = LoggerFactory.getLogger(ObjConverter.class);

    /**
     * 转换
     *
     * @param source  被转换对象
     * @param clazz   转换目标类
     * @param fMatchs
     * @param <T>
     * @return
     */
    public static <T> T convert(Object source, Class<T> clazz, ForceMatch... fMatchs) {
        T target = null;
        if (source != null) {
            try {
                target = clazz.newInstance();
                convert(source, target, false, fMatchs);
            } catch (Exception e) {
                logger.warn("对象转换异常");
            }
        }
        return target;
    }

    /**
     * @param source
     * @param fMatchs
     * @param <T>
     * @return
     */
    public static <T> T convertAimNotHaveValueFiedFlag(Object source, T aim, ForceMatch... fMatchs) {
        if (aim == null) {
            return null;
        }
        if (source != null) {
            try {
                convert(source, aim, true, fMatchs);
            } catch (Exception e) {
                logger.warn("对象转换异常");
            }
        }
        return aim;
    }


    /**
     * 转换
     *
     * @param source  被转换对象
     * @param target  转换目标
     * @param fMatchs
     * @param <T>
     */
    public static <T> void convert(Object source, T target, boolean notConvertHaveValueFiedFlag , ForceMatch... fMatchs) {

        if (source == null || target == null) {
            return;
        }
        try {
            Method[] methods = target.getClass().getMethods();
            for (Method tarMethod : methods) {
                String tarMethodName = tarMethod.getName();
                Method souMethod = getSourceMethod(tarMethodName, source.getClass(), fMatchs);
                if (souMethod != null) {
                    try {
                        //值不为null
                        if (notConvertHaveValueFiedFlag) {
                            Object oldValue = souMethod.invoke(target);
                            if (oldValue == null || ((oldValue instanceof String) && (oldValue).equals(""))) {
                                Object souValue = souMethod.invoke(source);
                                if (souValue != null) {
                                    tarMethod.invoke(target, souMethod.invoke(source));
                                }
                            }
                        } else {
                            tarMethod.invoke(target, souMethod.invoke(source));
                        }
                    } catch (Exception e) {
                        logger.warn("对象属性copy异常");
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("对象转换异常");
        }
    }

    /**
     * 获取被转换对象方法
     *
     * @param tarMethodName
     * @param souClazz
     * @param fMatchs
     * @return
     * @throws Exception
     */
    private static Method getSourceMethod(String tarMethodName, Class<?> souClazz, ForceMatch... fMatchs)
            throws Exception {
        Method souMethod = null;
        if (tarMethodName.startsWith("set")) {
            try {
                souMethod = souClazz.getMethod(tarMethodName.replaceFirst("s", "g"));
            } catch (NoSuchMethodException e) {
                // 强制匹配
                String tarFieldName = getFieldName(tarMethodName);
                for (ForceMatch fMatch : fMatchs) {
                    String souMethodName = null;
                    if (tarFieldName.equals(fMatch.getFieldx())) {
                        souMethodName = getGetMethodName(fMatch.getFieldy());
                    } else if (tarFieldName.equals(fMatch.getFieldy())) {
                        souMethodName = getGetMethodName(fMatch.getFieldx());
                    }
                    if (souMethodName != null) {
                        try {
                            souMethod = souClazz.getMethod(souMethodName);
                        } catch (NoSuchMethodException e1) {
                            logger.warn("对象转换失败，强制匹配参数异常");
                            throw e;
                        }
                        break;
                    }
                }
            }
        }
        return souMethod;
    }

    /**
     * 根据字段名字获取get方法名
     *
     * @param fieldName
     * @return
     */
    private static String getGetMethodName(String fieldName) {
        fieldName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        return "get" + fieldName;
    }

    /**
     * 根据方法名返回字段名
     *
     * @param methodName
     * @return
     */
    private static String getFieldName(String methodName) {
        String fieldName = methodName.substring(3, methodName.length());
        return Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);
    }

    public static class ForceMatch {

        private String fieldx;
        private String fieldy;

        public ForceMatch(String fieldx, String fieldy) {
            this.fieldx = fieldx;
            this.fieldy = fieldy;
        }

        public String getFieldx() {
            return fieldx;
        }

        public void setFieldx(String fieldx) {
            this.fieldx = fieldx;
        }

        public String getFieldy() {
            return fieldy;
        }

        public void setFieldy(String fieldy) {
            this.fieldy = fieldy;
        }

    }
}
