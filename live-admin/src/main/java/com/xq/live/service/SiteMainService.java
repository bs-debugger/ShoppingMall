package com.xq.live.service;

import java.util.List;
import java.util.Map;

import com.xq.live.model.SiteMain;
import com.xq.live.vo.BaseVo;

public interface SiteMainService {

	/**
	 * 管理站点类型
	 * 
	 * @param siteTypeId
	 * @param siteId
	 */
	public int addTypeRel(Integer[] typeIds, Integer siteId);

	/**
	 * 管理站点类型
	 * 
	 * @param siteTypeId
	 * @param siteId
	 */
	public List<Map<String, Object>> getTypesBySiteId(Integer siteId);

	public int delete(Object[] ids) throws Exception;
	
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
	public List<SiteMain> queryWithPg(BaseVo inVo) throws Exception;	
	
	public int add(SiteMain t);
	
	public int update(SiteMain t);
	
	public SiteMain queryById(Integer id);

}
