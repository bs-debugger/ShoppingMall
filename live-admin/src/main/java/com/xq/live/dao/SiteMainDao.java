package com.xq.live.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xq.live.model.SiteMain;

@Repository("siteMainDao")
public interface SiteMainDao extends BaseDao<SiteMain>{
	/**
	 * 管理站点类型
	 * 
	 * @param siteTypeId
	 * @param siteId
	 */
	public int addTypeRel(Map<String, Object> map);

	/**
	 * 删除关联的数据
	 * 
	 * @param siteTypeId
	 * @param siteId
	 */
	public int deleteTypeRel(Integer siteId);
	
	/**
	 * 管理站点类型
	 * 
	 * @param siteTypeId
	 * @param siteId
	 */
	public List<Map<String, Object>> getTypesBySiteId(Integer siteId);	
}
