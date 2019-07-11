package com.xq.live.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xq.live.common.RedisCache;
import com.xq.live.dao.*;
import com.xq.live.model.*;
import com.xq.live.service.GoodsBargainLogService;
import com.xq.live.vo.in.ActInfoInVo;
import com.xq.live.vo.in.GoodsBargainLogInVo;
import com.xq.live.vo.in.GoodsGoldLogInVo;
import com.xq.live.vo.out.GoodsBargainLogOut;
import com.xq.live.vo.out.GoodsSkuOut;
import com.xq.live.web.utils.RedEnvelope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by ss on 2018/11/2.
 */
@Service
public class GoodsBargainLogServiceImpl implements GoodsBargainLogService {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ActInfoMapper actInfoMapper;

    @Autowired
    private GoodsSkuMapper goodsSkuMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private GoodsBargainLogMapper goodsBargainLogMapper;

    @Autowired
    private GoodsGoldLogMapper goodsGoldLogMapper;


    /**
     * 查询砍菜人列表和缓存的信息
     * @param inVo
     * @return
     * localhost:8080/bargain/skuGroup?shopId=35&userId=52108&skuId=218&parentId=1400&groupId=12
     */
    @Override
    public List<GoodsBargainLogOut> skuGrouplist(GoodsBargainLogInVo inVo) {
        Date time = new Date();

        //获取缓存
        String skuKey="ActId_"+inVo.getActId()+"GoodsBargainGroup_"+"parent_id_"+inVo.getParentId()+"sku_id_"+inVo.getSkuId()+"group_id_"+inVo.getGroupId();
        String hget = (String)redisCache.hget("ActId_"+inVo.getActId()+"GoodsBargainGroup_parent_id_" + inVo.getParentId(),skuKey);
        if (hget==null||hget.length()<1){
            return null;//用户购买成功，缓存删除，砍菜已结束
        }
        GoodsBargainLogOut goodsBargainLogOut = JSON.parseObject(hget, GoodsBargainLogOut.class);
        if (goodsBargainLogOut==null){
            return null;
        }
        if (time.getTime()>=goodsBargainLogOut.getEndTime().getTime()){
            //获取缓存
            String bargainKey="ActId_"+inVo.getActId()+"GoodsBargainGroup_"+"parent_id_" + inVo.getUserId()+"sku_id_"+goodsBargainLogOut.getSkuId()+"group_id_"+goodsBargainLogOut.getGroupId();
            String goldKey="ActId_"+inVo.getActId()+"GoodsGoldGroup_"+"parent_id_" + inVo.getUserId()+"ref_id_"+goodsBargainLogOut.getSkuId()+"group_id_"+goodsBargainLogOut.getGroupId();
            redisCache.hdel("ActId_"+inVo.getActId()+"GoodsGoldGroup_parent_id_" + inVo.getUserId(), goldKey);
            redisCache.hdel("ActId_"+inVo.getActId()+"GoodsBargainGroup_parent_id_" + inVo.getUserId(), bargainKey);
            //砍菜时间已失效
            return null;
        }
        List<GoodsBargainLog> skulist=goodsBargainLogMapper.selectByGroup(inVo);//砍菜价钱
        GoodsGoldLogInVo goldInVo=new GoodsGoldLogInVo();
        goldInVo.setGroupId(inVo.getGroupId());
        goldInVo.setRefId(inVo.getSkuId());
        goldInVo.setUserId(inVo.getParentId());
        List<GoodsGoldLog> goldLog=goodsGoldLogMapper.selectByGroup(goldInVo);//金币
        if (skulist.size()!=goldLog.size()){
            return null;
        }

        List<GoodsBargainLogOut> logOuts=new ArrayList<GoodsBargainLogOut>();
        logOuts.add(goodsBargainLogOut);
        Integer i=0;
        for (GoodsGoldLog log:goldLog){
            GoodsBargainLogOut gainLog=new GoodsBargainLogOut();
            gainLog.setGroupId(log.getGroupId());
            gainLog.setShopId(log.getShopId());
            gainLog.setSkuId(log.getRefId());
            gainLog.setGoldAmount(log.getGoldAmount());
            gainLog.setSkuAmount(skulist.get(i).getSkuAmount());
            User user=userMapper.selectByPrimaryKey(log.getUserId());
            gainLog.setUser(user);
            if (user!=null){
                if (!user.getId().toString().equals(inVo.getParentId().toString())){
                    gainLog.setParentId(inVo.getParentId());
                }
            }
            gainLog.setCreateTime(log.getCreateTime());
            logOuts.add(gainLog);
            i++;
        }
        return logOuts;
    }

