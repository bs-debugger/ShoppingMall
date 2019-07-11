package com.xq.live.dao;

import java.util.List;

import com.xq.live.model.SysMenuBtn;
import org.springframework.stereotype.Repository;

@Repository("sysMenuBtnDao")
public interface SysMenuBtnDao extends BaseDao<SysMenuBtn>{
	
	public List<SysMenuBtn> getMenuBtnByUser(Integer userid);
	
	public List<SysMenuBtn> queryByMenuid(Integer menuid);
	
	public List<SysMenuBtn> queryByMenuUrl(String url); 
	
	public int deleteByMenuid(Integer menuid);
	
	public List<SysMenuBtn> queryByAll();
	
}
