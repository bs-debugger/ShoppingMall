package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.GoodsSpu;
import com.xq.live.vo.in.GoodsSpuInVo;
import com.xq.live.vo.out.GoodsSpuOut;

import java.util.List;

/**
 * 商城系统Sku的Service
 * Created by lipeng on 2018/8/29.
 */
public interface GoodsSpuService {

    GoodsSpu selectOne(Long id);

    Long add(GoodsSpuInVo inVo);

    Integer update(GoodsSpuInVo inVo);

    Pager<GoodsSpuOut> list(GoodsSpuInVo inVo);

    List<GoodsSpuOut> top(GoodsSpuInVo inVo);
}
