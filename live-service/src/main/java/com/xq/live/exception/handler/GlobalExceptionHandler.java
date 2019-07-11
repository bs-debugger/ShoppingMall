package com.xq.live.exception.handler;

/**
 * 全局异常处理
 *
 * @author zhangpeng32
 * @create 2018-01-17 19:38
 */

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.exception.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 * @Author: wangxc
 * @GitHub: https://github.com/vector4wang
 * @CSDN: http://blog.csdn.net/qqhjqs?viewmode=contents
 * @BLOG: http://vector4wang.tk
 * @wxid: BMHJQS
 * 参考:http://neverflyaway.iteye.com/blog/2301571  http://blog.csdn.net/u010935920/article/details/71024018
 */
@ControllerAdvice(annotations = {RestController.class})
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger("GlobalExceptionHandler");

    /**
     * 系统异常处理，比如：404,500
     * @param req
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public BaseResp<?> defaultErrorHandler(HttpServletRequest req, Exception e) {
        logger.error("", e);
        BaseResp baseResp = new BaseResp();
        baseResp.setMessage(e.getMessage());
        if (e instanceof org.springframework.web.servlet.NoHandlerFoundException) {
            baseResp.setCode(ResultStatus.http_status_not_found.getErrorCode());
        } else {
            baseResp.setCode(ResultStatus.http_status_internal_server_error.getErrorCode());
        }
        baseResp.setData("");
        return baseResp;
    }

    /**
     * 自定义异常捕获并返回结果
     * @author feitao <yyimba@qq.com> 2019/5/6 9:13
     */
    @ExceptionHandler(value = AppException.class)
    @ResponseBody
    public BaseResp<?> defaultErrorHandler(AppException e) {
        logger.error(e.getMessage(), e);
        BaseResp baseResp = new BaseResp();
        baseResp.setCode(e.getResultStatus().getErrorCode());
        baseResp.setMessage(e.getResultStatus().getErrorMsg());
        baseResp.setData("");
        return baseResp;
    }

}
