package com.xq.live.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import org.codehaus.jackson.map.util.JSONPObject;

/**
 * <br>
 * <b>功能：</b>详细的功能描述<br>
 * <b>作者：</b>Zhang Peng<br>
 * <b>日期：</b> Dec 14, 2014 <br>
 * <b>更新者：</b><br>
 * <b>日期：</b> <br>
 * <b>更新内容：</b><br>
 */
public class HtmlUtil {

	/**
	 * 
	 * <br>
	 * <b>功能：</b>输出json格式<br>
	 * <b>作者：</b>Zhang Peng<br>
	 * <b>日期：</b> Dec 14, 2014 <br>
	 * 
	 * @param response
	 * @param jsonStr
	 * @throws Exception
	 */
	public static void writerJson(HttpServletResponse response, String jsonStr) {
		writer(response, jsonStr);
	}

	public static void writerJson(HttpServletResponse response, Object object) {
		response.setContentType("application/json");
		writer(response, JSONObject.toJSONString(object));
	}

	public static void main(String[] args) {
		Map map =new HashMap();
		map.put("id","123456");
		map.put("name", "myname");
		System.out.println(JSONObject.toJSONString(map));
	}

	/**
	 * 
	 * <br>
	 * <b>功能：</b>输出HTML代码<br>
	 * <b>作者：</b>Zhang Peng<br>
	 * <b>日期：</b> Dec 14, 2014 <br>
	 * 
	 * @param response
	 * @param htmlStr
	 * @throws Exception
	 */
	public static void writerHtml(HttpServletResponse response, String htmlStr) {
		writer(response, htmlStr);
	}

	private static void writer(HttpServletResponse response, String str) {
		try {
//			StringBuffer result = new StringBuffer();
			// 设置页面不缓存
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = null;
			out = response.getWriter();
			out.print(str);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
