package com.xq.live.service;

import java.util.List;

import com.xq.live.model.SysMenuBtn;

public interface SysMenuBtnService {
	/**
	 * <p> 根据userId查询系统菜单按钮
	 * <p> @param userid
	 * <p> @return
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */
	public List<SysMenuBtn> getMenuBtnByUser(Integer userid);
	
	public List<SysMenuBtn> queryByMenuid(Integer menuid);
	
	public List<SysMenuBtn> queryByMenuUrl(String url); 
	
	public int deleteByMenuid(Integer menuid);	
	
	public List<SysMenuBtn> queryByAll();
}
