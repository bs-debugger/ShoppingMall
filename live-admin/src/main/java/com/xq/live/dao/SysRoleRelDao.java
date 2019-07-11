package com.xq.live.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xq.live.model.SysRoleRel;

@Repository("sysRoleRelDao")
public interface SysRoleRelDao extends BaseDao<SysRoleRel>{
	/**
	 * <p> 根据roleId和relType删除记录
	 * <p> @param param
	 * <p> @return
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */
	public void deleteByRoleId(Map<String, Object> param);
	
	/**
	 * <p> 根据关联对象id,关联类型删除 
	 * <p> @param param
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */
	public void deleteByObjId(Map<String, Object> param);
	
	public List<SysRoleRel> queryByObjId(Map<String, Object> param);
	
	public List<SysRoleRel> queryByRoleId(Map<String, Object> param);
}
