package com.xq.live.dao;

import com.xq.live.model.SmsSend;
import com.xq.live.vo.in.SmsSendInVo;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsSendMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SmsSend record);

    SmsSend selectByPrimaryKey(Long id);

    SmsSend selectByMobile(SmsSendInVo inVo);

    int updateByPrimaryKeySelective(SmsSend record);

    int updateByPrimaryKey(SmsSend record);

    int create(SmsSendInVo inVo);
}
