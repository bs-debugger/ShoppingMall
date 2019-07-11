package com.xq.live.web.exception;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * 权限异常处理
 *
 * @author lipeng
 * Created on 2018/7/18 20:10.
 */
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler,Serializable {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException{
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        JSONObject result = new JSONObject();
        JSONObject header = new JSONObject();
        if (e instanceof AuthorizationServiceException) {
            header.put("errorcode", "8002");
            header.put("errorinfo", "8002失败");
            result.put("header", header);
        } else {
            header.put("errorcode", "8001");
            header.put("errorinfo", "8001失败");
            result.put("header", header);
        }
        response.getWriter().write(JSONObject.toJSONString(result));
    }

}
