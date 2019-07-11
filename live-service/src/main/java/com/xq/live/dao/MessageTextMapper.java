package com.xq.live.dao;

import com.xq.live.model.MessageText;
import com.xq.live.vo.in.MessageInVo;
import com.xq.live.vo.out.MessageOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageTextMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MessageText record);

    int insertSelective(MessageText record);

    MessageText selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MessageText record);

    int updateByPrimaryKey(MessageText record);

    List<MessageText> list(MessageInVo inVo);

    int listTotal(MessageInVo inVo);
}