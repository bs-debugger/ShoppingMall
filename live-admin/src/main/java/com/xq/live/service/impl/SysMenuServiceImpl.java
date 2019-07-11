package com.xq.live.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xq.live.dao.SysMenuDao;
import com.xq.live.model.SysMenu;
import com.xq.live.model.SysMenuBtn;
import com.xq.live.vo.BaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xq.live.dao.SysMenuBtnDao;
import com.xq.live.dao.SysRoleRelDao;
import com.xq.live.model.SysRoleRel.RelType;
import com.xq.live.service.SysMenuService;

@Service("sysMenuService")
public class SysMenuServiceImpl implements SysMenuService {
	@Autowired
	private SysMenuDao sysMenuDao;
	@Autowired
	private SysMenuBtnDao sysMenuBtnDao;
	@Autowired
	private SysRoleRelDao sysRoleRelDao;

	@Override
	public List<SysMenu> getRootMenu(Integer menuId) {
		return sysMenuDao.getRootMenu(menuId);
	}

	@Override
	public List<SysMenu> getChildMenu() {
		return sysMenuDao.getChildMenu();
	}

	@Override
	public List<SysMenu> getRootMenuByUser(Integer userId) {
		return sysMenuDao.getRootMenuByUser(userId);
	}

	@Override
	public List<SysMenu> getChildMenuByUser(Integer userId) {
		return sysMenuDao.getChildMenuByUser(userId);
	}

	@Override
	public int queryWithCount(BaseVo inVo) throws Exception {
		return sysMenuDao.queryWithCount(inVo);
	}

	@Override
	public List<SysMenu> queryWithPg(BaseVo inVo) throws Exception {
		Integer rowCount = queryWithCount(inVo);
		inVo.getPager().setRowCount(rowCount);
		return sysMenuDao.queryWithPg(inVo);
	}

	@Override
	public List<SysMenu> getMenuByRoleId(Integer roleId) {
		return sysMenuDao.getMenuByRoleId(roleId);
	}

	@Override
	public SysMenu queryById(Integer id) {
		return sysMenuDao.queryById(id);
	}

	@Override
	public int delete(Integer[] ids) {
		int result = 0;
		//删除关联关系
		for(Integer id : ids){
			sysMenuDao.delete(id);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("objId", id);
			map.put("relType", RelType.MENU.key);
			sysRoleRelDao.deleteByObjId(map);
			sysMenuBtnDao.deleteByMenuid(id);
			result++;
		}		
		return result;
	}

	@Override
	public int add(SysMenu menu) throws Exception{
		int result = sysMenuDao.add(menu);
		saveBtns(menu.getId(),menu.getBtns());
		return result;
	}

	@Override
	public int update(SysMenu menu) throws Exception {
		int result = sysMenuDao.update(menu);
		saveBtns(menu.getId(), menu.getBtns());
		return result;
	}

	/**
	 * 保存菜单btn
	 * 
	 * @param btns
	 * @throws Exception
	 */
	public int saveBtns(Integer menuid, List<SysMenuBtn> btns) throws Exception {
		int result = 0;
		if (btns == null || btns.isEmpty()) {
			return result;
		}
		for (SysMenuBtn btn : btns) {
			if (btn.getId() != null && "1".equals(btn.getDeleteFlag())) {
				sysMenuBtnDao.delete(btn.getId());
				continue;
			}
			btn.setMenuid(menuid);
			if (btn.getId() == null) {
				sysMenuBtnDao.add(btn);
			} else {
				sysMenuBtnDao.update(btn);
			}
			result++;
		}
		return result;
	}
}
