package com.xq.live.service;

import java.util.List;

import com.xq.live.model.SysRoleRel;

public interface SysRoleRelService {
	/**
	 * <p> 新增
	 * <p> @param rel
	 * <p> @return
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */
	public Integer add(SysRoleRel rel);
	
	public List<SysRoleRel> queryByRoleId(Integer roleId,Integer relType);
}
