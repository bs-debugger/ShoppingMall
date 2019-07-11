package com.xq.live.common;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xuzhen
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2019/5/614:11
 */
public class PicUtil {
    /**
     * @param s
     * @return 获得图片
     */
    public static List<String> getImg(String s)
    {
        String regex;
        List<String> list = new ArrayList<String>();
        regex = "src=\"(.*?)\"";
        Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
        Matcher ma = pa.matcher(s);
        while (ma.find())
        {
            list.add(ma.group());
        }
        return list;
    }
}