    /**
     * 查询砍菜缓存
     * @param inVo
     * @return
     */
    @Override
    public List<GoodsBargainLogOut> skuGroupRedis(GoodsBargainLogInVo inVo) {
        Date time = new Date();
        List<GoodsBargainLogOut> logOuts=new ArrayList<GoodsBargainLogOut>();
        ActInfoInVo actInfoInVo = new ActInfoInVo();
        if (inVo.getActId()!=null){
            actInfoInVo.setId(inVo.getActId());
        }
        if (inVo.getType()!=null){
            actInfoInVo.setType(inVo.getType());
        }
        List<ActInfo> actList=actInfoMapper.actTypeList(actInfoInVo);
        for (ActInfo actInfo:actList){
            Map<Object,String> keyArrays = redisCache.hmget("ActId_" + actInfo.getId() + "GoodsBargainGroup_parent_id_" + inVo.getUserId());
            if(keyArrays!=null||keyArrays.size()>=0){
                for (Map.Entry<Object, String> a: keyArrays.entrySet()){
                    Object key =a.getKey();
                    String value=a.getValue();
                    if (key!=null||value!=null){
                        GoodsBargainLogOut goodsBargainLogOut = JSON.parseObject(value, GoodsBargainLogOut.class);
                        if (time.getTime()-goodsBargainLogOut.getEndTime().getTime()>=0){
                            //获取缓存
                            String skuKey="ActId_"+actInfo.getId()+"GoodsBargainGroup_"+"parent_id_" + inVo.getUserId()+"sku_id_"+goodsBargainLogOut.getSkuId()+"group_id_"+goodsBargainLogOut.getGroupId();
                            String valueKey="ActId_"+actInfo.getId()+"GoodsGoldGroup_"+"parent_id_" + inVo.getUserId()+"ref_id_"+goodsBargainLogOut.getSkuId()+"group_id_"+goodsBargainLogOut.getGroupId();
                            redisCache.hdel("ActId_" + actInfo.getId() + "GoodsGoldGroup_parent_id_" + inVo.getUserId(), valueKey);
                            redisCache.hdel("ActId_" + actInfo.getId() + "GoodsBargainGroup_parent_id_" + inVo.getUserId(), skuKey);
                            //砍菜时间已失效
                        }else {
                            if (goodsBargainLogOut.getShopId()>0){
                                Shop shop=shopMapper.selectByPrimaryKey(goodsBargainLogOut.getShopId());
                                goodsBargainLogOut.setShop(shop);
                            }
                            if (inVo.getSkuId()!=null){
                                if (inVo.getSkuId().toString().equals(goodsBargainLogOut.getSkuId().toString())){
                                    logOuts.add(goodsBargainLogOut);
                                    break;
                                }
                                continue;
                            }
                            logOuts.add(goodsBargainLogOut);
                        }
                    }
                }
            }
        }
        // 进行排序
        Collections.sort(logOuts, new Comparator<GoodsBargainLogOut>() {
            public int compare(GoodsBargainLogOut o1, GoodsBargainLogOut o2) {
                if (o1.getGroupId()>(o2.getGroupId())) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        return logOuts;
    }

}
