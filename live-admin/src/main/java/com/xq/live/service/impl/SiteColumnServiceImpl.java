package com.xq.live.service.impl;

import java.util.List;

import com.xq.live.dao.SiteColumnDao;
import com.xq.live.model.SiteColumn;
import com.xq.live.service.SiteColumnService;
import com.xq.live.vo.BaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("siteColumnService")
public class SiteColumnServiceImpl implements SiteColumnService {
	@Autowired
	private SiteColumnDao siteColumnDao;
	
	@Override
	public List<SiteColumn> queryBySiteId(Integer siteId) {
		return siteColumnDao.queryBySiteId(siteId);
	}

	@Override
	public int queryWithCount(BaseVo inVo) throws Exception {
		return siteColumnDao.queryWithCount(inVo);
	}

	@Override
	public List<SiteColumn> queryWithPg(BaseVo inVo) throws Exception {
		return siteColumnDao.queryWithPg(inVo);
	}

}
