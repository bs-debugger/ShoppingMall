package com.xq.live.service;

import java.util.List;

import com.xq.live.model.SysMenu;
import com.xq.live.vo.BaseVo;

public interface SysMenuService{
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
	 * 根据用户id查询父菜单
	 * @param roleId
	 * @return
	 */
	public List<SysMenu> getRootMenuByUser(Integer userId);
	
	/**
	 * 根据用户id查询子菜单
	 * @param roleId
	 * @return
	 */
	public List<SysMenu> getChildMenuByUser(Integer userId);
	/**
	 * <p> 查询记录总数
	 * <p> @param model
	 * <p> @return
	 * <p> @throws Exception
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */
	public int queryWithCount(BaseVo inVo)throws Exception;
	
	/**
	 * <p> 分页查询列表信息
	 * <p> @param inVo
	 * <p> @return
	 * <p> @throws Exception
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */
	public List<SysMenu> queryWithPg(BaseVo inVo) throws Exception;
	
	/**
	 * 根据权限id查询菜单
	 * @param roleId
	 * @return
	 */
	public List<SysMenu> getMenuByRoleId(Integer roleId);
	/**
	 * <p> 根据ID查询
	 * <p> @param id
	 * <p> @return
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */
	public SysMenu queryById(Integer id);
	/**
	 * <p> 删除操作
	 * <p> @param id
	 * <p> @return
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */
	public int delete(Integer[] id)throws Exception;
	/**
	 * <p> 新增
	 * <p> @param sysMenu
	 * <p> @return
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */
	public int add(SysMenu sysMenu)throws Exception;
	/**
	 * <p> 更新
	 * <p> @param menu
	 * <p> @return
	 * <p> @throws Exception
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */
	public int update(SysMenu menu) throws Exception;
}
