package com.xq.live.web.controllerForWeb;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.User;
import com.xq.live.service.ActTopicService;
import com.xq.live.vo.in.ActTopicInVo;
import com.xq.live.vo.out.ActTopicOut;
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
 * 主题活动接口
 * Created by lipeng on 2018/6/29.
 */
@RestController
@RequestMapping(value = "/website/actTopic")
public class ActTopicForWebController {

    @Autowired
    private ActTopicService actTopicService;

    /**
     * 主题报名接口
     *
     * 注:此处的userId是当前用户的userId，从网关中取
     * @param inVo
     * @param result
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid ActTopicInVo inVo, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        Long id = actTopicService.add(inVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 分页查询参与商家列表信息(针对的是新活动，带有开始时间和截止时间，可以多次投票)
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/listNewAct", method = RequestMethod.GET)
    public BaseResp<Pager<ActTopicOut>> listlistNewAct(ActTopicInVo inVo){
        if(inVo.getActId()==null){
            return new BaseResp<Pager<ActTopicOut>>(-1,"actId必填", null);
        }
        //为了分享，这个地方注释掉，但是还是必须要填
        /*if(inVo.getZanUserId()==null){
            return new BaseResp<Pager<ActTopicOut>>(-1,"zanUserId必填",null);
        }*/
        Pager<ActTopicOut> result = actTopicService.listForNewAct(inVo);
        return new BaseResp<Pager<ActTopicOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 分页查询参与商家列表信息(针对的是新活动，带有开始时间和截止时间，可以多次投票)
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/zanAndHitTotal", method = RequestMethod.GET)
    public BaseResp<ActTopicOut> zanAndHitTotal(ActTopicInVo inVo){
        if(inVo.getActId()==null){
            return new BaseResp<ActTopicOut>(-1,"actId必填", null);
        }
        //为了分享，这个地方注释掉，但是还是必须要填
        /*if(inVo.getZanUserId()==null){
            return new BaseResp<Pager<ActTopicOut>>(-1,"zanUserId必填",null);
        }*/
        ActTopicOut result = actTopicService.zanAndHitTotal(inVo);
        return new BaseResp<ActTopicOut>(ResultStatus.SUCCESS, result);
    }
}
