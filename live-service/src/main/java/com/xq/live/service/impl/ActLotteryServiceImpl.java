package com.xq.live.service.impl;

import com.xq.live.dao.ActLotteryMapper;
import com.xq.live.dao.GoodsSkuMapper;
import com.xq.live.dao.GoodsSpuMapper;
import com.xq.live.dao.OrderCouponMapper;
import com.xq.live.model.ActLottery;
import com.xq.live.model.GoodsSku;
import com.xq.live.service.ActLotteryService;
import com.xq.live.vo.in.ActLotteryInVo;
import com.xq.live.vo.out.GoodsSpuOut;
import com.xq.live.vo.out.OrderCouponOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by admin on 2019/1/8.
 */
@Service
public class ActLotteryServiceImpl implements ActLotteryService {

    @Autowired
    private ActLotteryMapper actLotteryMapper;

    @Autowired
    private OrderCouponMapper orderCouponMapper;

    @Autowired
    private GoodsSkuMapper goodsSkuMapper;

    @Autowired
    private GoodsSpuMapper goodsSpuMapper;


    @Override
    public ActLottery selectUserLottery(ActLotteryInVo inVo) {
        return actLotteryMapper.selectUserLottery(inVo);
    }

    @Override
    public Integer add(ActLotteryInVo inVo) {
        if(inVo.getUserId()==null||inVo.getActId()==null)
            return null;
        ActLottery actLottery =actLotteryMapper.selectUserLottery(inVo);
        if(actLottery==null||actLottery.getId()==null){//沒有记录增加记录
            ActLottery actLottery1=new ActLottery();
            actLottery1.setUserId(inVo.getUserId());
            actLottery1.setActId(inVo.getActId());
            actLottery1.setTotalNumber(inVo.getTotalNumber());
           Integer i= actLotteryMapper.insert(actLottery1);
            return i;
        }else{//有记录则增加记录抽奖次数
            Integer i=actLotteryMapper.updateUpTotalNumber(inVo);
            return i;
        }
    }


    /*核销之后增加抽奖次数*/
    @Override
    public Integer hxAdd(ActLotteryInVo inVo, Long id) {
        OrderCouponOut orderCouponOut = orderCouponMapper.selectDetailByPrimaryKey(id);
        if(orderCouponOut==null){
            return null;
        }
        GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(orderCouponOut.getGoodsSkuId());
        if(goodsSku==null){
            return null;
        }
        //查询商品对应的spu的详细信息和spu对应的文描信息
        GoodsSpuOut goodsSpuOut = goodsSpuMapper.selectDetailById(goodsSku.getSpuId());
        if (goodsSpuOut.getCategoryId().toString().equals(new Long(12).toString())){
            return null;
        }
        return this.add(inVo);
    }
}
