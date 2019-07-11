package com.xq.live.service.impl;

import java.util.List;

import com.xq.live.service.SiteTypeService;
import com.xq.live.vo.BaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xq.live.dao.SiteTypeDao;
import com.xq.live.exception.ServiceException;
import com.xq.live.model.SiteType;
import com.xq.live.vo.SiteTypeInVo;

@Service("siteTypeService")
public class SiteTypeServiceImpl implements SiteTypeService {

	@Autowired
	private SiteTypeDao siteTypeDao;

	@Override
	public Integer add(SiteType t) throws Exception {
		validation(t, 0);
		Integer i = siteTypeDao.add(t);
		return i;
	}

	@Override
	public Integer update(SiteType t) throws Exception {
		validation(t, 1);
		Integer i = siteTypeDao.update(t);
		return i;
	}

	@Override
	public Integer delete(Object... ids) throws Exception {
		Integer result = 0;
		if (ids == null || ids.length < 1) {
			return result;
		}
		for (Object id : ids) {
			siteTypeDao.deleteSiteRel((Integer) id);
			siteTypeDao.delete(id);
			result++;
		}
		return result;
	}

	@Override
	public List<SiteType> queryByAll() throws Exception {
		return siteTypeDao.queryByAll();
	}

	@Override
	public List<SiteType> queryBySiteId(Integer siteId) throws Exception {
		return siteTypeDao.queryBySiteId(siteId);
	}

	/**
	 * 验证是否重复
	 * 
	 * @param t
	 * @param num
	 * @throws Exception
	 */
	private void validation(SiteType t, int num) throws Exception {
		SiteType bean = (SiteType) t;
		// 验证name是否重复
		SiteTypeInVo model = new SiteTypeInVo();
		model.setName(bean.getName());
		int count = siteTypeDao.queryWithCount(model);
		if (count > num) {
			throw new ServiceException("name is can't be duplicate");
		}
		// 验证code是否重复
		model.setName(null);
		model.setCode(bean.getCode());
		count = siteTypeDao.queryWithCount(model);
		if (count > num) {
			throw new ServiceException("code is can't be duplicate");
		}
	}

	@Override
	public int queryWithCount(BaseVo inVo) throws Exception {
		return siteTypeDao.queryWithCount(inVo);
	}

	@Override
	public List<SiteType> queryWithPg(BaseVo inVo) throws Exception {
		return siteTypeDao.queryWithPg(inVo);
	}

	@Override
	public SiteType queryById(Integer id) throws Exception {
		return siteTypeDao.queryById(id);
	}
}
