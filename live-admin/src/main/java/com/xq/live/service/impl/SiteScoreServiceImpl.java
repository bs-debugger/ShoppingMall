package com.xq.live.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xq.live.vo.BaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xq.live.dao.SiteScoreDao;
import com.xq.live.model.SiteScore;
import com.xq.live.model.SiteScore.ScoreType;
import com.xq.live.service.SiteScoreService;

@Service("siteScoreService")
public class SiteScoreServiceImpl implements SiteScoreService {
	@Autowired
	private SiteScoreDao siteScoreDao;
	
	@Override
	public int saveScore(Integer siteId, Integer scoreType) {
		int result = 0;
		SiteScore score = siteScoreDao.queryById(siteId);
		if(score == null){
			score = new SiteScore(siteId,0,0,0,0,0,0);
			result = siteScoreDao.add(score);
		}else{
			Map<String, Object> map = new HashMap<String, Object>();
			String value = ScoreType.get(scoreType).value;
			map.put("siteId", siteId);
			map.put("column", value);
			result = siteScoreDao.saveScore(map);
		}
		return result;
	}

	@Override
	public int queryWithCount(BaseVo inVo) throws Exception {
		return siteScoreDao.queryWithCount(inVo);
	}

	@Override
	public List<SiteScore> queryWithPg(BaseVo inVo) throws Exception {
		return siteScoreDao.queryWithPg(inVo);
	}

}
