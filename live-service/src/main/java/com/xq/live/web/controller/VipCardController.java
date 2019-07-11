package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.User;
import com.xq.live.service.VipCardService;
import com.xq.live.vo.in.VipCardInVo;
import com.xq.live.vo.out.VipCardOut;
import com.xq.live.web.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by admin on 2019/5/20.
 */
@Api(tags = "会员信息-VipCardController")
@RestController
@RequestMapping(value = "/vipCard")
public class VipCardController {

    @Autowired
    private VipCardService vipCardService;

    @ApiOperation(value = "新增会员")
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<VipCardOut> addUser(VipCardInVo vipCardInVo, HttpServletRequest request){
        if(vipCardInVo==null||vipCardInVo.getVipTypeId()==null){
            return new BaseResp<VipCardOut>(ResultStatus.error_param_empty);
        }
        User user = UserContext.getUserSession();
        vipCardInVo.setUserId(user.getId());
        VipCardOut re= vipCardService.add(vipCardInVo);
        return new BaseResp<VipCardOut>(ResultStatus.SUCCESS, re);
    }

    @ApiOperation(value = "查询用户会员信息")
    @ResponseBody
    @RequestMapping(value = "/getByUserAndCardType", method = RequestMethod.POST)
    public BaseResp<VipCardOut> getByUserAndCardType(VipCardInVo vipCardInVo, HttpServletRequest request){
        if(vipCardInVo==null||vipCardInVo.getVipTypeId()==null){
            return new BaseResp<VipCardOut>(ResultStatus.error_param_empty);
        }
        User user = UserContext.getUserSession();
        vipCardInVo.setUserId(user.getId());
        VipCardOut re= vipCardService.getByUserAndCardType(vipCardInVo);
        return new BaseResp<VipCardOut>(ResultStatus.SUCCESS, re);
    }

}
