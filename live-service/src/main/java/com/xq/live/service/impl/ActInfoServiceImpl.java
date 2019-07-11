package com.xq.live.service.impl;

import com.xq.live.common.Constants;
import com.xq.live.common.Pager;
import com.xq.live.common.ShopCodeUtil;
import com.xq.live.config.ConstantsConfig;
import com.xq.live.dao.*;
import com.xq.live.model.*;
import com.xq.live.service.ActInfoService;
import com.xq.live.vo.in.ActInfoInVo;
import com.xq.live.vo.in.ActShopInVo;
import com.xq.live.vo.in.ActUserInVo;
import com.xq.live.vo.out.ActInfoOut;
import com.xq.live.vo.out.ActUserOut;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * 活动实现类
 *
 * @author zhangpeng32
 * @create 2018-02-07 17:21
 **/
@Service
public class ActInfoServiceImpl implements ActInfoService {
    @Autowired
    private ActInfoMapper actInfoMapper;

    @Autowired
    private VoteMapper voteMapper;

    @Autowired
    private ActShopMapper actShopMapper;

    @Autowired
    private ActUserMapper actUserMapper;

    @Autowired
    private AccessLogMapper accessLogMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ConstantsConfig constantsConfig;

    private static Logger logger = Logger.getLogger(ActInfoServiceImpl.class);

    @Override
    public Long add(ActInfo actInfo) {
        actInfo.setCreateTime(new Date());
        int r = actInfoMapper.insert(actInfo);
        if (r < 1) {
            return null;
        }
        return actInfo.getId();
    }

    @Override
    public int update(ActInfo actInfo) {
        return actInfoMapper.updateByPrimaryKeySelective(actInfo);
    }

    @Override
    public int delete(Long id) {
        ActInfo actInfo = new ActInfo();
        actInfo.setId(id);
        actInfo.setIsDeleted(1);
        //        return actInfoMapper.deleteByPrimaryKey(id);
        return actInfoMapper.updateByPrimaryKeySelective(actInfo);
    }

