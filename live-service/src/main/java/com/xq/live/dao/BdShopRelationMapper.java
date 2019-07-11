package com.xq.live.dao;

import com.xq.live.vo.in.ShopRelationInVo;
import com.xq.live.vo.out.ShopRelationOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BdShopRelationMapper {

    /**
     * 查询商户认领列表
     * @param shopRelationInVo
     * @return
     */
    List<ShopRelationOut> showList(ShopRelationInVo shopRelationInVo);

    /**
     * 判断商户是否被认领
     * @param shopId
     * @return
     */
    int shopHasRelation(Long shopId);

    /**
     * 认领新商户
     * @param shopRelationInVo
     * @return
     */
    int save(ShopRelationInVo shopRelationInVo);

    /**
     * 解除商户
     * @param shopRelationInVo
     * @return
     */
    int remove(ShopRelationInVo shopRelationInVo);

}