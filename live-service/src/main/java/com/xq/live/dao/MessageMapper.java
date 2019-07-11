package com.xq.live.dao;

import com.xq.live.model.Message;
import com.xq.live.vo.in.MessageInVo;
import com.xq.live.vo.out.MessageOut;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MessageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Message record);

    int insertSelective(Message record);

    Message selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKey(Message record);

    List<MessageOut> list(MessageInVo inVo);

    int listTotal(MessageInVo inVo);

    MessageOut detail(MessageInVo inVo);

    Message selectByRecId(Map<String, Object> paramsMap);

    /**
     * 获取活动列表
     * 查询活动时message_text有未删除而message中无对应已读标记的默认为用户的未读消息
     * message中有对应message_text记录的则取message中的状态
     * @param inVo
     * @return
     */
    List<MessageOut> listAct(MessageInVo inVo);

    int listActTotal(MessageInVo inVo);

    /**
     * 通知一键已读
     * 将该用户下的所有通知消息标记为已读状态
     * @param userId
     * @return
     */
    int readAllNotic(Long userId);

    /**
     * 活动已读
     * 实际message中无相关未读记录，直接生成一条和message_text相关联的记录，并标记为已读状态
     * @param inVo
     * @return
     */
    int readAct(MessageInVo inVo);

    /**
     * 获取用户未读消息总数
     * @param inVo
     * @return
     */
    int unreadMessageTotal(MessageInVo inVo);

}
