package com.xq.live.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class AccountController extends BaseController{
/*	@Resource(name = "accountService")
	private AccountService accountService;
	
	@RequestMapping("/dataList")
	public void dataList(CashDrawInVo inVo, HttpServletResponse response) throws Exception {
		ServiceResult<List<CashDrawOut>> result = accountService.queryWithPg(inVo);
		// 设置页面数据
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("total", result.getTotal());
		jsonMap.put("rows", result.getData());
		HtmlUtil.writerJson(response, jsonMap);
	}
	
	*//**
	 * 
	 * @param url
	 * @param classifyId
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping("/list")
	public ModelAndView list(CashDrawInVo inVo, HttpServletRequest request) throws Exception {
		Map<String, Object> context = new HashMap<String, Object>();
		return forword("account/account", context);
	}
	
	@RequestMapping("/payMoney")
	public void payMoney(CashDrawInVo inVo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysUser user = SessionUtils.getUser(request);
		inVo.setUpdateOperatorId(Long.valueOf(user.getId()));
		inVo.setUpdateOperatorName(user.getNickName());
		ServiceResult<Integer> result = accountService.updateCashDraw(inVo);
		// 设置页面数据
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("code", result.getCode());
		jsonMap.put("msg", result.getMsg());
		HtmlUtil.writerJson(response, jsonMap);
	}*/
}
