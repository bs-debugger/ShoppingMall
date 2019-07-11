package com.xq.live.dao;


import com.xq.live.model.SmsInfo;

import java.util.List;

public interface SmsInfoMapper {
    int deleteByPrimaryKey(Long smsId);

    int insert(SmsInfo record);

    int insertSelective(SmsInfo record);

    SmsInfo selectByPrimaryKey(Long smsId);

    int updateByPrimaryKeySelective(SmsInfo record);

    int updateByPrimaryKey(SmsInfo record);

    /**
     * 根据内容查找短信信息
     * @param smsType
     * @return
     */
    List<SmsInfo> findSmsTemplateByType(Integer smsType);
}