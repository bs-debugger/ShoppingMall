package com.xq.live.dao;

import com.xq.live.model.SysUser;
import org.springframework.stereotype.Repository;

import com.xq.live.vo.SysUserInVo;

@Repository
public interface SysUserDao extends BaseDao<SysUser>{

	/**
	 * 检查登录
	 * @param inVo
	 * @return
	 */
	public SysUser queryLogin(SysUserInVo inVo);
	
	/**
	 * <p> 根据id查询user
	 * <p> @param userId
	 * <p> @return
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */
	public SysUser queryById(Integer userId);
	
	/**
	 * 查询邮箱总数，检查是否存在
	 * @param email
	 * @return
	 */
	public int getUserCountByEmail(String email);	
}
