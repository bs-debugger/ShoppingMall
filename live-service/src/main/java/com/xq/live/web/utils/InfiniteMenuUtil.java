package com.xq.live.web.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by lipeng on 2018/11/1.
 */
public class InfiniteMenuUtil {
    @SuppressWarnings("unchecked")
    public <T> List<T> loadMenu(List<T> menus) {
        List<T> rootMenus = new ArrayList<T>();
        if (menus != null && menus.size() != 0) {
            List<Method> methodsList = Arrays.asList(menus.get(0).getClass().getDeclaredMethods());
            //这里可以自己定制啦,我定制的是以pid,id,children结尾的get方法为分别getPid，getId，getChildren.所以menu类(Category)的属性名要符合定制规范
            Method getId = null;
            Method getPid = null;
            Method getChildren = null;
            //get getMethod
            for (Method item : methodsList) {
                if ("get".equals(item.getName().toLowerCase().substring(0,3))&&item.getName().length()>=6&&"pid".equals(item.getName().toLowerCase().substring(item.getName().length() - 3, item.getName().length()))){
                    getPid = item;
                    continue;
                }
                if ("get".equals(item.getName().toLowerCase().substring(0,3))&&item.getName().length()>=5&&"id".equals(item.getName().toLowerCase().substring(item.getName().length() - 2, item.getName().length()))){
                    getId = item;
                    continue;
                }
                if ("get".equals(item.getName().toLowerCase().substring(0,3))&&item.getName().length()>=11&&"children".equals(item.getName().toLowerCase().substring(item.getName().length() - 8, item.getName().length()))){
                    getChildren = item;
                }
            }
            if (getId!=null&&getPid!=null&&getChildren!=null){
                //get menuMap
                Map<Long, T> menuMap = new HashMap<Long, T>();
                for (T menu : menus) {
                    Long id = null;
                    try {
                        id = (Long)getId.invoke(menu);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    menuMap.put(id,menu);
                }
                //add children
                for (T menu:menus) {
                    Long pid = null;
                    try {
                        pid = (Long)getPid.invoke(menu);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    if (pid==null||pid==0){
                        rootMenus.add(menu);
                    }else {
                        T t = menuMap.get(pid);
                        List<T> ts;
                        try {
                            ts = (List<T>) getChildren.invoke(t);
                            ts.add(menu);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return rootMenus;
    }
}
