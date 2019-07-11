package com.xq.live.dao;

import java.util.List;

import com.xq.live.model.SiteType;
import org.springframework.stereotype.Repository;

/**
 * SiteType Mapper
 * @author Administrator
 *
 */
@Repository("siteTypeDao")
public interface SiteTypeDao extends BaseDao<SiteType> {
	
	/**
	 * 删除管理站点数据
	 * @param siteTypeId
	 * @param siteId
	 */	
	public void deleteSiteRel(Integer siteTypeId);
	
	
	/**
	 * 查所有站点类型
	 * @return
	 */
	public List<SiteType> queryByAll();
	
	
	public List<SiteType> queryBySiteId(Integer siteId);
	
}
