package com.xq.live.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.xq.live.model.SiteScore;
import com.xq.live.service.SiteScoreService;
import com.xq.live.vo.SiteScoreInVo;

@Controller
@RequestMapping("/sys/siteScore")
public class SiteScoreController extends BaseController {

	@Autowired
	private SiteScoreService siteScoreService;

	/**
	 * ilook 首页
	 * @param url
	 * @param classifyId
	 * @return
	 */
	@RequestMapping("/list")
	public ModelAndView list(SiteScoreInVo model, HttpServletRequest request) throws Exception {
		Map<String, Object> context = new HashMap<String, Object>();
		List<SiteScore> dataList = siteScoreService.queryWithPg(model);
		// 设置页面数据
		context.put("dataList", dataList);
		return forword("siteScore/list", context);
	}
}
