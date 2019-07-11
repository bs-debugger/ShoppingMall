package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.common.RedisCache;
import com.xq.live.dao.*;
import com.xq.live.model.AccessLog;
import com.xq.live.model.Attachment;
import com.xq.live.model.Topic;
import com.xq.live.model.User;
import com.xq.live.service.TopicService;
import com.xq.live.vo.in.CommentInVo;
import com.xq.live.vo.in.TopicInVo;
import com.xq.live.vo.in.ZanInVo;
import com.xq.live.vo.out.TopicForZanOut;
import com.xq.live.vo.out.TopicOut;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-08 14:01
 * @copyright:hbxq
 **/
@Service
public class TopicServiceImpl implements TopicService {
    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ZanMapper zanMapper;

    @Autowired
    private AccessLogMapper accessLogMapper;

    @Autowired
    private RedisCache redisCache;


    @Override
    public TopicForZanOut selectOne(Long id) {
        Topic topic = topicMapper.selectByPrimaryKey(id);
        TopicForZanOut topicForZanOut = new TopicForZanOut();
        if(topic != null){
            BeanUtils.copyProperties(topic,topicForZanOut);
            topicForZanOut = getPicUrls(topicForZanOut);
        }
        return topicForZanOut;
    }

    @Override
    public TopicForZanOut selectByZan( TopicInVo inVo) {
        TopicForZanOut result = topicMapper.selectByZan(inVo);
        /*if(inVo==null||inVo.getZanSourceType()==null||inVo.getZanUserName()==null){
            return null;
        }*/
        /*try {
            //新开一个线程记录访问日志
            new Thread(new Runnable() {
                @Override
                public void run() {
                    *//**
                     * 1、查询用户是否存在访问记录
                     * 2、记录用户访问日志
                     *//*
                    AccessLog accessLog = new AccessLog();
                    accessLog.setUserId(Long.parseLong(inVo.getZanUserId()));
                    accessLog.setUserName(inVo.getZanUserName());
                    accessLog.setUserIp(inVo.getZanUserIp());
                    accessLog.setSource(inVo.getZanSourceType());
                    accessLog.setRefId(inVo.getId());
                    accessLog.setBizType(AccessLog.BIZ_TYPE_TOPIC_VIEW);
                    int cnt = accessLogMapper.checkRecordExist(accessLog);
                    if (cnt == 0) {
                        int logCnt = accessLogMapper.insert(accessLog);
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        *//**
         * 查询文章的浏览量
         *//*
        Map<String, Object> logMap = new HashMap<String, Object>();
        logMap.put("bizType", AccessLog.BIZ_TYPE_TOPIC_VIEW);
        logMap.put("refId", inVo.getId());
        int viewNum = accessLogMapper.countViewNum(logMap);

        result.setViewNum(viewNum);*/
        return result;
    }

    @Override
    public Long add(Topic topic) {
        int r = topicMapper.insert(topic);
        if(r == 0){
            return null;
        }
        return topic.getId();
    }

    @Override
    public int update(Topic topic) {
        topic.setUpdateTime(new Date());
        return topicMapper.updateByPrimaryKeySelective(topic);
    }

    @Override
    public int delete(Long id) {
        Topic topic = new Topic();
        /*Topic topic = topicMapper.selectByPrimaryKey(id);
        if(topic==null){
            return 0;
        }*/
        topic.setId(id);
        topic.setIsDeleted(1);//1是删除状态
        int i = topicMapper.updateByPrimaryKeySelective(topic);
        return i;
    }

    @Override
    public Pager<TopicForZanOut> list(TopicInVo inVo) {
        Pager<TopicForZanOut> pager = new Pager<TopicForZanOut>();
        int total = topicMapper.listTotal(inVo);

        if(total > 0){
            List<TopicForZanOut> list = topicMapper.list(inVo);
            /*List<TopicOut> listForOut = new ArrayList<TopicOut>();*/
            for(TopicForZanOut t : list){
                /**
                 * 及时读取浏览量和转发量
                 */
                String key = "topicHits_" + t.getId().toString();
                Integer hits = redisCache.get(key, Integer.class);
                String keyForTrans = "topicTrans_" + t.getId().toString();
                Integer trans = redisCache.get(keyForTrans, Integer.class);

                if (hits != null) {
                    t.setHitNum(hits);
                }
                if (trans != null) {
                    t.setTransNum(trans);
                }
                t = getPicUrls(t);
                /**
                 * 通过用户id查询用户详情
                 *//*
                User user = userMapper.selectByPrimaryKey(t.getUserId());
                CommentInVo commentInVo = new CommentInVo();
                commentInVo.setRefId(t.getId());
                commentInVo.setCmtType(2);
                int listTotal = commentMapper.listTotal(commentInVo);//查询评论总数
                ZanInVo zanInVo = new ZanInVo();
                zanInVo.setRefId(t.getId());
                zanInVo.setType(2);
                int zan = zanMapper.total(zanInVo);//查询点赞总数
                TopicOut topicOut = new TopicOut();
                BeanUtils.copyProperties(t,topicOut);
                topicOut.setIconUrl(user.getIconUrl());
                topicOut.setCommentNum(listTotal);
                topicOut.setZan(zan);
                listForOut.add(topicOut);*/
            }
            pager.setList(list);
        }
        pager.setTotal(total);  //总记录数
        pager.setPage(inVo.getPage());     //当前页
        return pager;
    }

