package com.xq.live.service;

import com.github.pagehelper.PageInfo;
import com.xq.live.vo.in.ShopRelationInVo;
import com.xq.live.vo.out.ShopRelationOut;

public interface ShopRelationService {

    PageInfo<ShopRelationOut> showList(ShopRelationInVo shopRelationInVo);

    Boolean save(ShopRelationInVo shopRelationInVo);

}
