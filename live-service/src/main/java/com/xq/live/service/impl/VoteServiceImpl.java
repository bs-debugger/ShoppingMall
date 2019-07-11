package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.ActGoodsSkuMapper;
import com.xq.live.dao.ActOrderMapper;
import com.xq.live.dao.OrderInfoMapper;
import com.xq.live.dao.VoteMapper;
import com.xq.live.model.ActGoodsSku;
import com.xq.live.model.OrderInfo;
import com.xq.live.model.Vote;
import com.xq.live.service.ActGoodsSkuService;
import com.xq.live.service.ActLotteryService;
import com.xq.live.service.ActOrderService;
import com.xq.live.service.VoteService;
import com.xq.live.vo.in.ActGoodsSkuInVo;
import com.xq.live.vo.in.ActLotteryInVo;
import com.xq.live.vo.in.VoteInVo;
import com.xq.live.vo.out.ActOrderOut;
import com.xq.live.vo.out.OrderInfoOut;
import com.xq.live.vo.out.OrderItemOut;
import com.xq.live.vo.out.VoteOut;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-21 21:08
 * @copyright:hbxq
 **/
@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteMapper voteMapper;

    @Autowired
    private ActOrderService actOrderService;

    @Autowired
    private ActOrderMapper actOrderMapper;

    @Autowired
    private ActGoodsSkuService actGoodsSkuService;

    @Autowired
    private ActLotteryService actLotteryService;

    @Autowired
    private ActGoodsSkuMapper actGoodsSkuMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Override
    public Vote get(Long id) {
        return voteMapper.selectByPrimaryKey(id);
    }

    @Override
    public Long add(VoteInVo vote) {
        Vote in = new Vote();
        BeanUtils.copyProperties(vote,in);
        int ret = voteMapper.insert(in);
        if(ret < 1){
            return null;
        }
        return in.getId();
    }

    @Override
    public int delete(Long id) {
        return voteMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteByInVo(VoteInVo inVo) {
        return voteMapper.deleteByInVo(inVo);
    }

    @Override
    public Integer canVote(VoteInVo inVo) {
        List<Vote> votes = voteMapper.canVote(inVo);
        if(votes!=null&&votes.size()>0){
            return 1;
        }
        return null;
    }

    @Override
    public Integer canGetSku(VoteInVo inVo) {
        List<Vote> votes = voteMapper.canGetSku(inVo);
        Integer i = 0;
        if(votes==null){
            return null;
        }
        for (Vote vote : votes) {
            if(vote.getShopId()!=null&&vote.getPlayerUserId()!=null){
                return 1;
            }else if((vote.getShopId()!=null&&vote.getPlayerUserId()==null)||(vote.getShopId()==null&&vote.getPlayerUserId()!=null)){
                i++;
            }
        }
        if(i>=2){
            return 1;
        }
        return null;
    }

    @Override
    public List<Vote> getDaysList(VoteInVo inVo) {
        return voteMapper.getDaysList(inVo);
    }

    @Override
    public Integer addByOrderIdFroAct(OrderInfo orderInfo) {
        ActOrderOut actOrder= actOrderMapper.selectFirstDistributionByOrderId(orderInfo.getId());
        Long actId=null;
        Long skuId=null;
        Long actGoodsSkuId=null;
        if(actOrder!=null&&actOrder.getActGoodsSku()!=null&&45==actOrder.getActGoodsSku().getActId()){
             actId=actOrder.getActGoodsSku().getActId();
             skuId=actOrder.getActGoodsSku().getSkuId();
             actGoodsSkuId=actOrder.getActGoodsSku().getId();
        }

        //判断是否为参加万达活动的商品产生的订单
        Boolean isWanDa=false;
        OrderInfoOut orderInfoOut= orderInfoMapper.getDetail(orderInfo.getId());
        if(orderInfoOut!=null&&orderInfoOut.getOrderItemOuts()!=null&&orderInfoOut.getOrderItemOuts().size()>0) {
            for(OrderItemOut orderItemOut:orderInfoOut.getOrderItemOuts()){
                List<ActGoodsSku> actGoodsSkuList= actGoodsSkuMapper.selectListBySkuId(orderItemOut.getGoodsSkuId());
                if(actGoodsSkuList!=null&&actGoodsSkuList.size()>0){
                    for(ActGoodsSku actGoodsSku:actGoodsSkuList){
                        if(actGoodsSku.getActId()==45){
                            isWanDa=true;
                            actId=actGoodsSku.getActId();
                            skuId=actGoodsSku.getSkuId();
                            actGoodsSkuId=actGoodsSku.getId();
                            break;
                        }
                    }
                }
            }
        }

        if((actOrder==null||actOrder.getActGoodsSku()==null||45!=actOrder.getActGoodsSku().getActId())&&!isWanDa)//只有万达活动参与，actId为45
            return null;
        VoteInVo inVo=new VoteInVo();
        inVo.setActId(actId);
        inVo.setUserId(orderInfo.getUserId());
        inVo.setType(Vote.VOTE_TYPE_OTHER);
        inVo.setSkuId(skuId);
        inVo.setActGoodsSkuId(actGoodsSkuId);
        inVo.setVoteNum(5);//核销一次投5票
        Long id  = this.add(inVo);

        ActGoodsSkuInVo actGoodsSkuInVo=new ActGoodsSkuInVo();
        actGoodsSkuInVo.setId(inVo.getActGoodsSkuId());
        actGoodsSkuInVo.setVoteNum(5);
        actGoodsSkuService.updateVoteNumUp(actGoodsSkuInVo);//商品活动增加一票

        /*ActLotteryInVo actLotteryInVo=new ActLotteryInVo();
        actLotteryInVo.setUserId(orderInfo.getUserId());
        actLotteryInVo.setActId(inVo.getActId());
        actLotteryInVo.setTotalNumber(5);
        actLotteryService.add(actLotteryInVo);//投票之后增加抽奖次数*/

        return null;
    }

    @Override
    public Pager<VoteOut> selectUserRanking(VoteInVo inVo) {
        Pager<VoteOut> result = new Pager<VoteOut>();
        int listTotal = voteMapper.listUserRankingTotal(inVo);
        if(listTotal > 0){
            List<VoteOut> list = voteMapper.listUserRanking(inVo);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        result.setTotal(listTotal);
        return result;
    }
}
