package com.xq.live.aop.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xq.live.aop.annotation.InvokeLog;
import com.xq.live.dao.AccessLogMapper;
import com.xq.live.dao.UserMapper;
import com.xq.live.model.AccessLog;
import com.xq.live.model.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @package: com.xq.live.common.aop.aspect
 * @description: TODO
 * @author: zhangpeng32
 * @date: 2018/3/18 16:37
 * @version: 1.0
 */
@Component
@Aspect
public class InvokeLogAspect {
    private static final Logger log = LoggerFactory.getLogger(InvokeLogAspect.class);

    @Autowired
    UserMapper userMapper;

    @Autowired
    AccessLogMapper accessLogMapper;

    /**
     * 切入点
     */
    @Pointcut("@annotation(com.xq.live.aop.annotation.InvokeLog) ")
    public void entryPoint() {
        // 无需内容
    }

    @Before("entryPoint()")
    public void before(JoinPoint joinPoint) {
        log.info("=====================开始执行前置通知==================");
        try {
            String targetName = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Object[] arguments = joinPoint.getArgs();
            Class<?> targetClass = Class.forName(targetName);
            Method[] methods = targetClass.getMethods();
            String operation = "";
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Class<?>[] clazzs = method.getParameterTypes();
                    if (clazzs.length == arguments.length) {
                        operation = method.getAnnotation(InvokeLog.class).operation();// 操作人
                        break;
                    }
                }
            }
            StringBuilder paramsBuf = new StringBuilder();
            for (Object arg : arguments) {
                paramsBuf.append(arg);
                paramsBuf.append("&");
            }

            // *========控制台输出=========*//
            log.info("[X用户]执行了[" + operation + "],类:" + targetName + ",方法名：" + methodName + ",参数:"
                    + paramsBuf.toString());
            log.info("=====================执行前置通知结束==================");
        } catch (Throwable e) {
            log.info("around " + joinPoint + " with exception : " + e.getMessage());
        }

    }

    /**
     * 环绕通知处理处理
     *
     * @param point
     * @throws Throwable
     */
    @Around("entryPoint()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 先执行业务,注意:业务这样写业务发生异常不会拦截日志。
        Object result = point.proceed();
        try {
            handleAround(point);// 处理日志
        } catch (Exception e) {
            log.error("日志记录异常", e);
        }
        return result;
    }

    /**
     * around日志记录
     *
     * @param point
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    public void handleAround(ProceedingJoinPoint point) throws Exception {
        Signature sig = point.getSignature();
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        MethodSignature msig = (MethodSignature) sig;
        Object target = point.getTarget();
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        // 方法名称
        String methodName = currentMethod.getName();
        // 获取注解对象
        InvokeLog aLog = currentMethod.getAnnotation(InvokeLog.class);
        // 类名
        String className = point.getTarget().getClass().getName();
        // 方法的参数
        Object[] params = point.getArgs();

        StringBuilder paramsBuf = new StringBuilder();
        for (Object arg : params) {
            paramsBuf.append(arg);
            paramsBuf.append("&");
        }
/*        Long refId = (Long)params[0];
        Long userId = (Long)params[1];
        User user = userMapper.selectByPrimaryKey(userId);

        *//**
         * 1、查询用户是否存在访问记录
         * 2、记录用户访问日志
         *//*
        AccessLog accessLog = new AccessLog();
        accessLog.setUserId(user.getId());
        accessLog.setUserName(user.getUserName());
        accessLog.setUserIp(user.getUserIp());
        accessLog.setSource(user.getSourceType());
        accessLog.setRefId(refId);
        accessLog.setBizType(aLog.type().getCode());
        int cnt = accessLogMapper.checkRecordExist(accessLog);
        if (cnt == 0) {
            accessLogMapper.insert(accessLog);
        }*/
        // 处理log。。。。
        log.info("[X用户]执行了[" + aLog.operation() + "],类:" + className + ",方法名：" + methodName + ",参数:"
                + paramsBuf.toString());

    }

    @After("entryPoint()")
    public void doAfter(JoinPoint joinPoint){
        log.info("=====================开始执行后置通知==================");
        try {
            String targetName = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Object[] arguments = joinPoint.getArgs();
            Class<?> targetClass = Class.forName(targetName);
            Method[] methods = targetClass.getMethods();
            String operation = "";
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Class<?>[] clazzs = method.getParameterTypes();
                    if (clazzs.length == arguments.length) {
                        operation = method.getAnnotation(InvokeLog.class).operation();// 操作人
                        break;
                    }
                }
            }
            StringBuilder paramsBuf = new StringBuilder();
            for (Object arg : arguments) {
                paramsBuf.append(arg);
                paramsBuf.append("&");
            }

            // *========控制台输出=========*//
            log.info("[X用户]执行了[" + operation + "],类:" + targetName + ",方法名：" + methodName + ",参数:"
                    + paramsBuf.toString());
            log.info("=====================执行后置通知结束==================");
        } catch (Throwable e) {
            log.info("after " + joinPoint + " with exception : " + e.getMessage());
        }
    }
}
