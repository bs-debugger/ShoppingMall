package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.GoodsRulesConfigMapper;
import com.xq.live.model.GoodsRulesConfig;
import com.xq.live.service.GoodsRulesConfigService;
import com.xq.live.vo.in.GoodsRulesConfigInVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by admin on 2019/4/13.
 */
@Service
public class GoodsRulesConfigServiceImpl implements GoodsRulesConfigService {

    @Autowired
    private GoodsRulesConfigMapper goodsRulesConfigMapper;

    @Override
    public Pager<GoodsRulesConfig> list(GoodsRulesConfigInVO inVO) {
        Pager<GoodsRulesConfig> result = new Pager<GoodsRulesConfig>();
        int listTotal =goodsRulesConfigMapper.listTotal(inVO);
        if(listTotal > 0){
            List<GoodsRulesConfig> list=goodsRulesConfigMapper.list(inVO);
            result.setList(list);
        }
        result.setPage(inVO.getPage());
        result.setTotal(listTotal);
        return result;
    }
}
