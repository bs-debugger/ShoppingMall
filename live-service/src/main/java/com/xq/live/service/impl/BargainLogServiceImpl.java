package com.xq.live.service.impl;

import com.xq.live.common.RedisCache;
import com.xq.live.dao.BargainLogMapper;
import com.xq.live.dao.GoldLogMapper;
import com.xq.live.dao.SkuMapper;
import com.xq.live.dao.UserMapper;
import com.xq.live.model.BargainLog;
import com.xq.live.model.GoldLog;
import com.xq.live.model.Sku;
import com.xq.live.model.User;
import com.xq.live.service.BargainLogService;
import com.xq.live.vo.in.BargainLogInVo;
import com.xq.live.vo.in.GoldLogInVo;
import com.xq.live.vo.in.SkuInVo;
import com.xq.live.vo.out.BargainLogOut;
import com.xq.live.vo.out.SkuForTscOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户砍菜减价
 * Created by ss on 2018/8/11.
 */
@Service
public class BargainLogServiceImpl implements BargainLogService{
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private GoldLogMapper goldLogMapper;

    @Autowired
    private BargainLogMapper bargainLogMapper;

    @Autowired
    private RedisCache redisCache;




    /**
     * 查询砍菜人列表和缓存的信息
     * @param inVo
     * @return
     * localhost:8080/bargain/skuGroup?shopId=35&userId=52108&skuId=218&parentId=1400&groupId=12
     */
    @Override
    public List<BargainLogOut> skuGrouplist(BargainLogInVo inVo) {
        //获取缓存
        String skuKey="BargainGroup_"+"parent_id"+inVo.getParentId()+"sku_id_"+inVo.getSkuId()+"group_id_"+inVo.getGroupId();
        Map<String,Object> stringBargainMap=redisCache.get(skuKey, Map.class);
        if (stringBargainMap==null){
            return null;
        }
        BargainLogOut bargainLogOut=(BargainLogOut)stringBargainMap.get(skuKey);
        if (bargainLogOut==null){
            return null;
        }
        List<BargainLog> skulist=bargainLogMapper.selectByGroup(inVo);//砍菜价钱
        GoldLogInVo goldInVo=new GoldLogInVo();
        goldInVo.setGroupId(inVo.getGroupId());
        goldInVo.setRefId(inVo.getSkuId());
        goldInVo.setUserId(inVo.getParentId());
        List<GoldLog> goldLog=goldLogMapper.selectByGroup(goldInVo);//金币
        if (skulist.size()!=goldLog.size()){
            return null;
        }
        List<User> userList=new ArrayList<>();
        for (GoldLog log:goldLog){
            User user=userMapper.selectByPrimaryKey(log.getUserId());
            userList.add(user);
        }

        List<BargainLogOut> logOuts=new ArrayList<BargainLogOut>();
        logOuts.add(bargainLogOut);
        Integer i=0;
        for (GoldLog log:goldLog){
            BargainLogOut gainLog=new BargainLogOut();
            gainLog.setGroupId(log.getGroupId());
            gainLog.setShopId(log.getShopId());
            gainLog.setSkuId(log.getRefId());
            gainLog.setGoldAmount(log.getGoldAmount());

            gainLog.setSkuAmount(skulist.get(i).getSkuAmount());
            if (userList.get(i)!=null){
                gainLog.setUserId(userList.get(i).getId());
                gainLog.setNickName(userList.get(i).getNickName());
                gainLog.setIconUrl(userList.get(i).getIconUrl());
                gainLog.setUserName(userList.get(i).getUserName());
                if (!userList.get(i).getId().toString().equals(inVo.getParentId().toString())){
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
    public List<BargainLogOut> skuGroupRedis(BargainLogInVo inVo) {

        //获取缓存
        String skuKey=null;
        List<BargainLogOut> logOuts=new ArrayList<BargainLogOut>();
        if (inVo.getSkuId()!=null){
            skuKey="BargainGroup_"+"parent_id"+inVo.getUserId()+"sku_id_"+inVo.getSkuId()+"group_id_"+"*";
        }else {
            skuKey="BargainGroup_"+"parent_id"+inVo.getUserId()+"sku_id_"+"*"+"group_id_"+"*";
        }
        //获取缓存
        Set key=redisCache.selAll(skuKey);
        Object[] keylist=key.toArray();

        for (Object sku:keylist){
            System.out.println((String)sku);
            Map<String,Object> stringBargainMap=redisCache.get((String) sku, Map.class);
            if (stringBargainMap==null){
                return null;
            }
            BargainLogOut bargainLogOut=(BargainLogOut)stringBargainMap.get(sku.toString());
            SkuInVo skuInVo = new SkuInVo();
            skuInVo.setId(bargainLogOut.getSkuId());
            List<SkuForTscOut> skuForTscOuts=skuMapper.queryKjcList(skuInVo);
            bargainLogOut.setSkuName(skuForTscOuts.get(0).getSkuName());
            bargainLogOut.setPicUrl(skuForTscOuts.get(0).getPicUrl());
            bargainLogOut.setSkuType(skuForTscOuts.get(0).getSkuType());
            if (bargainLogOut==null){
                return null;
            }
            logOuts.add(bargainLogOut);
        }
        // 进行排序
        for (int i=0;i<logOuts.size()-1;i++){
            for (int j=0;j<logOuts.size()-1-i;j++){
                if (logOuts.get(j).getGroupId()>logOuts.get(j+1).getGroupId()){
                    BargainLogOut temp= logOuts.get(j);
                    logOuts.set(j,logOuts.get(j+1));
                    logOuts.set(j+1,temp);
                }
            }
        }
        return logOuts;
    }
}
