package com.xq.live.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xq.live.model.SysMenu;
import com.xq.live.service.SysRoleRelService;
import com.xq.live.service.SysRoleService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.xq.live.model.SysRole;
import com.xq.live.model.SysRoleRel;
import com.xq.live.model.SysRoleRel.RelType;
import com.xq.live.service.SysMenuService;
import com.xq.live.utils.HtmlUtil;
import com.xq.live.vo.SysRoleInVo;

@Controller
@RequestMapping("/sysRole") 
public class SysRoleController extends BaseController {
	private final static Logger log= Logger.getLogger(SysRoleController.class);
	@Autowired(required=false) 
	private SysRoleService sysRoleService;
	@Autowired(required=false) 
	private SysMenuService sysMenuService; 
	@Autowired(required=false) 
	private SysRoleRelService sysRoleRelService;
	/**
	 * 
	 * @param url
	 * @param classifyId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/role")
	public ModelAndView  list(SysRoleInVo inVo,HttpServletRequest request) throws Exception{
		Map<String,Object>  context = new HashMap<String, Object>();
		return forword("sys/sysRole",context); 
	}
	
	/**
	 * ilook 首页
	 * @param url
	 * @param classifyId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/dataList") 
	public void  datalist(SysRoleInVo inVo,HttpServletResponse response) throws Exception{
		List<SysRole> dataList = sysRoleService.queryWithPg(inVo);
		//设置页面数据
		Map<String,Object> jsonMap = new HashMap<String,Object>();
		jsonMap.put("total",inVo.getPager().getRowCount());
		jsonMap.put("rows", dataList);
		HtmlUtil.writerJson(response, jsonMap);
	}
	
	/**
	 * 添加或修改数据
	 * @param url
	 * @param classifyId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/save")
	public void save(SysRole bean,Integer[] menuIds,Integer[] btnIds,HttpServletResponse response) throws Exception{
		if(bean.getId() == null){
			sysRoleService.add(bean,menuIds,btnIds);
		}else{
			sysRoleService.update(bean,menuIds,btnIds);
		}
		sendSuccessMessage(response, "保存成功~");
	}
	
	@RequestMapping("/getId")
	public void getId(Integer id,HttpServletResponse response) throws Exception{
		Map<String, Object> context = new HashMap<String, Object>();
		SysRole bean  = sysRoleService.queryById(id);
		if(bean  == null){
			sendFailureMessage(response, "没有找到对应的记录!");
			return;
		}
		//获取权限关联的菜单
		Integer[] menuIds = null;
		List<SysMenu> menuList =  sysMenuService.getMenuByRoleId(id);
		if(menuList != null){
			menuIds = new Integer[menuList.size()];
			int i = 0;
			for(SysMenu item : menuList){
				menuIds[i] = item.getId();
				i++;
			}
		}
		//获取权限下关联的按钮
		Integer[] btnIds = null;
		List<SysRoleRel>  btnList =sysRoleRelService.queryByRoleId(id, RelType.BTN.key);
		if(btnList != null){
			btnIds = new Integer[btnList.size()];
			int i = 0;
			for(SysRoleRel item : btnList){
				btnIds[i] = item.getObjId();
				i++;
			}
		}
		//将对象转成Map
		Map<String,Object> data = BeanUtils.describe(bean);
		data.put("menuIds", menuIds);
		data.put("btnIds", btnIds);
		context.put(SUCCESS, true);
		context.put("data", data);
		HtmlUtil.writerJson(response, context);
	}
	
	@RequestMapping("/delete")
	public void delete(Integer[] id,HttpServletResponse response) throws Exception{
		sysRoleService.delete(id);
		sendSuccessMessage(response, "删除成功");
	}
	
	@RequestMapping("/loadRoleList")
	public void loadRoleList(HttpServletResponse response) throws Exception{
		List<SysRole>  roloList = sysRoleService.queryAllList();
		HtmlUtil.writerJson(response, roloList);
	}
	
}
