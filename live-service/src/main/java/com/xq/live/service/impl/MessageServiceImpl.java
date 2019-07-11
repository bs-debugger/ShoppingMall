package com.xq.live.service.impl;

import com.alibaba.fastjson.JSON;
import com.xq.live.common.Pager;
import com.xq.live.common.RedisCache;
import com.xq.live.dao.MessageMapper;
import com.xq.live.dao.MessageTextMapper;
import com.xq.live.model.Message;
import com.xq.live.model.MessageText;
import com.xq.live.model.User;
import com.xq.live.service.MessageService;
import com.xq.live.service.UserService;
import com.xq.live.vo.in.FormIdInVo;
import com.xq.live.vo.in.MessageInVo;
import com.xq.live.vo.out.MessageOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @package: com.xq.live.service.impl
 * @description: 站内信service实现类
 * @author: zhangpeng32
 * @date: 2018/3/28 18:49
 * @version: 1.0
 */
@Service
public class MessageServiceImpl implements MessageService{

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MessageTextMapper messageTextMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserService userService;

    @Override
    public Long add(Message msg) {
        int ret = messageMapper.insert(msg);
        if(ret < 1){
            return null;
        }
        return msg.getId();
    }

    @Override
    public Integer update(Message msg) {
        return messageMapper.updateByPrimaryKeySelective(msg);
    }

