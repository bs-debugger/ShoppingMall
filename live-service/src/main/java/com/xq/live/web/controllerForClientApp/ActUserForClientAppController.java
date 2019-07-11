package com.xq.live.web.controllerForClientApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.User;
import com.xq.live.service.ActUserService;
import com.xq.live.vo.in.ActUserInVo;
import com.xq.live.vo.out.ActUserOut;
import com.xq.live.web.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 选手活动接口
 * Created by lipeng on 2018/4/27.
 */
@RestController
@RequestMapping(value = "clientApp/actUser")
public class ActUserForClientAppController {


    @Autowired
    private ActUserService actUserService;

    /**
     * 选手报名接口
     * 注:这里的userId是当前用户的id,要从网关中取
     * @param inVo
     * @param result
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid ActUserInVo inVo, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        //判断选手是否已经报名了活动
        ActUserOut byInVo = actUserService.findByInVo(inVo);
        if(byInVo != null){
            return new BaseResp<Long>(ResultStatus.error_act_user_exist,byInVo.getId());
        }

        Long id = actUserService.add(inVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 分页查询参与商家列表信息(针对的是新活动，带有开始时间和截止时间，可以多次投票)
     * 注:这里的voteUserId是当前用户的userId，要从网关中取
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/listNewAct", method = RequestMethod.GET)
    public BaseResp<Pager<ActUserOut>> listlistNewAct(ActUserInVo inVo){
        User user = UserContext.getUserSession();
        inVo.setVoteUserId(user.getId());
        if(inVo.getVoteUserId()==null){
            return new BaseResp<Pager<ActUserOut>>(-1,"voteUserId必填", null);
        }
        if(inVo.getActId()==null){
            return new BaseResp<Pager<ActUserOut>>(-1,"actId必填", null);
        }
        if(inVo.getBeginTime()==null||inVo.getEndTime()==null){
            return new BaseResp<Pager<ActUserOut>>(-1,"时间必填",null);
        }
        /*if(inVo.getType()==null){
            return new BaseResp<Pager<ActUserOut>>(-1,"type必填",null);
        }*/
        Pager<ActUserOut> result = actUserService.listForNewAct(inVo);
        return new BaseResp<Pager<ActUserOut>>(ResultStatus.SUCCESS, result);
    }


    /**
     * 查询选手详情
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/findByInVo",method = RequestMethod.GET)
    public BaseResp<ActUserOut> findByInVo(ActUserInVo inVo){
        if(inVo.getVoteUserId()==null){
            return new BaseResp<ActUserOut>(-1,"voteUserId必填", null);
        }
        if(inVo.getActId()==null){
            return new BaseResp<ActUserOut>(-1,"actId必填", null);
        }
        if(inVo.getBeginTime()==null||inVo.getEndTime()==null){
            return new BaseResp<ActUserOut>(-1,"时间必填",null);
        }
        if(inVo.getUserId()==null){
            return new BaseResp<ActUserOut>(-1,"userId必填",null);
        }
        ActUserOut byInVo = actUserService.findByInVo(inVo);
        return new BaseResp<ActUserOut>(ResultStatus.SUCCESS,byInVo);
    }
}
