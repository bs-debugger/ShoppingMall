package com.xq.live.service;

import java.util.List;

import com.xq.live.model.SysRole;
import com.xq.live.vo.BaseVo;

public interface SysRoleService {
	/**
	 * 添加角色&菜单关系
	 */
	public int addRoleMenuRel(Integer roleId, Integer[] menuIds) throws Exception;

	/**
	 * 添加角色&菜单关系
	 */
	public int addRoleBtnRel(Integer roleId, Integer[] btnIds) throws Exception;

	/**
	 * 添加
	 * 
	 * @param role
	 * @param menuIds
	 * @throws Exception
	 */
	public int add(SysRole role, Integer[] menuIds, Integer[] btnIds) throws Exception;

	/**
	 * 删除
	 * 
	 * @param id
	 * @throws Exception
	 */
	public int delete(Integer[] ids) throws Exception;

	/**
	 * 修改
	 * 
	 * @param role
	 * @param menuIds
	 * @throws Exception
	 */
	public int update(SysRole role, Integer[] menuIds, Integer[] btnIds) throws Exception;

	/**
	 * 查询全部有效的权限
	 * 
	 * @return
	 */
	public List<SysRole> queryAllList();

	/**
	 * 查询全部有效的权限
	 * 
	 * @return
	 */
	public List<SysRole> queryByUserid(Integer userid);
	
	public SysRole queryById(Integer id);

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
	public List<SysRole> queryWithPg(BaseVo inVo) throws Exception;
}
