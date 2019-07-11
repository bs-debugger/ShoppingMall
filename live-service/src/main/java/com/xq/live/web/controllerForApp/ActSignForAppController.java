package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.exception.ActSignFailException;
import com.xq.live.model.ActShop;
import com.xq.live.model.ActSign;
import com.xq.live.service.ActShopService;
import com.xq.live.service.ActSignService;
import com.xq.live.service.ActUserService;
import com.xq.live.vo.in.ActShopInVo;
import com.xq.live.vo.in.ActSignInVo;
import com.xq.live.vo.in.ActUserInVo;
import com.xq.live.vo.out.ActUserOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 新活动报名材料接口
 * Created by lipeng on 2018/4/28.
 */
@RestController
@RequestMapping(value = "app/actSign")
public class ActSignForAppController {
    @Autowired
    private ActSignService actSignService;

    @Autowired
    private ActShopService actShopService;

    @Autowired
    private ActUserService actUserService;

    /**
     * 选手报名接口
     * @param inVo
     * @param result
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid ActSignInVo inVo, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        //判断是否已经报名了活动递交材料
        ActSign sign = actSignService.isSign(inVo);
        if(sign != null){
            return new BaseResp<Long>(ResultStatus.error_act_sign_exist,sign.getId());
        }

        Long id = null;
        try {
            id = actSignService.add(inVo);
        } catch (ActSignFailException e) {
            return new BaseResp<Long>(ResultStatus.error_act_sign_fail);
        }
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 判断是否报名
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/isSign",method = RequestMethod.GET)
    public BaseResp<Long> isSign(ActSignInVo inVo){
        //判断是否已经报名了活动递交材料
        ActSign sign = actSignService.isSign(inVo);
        if(sign != null){
            return new BaseResp<Long>(ResultStatus.error_act_sign_exist,sign.getId());
        }

        ActShopInVo inVoForShop = new ActShopInVo();
        ActUserInVo inVoForUser = new ActUserInVo();

        if(inVo!=null&&inVo.getType()==ActSign.ACT_SIGN_TYPE_PLAYER) {
            inVoForUser.setUserId(inVo.getRefId());
            inVoForUser.setActId(inVo.getActId());
            //判断选手是否已经报名了活动
            ActUserOut byInVo = actUserService.findByInVo(inVoForUser);
            if (byInVo != null) {
                return new BaseResp<Long>(ResultStatus.error_act_user_exist);
            }
        }else if(inVo!=null&&inVo.getType()==ActSign.ACT_SIGN_TYPE_SHOP){
            inVoForShop.setShopId(inVo.getRefId());
            inVoForShop.setActId(inVo.getActId());
            //判断商家是否已经报名了活动
            ActShop as = actShopService.findByInVo(inVoForShop);
            if (as != null) {
                return new BaseResp<Long>(ResultStatus.error_act_shop_exist);
            }
        }

        return new BaseResp<Long>(ResultStatus.SUCCESS);
    }
}
