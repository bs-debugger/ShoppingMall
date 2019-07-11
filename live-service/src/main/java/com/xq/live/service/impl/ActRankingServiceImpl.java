package com.xq.live.service.impl;

import com.xq.live.dao.ActRankingMapper;
import com.xq.live.dao.ShopMapper;
import com.xq.live.dao.UserMapper;
import com.xq.live.model.Shop;
import com.xq.live.service.ActRankingService;
import com.xq.live.vo.in.ActRankingInVo;
import com.xq.live.vo.out.ActRankingOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ss on 2019/3/1.
 */
@Service
public class ActRankingServiceImpl implements ActRankingService{

    @Autowired
    private ActRankingMapper actRankingMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 查询获奖商品列表
     * @param inVo
     * @return
     */
    @Override
    public List<ActRankingOut> selectActInfoList(ActRankingInVo inVo) {
        Integer i = actRankingMapper.selectActInfoTotal(inVo);
        List<ActRankingOut> list = new ArrayList<ActRankingOut>();
        if (i!=null&&i>0){
            list = actRankingMapper.selectActInfoList(inVo);
        }
        return list;
    }
}
