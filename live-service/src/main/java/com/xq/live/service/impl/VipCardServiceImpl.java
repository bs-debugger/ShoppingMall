package com.xq.live.service.impl;

import com.xq.live.common.RandomStringUtil;
import com.xq.live.dao.UserMapper;
import com.xq.live.dao.VipCardMapper;
import com.xq.live.dao.VipGradeConfigMapper;
import com.xq.live.dao.VipTypeMapper;
import com.xq.live.model.User;
import com.xq.live.model.VipCard;
import com.xq.live.model.VipGradeConfig;
import com.xq.live.service.VipCardService;
import com.xq.live.vo.in.VipCardInVo;
import com.xq.live.vo.out.VipCardOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Created by admin on 2019/5/16.
 */
@Service
public class VipCardServiceImpl implements VipCardService {
    @Autowired
    private VipCardMapper vipCardMapper;

    @Autowired
    private VipTypeMapper vipTypeMapper;

    @Autowired
    private  UserMapper userMapper;

    @Autowired
    private VipGradeConfigMapper vipGradeConfigMapper;

    @Override
    public VipCardOut getByUserAndCardType(VipCardInVo vipCardInVo) {
        return vipCardMapper.getByUserAndCardType(vipCardInVo);
    }

    @Override
    @Transactional
    public VipCardOut add(VipCardInVo vipCardInVo) {
        VipCardOut vipCardOut=vipCardMapper.getByUserAndCardType(vipCardInVo);
        if(vipCardOut!=null){
            return vipCardOut;
        }

        Integer grade=1;//新建会员等级为1
        User user= userMapper.selectByPrimaryKey(vipCardInVo.getUserId());
        VipGradeConfig vipGradeConfig=new VipGradeConfig();
        vipGradeConfig.setVipTypeId(vipCardInVo.getVipTypeId());
        vipGradeConfig.setGrade(grade);
        vipGradeConfig=vipGradeConfigMapper.selectByTypeAndGrade(vipGradeConfig);
        String cardNo= RandomStringUtil.getRandomCode(12, 0);

        VipCard vipCard=new VipCard();
        vipCard.setUserId(user.getId());
        vipCard.setUserName(user.getUserName());
        vipCard.setVipTypeId(vipCardInVo.getVipTypeId());
        vipCard.setStatus(VipCard.VIP_STATUS_NORMAL);
        vipCard.setGrade(grade);
        vipCard.setGradeName(vipGradeConfig.getGradeName());
        vipCard.setDiscount(vipGradeConfig.getDiscount());
        vipCard.setAccountAmount(BigDecimal.ZERO);
        vipCard.setAvailablePoints(0);
        vipCard.setCardNo(cardNo);
        vipCard.setShowNo(cardNo);
        vipCard.setTotalPoints(0);
        if(null!=vipCardInVo.getTimeLimitType()&&null!=vipCardInVo.getExpiryDate()&&vipCardInVo.getTimeLimitType()==VipCard.VIP_TIMELIMITTYPE_LIMIT){
            vipCard.setTimeLimitType(vipCardInVo.getTimeLimitType());
            vipCard.setExpiryDate(vipCardInVo.getExpiryDate());
        }else{
            vipCard.setTimeLimitType(VipCard.VIP_TIMELIMITTYPE_PERMANENT);
        }
        vipCardMapper.insert(vipCard);
        VipCardOut vipCardOut1=vipCardMapper.getDetail(vipCard.getId());
        return vipCardOut1;
    }
}
