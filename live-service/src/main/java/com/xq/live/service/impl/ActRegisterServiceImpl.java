package com.xq.live.service.impl;

import com.xq.live.dao.ActInfoMapper;
import com.xq.live.dao.ActRegisterMapper;
import com.xq.live.model.ActInfo;
import com.xq.live.model.ActRegister;
import com.xq.live.service.ActLotteryService;
import com.xq.live.service.ActRegisterService;
import com.xq.live.vo.in.ActLotteryInVo;
import com.xq.live.vo.out.ActRegisterOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 签到记录相关serviceImpl
 */
@Service
public class ActRegisterServiceImpl implements ActRegisterService {
    @Autowired
    private ActRegisterMapper actRegisterMapper;

    @Autowired
    private ActLotteryService actLotteryService;

    @Autowired
    private ActInfoMapper actInfoMapper;

    @Override
    public ActRegisterOut findRegisterDetailByUserId(ActRegister actRegister) {
        ActRegisterOut registerDetailByUserId = actRegisterMapper.findRegisterDetailByUserId(actRegister);
        if(registerDetailByUserId!=null) {
            ActInfo actInfo = actInfoMapper.selectByPrimaryKey(actRegister.getActId());
            registerDetailByUserId.setActInfo(actInfo);
        }
        return registerDetailByUserId;
    }

    @Override
    public Long add(ActRegister actRegister) {
        ActRegisterOut registerDetailByUserId = actRegisterMapper.findRegisterDetailByUserId(actRegister);
        if(registerDetailByUserId==null||registerDetailByUserId.getIsRegister()==0) {
            int insert = actRegisterMapper.insert(actRegister);
            if(insert<1){
                return null;
            }
            ActLotteryInVo actLotteryInVo=new ActLotteryInVo();
            actLotteryInVo.setUserId(actRegister.getUserId());
            actLotteryInVo.setActId(actRegister.getActId());
            actLotteryInVo.setTotalNumber(1);
            actLotteryService.add(actLotteryInVo);
            return actRegister.getId();
        }
        return null;
    }
}
