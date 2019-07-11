package com.xq.live.dao;

import java.util.List;

import com.xq.live.model.SysRole;
import org.springframework.stereotype.Repository;

@Repository("sysRoleDao")
public interface SysRoleDao extends BaseDao<SysRole>{
	/**
	 *查询全部有效的权限
	 * @return
	 */
	public List<SysRole> queryAllList();
	
	/**
	 *根据用户Id查询权限
	 * @return
	 */
	public List<SysRole> queryByUserId(Integer userid);	
}
