package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.Message;
import com.xq.live.vo.in.FormIdInVo;
import com.xq.live.vo.in.MessageInVo;
import com.xq.live.vo.out.MessageOut;

import java.util.List;

/**
 * @package: com.xq.live.service
 * @description: 站内信service接口
 * @author: zhangpeng32
 * @date: 2018/3/28 18:47
 * @version: 1.0
 */
public interface MessageService {
    /**
     * 新增
     * @param msg
     * @return
     */
    Long add(Message msg);

    /**
     * 根据id更新记录
     * @param msg
     * @return
     */
    Integer update(Message msg);

    /**
     * 分页查询消息列表
     * @param inVo
     * @return
     */
    Pager<MessageOut> list(MessageInVo inVo);

    /**
     * 查询列表
     * @param inVo
     * @return
     */
    List<MessageOut> myMsgList(MessageInVo inVo);

    /**
     * 查询消息详情
     * @param inVo
     * @return
     */
    MessageOut detail(MessageInVo inVo);

    /**
     * 删除一条站内信
     * @param inVo
     * @return
     */
    Integer delete(MessageInVo inVo);

    /**
     * 新增一条消息
     * @param title 消息标题
     * @param content 站内信内容
     * @param type 类型 1 系统消息 2 公共消息 3 个人消息
     * @param receiverId 接受者id
     * @param senderId 发送者id
     */
    Integer addMessage(String title,String content,Integer type,Long receiverId,Long senderId);

    /**
     *获取活动列表
     * 查询活动时message_text有未删除而message中无对应已读标记的默认为用户的未读消息
     * message中有对应message_text记录的则取message中的状态
     * @param inVo
     * @return
     */
    Pager<MessageOut> listAct(MessageInVo inVo);

    /**
     * 通知一键已读
     * 将该用户下的所有通知消息标记为已读状态
     * @param userId
     * @return
     */
    Integer readAllNotic(Long userId);

    /**
     * 活动已读
     * 实际message中无相关未读记录，直接生成一条和message_text相关联的记录，并标记为已读状态
     * @param inVo
     * @return
     */
    Integer readAct(MessageInVo inVo);

    /**
     * 获取未读消息总数
     * @param inVo
     * @return
     */
    List<MessageOut> unreadMessageTotal(MessageInVo inVo);

    /**
     * 获取formID的
     * @param userId
     * @return
     */
    List<FormIdInVo> getFormIdList(Long userId);

}
