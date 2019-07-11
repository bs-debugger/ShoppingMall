package com.xq.live.service;

import java.util.List;

import com.xq.live.model.SiteColumn;
import com.xq.live.vo.BaseVo;

public interface SiteColumnService {
	/**
	 * 根据站点id查询栏目
	 * @param siteId
	 */
	public List<SiteColumn> queryBySiteId(Integer siteId);
	
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
	public List<SiteColumn> queryWithPg(BaseVo inVo) throws Exception;
}
