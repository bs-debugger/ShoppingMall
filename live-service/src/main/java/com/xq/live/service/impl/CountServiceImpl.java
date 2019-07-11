package com.xq.live.service.impl;

import com.xq.live.common.RedisCache;
import com.xq.live.config.ActSkuConfig;
import com.xq.live.dao.*;
import com.xq.live.model.*;
import com.xq.live.service.CountService;
import com.xq.live.vo.in.*;
import com.xq.live.vo.out.ActSkuOut;
import com.xq.live.vo.out.ActUserOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @package: com.xq.live.service.impl
 * @description: 各种统计服务
 * @author: zhangpeng32
 * @date: 2018/3/19 20:23
 * @version: 1.0
 */
@Service
public class CountServiceImpl implements CountService {

    @Autowired
    RedisCache redisCache;

    @Autowired
     TopicMapper topicMapper;

    @Autowired
    ShopMapper shopMapper;

    @Autowired
    ActUserMapper actUserMapper;

    @Autowired
    ActShopMapper actShopMapper;

    @Autowired
    ActGroupMapper actGroupMapper;

    @Autowired
    ActSkuMapper actSkuMapper;

    @Autowired
    ActTopicMapper actTopicMapper;

    @Autowired
    ActSkuConfig actSkuConfig;



    private static Long viewArticleTime = System.currentTimeMillis();

    @Override
    public Integer topicHits(Long topicId) {
        String key = "topicHits_" + topicId.toString();
        Integer hits = redisCache.get(key, Integer.class);
        if (hits == null) {
            Topic topic = topicMapper.selectByPrimaryKey(topicId);
            hits = topic.getHitNum() == null ? 0 : topic.getHitNum();
        }
        hits++;
        redisCache.set(key, hits, 1l, TimeUnit.DAYS);
        Long time = System.currentTimeMillis();
        if (time > (viewArticleTime + 300000)) {    //5分钟更新一次数据到数据库,viewArticleTime:写数据库的周期
            viewArticleTime = time;
            Topic topic = new Topic();
            topic.setId(topicId);
            topic.setHitNum(hits);
            topicMapper.updateByPrimaryKeySelective(topic);
            redisCache.del(key);
        }
        return hits;
    }

    @Override
    public Integer topicTrans(Long topicId) {
        String key = "topicTrans_" + topicId.toString();
        Integer trans = redisCache.get(key, Integer.class);
        if (trans == null) {
            Topic topic = topicMapper.selectByPrimaryKey(topicId);
            trans = topic.getTransNum() == null ? 0 : topic.getTransNum();
        }
        trans++;
        redisCache.set(key, trans, 1l, TimeUnit.DAYS);
        Long time = System.currentTimeMillis();
        if (time > (viewArticleTime + 300000)) {    //5分钟更新一次数据到数据库,viewArticleTime:写数据库的周期
            viewArticleTime = time;
            Topic topic = new Topic();
            topic.setId(topicId);
            topic.setTransNum(trans);
            topicMapper.updateByPrimaryKeySelective(topic);
            redisCache.del(key);
        }
        /*Topic topic = new Topic();
        topic.setId(topicId);
        topic.setTransNum(trans);
        topicMapper.updateByPrimaryKeySelective(topic);
        redisCache.del(key);*/
        return trans;
    }

    @Override
    public Integer shopPops(Long shopId) {
        String key = "shopPops_" + shopId.toString();
        Integer pops = redisCache.get(key, Integer.class);
        if (pops == null) {
            Shop shop = shopMapper.selectByPrimaryKey(shopId);
            pops = shop.getPopNum() == null ? 0 : shop.getPopNum();
        }
        pops++;
        redisCache.set(key, pops, 1l, TimeUnit.DAYS);
        Long time = System.currentTimeMillis();
        if (time > (viewArticleTime + 300000)) {    //5分钟更新一次数据到数据库,viewArticleTime:写数据库的周期
            viewArticleTime = time;
            Shop shop = new Shop();
            shop.setId(shopId);
            shop.setPopNum(pops);
            shopMapper.updateByPrimaryKeySelective(shop);
            redisCache.del(key);
        }
        return pops;
    }

