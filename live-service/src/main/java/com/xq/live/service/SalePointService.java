package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.SalePoint;
import com.xq.live.model.SalePointUser;
import com.xq.live.vo.in.SalePointInVo;
import com.xq.live.vo.in.SalePointUserInVo;
import com.xq.live.vo.out.GoodsSkuOut;
import com.xq.live.vo.out.SalePointOut;
import com.xq.live.vo.out.SalePointUserOut;

import java.util.List;

/**
 * Created by admin on 2018/9/26.
 */
public interface SalePointService {

    /**
     * 销售点列表
     * @param inVo
     * @return
     */
    Pager<SalePointOut> list(SalePointInVo inVo);

    /**
     * 根据销售点ip查询销售点下的商品
     * @param inVo
     * @return
     */
    Pager<GoodsSkuOut> getGoodsListBySalePointId(SalePointInVo inVo);

    /**
     *通过userId获取用户销售点信息
     * @param userId
     * @return
     */
    List<SalePointUser> getSalePointUserByUserId(Long userId);

    /**
     * 过ID获取销售点信息
     * @param id
     * @return
     */
    SalePoint getSalePointByID(Long id);

    /**
     * 通过goodsSkuId获取销售点信息
     * @param salePointInVo
     * @return
     */
    List<SalePointOut> getSalePointByGoodsSkuId(SalePointInVo salePointInVo);

    /**
     * 添加销售点的核销员
     * @param salePointUser
     * @return
     */
    Integer addSalePointUser(SalePointUserInVo salePointUser);

    /**
     * 查询销售点的核销员
     * @param salePointUser
     * @return
     */
    List<SalePointUserOut> selectByUserIdAndSalepintList(SalePointUserInVo salePointUser);

    /**
     * 删除销售点的核销员
     * @param salePointUser
     * @return
     */
    Integer deleteSalepintUser(SalePointUserInVo salePointUser);

    int deleteGoodsSkuBySalePointId(SalePointInVo salePointInVo);
}
