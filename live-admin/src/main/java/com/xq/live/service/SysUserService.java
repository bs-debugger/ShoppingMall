package com.xq.live.service;

import java.util.List;

import com.xq.live.model.SysRoleRel;
import com.xq.live.model.SysUser;
import com.xq.live.vo.BaseVo;

public interface SysUserService {
	/**
	 * <p> 根据id删除用户信息
	 * <p> @param ids
	 * <p> @throws Exception
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */
	public int delete(Integer[] ids) throws Exception;
	
	/**
	 * <p> 检查登录
	 * <p> @param email
	 * <p> @param pwd
	 * <p> @return
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */
	public SysUser queryLogin(String email, String pwd);
	
	/**
	 * <p> 更新用户信息
	 * <p> @param user
	 * <p> @return
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */
	public int update(SysUser user);
	/**
	 * <p> 根据id查询用户
	 * <p> @param userId
	 * <p> @return
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */
	public SysUser queryById(Integer userId);
	
	/**
	 * <p> 分页查询
	 * <p> @param inVo
	 * <p> @return
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */
	public List<SysUser> queryWithPg(BaseVo inVo) throws Exception;
	
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
	 * 查询邮箱总数，检查是否存在
	 * @param email
	 * @return
	 */
	public int getUserCountByEmail(String email);
	/**
	 * <p> 新增
	 * <p> @param user
	 * <p> @return
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */
	public int add(SysUser user);
	/**
	 * <p> 更新不为空的字段
	 * <p> @param user
	 * <p> @return
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */
	public int updateBySelective(SysUser user);
	
	/**
	 * 查询用户权限
	 * @param userId
	 * @return
	 */
	public List<SysRoleRel> getUserRole(Integer userId);
	
	/**
	 * 添加用户权限
	 * @param userId
	 * @param roleIds
	 * @throws Exception
	 */
	public int addUserRole(Integer userId,Integer[] roleIds) throws Exception;	
}
