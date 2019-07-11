package com.xq.live.service.impl;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.dao.SmsInfoMapper;
import com.xq.live.model.SmsInfo;
import com.xq.live.service.SmsInfoService;
import com.xq.live.vo.out.ShopAccountOut;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SmsInfoServiceImpl implements SmsInfoService {

    @Resource
    SmsInfoMapper smsInfoMapper;

    @Override
    public BaseResp<List<SmsInfo>> findSmsTemplateByType(Integer smsType) {
        return new BaseResp<List<SmsInfo>>(ResultStatus.SUCCESS, smsInfoMapper.findSmsTemplateByType(smsType));
    }
}
