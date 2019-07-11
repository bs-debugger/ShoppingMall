package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.ActShop;
import com.xq.live.model.User;
import com.xq.live.service.ActShopService;
import com.xq.live.vo.in.ActShopInVo;
import com.xq.live.vo.out.ActShopByShopIdOut;
import com.xq.live.vo.out.ActShopOut;
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
 * 商家活动controller
 *
 * @author lipeng
 * @date 2018-03-06 21:09
 * @copyright:hbxq
 **/
@RestController
@RequestMapping("/app/actshop")
public class ActShopForAppController {

    @Autowired
    private ActShopService actShopService;

    /**
     * 分页查询参加活动的商家列表
     *
     *注:voteUserId是当前用户的userId，所以可以从网关中获取
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResp<Pager<ActShopOut>> list(ActShopInVo inVo){
        User user = UserContext.getUserSession();
        inVo.setVoteUserId(user.getId());
        if(inVo.getVoteUserId()==null){
            return new BaseResp<Pager<ActShopOut>>(-1,"voteUserId必填", null);
        }
        if(inVo.getActId()==null){
            return new BaseResp<Pager<ActShopOut>>(-1,"actId必填", null);
        }
        Pager<ActShopOut> result = actShopService.list(inVo);
        return new BaseResp<Pager<ActShopOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 分页查询参加活动的商家列表
     *
     *注:voteUserId是当前用户的userId，所以可以从网关中获取
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/listNewAct", method = RequestMethod.GET)
    public BaseResp<Pager<ActShopOut>> listlistNewAct(ActShopInVo inVo){
        User user = UserContext.getUserSession();
        inVo.setVoteUserId(user.getId());
        if(inVo.getVoteUserId()==null){
            return new BaseResp<Pager<ActShopOut>>(-1,"voteUserId必填", null);
        }
        if(inVo.getActId()==null){
            return new BaseResp<Pager<ActShopOut>>(-1,"actId必填", null);
        }
        if(inVo.getBeginTime()==null||inVo.getEndTime()==null){
            return new BaseResp<Pager<ActShopOut>>(-1,"时间必填",null);
        }
        /*if(inVo.getType()==null){
            return new BaseResp<Pager<ActShopOut>>(-1,"type必填",null);
        }*/
        Pager<ActShopOut> result = actShopService.listForNewAct(inVo);
        return new BaseResp<Pager<ActShopOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 查询参加活动的商家列表
     *
     *注:voteUserId是当前用户的userId，所以可以从网关中获取
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public BaseResp<List<ActShopOut>> top(ActShopInVo inVo){
        User user = UserContext.getUserSession();
        inVo.setVoteUserId(user.getId());
        if(inVo.getVoteUserId()==null){
            return new BaseResp<List<ActShopOut>>(-1,"voteUserId必填", null);
        }
        List<ActShopOut> result = actShopService.top(inVo);
        return new BaseResp<List<ActShopOut>>(ResultStatus.SUCCESS, result);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid ActShop actShop, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        //判断商家是否已经报名了活动
        ActShopInVo inVo = new ActShopInVo();
        inVo.setActId(actShop.getActId());
        inVo.setShopId(actShop.getShopId());
        ActShop as = actShopService.findByInVo(inVo);
        if(as != null){
            return new BaseResp<Long>(ResultStatus.error_act_shop_exist);
        }

        Long id = actShopService.add(actShop);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }



    /**
     * 查询商家参与的活动列表
     *
     *注:voteUserId是当前用户的userId，所以可以从网关中获取
     * @param inVo
     * @return
     */
    @RequestMapping(value = "listForActByShopId",method = RequestMethod.GET)
    public BaseResp<List<ActShopByShopIdOut>> listForActByShopId(ActShopInVo inVo){
        User user = UserContext.getUserSession();
        inVo.setVoteUserId(user.getId());
             if(inVo.getVoteUserId()==null){
                 return new BaseResp<List<ActShopByShopIdOut>>(-1,"voteUserId必填",null);
             }
             if (inVo.getShopId()==null){
                 return new BaseResp<List<ActShopByShopIdOut>>(-1,"shopId必填",null);
             }
        List<ActShopByShopIdOut> result = actShopService.listForActByShopId(inVo);
        return new BaseResp<List<ActShopByShopIdOut>>(ResultStatus.SUCCESS,result);
    }

    /**
     * 查询商家是否参与用活动券的活动
     * @param shopId
     * @return
     */
    @RequestMapping(value = "/searchForShopId",method =RequestMethod.GET)
    public BaseResp<Integer> searchForShopId(Long shopId){
        Integer integer = actShopService.searchForShopId(shopId);
        if(integer<1){
            return new BaseResp<Integer>(-1,"商家没有参与该活动",integer);
        }
        return new BaseResp<Integer>(ResultStatus.SUCCESS,integer);
    }

    /**
     * 查询商家是否参与用活动券的活动(适用于所有的活动券)
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/searchForShopIdNew",method =RequestMethod.GET)
    public BaseResp<Integer> searchForShopIdNew(ActShopInVo inVo){
        Integer integer = actShopService.searchForShopIdNew(inVo);
        if(integer<1){
            return new BaseResp<Integer>(-1,"商家没有参与该活动",integer);
        }
        return new BaseResp<Integer>(ResultStatus.SUCCESS,integer);
    }
}