    @Override
    public Pager<MessageOut> list(MessageInVo inVo) {
        Pager<MessageOut> result = new Pager<MessageOut>();
        int total = messageMapper.listTotal(inVo);
        if(total > 0){
            List<MessageOut> list = messageMapper.list(inVo);
            result.setList(list);
        }
        result.setTotal(total);
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public List<MessageOut> myMsgList(MessageInVo inVo) {
        return messageMapper.list(inVo);
    }

    @Override
    @Transactional
    public MessageOut detail(MessageInVo inVo) {
        //1、查询消息记录是否存在，存在则查询，不存在则需要向message表新增一条记录
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("receiverId", inVo.getReceiverId());
        paramsMap.put("messageTextId", inVo.getMessageTextId());
        Message message = messageMapper.selectByRecId(paramsMap);
        if(message == null){
            //1、向message表插入记录
            message = new Message();
            message.setReceiverId(inVo.getReceiverId());
            message.setMessageTextId(inVo.getMessageTextId());
            message.setStatus(Message.MESSAGE_STATUS_READ); //标记为已读
            message.setIsDeleted(0);    //设置未删除
            if(inVo.getType() != null){
                if(inVo.getType() == MessageText.MESSAGE_TEXT_TYPE_SYSTEM){
                    message.setSenderId(Message.MESSAGE_SEND_ID_SYSTEM);    //系统消息
                }else if(inVo.getType() == MessageText.MESSAGE_TEXT_TYPE_COMMON){
                    message.setSenderId(Message.MESSAGE_SEND_ID_COMMON);    //公共消息
                }else{
                    message.setSenderId(Message.MESSAGE_SEND_ID_OTHER);    //其他???
                }
            }
            messageMapper.insert(message);
        }

        //2、查询明细
        MessageOut res =  messageMapper.detail(inVo);
        return res;
    }

    @Override
    public Integer delete(MessageInVo inVo) {
        Integer result = 0;

        //1、查询消息记录是否存在，存在则查询，不存在则需要向message表新增一条记录
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("receiverId", inVo.getReceiverId());
        paramsMap.put("messageTextId", inVo.getMessageTextId());
        Message message = messageMapper.selectByRecId(paramsMap);
        if(message == null){
            //1、向message表插入记录
            message = new Message();
            message.setReceiverId(inVo.getReceiverId());
            message.setMessageTextId(inVo.getMessageTextId());
            message.setIsDeleted(1);    //设置已删除
            message.setStatus(Message.MESSAGE_STATUS_UNREAD); //标记为未读
            if(inVo.getType() != null){
                if(inVo.getType() == MessageText.MESSAGE_TEXT_TYPE_SYSTEM){
                    message.setSenderId(Message.MESSAGE_SEND_ID_SYSTEM);    //系统消息
                }else if(inVo.getType() == MessageText.MESSAGE_TEXT_TYPE_COMMON){
                    message.setSenderId(Message.MESSAGE_SEND_ID_COMMON);    //公共消息
                }else{
                    message.setSenderId(Message.MESSAGE_SEND_ID_OTHER);    //其他???
                }
            }
            result = messageMapper.insert(message);
        }else{  //如果记录已经存在，则更新删除状态即可
            message.setIsDeleted(1);    //设置已删除
            message.setStatus(Message.MESSAGE_STATUS_READ);//标记为已读
            result = messageMapper.updateByPrimaryKey(message);
        }
        return result;
    }

    @Override
    public Integer addMessage(String title, String content, Integer type, Long receiverId, Long senderId) {
        MessageText messageText=new MessageText();
        messageText.setTitle(title);
        messageText.setContent(content);
        messageText.setType(type);
        messageText.setIsDeleted(0);
        messageTextMapper.insert(messageText);

        Message message=new Message();
        message.setMessageTextId(messageText.getId());
        message.setReceiverId(receiverId);
        message.setSenderId(senderId);
        message.setStatus(Message.MESSAGE_STATUS_UNREAD);
        message.setIsDeleted(0);
        messageMapper.insert(message);
        return null;
    }

    @Override
    public Pager<MessageOut> listAct(MessageInVo inVo) {
        Pager<MessageOut> result = new Pager<MessageOut>();
        int total = messageMapper.listActTotal(inVo);
        if(total > 0){
            List<MessageOut> list = messageMapper.listAct(inVo);
            result.setList(list);
        }
        result.setTotal(total);
        result.setPage(inVo.getPage());
        return result;

    }

    @Override
    public Integer readAllNotic(Long userId) {
       int i= messageMapper.readAllNotic(userId);
        return i;
    }

    @Override
    public Integer readAct(MessageInVo inVo) {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("receiverId", inVo.getReceiverId());
        paramsMap.put("messageTextId", inVo.getMessageTextId());
        Message message = messageMapper.selectByRecId(paramsMap);
        if(message != null&&message.getStatus()==1){//避免重复插入数据
            return 0;
        }
        int i=messageMapper.readAct(inVo);
        return i;
    }

    @Override
    public List<MessageOut> unreadMessageTotal(MessageInVo inVo) {
        List<MessageOut> result=new ArrayList<>();

        //1 系统消息
        inVo.setType(MessageText.MESSAGE_TEXT_TYPE_SYSTEM);
        int i=messageMapper.unreadMessageTotal(inVo);
        List<MessageOut> list = messageMapper.listAct(inVo);
        if(list!=null&&list.size()>0){
            list.get(0).setTotal(i);//系统消息总数
            result.add(list.get(0));
        }

        //2 公共消息
        inVo.setType(MessageText.MESSAGE_TEXT_TYPE_COMMON);
        i=messageMapper.unreadMessageTotal(inVo);
        list = messageMapper.listAct(inVo);
        if(list!=null&&list.size()>0){
            list.get(0).setTotal(i);//公共消息总数
            result.add(list.get(0));
        }

        //3 个人消息
        inVo.setType(MessageText.MESSAGE_TEXT_TYPE_PRIVATE);
        i=messageMapper.unreadMessageTotal(inVo);
        list = messageMapper.listAct(inVo);
        if(list!=null&&list.size()>0){
            list.get(0).setTotal(i);//个人消息总数
            result.add(list.get(0));
        }
        return result;
    }

    @Override
    public List<FormIdInVo> getFormIdList(Long userId) {
        List<FormIdInVo> formIdInVos=new ArrayList<FormIdInVo>();
        User usr =userService.getUserById(userId);
        if(usr==null||usr.getId()==null){
            return null;
        }
        Map<Object, String> formIds = redisCache.hmget("formId_"+usr.getOpenId());
        if(formIds==null||formIds.size()==0){
            return null;
        }
        for(Map.Entry<Object, String> a: formIds.entrySet() ){
            Object key =a.getKey();
            String value=a.getValue();
            FormIdInVo formIdInVo = JSON.parseObject(value, FormIdInVo.class);
            Date date = new Date();   //当前时间
            Calendar calendar = Calendar.getInstance(); //得到日历
            calendar.setTime(date);//把当前时间赋给日历
            calendar.add(Calendar.DAY_OF_MONTH, -7);  //设置为七天前
            date = calendar.getTime();
            if(formIdInVo.getCerateTime().getTime()<date.getTime()){
                redisCache.hdel("formId_"+usr.getOpenId(),key);//超过七天缓存的formId删除
            }else{
                formIdInVos.add(formIdInVo);
            }
        }
        return formIdInVos;
    }
}
