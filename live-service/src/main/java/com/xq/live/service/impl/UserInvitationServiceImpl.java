package com.xq.live.service.impl;

import com.xq.live.dao.UserInvitationMapper;
import com.xq.live.dao.UserMapper;
import com.xq.live.model.User;
import com.xq.live.model.UserInvitation;
import com.xq.live.service.ActLotteryService;
import com.xq.live.service.UserInvitationService;
import com.xq.live.vo.in.ActLotteryInVo;
import com.xq.live.vo.in.UserInvitationInVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by admin on 2019/4/10.
 */
@Service
public class UserInvitationServiceImpl implements UserInvitationService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserInvitationMapper userInvitationMapper;

    @Autowired
    private ActLotteryService actLotteryService;

    @Override
    public Integer add(UserInvitationInVO inVo) {
        User user= userMapper.selectByPrimaryKey(inVo.getUserId());
        inVo.setUserId(user.getId());
        inVo.setUserName(user.getUserName());

        Integer result=0;

        UserInvitationInVO usserInVO= new UserInvitationInVO();
        usserInVO.setUserId(inVo.getUserId());
        List<UserInvitation> userInvitationList=userInvitationMapper.list(usserInVO);
        if(userInvitationList!=null&&userInvitationList.size()>0){//该用戶已被其他用户邀请
            return result;
        }

        List<UserInvitation> userInvitations =userInvitationMapper.listTodayInvitation(inVo);
        if(userInvitations!=null&&userInvitations.size()>4){//邀请者当日已邀请过用户时，只建立邀请关系，不增加抽奖次数
            result=this.insert(inVo);
        }else{//邀请者当日首次邀请用户时，建立邀请关系，增加抽奖次数
            result=this.insert(inVo);
            ActLotteryInVo actLotteryInVo=new ActLotteryInVo();
            actLotteryInVo.setUserId(inVo.getInviterId());
            actLotteryInVo.setActId(inVo.getActId());
            actLotteryInVo.setTotalNumber(1);
            Integer re= actLotteryService.add(actLotteryInVo);
        }

        return result;
    }

    private Integer insert(UserInvitationInVO invitationInVO){
        UserInvitation userInvitation=new UserInvitation();
        BeanUtils.copyProperties(invitationInVO,userInvitation);
        Integer result= userInvitationMapper.insert(userInvitation);
        return  result;
    }

}
