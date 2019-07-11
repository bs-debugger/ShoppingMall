package com.xq.live.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xq.live.dao.SysRoleDao;
import com.xq.live.dao.SysRoleRelDao;
import com.xq.live.model.SysRole;
import com.xq.live.model.SysRoleRel;
import com.xq.live.model.SysRoleRel.RelType;
import com.xq.live.service.SysRoleService;
import com.xq.live.vo.BaseVo;

@Service("sysRoleService")
public class SysRoleServiceImpl implements SysRoleService {

	@Autowired
	private SysRoleRelDao sysRoleRelDao;

	@Autowired
	private SysRoleDao sysRoleDao;

	@Override
	public int addRoleMenuRel(Integer roleId, Integer[] menuIds) throws Exception {
		int result = 0;
		if (roleId == null || menuIds == null || menuIds.length < 1) {
			return 0;
		}
		for (Integer menuid : menuIds) {
			SysRoleRel rel = new SysRoleRel();
			rel.setRoleId(roleId);
			rel.setObjId(menuid);
			rel.setRelType(RelType.MENU.key);
			sysRoleRelDao.add(rel);
			result++;
		}
		return result;
	}

	@Override
	public int addRoleBtnRel(Integer roleId, Integer[] btnIds) throws Exception {
		int result = 0;
		if (roleId == null || btnIds == null || btnIds.length < 1) {
			return result;
		}
		for (Integer btnid : btnIds) {
			SysRoleRel rel = new SysRoleRel();
			rel.setRoleId(roleId);
			rel.setObjId(btnid);
			rel.setRelType(RelType.BTN.key);
			sysRoleRelDao.add(rel);
			result++;
		}
		return result;
	}

	@Override
	public int add(SysRole role, Integer[] menuIds, Integer[] btnIds) throws Exception {
		int result = sysRoleDao.add(role);
		addRoleMenuRel(role.getId(), menuIds);
		addRoleBtnRel(role.getId(), btnIds);
		return result;
	}

	@Override
	public int delete(Integer[] ids) throws Exception {
		int result = 0;
		for (Integer id : ids) {
			sysRoleDao.delete(id);
			// 清除关联关系
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("roleId", id);
			param.put("relType", null);
			sysRoleRelDao.deleteByRoleId(param);
			result++;
		}
		return result;
	}

	@Override
	public int update(SysRole role, Integer[] menuIds, Integer[] btnIds) throws Exception {
		int result = sysRoleDao.update(role);
		// 清除关联关系
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("roleId", role.getId());
		param.put("relType", RelType.MENU.key);
		sysRoleRelDao.deleteByRoleId(param);
		param.put("relType", RelType.BTN.key);
		sysRoleRelDao.deleteByRoleId(param);
		
		addRoleMenuRel(role.getId(), menuIds);
		addRoleBtnRel(role.getId(), btnIds);
		return result;
	}

	@Override
	public List<SysRole> queryAllList() {
		return sysRoleDao.queryAllList();
	}

	@Override
	public List<SysRole> queryByUserid(Integer userid) {
		return sysRoleDao.queryByUserId(userid);
	}

	@Override
	public SysRole queryById(Integer id) {
		return sysRoleDao.queryById(id);
	}

	@Override
	public int queryWithCount(BaseVo inVo) throws Exception {
		return sysRoleDao.queryWithCount(inVo);
	}

	@Override
	public List<SysRole> queryWithPg(BaseVo inVo) throws Exception {
		return sysRoleDao.queryWithPg(inVo);
	}

}
