package com.xq.live.dao;

import com.xq.live.model.Sku;
import com.xq.live.vo.in.SkuInVo;
import com.xq.live.vo.out.SkuForTscOut;
import com.xq.live.vo.out.SkuOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkuMapper {
    int deleteByPrimaryKey(Long id);

    int insertSkuShop(SkuInVo record);

    int insert(Sku record);

    int insertSelective(Sku record);

    Sku selectByPrimaryKey(Long id);

    Sku selectByskuId(Long id);

    Sku selectBySkuCode(String skuCode);

    Sku selectForActSku(Sku record);

    int updateByPrimaryKeySelective(Sku record);

    int updateByPrimaryKey(Sku record);

    List<SkuOut> list(SkuInVo inVo);

    int listTotal(SkuInVo inVo);

    SkuOut selectById(Long id);

    List<SkuForTscOut> queryTscList(SkuInVo inVo);

    SkuForTscOut getTscForZan(SkuInVo inVo);

    SkuForTscOut getTscBySkuNameAndShopId(SkuInVo inVo);

    int tscListTotal(SkuInVo inVo);

    List<SkuForTscOut> queryKjcList(SkuInVo inVo);

    SkuForTscOut getKjcForZan(SkuInVo inVo);

    int kjcListTotal(SkuInVo inVo);

    int qgcListTotal(SkuInVo inVo);

    List<SkuForTscOut> queryQgcList(SkuInVo inVo);

    SkuForTscOut getQgcForZan(SkuInVo inVo);

    SkuForTscOut getDhcForZan(SkuInVo inVo);

    int dhcListTotal(SkuInVo inVo);

    List<SkuForTscOut> queryDhcList(SkuInVo inVo);

    Integer updateToDeleteSkuAllocation(SkuInVo inVo);

    /**
     * 修改指定(螃蟹)的库存数量
     * @param inVo
     * @return
     */
    Integer updateSkuNum(SkuInVo inVo);
}
