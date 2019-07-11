package com.xq.live.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xq.live.dao.SysRoleRelDao;
import com.xq.live.model.SysRoleRel;
import com.xq.live.service.SysRoleRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sysRoleRelService")
public class SysRoleRelServiceImpl implements SysRoleRelService {
	
	@Autowired
	private SysRoleRelDao sysRoleRelDao;
	
	@Override
	public Integer add(SysRoleRel rel) {
		return sysRoleRelDao.add(rel);
	}

	@Override
	public List<SysRoleRel> queryByRoleId(Integer roleId, Integer relType) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("roleId", roleId);
		param.put("relType", relType);
		return sysRoleRelDao.queryByRoleId(param);
	}

}
