package com.xq.live.service;

import com.xq.live.model.CouponSku;
import com.xq.live.model.Sku;
import com.xq.live.vo.BaseVo;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-12 15:03
 * @copyright:hbxq
 **/
public interface CouponSkuService {

    /**
     * 分页查询数据
     * @param inVo
     * @return
     * @throws Exception
     */
    public List<CouponSku> queryWithPg(BaseVo inVo) throws Exception;

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
    CouponSku queryById(Long id)throws Exception;;

    /**
     * 新增
     * @param couponSku
     * @return
     * @throws Exception
     */
    int add(CouponSku couponSku)throws Exception;

    /**
     * 更新
     * @param couponSku
     * @return
     * @throws Exception
     */
    int update(CouponSku couponSku)throws Exception;

    /**
     * 删除
     * @param ids
     * @return
     * @throws Exception
     */
    int delete(Long[] ids)throws Exception;
}
