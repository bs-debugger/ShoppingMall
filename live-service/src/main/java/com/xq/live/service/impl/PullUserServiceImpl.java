package com.xq.live.service.impl;

import com.alibaba.fastjson.JSON;
import com.xq.live.config.GoldConfig;
import com.xq.live.dao.ActGoodsSkuMapper;
import com.xq.live.dao.ActOrderMapper;
import com.xq.live.dao.OrderCouponMapper;
import com.xq.live.dao.PullUserMapper;
import com.xq.live.model.ActGoodsSku;
import com.xq.live.model.ActOrder;
import com.xq.live.model.PullUser;
import com.xq.live.service.ActGoodsSkuService;
import com.xq.live.service.KafkaService;
import com.xq.live.service.PullUserService;
import com.xq.live.vo.in.OrderCouponInVo;
import com.xq.live.vo.out.ActOrderOut;
import com.xq.live.vo.out.PullUserOut;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 用户邀请的serviceImpl
 * Created by lipeng on 2018/8/15.
 */
@Service
public class PullUserServiceImpl implements PullUserService{

    @Autowired
    private GoldConfig goldConfig;

    @Autowired
    private OrderCouponMapper orderCouponMapper;

    @Autowired
    private KafkaService kafkaService;

    @Autowired
    private ActGoodsSkuMapper actGoodsSkuMapper;

    @Autowired
    private PullUserMapper pullUserMapper;

    @Autowired
    private ActOrderMapper actOrderMapper;

    @Autowired
    private ActGoodsSkuService actGoodsSkuService;

    @Override
    public PullUser get(Long id) {
        return pullUserMapper.selectByUserId(id);
    }

    @Override
    public Integer update(PullUser invo) {
        return pullUserMapper.updatePullNums(invo);
    }

    /**
     * 查看邀请螃蟹人数
     * @return
     */
    @Override
    public PullUser getInfo(Long id) {
        return pullUserMapper.getByUserIdPX(id);
    }

    /**
     * 查看邀请新人人数
     * @return
     */
    @Override
    public PullUserOut getForUserList(PullUser InVo) {
        PullUser pullUser=pullUserMapper.getByUserId(InVo);
        PullUserOut pullUserOut = new PullUserOut();
        if (pullUser==null){
        return pullUserOut;
        }
        BeanUtils.copyProperties(pullUser,pullUserOut );
        pullUserOut.setHaveNum(pullUserOut.getPullNum()/goldConfig.getLynum());
        return pullUserOut;
    }

    /**
     * 清空邀请螃蟹人数
     * @return
     */
    @Override
    public Integer updatePullNumsPx(PullUser record) {
        return pullUserMapper.updatePullNumsPx(record);
    }

    /**
     * 邀请人数加一
     * @return
     */
    @Override
    public Integer updatePullNumsUp(PullUser record) {
        return pullUserMapper.updatePullNumsUp(record);
    }

    /**
     * 拼团人数加一
     * @return
     */
    @Override
    public Integer updateActGroupNumUp(PullUser record,Long orderId) {
        ActOrderOut actOrderOut=actOrderMapper.selectFirstDistributionByOrderId(orderId);
        Date time = new Date();
        if (record.getType()==PullUser.PULL_TYPE_PT){
            record.setGroupId(actOrderOut.getActGoodsSkuId());
            ActOrder actOrder = new ActOrder();
            BeanUtils.copyProperties(actOrderOut, actOrder);
            if (actOrderOut!=null&&actOrderOut.getId()!=null){
                ActGoodsSku actGoodsSku = actGoodsSkuMapper.selectByPrimaryKey(actOrderOut.getActGoodsSkuId());
                if (actGoodsSku.getState()==ActGoodsSku.STATE_WAIT&&actOrderOut!=null&&actOrderOut.getId()!=null){
                    //如果用户已经参团成功-只加人数
                    if (actOrderOut.getState()==ActOrder.ACT_ORDER_GROUP_STATE_TUXEDO_SUCCESS){
                        return actOrderMapper.updateByPeoPleNum(actOrder);
                    }
                    /*//判断活动中人数
                    if (actGoodsSku.getCurrentNum()<=actGoodsSku.getPeopleNum()){
                        //修改参团状态
                        actOrder.setState(ActOrder.ACT_ORDER_GROUP_STATE_TUXEDO_SUCCESS);
                    }*/
                    //修改参团状态
                    actOrder.setState(ActOrder.ACT_ORDER_GROUP_STATE_TUXEDO_SUCCESS);
                    actOrderMapper.updateByPeoPleNum(actOrder);
                    if ((actGoodsSku.getCurrentNum()+1)>=actGoodsSku.getPeopleNum()){
                        actGoodsSku.setState(ActGoodsSku.STATE_SUCCESS);
                    }
                    actGoodsSkuMapper.updateCurrentNum(actGoodsSku);
                    if(actGoodsSku.getState()==ActGoodsSku.STATE_SUCCESS){
                        //修改票券状态
                        OrderCouponInVo orderCouponInVo = new OrderCouponInVo();
                        orderCouponInVo.setActGoodsSkuId(actGoodsSku.getId());
                        orderCouponMapper.updateModifyingTheDisplay(orderCouponInVo);
                        //成功修改该团产生的相关奖励金
                        Boolean topic = kafkaService.sendDataToTopic("distriButionTopic",  "distriButionTopic", JSON.toJSONString(actGoodsSku));
                        //actGoodsSkuService.succesDistribution(actGoodsSku);//成功修改该团产生的相关奖励金
                    }
                }
            }
        }
        return 0;
    }

    /**
     * 配置邀请
     * @return
     */
    @Override
    public Integer insertPx(PullUser record) {
        return pullUserMapper.insert(record);
    }
}
