package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.ActOrderMapper;
import com.xq.live.dao.PullUserMapper;
import com.xq.live.model.ActOrder;
import com.xq.live.service.ActOrderService;
import com.xq.live.vo.in.ActOrderInVo;
import com.xq.live.vo.out.ActOrderOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActOrderServiceImpl implements ActOrderService {

    @Autowired
    private ActOrderMapper actOrderMapper;

    @Autowired
    private PullUserMapper pullUserMapper;


    /**
     * 新增并且更新发起人
     * @param actOrder
     * @return
     */
    @Override
    public ActOrder add(ActOrder actOrder) {
        List<ActOrder> order = actOrderMapper.selectByActGoodsSkuId(actOrder);
        if (order!=null&&order.size()>0&&order.get(0).getUserId()!=null&&actOrder.getParentId()!=null){
            actOrderMapper.updateUserParent(order.get(0));
            return actOrderMapper.selectByPrimaryKey(order.get(0).getId());
        }else if(order!=null&&order.size()>0&&order.get(0).getUserId()!=null){
            return order.get(0);
        }else {
            int i=actOrderMapper.insertOrderLog(actOrder);
            if (i<1){
                return null;
            }
            ActOrder outOrder=actOrderMapper.selectByPrimaryKey(actOrder.getId());
            return outOrder;
        }
    }

    /**
     * 更新
     * @param actOrder
     * @return
     */
    @Override
    public int update(ActOrder actOrder) {
        return actOrderMapper.updateByPrimaryKey(actOrder);
    }

    /**
     * 根据id查询记录
     * @param id
     * @return
     */
    @Override
    public ActOrder selectOne(Long id) {
        return actOrderMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页查询活动列表
     * @return
     */
    @Override
    public Pager<ActOrderOut> list(ActOrderInVo inVo) {
        return null;
    }
}
