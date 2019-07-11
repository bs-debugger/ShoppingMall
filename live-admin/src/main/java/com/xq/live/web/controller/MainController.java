package com.xq.live.web.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xq.live.annotation.Auth;
import com.xq.live.model.*;
import com.xq.live.service.SysMenuService;
import com.xq.live.service.SysUserService;
import com.xq.live.utils.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.xq.live.model.SiteMain;
import com.xq.live.model.SysMenu;
import com.xq.live.model.SysMenuBtn;
import com.xq.live.model.SysUser;
import com.xq.live.model.TreeNode;
import com.xq.live.service.SysMenuBtnService;
import com.xq.live.utils.DateUtil;
import com.xq.live.utils.HtmlUtil;
import com.xq.live.utils.MethodUtil;
import com.xq.live.utils.SessionUtils;
import com.xq.live.utils.TreeUtil;
import com.xq.live.utils.URLUtils;


@Controller
public class MainController extends BaseController{
	private final static Logger log= Logger.getLogger(MainController.class);
	
	@Autowired(required=false) 
	private SysUserService sysUserService;
	
	@Autowired(required=false) 
	private SysMenuService sysMenuService;
	
	@Autowired(required=false) 
	private SysMenuBtnService sysMenuBtnService; 
	
	/**
	 * <p> 登录页面
	 * <p> @param request
	 * <p> @param response
	 * <p> @return
	 * <p> @throws Exception
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */
	@Auth(verifyLogin=false,verifyURL=false)
	@RequestMapping("/login")
	public String  index(HttpServletRequest request,HttpServletResponse response) throws Exception{
		return "/login";
	}
	/**
	 * <p> 登录
	 * <p> @param email
	 * <p> @param pwd
	 * <p> @param verifyCode
	 * <p> @param request
	 * <p> @param response
	 * <p> @throws Exception
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */
	@Auth(verifyLogin=false,verifyURL=false)
	@RequestMapping("/toLogin")
	public void  toLogin(String email,String pwd,String verifyCode,HttpServletRequest request,HttpServletResponse response) throws Exception{
		/**暂时无需验证码功能
		String vcode  = SessionUtils.getValidateCode(request);
		SessionUtils.removeValidateCode(request);//清除验证码，确保验证码只能用一次
		if(StringUtils.isBlank(verifyCode)){
			sendFailureMessage(response, "验证码不能为空.");
			return;
		}
		//判断验证码是否正确
		if(!verifyCode.toLowerCase().equals(vcode)){
			sendFailureMessage(response, "验证码输入错误.");
			return;
		}
		*/
		if(StringUtils.isBlank(email)){
			sendFailureMessage(response, "账号不能为空.");
			return;
		}
		if(StringUtils.isBlank(pwd)){
			sendFailureMessage(response, "密码不能为空.");
			return;
		}
		String msg = "用户登录日志:";
		SysUser user = sysUserService.queryLogin(email, MethodUtil.MD5(pwd));
		if(user == null){
			//记录错误登录日志
			log.debug(msg+"["+email+"]"+"账号或者密码输入错误.");
			sendFailureMessage(response, "账号或者密码输入错误.");
			return;
		}
		if(BaseModel.STATE.DISABLE.key == user.getState()){
			sendFailureMessage(response, "账号已被禁用.");
			return;
		}
		//登录次数加1 修改登录时间
		int loginCount = 0;
		if(user.getLoginCount() != null){
			loginCount = user.getLoginCount();
		}
		user.setLoginCount(loginCount+1);
		user.setLoginTime(DateUtil.getDateByString(""));
		sysUserService.update(user);
		//设置User到Session
		SessionUtils.setUser(request,user);
		//记录成功登录日志
		log.debug(msg+"["+email+"]"+"登录成功");
		sendSuccessMessage(response, "登录成功.");
	}

	/**
	 * ilook 首页
	 * @param url
	 * @param classifyId
	 * @return
	 */
	@Auth(verifyURL=false)
	@RequestMapping("/main") 
	public ModelAndView  main(SiteMain siteMain, HttpServletRequest request){
		Map<String,Object>  context = new HashMap<String,Object>();
		SysUser user = SessionUtils.getUser(request);
		List<SysMenu> rootMenus = null;
		List<SysMenu> childMenus = null;
		List<SysMenuBtn> childBtns = null;
		//超级管理员
		if(user != null && Constant.SuperAdmin.YES.key ==  user.getSuperAdmin()){
			rootMenus = sysMenuService.getRootMenu(null);// 查询所有根节点
			childMenus = sysMenuService.getChildMenu();//查询所有子节点
		}else{
			rootMenus = sysMenuService.getRootMenuByUser(user.getId() );//根节点
			childMenus = sysMenuService.getChildMenuByUser(user.getId());//子节点
			childBtns = sysMenuBtnService.getMenuBtnByUser(user.getId());//按钮操作
			buildData(childMenus,childBtns,request); //构建必要的数据
		}
		context.put("user", user);
		context.put("menuList", treeMenu(rootMenus,childMenus));
		return new ModelAndView("/main/main",context); 
	}	
	
