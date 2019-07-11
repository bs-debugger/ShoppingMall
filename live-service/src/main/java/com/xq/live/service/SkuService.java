package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.Sku;
import com.xq.live.model.SkuAllocation;
import com.xq.live.vo.in.SkuInVo;
import com.xq.live.vo.out.SkuForTscOut;
import com.xq.live.vo.out.SkuOut;

import java.util.List;

/**
 * SKU服务service
 *
 * @author zhangpeng32
 * @date 2018-02-09 10:32
 * @copyright:hbxq
 **/
public interface SkuService {

    /**
     * 查一条
     * @param id
     * @return
     */
    Sku get(Long id);

    /**
     * 分页查询
     * @param inVo
     * @return
     */
    Pager<SkuOut> list(SkuInVo inVo);

    /**
     * 分页查询
     * @param inVo
     * @return
     */
    List<SkuOut> listHome(SkuInVo inVo);

    /**
     * 查询列表
     * @param inVo
     * @return
     */
    List<SkuOut> top(SkuInVo inVo);

    /**
     * 新增sku
     * @param sku
     * @return
     */
    Long add(Sku sku);

    /**
     * 新增食典券
     * @param sku
     * @return
     */
    Long addSku(Sku sku);

    /**
     * 根据skuType,skuName,sellPrice来查询票券信息
     * @param inVo
     * @return
     */
    Sku selectForActSku(Sku inVo);

    /**
     * 根据id查询，返回结果包含促销信息
     * @param id
     * @return
     */
    SkuOut selectById(Long id);

    /**
     * 分页查询特色菜
     * @param inVo
     * @return
     */
    Pager<SkuForTscOut> queryTscList(SkuInVo inVo);

    Pager<SkuForTscOut> queryQgcNewList(SkuInVo inVo);

    /**
     * 查询单个推荐菜详情
     * @param inVo
     * @return
     */
    SkuForTscOut getTscForZan(SkuInVo inVo);

    /**
     * 判断是否是新用户首次下单
     * @param userId
     * @return
     */
    Integer isNewUser(Long userId);

    /**
     * 逻辑删除一条记录
     * @param id
     * @return
     */
    Integer delete(Long id);

    /**
     * 更新一条数据
     * @param sku
     * @return
     */
    Integer update(Sku sku);

    /**
     * 更新食典券和coupon_sku的信息
     * @param skuForAct
     * @return
     */
    Integer updateSku(Sku skuForAct);

    /**
     * 查询单个砍价菜，不需要关注他是否下架
     * @param inVo
     * @return
     */
    SkuForTscOut getKjcForZan(SkuInVo inVo);

    /**
     * 分页查询砍价菜
     * @param inVo
     * @return
     */
    Pager<SkuForTscOut> queryKjcList(SkuInVo inVo);

    /**
     * 分页查询砍价菜
     * @param inVo
     * @return
     */
    List<SkuForTscOut> queryKjcListHome(SkuInVo inVo);

    SkuForTscOut getQgcForZan(SkuInVo inVo);

    Pager<SkuForTscOut> queryQgcList(SkuInVo inVo);

    List<SkuForTscOut> queryQgcListHome(SkuInVo inVo);

    SkuForTscOut getDhcForZan(SkuInVo inVo);

    Pager<SkuForTscOut> queryDhcList(SkuInVo inVo);

    Integer addSkuAllocation(SkuAllocation skuAllocation);

    Integer isAdd(SkuAllocation skuAllocation);

    Integer deleteSkuAllocation(SkuAllocation skuAllocation);
}
