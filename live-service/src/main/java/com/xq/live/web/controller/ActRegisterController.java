package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.ActRegister;
import com.xq.live.model.User;
import com.xq.live.service.ActRegisterService;
import com.xq.live.vo.out.ActRegisterOut;
import com.xq.live.web.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 签到记录相关接口
 */
@Api(tags = "签到记录相关服务-ActRegisterController")
@RestController
@RequestMapping("/actRegister")
public class ActRegisterController {
    @Autowired
    private ActRegisterService actRegisterService;

    /**
     * 查询签到详情(主要查累计签到数目和今天是否已签到)
     * actId
     * 注意:这里面的userId是从网关中获取
     * @param actRegister
     * @return
     */
    @ApiOperation(value = "查询签到详情",notes = "主要查累计签到数目和今天是否已签到")
    @RequestMapping(value = "/findDetail",method = RequestMethod.GET)
    public BaseResp<ActRegisterOut> findDetail(ActRegister actRegister){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<ActRegisterOut>(ResultStatus.error_param_empty);
        }
        actRegister.setUserId(user.getId());
        ActRegisterOut registerDetailByUserId = actRegisterService.findRegisterDetailByUserId(actRegister);
        return new BaseResp<ActRegisterOut>(ResultStatus.SUCCESS,registerDetailByUserId);
    }

    /**
     * 签到
     *
     * 注意:这里面的userId是从网关中获取
     * @param actRegister
     * @return
     */
    @ApiOperation(value = "签到",notes = "每日签到接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "actId", value = "活动id",defaultValue = "46", required = true, dataType = "Long")
    })
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public BaseResp<Long> add(ActRegister actRegister){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        actRegister.setUserId(user.getId());
        Long re = actRegisterService.add(actRegister);
        if (re==null){
            return new BaseResp<Long>(ResultStatus.FAIL);
        }
        return new BaseResp<Long>(ResultStatus.SUCCESS,re);
    }

}
