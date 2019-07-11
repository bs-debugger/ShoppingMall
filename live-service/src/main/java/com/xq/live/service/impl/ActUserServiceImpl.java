package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.*;
import com.xq.live.model.*;
import com.xq.live.service.ActUserService;
import com.xq.live.vo.in.ActSignInVo;
import com.xq.live.vo.in.ActUserInVo;
import com.xq.live.vo.out.ActUserOut;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lipeng on 2018/4/27.
 */
@Service
public class ActUserServiceImpl implements ActUserService{
    @Autowired
    private ActUserMapper actUserMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private ActSignMapper actSignMapper;

    @Value("${jwt.password.salt}")
    private String salt;

    private static Logger logger = Logger.getLogger(ActInfoServiceImpl.class);

    @Override
    public ActUserOut findByInVo(ActUserInVo inVo) {
        ActUserOut byInVo = null;
        try {
             byInVo = actUserMapper.findByInVo(inVo);//主要是为了防止脏数据，其实也可以直接查询单个
            if (byInVo == null) {
                return null;
            }
            byInVo = getPicUrls(byInVo);
            return byInVo;
        }catch (Exception e){
            logger.error("查询活动选手异常TooManyResultException ：" + e.getMessage());
            return new ActUserOut();
        }
    }

    /**
     *
     * @return
     */
    public ActUserOut getPicUrls(ActUserOut byInVo){
        if(StringUtils.isNotEmpty(byInVo.getPicIds())){
            String picIds = byInVo.getPicIds();
            String[] temPicIds = picIds.split(",");
            if(temPicIds != null && temPicIds.length > 0){
                List<Long> ids = new ArrayList<Long>();
                for(String picId : temPicIds){
                    ids.add(Long.valueOf(picId));
                }
                if(ids.size() > 0){
                    Map<String, Object> paramsMap = new HashMap<String, Object>();
                    paramsMap.put("ids", ids);
                    List<Attachment> picUrls = attachmentMapper.selectByIds(paramsMap);
                    byInVo.setPicUrls(picUrls);
                }
            }
        }
        return byInVo;
    }

    @Override
    public Long add(ActUserInVo inVo) {
        int i = actUserMapper.countByActId(inVo.getActId());
        /*DecimalFormat mFormat = new DecimalFormat("000");//确定格式，把1转换为001
        String s = mFormat.format(i+1);*/
        String s =(i + 1)+"";
        inVo.setUserCode(s);
        int insert = actUserMapper.insert(inVo);//插入数据
        if(insert<1){
            return null;
        }
        User user = userMapper.selectByPrimaryKey(inVo.getUserId());
        user.setAge(inVo.getAge());
        user.setHeight(inVo.getHeight());
        //修改密码----begain
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(user.getUserName() + salt);//密码为userName + salt
        user.setPassword(encoder.encode(user.getPassword()));
        //end
        int k = userMapper.updateByPrimaryKeySelective(user);//修改user表的信息
        if (k < 1) {
            return null;
        }
        return inVo.getId();
    }

    @Override
    public Pager<ActUserOut> listForNewAct(ActUserInVo inVo) {
        Pager<ActUserOut> result = new Pager<ActUserOut>();
        int listTotal = actUserMapper.listTotal(inVo);
        result.setTotal(listTotal);
        if (listTotal > 0) {
            List<ActUserOut> list = actUserMapper.listForNewAct(inVo);
            for (ActUserOut actUserOut : list) {
                actUserOut = getPicUrls(actUserOut);
            }
            //如果是查询分组的，把分组的关联信息加入进去
            if(inVo.getType()!=null&&inVo.getType()== ActUser.ACT_USER_GROUP){
                for (ActUserOut actUserOut : list) {
                    Shop shop = shopMapper.selectByPrimaryKey(actUserOut.getShopId());
                    actUserOut.setLogoUrl(shop.getLogoUrl());
                    actUserOut.setShopName(shop.getShopName());


                    ActSignInVo actSignInVo = new ActSignInVo();
                    actSignInVo.setType(ActSign.ACT_SIGN_TYPE_SHOP);
                    actSignInVo.setRefId(shop.getId());
                    actSignInVo.setActId(actUserOut.getActId());
                    ActSign sign = actSignMapper.isSign(actSignInVo);

                    if(sign!=null) {
                        Sku sku = skuMapper.selectByPrimaryKey(sign.getSkuId());
                        if (sku != null) {
                            actUserOut.setSkuName(sku.getSkuName());
                        }
                    }
                }
            }
            //Collections.sort(list);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public List<ActUserOut> listActForId(ActUserInVo inVo) {
        return actUserMapper.listForNewAct(inVo);
    }

    @Override
    public int udateByLuoXuan(List<ActUserOut> userOuts) {
        return actUserMapper.udateByLuoXuan(userOuts);
    }

    @Override
    public int udateByLuoXuanTwo(List<ActUserOut> userOuts) {
        return actUserMapper.udateByLuoXuanTwo(userOuts);
    }
}
