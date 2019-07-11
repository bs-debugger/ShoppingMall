package com.xq.live.service;

import com.xq.live.common.BaseResp;
import com.xq.live.model.SmsInfo;

import java.util.List;

public interface SmsInfoService {

    /**
     * 根据内容查找短信模板内容
     * @param smsType
     * @return
     */
    BaseResp<List<SmsInfo>> findSmsTemplateByType(Integer smsType);
}
