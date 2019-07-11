package com.xq.live.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xq.live.dao.SiteMainDao;
import com.xq.live.model.SiteMain;
import com.xq.live.service.SiteMainService;
import com.xq.live.vo.BaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("siteMainService")
public class SiteMainServiceImpl implements SiteMainService {
	@Autowired
	private SiteMainDao siteMainDao;

	@Override
	public int addTypeRel(Integer[] typeIds, Integer siteId) {
		// 先清理掉关联关系
		Integer result = siteMainDao.deleteTypeRel(siteId);
		if (typeIds != null) {
			for (Integer typeId : typeIds) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("siteTypeId", typeId);
				map.put("siteId", siteId);
				siteMainDao.addTypeRel(map);
			}
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> getTypesBySiteId(Integer siteId) {
		return siteMainDao.getTypesBySiteId(siteId);
	}

	@Override
	public int delete(Object[] ids) throws Exception {
		Integer result = 0;
		for (Object id : ids) {
			siteMainDao.deleteTypeRel((Integer) id);
			siteMainDao.delete(id);
			result++;
		}
		return result;
	}

	@Override
	public int queryWithCount(BaseVo inVo) throws Exception {
		return siteMainDao.queryWithCount(inVo);
	}

	@Override
	public List<SiteMain> queryWithPg(BaseVo inVo) throws Exception {
		return siteMainDao.queryWithPg(inVo);
	}

	@Override
	public int add(SiteMain t) {
		return siteMainDao.add(t);
	}

	@Override
	public int update(SiteMain t) {
		return siteMainDao.update(t);
	}

	@Override
	public SiteMain queryById(Integer id) {
		return siteMainDao.queryById(id);
	}

}
