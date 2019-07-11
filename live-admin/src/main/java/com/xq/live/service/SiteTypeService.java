package com.xq.live.service;

import java.util.List;

import com.xq.live.model.SiteType;
import com.xq.live.vo.BaseVo;

public interface SiteTypeService {
	/**
	 * 添加站点类型
	 */
	public Integer add(SiteType t) throws Exception;

	/**
	 * 修改站点类型
	 */
	public Integer update(SiteType t) throws Exception;

	public Integer delete(Object... ids) throws Exception;

	public List<SiteType> queryByAll() throws Exception;

	public List<SiteType> queryBySiteId(Integer siteId) throws Exception;
	
	/**
	 * <p> 查询记录总数
	 * <p> @param model
	 * <p> @return
	 * <p> @throws Exception
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */
	public int queryWithCount(BaseVo inVo)throws Exception;
	
	/**
	 * <p> 分页查询列表信息
	 * <p> @param inVo
	 * <p> @return
	 * <p> @throws Exception
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */
	public List<SiteType> queryWithPg(BaseVo inVo) throws Exception;	
	
	public SiteType queryById(Integer id) throws Exception;	

}