    @Override
    public Integer voteNums(VoteInVo invo) {
        String key = null;
        Integer nums = 0;
        ActShop actShop = null;
        ActUserOut actUser = null;
        if(invo.getShopId()!=null&&invo.getPlayerUserId()!=null){
             key = "voteNums_" + invo.getActId() + invo.getShopId() + invo.getPlayerUserId();
        }else if(invo.getShopId()!=null&&invo.getPlayerUserId()==null){
            key = "voteNums_" + invo.getActId() + "_shopId_" +invo.getShopId();
            ActShopInVo actShopInVo = new ActShopInVo();
            actShopInVo.setShopId(invo.getShopId());
            actShopInVo.setActId(invo.getActId());
            //可以后期修改一下
            actShop = actShopMapper.findByInVo(actShopInVo);
            nums = actShop.getVoteNum() == null ? 0 : actShop.getVoteNum();
        }else if(invo.getShopId()==null&&invo.getPlayerUserId()!=null){
            key = "voteNums_" + invo.getActId() + "_player_" +invo.getPlayerUserId();
            ActUserInVo actUserInVo = new ActUserInVo();
            actUserInVo.setUserId(invo.getPlayerUserId());
            actUserInVo.setActId(invo.getActId());
            //可以后期修改一下
            actUser = actUserMapper.findByInVo(actUserInVo);
            nums = actUser.getVoteNum() == null ? 0 : actUser.getVoteNum();
        }
        Integer voteNums = redisCache.get(key, Integer.class);
        if (voteNums == null) {
            voteNums = nums;
        }
        if(invo.getType()== Vote.VOTE_ADD){
            voteNums ++;
        }else{
            voteNums --;
        }
        redisCache.set(key, voteNums, 1l, TimeUnit.DAYS);
        Long time = System.currentTimeMillis();
        if (time > (viewArticleTime + 300000)) {    //5分钟更新一次数据到数据库,viewArticleTime:写数据库的周期
            viewArticleTime = time;
            if(actShop != null){
                actShop.setVoteNum(voteNums);
                actShopMapper.updateByPrimaryKeySelective(actShop);
            }
            if(actUser != null){
                ActUserInVo actUserInVo = new ActUserInVo();
                actUserInVo.setId(actUser.getId());
                actUserInVo.setVoteNum(voteNums);
                actUserMapper.updateByPrimaryKeySelective(actUserInVo);
            }
            redisCache.del(key);
        }
        return voteNums;
    }

    @Override
    public Integer voteNumsNow(VoteInVo invo) {

        Integer nums = 0;
        ActShop actShop = null;
        ActUserOut actUser = null;
        ActGroup actGroup = null;
        ActSkuOut actSku = null;
        if(invo.getShopId()!=null&&invo.getPlayerUserId()!=null){
            ActGroupInVo actGroupInVo = new ActGroupInVo();
            actGroupInVo.setActId(invo.getActId());
            actGroupInVo.setShopId(invo.getShopId());
            actGroupInVo.setUserId(invo.getPlayerUserId());
            actGroup = actGroupMapper.findByInVo(actGroupInVo);
            nums = actGroup.getGroupVoteNum() == null ? 0 : actGroup.getGroupVoteNum();

        }else if(invo.getShopId()!=null&&invo.getPlayerUserId()==null){
            ActShopInVo actShopInVo = new ActShopInVo();
            actShopInVo.setShopId(invo.getShopId());
            actShopInVo.setActId(invo.getActId());
            //可以后期修改一下
            actShop = actShopMapper.findByInVo(actShopInVo);
            nums = actShop.getVoteNum() == null ? 0 : actShop.getVoteNum();
        }else if(invo.getShopId()==null&&invo.getPlayerUserId()!=null){
            ActUserInVo actUserInVo = new ActUserInVo();
            actUserInVo.setUserId(invo.getPlayerUserId());
            actUserInVo.setActId(invo.getActId());
            //可以后期修改一下
            actUser = actUserMapper.findByInVo(actUserInVo);
            nums = actUser.getVoteNum() == null ? 0 : actUser.getVoteNum();
        }else if(invo.getSkuId()!=null){
            ActSkuInVo actSkuInVo = new ActSkuInVo();
            actSkuInVo.setSkuId(invo.getSkuId());
            actSkuInVo.setActId(invo.getActId());
            actSku = actSkuMapper.findByInVo(actSkuInVo);
            nums = actSku.getVoteNum() == null ? 0 : actSku.getVoteNum();
        }
        if(invo.getType()== Vote.VOTE_ADD){
            nums ++;
        }else{
            nums --;
        }

            if(actShop != null&&actUser == null){
                actShop.setVoteNum(nums);
                actShopMapper.updateByPrimaryKeySelective(actShop);
            }
            if(actShop == null&&actUser != null){
                ActUserInVo actUserInVo = new ActUserInVo();
                actUserInVo.setId(actUser.getId());
                actUserInVo.setVoteNum(nums);
                actUserMapper.updateByPrimaryKeySelective(actUserInVo);
            }
            if(actGroup!=null){
                actGroup.setGroupVoteNum(nums);
                actGroupMapper.updateByPrimaryKeySelective(actGroup);
            }
            if(actSku!=null){
                ActSkuInVo actSkuInVo = new ActSkuInVo();
                actSkuInVo.setId(actSku.getId());
                actSkuInVo.setVoteNum(nums);
                actSkuMapper.updateByPrimaryKeySelective(actSkuInVo);
            }


        return nums;
    }

