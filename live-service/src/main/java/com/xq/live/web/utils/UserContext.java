package com.xq.live.web.utils;

import com.xq.live.common.Constants;
import com.xq.live.model.User;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * com.xq.live.web.utils
 * 用户登录信息封装的工具类
 * @author zhangpeng32
 * Created on 2018/6/7 下午4:18
 * @Description:
 */
public class UserContext {
    /**
     * 获取当前线程绑定的用户登录对象
     *
     * @return
     */
    public static User getUserSession() {
        return (User) RequestContextHolder.getRequestAttributes().getAttribute(Constants.USER_SESSION_KEY,  RequestAttributes.SCOPE_REQUEST);
    }

    /**
     * 将用户登录对象绑定到当前线程
     *
     * @param user
     */
    public static void setUserSession(User user) {
        RequestContextHolder.getRequestAttributes().setAttribute(Constants.USER_SESSION_KEY, user, RequestAttributes.SCOPE_REQUEST);
    }

    /**
     * 将用户登录对象从当前线程销毁
     */
    public static void removeUserSession() {
        RequestContextHolder.getRequestAttributes().removeAttribute(Constants.USER_SESSION_KEY,RequestAttributes.SCOPE_REQUEST);
    }

    /**
     * 获取用户id
     * @return
     */
    public static Long getUserId(){
        User user = getUserSession();
        if(user != null){
            return user.getId();
        }
        return null;
    }

    /**
     * 获取登录用户的userName
     * @return
     */
    public static String getUserName(){
        User user = getUserSession();
        if(user != null){
            return user.getUserName();
        }
        return null;
    }
}
