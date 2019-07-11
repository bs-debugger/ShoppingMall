package com.xq.live.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.xq.live.model.SiteColumn;
import com.xq.live.service.SiteColumnService;
import com.xq.live.vo.SiteColumnInVo;

@Controller
@RequestMapping("/sys/siteColumn")
public class SiteColumnController extends BaseController {
	private final static Logger log = Logger.getLogger(SiteColumnController.class);
	@Autowired
	private SiteColumnService siteColumnService;

	/**
	 * ilook 首页
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public ModelAndView list(SiteColumnInVo model, HttpServletRequest request) throws Exception {
		Map<String, Object> context = new HashMap<String, Object>();
		List<SiteColumn> dataList = siteColumnService.queryWithPg(model);
		// 设置页面数据
		context.put("dataList", dataList);
		return forword("siteColumn/list", context);
	}
}