    @Override
    public ActInfo selectOne(Long id) {
        return actInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public Pager<ActInfoOut> list(final ActInfoInVo inVo) {
        Pager<ActInfoOut> result = new Pager<ActInfoOut>();
        //int listTotal = actInfoMapper.listTotal(inVo);
        int listTotal = actInfoMapper.listTotalForShop(inVo);
        result.setTotal(listTotal);
        if (listTotal > 0) {
            // List<ActInfoOut> list = actInfoMapper.list(inVo);
            List<ActInfoOut> list = actInfoMapper.listForShop(inVo);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public List<ActInfoOut> top(ActInfoInVo inVo) {
        return actInfoMapper.list(inVo);
    }

    @Override
    public ActInfoOut detail(final ActInfoInVo inVo) {
        ActInfo actInfo = new ActInfo();
        actInfo.setId(inVo.getId());
        actInfo.setViewNum(0);
        actInfoMapper.updateByPrimaryKeySelective(actInfo);
        ActInfoOut actInfoOut = actInfoMapper.findActInfoById(inVo.getId());
        if (actInfoOut == null) {
            return null;
        }

        /*ActUserInVo actUserInVo = new ActUserInVo();
        actUserInVo.setActId(inVo.getId());
        actUserInVo.setUserId(inVo.getUserId());
        ActUserOut list =null;
        try {
            list = actUserMapper.findByInVo(actUserInVo);
            if (list != null) {
                actInfoOut.setIsSignForUser(ActUser.ACT_USER_IS_SIGN);//已报名
            } else {
                actInfoOut.setIsSignForUser(ActUser.ACT_USER_NO_SIGN);//未报名
            }
        }catch (Exception e){
            //不做操作，如果抛出了异常则证明有脏数据，则是已经报名
            actInfoOut.setIsSignForUser(ActUser.ACT_USER_IS_SIGN);//已报名
            logger.error("查询活动选手异常TooManyResultException ：" + e.getMessage());
        }

        User user = userMapper.selectByPrimaryKey(inVo.getUserId());
        //判断商家是否已经报名了活动
        if(user!=null&&user.getShopId()!=null) {
            ActShopInVo actShopInVo = new ActShopInVo();
            actShopInVo.setActId(inVo.getId());
            actShopInVo.setShopId(user.getShopId());
            try {
                ActShop byInVo = actShopMapper.findByInVo(actShopInVo);
                if (byInVo != null) {
                    actInfoOut.setIsSign(ActShop.ACT_SHOP_IS_SIGN);//已报名
                } else {
                    actInfoOut.setIsSign(ActShop.ACT_SHOP_NO_SIGN);//未报名
                }
            } catch (Exception e) {
                logger.error("查询活动商家异常TooManyResultException ：" + e.getMessage());
            }
        }
        try {
            //新开一个线程记录访问日志
            new Thread(new Runnable() {
                @Override
                public void run() {
                    *//**
                     * 1、查询用户是否存在访问记录
                     * 2、记录用户访问日志
                     *//*
                    AccessLog accessLog = new AccessLog();
                    accessLog.setUserId(inVo.getUserId());
                    accessLog.setUserName(inVo.getUserName());
                    accessLog.setUserIp(inVo.getUserIp());
                    accessLog.setSource(inVo.getSourceType());
                    accessLog.setRefId(inVo.getId());
                    accessLog.setBizType(AccessLog.BIZ_TYPE_ACT_VIEW);
                    int cnt = accessLogMapper.checkRecordExist(accessLog);
                    if (cnt == 0) {
                        int logCnt = accessLogMapper.insert(accessLog);
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

/*        ActInfoOut out = new ActInfoOut();
        out.setId(actInfo.getId());
        out.setActName(actInfo.getActName());
        out.setActStatus(actInfo.getActStatus());
        out.setActUrl(actInfo.getActUrl());
        out.setIsDeleted(actInfo.getIsDeleted());
        out.setCreateTime(actInfo.getCreateTime());
        out.setStartTime(actInfo.getStartTime());
        out.setMainPic(actInfo.getMainPic());
        out.setEndTime(actInfo.getEndTime());
        out.setUpdateTime(actInfo.getUpdateTime());
        //查询投票数
        int voteNum = voteMapper.countByActId(actInfo.getId());
        out.setVoteNum(voteNum);

        //查询参与商家数
        int shopNum = actShopMapper.countByActId(actInfo.getId());
        out.setShopNum(shopNum);

        Map<String, Object> logMap = new HashMap<String, Object>();
        logMap.put("bizType", AccessLog.BIZ_TYPE_ACT_VIEW);
        logMap.put("refId", inVo.getId());
        int viewNum = accessLogMapper.countViewNum(logMap);
        out.setViewNum(viewNum);*/

        return actInfoOut;

    }

    @Override
    public String uploadQRCodeToCos(ActInfoInVo inVo) {
        String imagePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "static" + File.separator + "images" + File.separator + "logo.jpg";

        String destPath = null;
        String text=null;
        if (inVo.getUserId()!=null){
            destPath= Thread.currentThread().getContextClassLoader().getResource("").getPath() + "upload" + File.separator +"ActInfo"+inVo.getId()+"userId"+inVo.getUserId()+System.currentTimeMillis() +".jpg";
            text = constantsConfig.getDomainXqUrl() + "/service?flag=3&actId=37&userId="+inVo.getUserId();
        }else {
            destPath= Thread.currentThread().getContextClassLoader().getResource("").getPath() + "upload" + File.separator +"ActInfo"+inVo.getId()+"actSku"+inVo.getActSku()+System.currentTimeMillis() +".jpg";
            text = constantsConfig.getDomainXqUrl() + "/service?flag=3&actId=37&actSku="+inVo.getActSku();
        }

        //String text = constantsConfig.getDomainXqUrl() + "/service?flag=3&actId=36&userId="+57;
        //生成logo图片到destPath
        try {
            ShopCodeUtil.encodeForAct(text, imagePath, destPath, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        UploadServiceImpl uploadService = new UploadServiceImpl();
        //上传文件到腾讯云cos--缩放0.8
        String imgUrl = uploadService.uploadFileToCos(destPath, "actInfo");
        int i=0;
        do {
            i++;
            if (imgUrl==null){
                imgUrl=uploadService.uploadFileToCos(destPath, "actInfo");
            }
            if (imgUrl!=null){
                break;
            }
            if (i==4){
                break;
            }
        }while (true);
        if (StringUtils.isEmpty(imgUrl)) {
            return null;
        }
        //删除服务器上临时文件
        uploadService.deleteTempImage(new Triplet<String, String, String>(destPath, null, null));
        return imgUrl;
    }
}
