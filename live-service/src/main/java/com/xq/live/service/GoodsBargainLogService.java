package com.xq.live.service;

import com.xq.live.vo.in.GoodsBargainLogInVo;
import com.xq.live.vo.out.GoodsBargainLogOut;

import java.util.List;

/**
 * Created by ss on 2018/11/2.
 * 用户砍价业务
 */
public interface GoodsBargainLogService {
    /**
     * 查询砍价列表和缓存
     * @param inVo
     * @return
     */
    List<GoodsBargainLogOut> skuGrouplist(GoodsBargainLogInVo inVo);

    /**
     * 查询砍价缓存
     * @param inVo
     * @return
     */
    List<GoodsBargainLogOut> skuGroupRedis(GoodsBargainLogInVo inVo);

}
