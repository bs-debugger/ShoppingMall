package com.xq.live.dao;

import java.util.List;

import com.xq.live.vo.BaseVo;

/**
 * <p> 基础dao
 * <p> User: Zhang Peng
 * <p> Date: 2015年11月16日
 * <p> Version: 1.0
 */
public interface BaseDao<T> {
	/**
	 * <p> 新增
	 * <p> @param t
	 * <p> @return
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */
	public Integer add(T t);
	/**
	 * <p> 根据id更新字段 
	 * <p> @param t
	 * <p> @return
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */
	public Integer update(T t);
	/**
	 * <p> 只更新不为空的字段
	 * <p> @param t
	 * <p> @return
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */
	public Integer updateBySelective(T t);
	/**
	 * <p> 根据id删除记录
	 * <p> @param id
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */
	public Integer delete(Object id);
	/**
	 * <p> 查询记录数
	 * <p> @param model
	 * <p> @return
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */
	public int queryWithCount(BaseVo inVo);
	/**
	 * <p> 分页查询
	 * <p> @param model
	 * <p> @return
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */
	public List<T> queryWithPg(BaseVo inVo);
	
	/**
	 * <p> 根据id查询
	 * <p> @param id
	 * <p> @return
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 */
	public T queryById(Object id);
}