    @Override
    public List<TopicForZanOut> listHome(TopicInVo inVo) {
            List<TopicForZanOut> list = topicMapper.list(inVo);
            for(TopicForZanOut t : list){
                /**
                 * 及时读取浏览量和转发量
                 */
                String key = "topicHits_" + t.getId().toString();
                Integer hits = redisCache.get(key, Integer.class);
                String keyForTrans = "topicTrans_" + t.getId().toString();
                Integer trans = redisCache.get(keyForTrans, Integer.class);

                if (hits != null) {
                    t.setHitNum(hits);
                }
                if (trans != null) {
                    t.setTransNum(trans);
                }
                t = getPicUrls(t);
            }
        return list;
    }

    @Override
    public Pager<TopicForZanOut> myList(TopicInVo inVo) {
        Pager<TopicForZanOut> pager = new Pager<TopicForZanOut>();
        int total = topicMapper.myListTotal(inVo);

        if(total > 0){
            List<TopicForZanOut> list = topicMapper.myList(inVo);
            /*List<TopicOut> listForOut = new ArrayList<TopicOut>();
            for(TopicForZanOut t : list){
                t = getPicUrls(t);
                User user = userMapper.selectByPrimaryKey(t.getUserId());
                CommentInVo commentInVo = new CommentInVo();
                commentInVo.setRefId(t.getId());
                commentInVo.setCmtType(2);
                int listTotal = commentMapper.listTotal(commentInVo);
                ZanInVo zanInVo = new ZanInVo();
                zanInVo.setRefId(t.getId());
                zanInVo.setType(2);
                int zan = zanMapper.total(zanInVo);
                TopicOut topicOut = new TopicOut();
                BeanUtils.copyProperties(t,topicOut);
                topicOut.setIconUrl(user.getIconUrl());
                topicOut.setCommentNum(listTotal);
                topicOut.setZan(zan);
                listForOut.add(topicOut);*/
            for(TopicForZanOut t : list){
                /**
                 * 及时读取浏览量和转发量
                 */
                String key = "topicHits_" + t.getId().toString();
                Integer hits = redisCache.get(key, Integer.class);
                String keyForTrans = "topicTrans_" + t.getId().toString();
                Integer trans = redisCache.get(keyForTrans, Integer.class);

                if (hits != null) {
                    t.setHitNum(hits);
                }
                if (trans != null) {
                    t.setTransNum(trans);
                }
                t = getPicUrls(t);
            }
            pager.setList(list);
            }

        pager.setTotal(total);  //总记录数
        pager.setPage(inVo.getPage());     //当前页
        return pager;
    }

    @Override
    public List<TopicForZanOut> top(TopicInVo inVo) {
        List<TopicForZanOut> list = topicMapper.list(inVo);
        /*List<TopicOut> listForOut = new ArrayList<TopicOut>();*/
        for(TopicForZanOut t : list){
            t = getPicUrls(t);
            /*User user = userMapper.selectByPrimaryKey(t.getUserId());
            CommentInVo commentInVo = new CommentInVo();
            commentInVo.setRefId(t.getId());
            commentInVo.setCmtType(2);
            int listTotal = commentMapper.listTotal(commentInVo);
            TopicOut topicOut = new TopicOut();
            BeanUtils.copyProperties(t,topicOut);
            topicOut.setIconUrl(user.getIconUrl());
            topicOut.setCommentNum(listTotal);
            listForOut.add(topicOut);*/
        }
        return list;
    }

    /**
     *
     * @return
     */
    public TopicForZanOut getPicUrls(TopicForZanOut topic){
        if(StringUtils.isNotEmpty(topic.getPicIds())){
            String picIds = topic.getPicIds();
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
                    topic.setPicUrls(picUrls);
                }
            }
        }
        return topic;
    }
}
