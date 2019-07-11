package com.xq.live.service;

import com.xq.live.model.Sku;
import com.xq.live.model.So;
import com.xq.live.vo.BaseVo;
import com.xq.live.vo.out.SoOut;

import java.util.List;

/**
 * 订单service
 *
 * @author zhangpeng32
 * @date 2018-02-22 17:50
 * @copyright:hbxq
 **/
public interface SoService {
    /**
     * 分页查询数据
     * @param inVo
     * @return
     * @throws Exception
     */
    public List<SoOut> queryWithPg(BaseVo inVo) throws Exception;

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
    SoOut queryById(Long id)throws Exception;;
}
