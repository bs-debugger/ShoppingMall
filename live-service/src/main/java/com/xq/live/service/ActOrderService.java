package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.ActOrder;
import com.xq.live.vo.in.ActOrderInVo;
import com.xq.live.vo.out.ActOrderOut;

/**
 * 团-订单-商品关联
 *
 * @author
 * @create 2018-02-07 17:18
 **/
public interface ActOrderService {
    /**
     * 新增
     * @param actOrder
     * @return
     */
    ActOrder add(ActOrder actOrder);

    /**
     * 更新
     * @param actOrder
     * @return
     */
    int update(ActOrder actOrder);

    /**
     * 根据id查询记录
     * @param id
     * @return
     */
    ActOrder selectOne(Long id);

    /**
     * 分页查询活动列表
     * @return
     */
    Pager<ActOrderOut> list(ActOrderInVo inVo);

}
