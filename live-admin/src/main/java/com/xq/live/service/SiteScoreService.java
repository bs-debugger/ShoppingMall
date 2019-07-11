package com.xq.live.service;

import java.util.List;

import com.xq.live.model.SiteScore;
import com.xq.live.vo.BaseVo;

public interface SiteScoreService {

	public int saveScore(Integer siteId, Integer scoreType);

	/**
	 * <p>
	 * 查询记录总数
	 * <p>
	 * @param model
	 * <p>
	 * @return
	 * <p>
	 * @throws Exception
	 * <p>
	 * User: Zhang Peng
	 * <p>
	 * Date: 2015年11月16日
	 */
	public int queryWithCount(BaseVo inVo) throws Exception;

	/**
	 * <p>
	 * 分页查询列表信息
	 * <p>
	 * @param inVo
	 * <p>
	 * @return
	 * <p>
	 * @throws Exception
	 * <p>
	 * User: Zhang Peng
	 * <p>
	 * Date: 2015年11月16日
	 */
	public List<SiteScore> queryWithPg(BaseVo inVo) throws Exception;
}
