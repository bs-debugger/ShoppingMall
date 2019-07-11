package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.GoodsRulesConfig;
import com.xq.live.vo.in.GoodsRulesConfigInVO;

/**
 * Created by admin on 2019/4/13.
 */
public interface GoodsRulesConfigService {

    Pager<GoodsRulesConfig> list(GoodsRulesConfigInVO inVO);
}
