package com.xq.live.service;

import com.xq.live.model.Sku;
import com.xq.live.model.User;
import com.xq.live.vo.BaseVo;
import com.xq.live.vo.UserInVo;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-12 13:40
 * @copyright:hbxq
 **/
public interface SkuService {

    /**
     * 分页查询数据
     * @param inVo
     * @return
     * @throws Exception
     */
    public List<Sku> queryWithPg(BaseVo inVo) throws Exception;

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
     * 根据id查询记录
     * @param id
     * @return
     */
    Sku queryById(Long id)throws Exception;;

    /**
     * 新增
     * @param sku
     * @return
     * @throws Exception
     */
    int add(Sku sku)throws Exception;

    /**
     * 更新
     * @param sku
     * @return
     * @throws Exception
     */
    int update(Sku sku)throws Exception;

    /**
     * 删除
     * @param ids
     * @return
     * @throws Exception
     */
    int delete(Long[] ids)throws Exception;
}
