package com.xq.live.dao;

import java.util.List;

import com.xq.live.model.SysMenu;
import org.springframework.stereotype.Repository;

@Repository("sysMenuDao")
public interface SysMenuDao extends BaseDao<SysMenu>{
	/**
	 * <p> 获取顶级菜单
	 * <p> @param menuId
	 * <p> @return
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */	
	public List<SysMenu> getRootMenu(Integer menuId);
	/**
	 * <p> 获取子菜单
	 * <p> @return
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */	
	public List<SysMenu> getChildMenu();
	
	/**
	 * 根据用户id查询父菜单菜单
	 * @param userId
	 * @return
	 */
	public List<SysMenu> getRootMenuByUser(Integer userId);	
	
	/**
	 * 根据用户id查询子菜单菜单
	 * @param userId
	 * @return
	 */
	public List<SysMenu> getChildMenuByUser(Integer userId);	
	
	/**
	 * 根据权限id查询菜单
	 * @param roleId
	 * @return
	 */
	public List<SysMenu> getMenuByRoleId(Integer roleId);
}
