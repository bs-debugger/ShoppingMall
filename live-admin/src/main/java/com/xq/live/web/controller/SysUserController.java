package com.xq.live.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xq.live.utils.SessionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.xq.live.exception.ServiceException;
import com.xq.live.model.BaseModel.DELETED;
import com.xq.live.model.BaseModel.STATE;
import com.xq.live.model.SysRole;
import com.xq.live.model.SysRoleRel;
import com.xq.live.model.SysUser;
import com.xq.live.service.SysRoleService;
import com.xq.live.service.SysUserService;
import com.xq.live.utils.HtmlUtil;
import com.xq.live.utils.MethodUtil;
import com.xq.live.vo.SysUserInVo;

@Controller
@RequestMapping("/sysUser") 
public class SysUserController extends BaseController {

	private final static Logger log= Logger.getLogger(SysUserController.class);
	
	@Autowired
	private SysUserService sysUserService; 
	@Autowired
	private SysRoleService sysRoleService; 
	/**
	 * ilook 首页
	 * @param url
	 * @param classifyId
	 * @return
	 */
	@RequestMapping("/list") 
	public ModelAndView  list(SysUserInVo inVo,HttpServletRequest request) throws Exception{
		Map<String,Object>  context = new HashMap<String, Object>();
		List<SysUser> dataList = sysUserService.queryWithPg(inVo);
		//设置页面数据
		context.put("dataList", dataList);
		return forword("sys/sysUser",context); 
	}
	
	
	/**
	 * json 列表页面
	 * @param url
	 * @param classifyId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/dataList") 
	public void  dataList(SysUserInVo inVo,HttpServletResponse response) throws Exception{
		List<SysUser> dataList = sysUserService.queryWithPg(inVo);
		for(SysUser user: dataList){
			List<SysRole> list = sysRoleService.queryByUserid(user.getId());
			user.setRoleStr(rolesToStr(list));
		}
		//设置页面数据
		Map<String,Object> jsonMap = new HashMap<String,Object>();
		jsonMap.put("total",inVo.getPager().getRowCount());
		jsonMap.put("rows", dataList);
		HtmlUtil.writerJson(response, jsonMap);
	}
	
	/**
	 * 角色列表转成字符串
	 * @param list
	 * @return
	 */
	private String rolesToStr(List<SysRole> list){
		if(list == null || list.isEmpty()){
			return null;
		}
		StringBuffer str = new StringBuffer();
		for(int i=0;i<list.size();i++){
			SysRole role = list.get(i);
			str.append(role.getRoleName());
			if((i+1) < list.size()){
				str.append(",");
			}
		}
		return str.toString();
	}

	/**
	 * 添加或修改数据
	 * @param bean
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/save")
	public void save(SysUser bean,HttpServletResponse response) throws Exception{
		Map<String,Object>  context = new HashMap<String,Object>();
		int count = sysUserService.getUserCountByEmail(bean.getEmail());
		if(bean.getId() == null){
			if(count > 0){
				throw new ServiceException("用户已存在.");
			}
			bean.setDeleted(DELETED.NO.key);
			sysUserService.add(bean);
		}else{
			if(count > 1){
				throw new ServiceException("用户已存在.");
			}
			sysUserService.updateBySelective(bean);
		}
		sendSuccessMessage(response, "保存成功~");
	}
	
	@RequestMapping("/getId")
	public void getId(Integer id,HttpServletResponse response) throws Exception{
		Map<String,Object>  context = new HashMap<String, Object>();
		SysUser bean  = sysUserService.queryById(id);
		if(bean  == null){
			sendFailureMessage(response, "没有找到对应的记录!");
			return;
		}
		context.put(SUCCESS, true);
		context.put("data", bean);
		HtmlUtil.writerJson(response, context);
	}
	
	@RequestMapping("/delete")
	public void delete(Integer[] ids,HttpServletResponse response) throws Exception{
		sysUserService.delete(ids);
		sendSuccessMessage(response, "删除成功");
	}


	/**
	 * 更改密码
	 * @param id
	 * @param oldPwd
	 * @param newPwd
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/updatePwd")
	public void updatePwd(Integer id,String oldPwd,String newPwd,HttpServletRequest request,HttpServletResponse response) throws Exception{
		boolean isAdmin = SessionUtils.isAdmin(request); //是否超级管理员
		SysUser bean  = sysUserService.queryById(id);
		if(bean.getId() == null || DELETED.YES.key == bean.getDeleted()){
			sendFailureMessage(response, "Sorry ,User is not exists.");
			return;
		}
		if(StringUtils.isBlank(newPwd)){
			sendFailureMessage(response, "Password is required.");
			return;
		}
		//不是超级管理员，匹配旧密码
		if(!isAdmin && !MethodUtil.ecompareMD5(oldPwd,bean.getPwd())){
			sendFailureMessage(response, "Wrong old password.");
			return;
		}
		bean.setPwd(MethodUtil.MD5(newPwd));
		sysUserService.update(bean);
		sendSuccessMessage(response, "保存成功~");
	}
	

	
	/**
	 * 用户授权页面
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/userRole") 
	public ModelAndView  userRole(HttpServletRequest request) throws Exception{
		Map<String,Object>  context = new HashMap<String, Object>();
		return forword("/sys/sysUserRole", context);
	}
	
	/**
	 * 用户授权列表
	 * @param model
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/userList") 
	public void  userList(SysUserInVo inVo,HttpServletResponse response) throws Exception{
		inVo.setState(STATE.ENABLE.key);
		dataList(inVo, response);
	}

	/**
	 * 查询用户信息
	 * @param id
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/getUser") 
	public void getUser(Integer id,HttpServletResponse response)  throws Exception{
		Map<String,Object>  context = new HashMap<String, Object>();
		SysUser bean  = sysUserService.queryById(id);
		if(bean  == null){
			sendFailureMessage(response, "没有找到对应的记录!");
			return;
		}
		Integer[] roleIds = null;
		List<SysRoleRel>  roles  =sysUserService.getUserRole(bean.getId());
		if(roles != null){
			roleIds = new Integer[roles.size()];
			int i = 0;
			for(SysRoleRel rel : roles ){
				roleIds[i] = rel.getRoleId();
				i++;
			}
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", bean.getId());
		data.put("email", bean.getEmail());
		data.put("roleIds", roleIds);
		context.put(SUCCESS, true);
		context.put("data", data);
		HtmlUtil.writerJson(response, context);
		
	}
	
	/**
	 * 添加或修改数据
	 * @param url
	 * @param classifyId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/addUserRole")
	public void addUserRole(Integer id,Integer roleIds[],HttpServletResponse response) throws Exception{
		sysUserService.addUserRole(id, roleIds);
		sendSuccessMessage(response, "保存成功");
	}

}
