package com.xq.live.dao;

import java.util.Map;

import com.xq.live.model.SiteScore;
import org.springframework.stereotype.Repository;

@Repository("siteScoreDao")
public interface SiteScoreDao extends BaseDao<SiteScore> {
	public Integer saveScore(Map<String, Object> map);
}
