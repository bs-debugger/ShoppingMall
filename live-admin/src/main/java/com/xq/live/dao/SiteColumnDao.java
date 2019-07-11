package com.xq.live.dao;

import java.util.List;

import com.xq.live.model.SiteColumn;
import org.springframework.stereotype.Repository;

@Repository("siteColumnDao")
public interface SiteColumnDao extends BaseDao<SiteColumn>{
	/**
	 * 根据站点id查询栏目
	 * @param siteId
	 */
	public List<SiteColumn> queryBySiteId(Integer siteId);
}