	/**
	 * 构建树形数据
	 * @return
	 */
	private void buildData(List<SysMenu> childMenus,List<SysMenuBtn> childBtns,HttpServletRequest request){
		//能够访问的url列表
		List<String> accessUrls  = new ArrayList<String>();
		//菜单对应的按钮
		Map<String,List> menuBtnMap = new HashMap<String,List>(); 
		for(SysMenu menu: childMenus){
			//判断URL是否为空
			if(StringUtils.isNotBlank(menu.getUrl())){
				List<String> btnTypes = new ArrayList<String>();
				for(SysMenuBtn btn  : childBtns){
					if(menu.getId().equals(btn.getMenuid())){
						btnTypes.add(btn.getBtnType());
						URLUtils.getBtnAccessUrls(menu.getUrl(), btn.getActionUrls(),accessUrls);
					}
				}
				menuBtnMap.put(menu.getUrl(), btnTypes);
				URLUtils.getBtnAccessUrls(menu.getUrl(), menu.getActions(),accessUrls);
				accessUrls.add(menu.getUrl());
			}
		}
		SessionUtils.setAccessUrl(request, accessUrls);//设置可访问的URL
		SessionUtils.setMemuBtnMap(request, menuBtnMap); //设置可用的按钮
	}
	
	/**
	 * 构建树形数据
	 * @return
	 */
	private List<TreeNode> treeMenu(List<SysMenu> rootMenus,List<SysMenu> childMenus){
		TreeUtil util = new TreeUtil(rootMenus,childMenus);
		return util.getTreeNode();
	}
	
	/**
	 * 获取Action下的按钮
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Auth(verifyURL=false)
	@RequestMapping("/getActionBtn")
	public void  getActionBtn(String url,HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		List<String> actionTypes = new ArrayList<String>();
		//判断是否超级管理员
		if(SessionUtils.isAdmin(request)){
			result.put("allType", true);
		}else{
			String menuUrl = URLUtils.getReqUri(url);
			menuUrl = StringUtils.remove(menuUrl,request.getContextPath());
			//获取权限按钮
			actionTypes = SessionUtils.getMemuBtnListVal(request, StringUtils.trim(menuUrl));
			result.put("allType", false);
			result.put("types", actionTypes);
		}
		result.put(SUCCESS, true);
		HtmlUtil.writerJson(response, result);
	}	
	
	
	/**
	 * 退出登录
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Auth(verifyLogin=false,verifyURL=false)
	@RequestMapping("/logout")
	public void  logout(HttpServletRequest request,HttpServletResponse response) throws Exception{
		SessionUtils.removeUser(request);
		response.sendRedirect("/login.shtml");
	}	
	
	/**
	 * 修改密码
	 * @param url
	 * @param classifyId
	 * @return
	 * @throws Exception 
	 */
	@Auth(verifyURL=false)
	@RequestMapping("/modifyPwd")
	public void modifyPwd(String oldPwd,String newPwd,HttpServletRequest request,HttpServletResponse response) throws Exception{
		SysUser user = SessionUtils.getUser(request);
		if(user == null){
			sendFailureMessage(response, "对不起,登录超时.");
			return;
		}
		SysUser bean  = sysUserService.queryById(user.getId());
		if(bean.getId() == null || BaseModel.DELETED.YES.key == bean.getDeleted()){
			sendFailureMessage(response, "对不起,用户不存在.");
			return;
		}
		if(StringUtils.isBlank(newPwd)){
			sendFailureMessage(response, "密码不能为空.");
			return;
		}
		//不是超级管理员，匹配旧密码
		if(!MethodUtil.ecompareMD5(oldPwd,bean.getPwd())){
			sendFailureMessage(response, "旧密码输入不匹配.");
			return;
		}
		bean.setPwd(MethodUtil.MD5(newPwd));
		sysUserService.update(bean);
		sendSuccessMessage(response, "Save success.");
	}	
	
	public static void main(String[] args) {
		String reqUrl = "http://localhost/sysUser/list.shtml";
		try {
			URL url  = new URL(reqUrl);
			System.err.println(url.getPath());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}	
}
