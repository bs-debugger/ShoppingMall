package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.config.GoldConfig;
import com.xq.live.model.PullUser;
import com.xq.live.model.User;
import com.xq.live.service.PullUserService;
import com.xq.live.vo.out.PullUserOut;
import com.xq.live.web.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户邀请的接口
 * Created by lipeng on 2018/8/15.
 */
@RestController
@RequestMapping("/pullUser")
public class PullUserController {

    @Autowired
    private GoldConfig goldConfig;

    @Autowired
    private PullUserService pullUserService;

    /**
     * 根据userId查询一条记录
     *
     * 注:此接口也可以判断用户是否有分享赚钱这个权利
     *
     * 注:该userId是当前用户userId要从网关中获取
     *
     * 在注册之前，通过分享图片得到的userId和分享任务页面得到userId，来调用此接口，来判断是否有权利调用更新拉取人数接口
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public BaseResp<PullUser> get() {
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<PullUser>(ResultStatus.error_param_empty);
        }
        PullUser cp = pullUserService.get(user.getId());
        return new BaseResp<PullUser>(ResultStatus.SUCCESS, cp);
    }


    /**
     * 通过userId更新用户拉取的人数
     * @param invo
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BaseResp<Integer> update(PullUser invo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        if(invo==null||invo.getUserId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        Integer result = pullUserService.update(invo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }


    /**
     * 通过userId查询邀请新人人数
     * @param InVo
     * @return
     * pullUser/getForNum
     */
    @RequestMapping(value = "/getForNum", method = RequestMethod.GET)
    public BaseResp<PullUserOut> getForNum(PullUser InVo) {
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<PullUserOut>(ResultStatus.error_param_empty);
        }
        InVo.setUserId(user.getId());
        PullUserOut cp = pullUserService.getForUserList(InVo);
        if (cp==null){
            return new BaseResp<PullUserOut>(ResultStatus.FAIL,null);
        }
        return new BaseResp<PullUserOut>(ResultStatus.SUCCESS, cp);
    }


    /**
     * 邀请人数加一
     * @param record
     * @return
     * localhost:8080/pullUser/updateNumsUp?type=3?UserId=
     */
    @RequestMapping(value = "/updateNumsUp", method = RequestMethod.POST)
    public BaseResp<Integer> updateNumsUp(PullUser record) {
        if(record==null||record.getUserId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        Integer integer = pullUserService.updatePullNumsUp(record);
        if (integer==null||integer<1){
            return new BaseResp<Integer>(ResultStatus.FAIL);
        }
        return new BaseResp<Integer>(ResultStatus.SUCCESS);
    }


    /**
     * 配置邀请
     * @param record
     * @return
     * localhost:8080/pullUser/insertUserPull?type=3
     */
    @RequestMapping(value = "/insertUserPull", method = RequestMethod.POST)
    public BaseResp<Integer> insertUserPull(PullUser record) {
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        record.setUserId(user.getId());
        PullUserOut pullUser=pullUserService.getForUserList(record);
        if (pullUser!=null&&pullUser.getId()!=null){
            return new BaseResp<Integer>(ResultStatus.SUCCESS);
        }
        Integer integer = pullUserService.insertPx(record);
        if (integer==null||integer<1){
            return new BaseResp<Integer>(ResultStatus.FAIL);
        }
        return new BaseResp<Integer>(ResultStatus.SUCCESS, integer);
    }




}
