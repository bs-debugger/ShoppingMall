package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.User;
import com.xq.live.service.UserInvitationService;
import com.xq.live.vo.in.UserInvitationInVO;
import com.xq.live.web.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by admin on 2019/4/10.
 */
@RestController
@RequestMapping(value = "/userInvitation")
public class UserInvitationController {

    @Autowired
    private UserInvitationService userInvitationService;

    /**
     * 增加抽奖次数
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public BaseResp<Integer> add(UserInvitationInVO inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null||inVo==null||inVo.getUserId()==null||inVo.getActId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        inVo.setInviterId(inVo.getUserId());//前端传的userId实际是邀请人的id，此处数据错误需要交换
        inVo.setUserId(user.getId());
        inVo.setUserName(user.getUserName());
        Integer result=userInvitationService.add(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS,result);
    }

}