    @Override
    public Integer zanNumsNow(Zan zan) {
        Topic topic = topicMapper.selectByPrimaryKey(zan.getRefId());
        /*TopicInVo topicInVo = new TopicInVo();
        topicInVo.setUserId(topic.getUserId());
        Integer integer = topicMapper.zanTotalForUser(topicInVo);*/
        //注意:此处需要修改，为了以后的多个活动做准备，需要根据userId来查询选手参加了哪些活动，然后全部修改其投票数目
        ActUserInVo actUserInVo = new ActUserInVo();
        actUserInVo.setUserId(topic.getUserId());
        actUserInVo.setActId(actSkuConfig.getActId());//先默认查询37这个活动，准备让同类型的活动的投票数都相同，后期可以扩展的时候变动
        List<ActUser> actUsers = actUserMapper.selectByUserId(actUserInVo);
        //注意:此处需要修改，为了以后的多个活动做准备，需要根据userId来查询选手参加了哪些活动，然后全部修改其投票数目
        ActTopicInVo actTopicInVo = new ActTopicInVo();
        actTopicInVo.setTopicId(topic.getId());
        //actTopicInVo.setActId(actSkuConfig.getActId());//先默认查询37这个活动，准备让同类型的活动的投票数都相同，后期可以扩展的时候变动
        List<ActTopic> actTopics = actTopicMapper.selectByTopicId(actTopicInVo);
        Integer integer =0;
        Integer numForTopic = 0;
        if(actUsers!=null&&actUsers.size()>0){
            integer = actUsers.get(0).getVoteNum();
        }
        if(actTopics!=null&&actTopics.size()>0){
            numForTopic = actTopics.get(0).getVoteNum();
        }
        if(zan.getZanType()== Zan.ZAN_ADD){
            integer ++;
            numForTopic++;
        }else{
            integer --;
            numForTopic--;
        }
        //当integer小于0的时候,直接返回0
        if(integer<0){
            integer = 0;
        }
        if(numForTopic<0){
            numForTopic = 0;
        }
        //注意:此处需要修改，为了以后的多个活动做准备，需要根据userId来查询选手参加了哪些活动，然后全部修改其投票数目
        ActUserInVo invo = new ActUserInVo();
        invo.setUserId(topic.getUserId());
        invo.setVoteNum(integer);
        ActTopicInVo topicInVo = new ActTopicInVo();
        topicInVo.setTopicId(topic.getId());
        topicInVo.setVoteNum(numForTopic);
        /*//此处判断活动id，是为了方便以后的扩展，当需要对单个活动进行投票的时候，就传入acrId
        if(zan.getActId()!=null) {
            actUserInVo.setActId(zan.getActId());
        }*/
        actUserMapper.updateForVoteNums(invo);
        actTopicMapper.updateForVoteNums(topicInVo);
        return integer;
    }

    @Override
    public Integer zanTotal(Long userId) {
        TopicInVo topicInVo = new TopicInVo();
        topicInVo.setUserId(userId);//个人主页的userId
        Integer integer = topicMapper.zanTotalForUser(topicInVo);
        return integer;
    }

    @Override
     public   Map<String,Integer> actVoteNums(Long userId,Long actId) {
        String keyUser = "actVoteNumsUser_" + actId + "_" +userId;
        String keySku  = "actVoteNumsSku_" + actId + "_" +userId;
        Integer userNums = redisCache.get(keyUser, Integer.class);
        Integer skuNums = redisCache.get(keySku, Integer.class);
        userNums = userNums == null ? 1 : userNums;
        skuNums = skuNums == null ? 1 : skuNums;
        Map<String,Integer> map = new HashMap<String,Integer>();
        map.put("user",userNums);
        map.put("sku",skuNums);
        return map;
    }
}
