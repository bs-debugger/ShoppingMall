package com.xq.live.service.impl;

import java.util.List;

import com.xq.live.dao.SysMenuBtnDao;
import com.xq.live.model.SysMenuBtn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xq.live.service.SysMenuBtnService;

@Service("sysMenuBtnService")
public class SysMenuBtnServiceImpl implements SysMenuBtnService {
	
	@Autowired
	private SysMenuBtnDao sysMenuBtnDao;

	@Override
	public List<SysMenuBtn> getMenuBtnByUser(Integer userid) {
		return sysMenuBtnDao.getMenuBtnByUser(userid);
	}

	@Override
	public List<SysMenuBtn> queryByMenuid(Integer menuid) {
		return sysMenuBtnDao.queryByMenuid(menuid);
	}

	@Override
	public List<SysMenuBtn> queryByMenuUrl(String url) {
		return sysMenuBtnDao.queryByMenuUrl(url);
	}

	@Override
	public int deleteByMenuid(Integer menuid) {
		return sysMenuBtnDao.deleteByMenuid(menuid);
	}

	@Override
	public List<SysMenuBtn> queryByAll() {
		return sysMenuBtnDao.queryByAll();
	}

}
