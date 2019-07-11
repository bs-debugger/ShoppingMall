package com.xq.live.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xq.live.vo.BaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xq.live.dao.SysRoleRelDao;
import com.xq.live.dao.SysUserDao;
import com.xq.live.model.SysRoleRel;
import com.xq.live.model.SysRoleRel.RelType;
import com.xq.live.model.SysUser;
import com.xq.live.service.SysUserService;
import com.xq.live.vo.SysUserInVo;

@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	private SysUserDao sysUserDao;

	@Autowired
	private SysRoleRelDao sysRoleRelDao;

	@Override
	public int delete(Integer[] ids) throws Exception {
		int result = 0;
		for (Integer id : ids) {
			// 删除用户
			sysUserDao.delete(id);
			// 删除关联关系
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("objId", id);
			param.put("relType", RelType.USER.key);
			sysRoleRelDao.deleteByRoleId(param);
			result++;
		}
		return result;
	}

	@Override
	public SysUser queryLogin(String email, String pwd) {
		SysUserInVo inVo = new SysUserInVo();
		inVo.setEmail(email);
		inVo.setPwd(pwd);
		return sysUserDao.queryLogin(inVo);
	}

	@Override
	public int update(SysUser user) {
		return sysUserDao.update(user);
	}

	@Override
	public SysUser queryById(Integer userId) {
		return sysUserDao.queryById(userId);
	}

	@Override
	public List<SysUser> queryWithPg(BaseVo inVo) throws Exception {
		Integer rowCount = queryWithCount(inVo);
		inVo.getPager().setRowCount(rowCount);
		return sysUserDao.queryWithPg(inVo);
	}

	@Override
	public int queryWithCount(BaseVo inVo) throws Exception {
		return sysUserDao.queryWithCount(inVo);
	}

	@Override
	public int getUserCountByEmail(String email) {
		return sysUserDao.getUserCountByEmail(email);
	}

	@Override
	public int add(SysUser user) {
		return sysUserDao.add(user);
	}

	@Override
	public int updateBySelective(SysUser user) {
		return sysUserDao.updateBySelective(user);
	}

	@Override
	public List<SysRoleRel> getUserRole(Integer userId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("objId", userId);
		param.put("relType", RelType.USER.key);
		return sysRoleRelDao.queryByObjId(param);
	}

	@Override
	public int addUserRole(Integer userId, Integer[] roleIds) throws Exception {
		int result = 0;
		if (userId == null || roleIds == null || roleIds.length < 1) {
			return result;
		}
		// 清除关联关系
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("objId", userId);
		param.put("relType", RelType.USER.key);
		sysRoleRelDao.deleteByObjId(param);
		for (Integer roleId : roleIds) {
			SysRoleRel rel = new SysRoleRel();
			rel.setRoleId(roleId);
			rel.setObjId(userId);
			rel.setRelType(RelType.USER.key);
			sysRoleRelDao.add(rel);
			result++;
		}
		return result;
	}
}
