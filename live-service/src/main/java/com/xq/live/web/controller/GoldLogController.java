package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.User;
import com.xq.live.service.GoldLogService;
import com.xq.live.vo.in.GoldLogInVo;
import com.xq.live.vo.out.BargainLogOut;
import com.xq.live.web.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户金币日志
 * Created by ss on 2018/8/7.
 */
@RestController
@RequestMapping(value = "/gold")
public class GoldLogController {

    @Autowired
    private GoldLogService goldLogService;

    /**
     * 更新用户金币
     * @param inVo
     * @return
     * gold/update?refId=218&groupId=3&parentId=1400
     * 菜id发起人id小组id
     *
     * 注意:该parentId是当前用户userID,要从网关中获取
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BaseResp<Integer> update(GoldLogInVo inVo) {
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        inVo.setParentId(user.getId());
        if (inVo==null||inVo.getGroupId()==null||inVo.getRefId()==null||inVo.getParentId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        int result = goldLogService.changeState(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }

    /**
     * 帮忙砍菜人领金币和砍菜(好友)(砍菜)
     * @param inVo
     * @return
     * localhost:8080/gold/helpfriend?shopId=35&userId=52108&refId=218&parentId=1400&groupId=12
     * 商家id-用户id-菜id-发起人id-小组id
     *
     * 注意:该userId是当前用户userId要从网关中获取
     */
    @RequestMapping(value = "/helpfriend",method = RequestMethod.POST)
    public BaseResp<Map<String,Object>> helpfriend(GoldLogInVo inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Map<String,Object>>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        if(inVo.getUserId().toString().equals(inVo.getParentId().toString())){
            return new BaseResp<Map<String,Object>>(ResultStatus.FAIL);
        }
        Map<String,Object> map = goldLogService.helpfriend(inVo);
        if (map==null){
            return new BaseResp<Map<String,Object>>(ResultStatus.error_gold_select_null);
        }
        return new BaseResp<Map<String,Object>>(ResultStatus.SUCCESS,map);
    }

    /**
     * 帮忙砍菜人领金币和砍菜(好友)(抢购)
     * @param inVo
     * @return
     * localhost:8080/gold/helpfriendQg?shopId=35&userId=52108&refId=218&parentId=1400&groupId=12
     * 商家id-用户id-菜id-发起人id-小组id
     */
    @RequestMapping(value = "/helpfriendQg",method = RequestMethod.POST)
    public BaseResp<Map<String,Object>> helpfriendQg(GoldLogInVo inVo){
        if(inVo.getUserId().toString().equals(inVo.getParentId().toString())){
            return new BaseResp<Map<String,Object>>(ResultStatus.FAIL);
        }
        Map<String,Object> map = goldLogService.helpfriend(inVo);
        if (map==null){
            return new BaseResp<Map<String,Object>>(ResultStatus.error_gold_select_null);
        }
        return new BaseResp<Map<String,Object>>(ResultStatus.SUCCESS,map);
    }


    /**
     * 用户发起领金币(发起人)(砍菜)
     * @param inVo
     * @return
     * gold/initiator?refId=218&userId=1400&shopId=35&amount=24.50&skuMoneyOut=60&skuMoneyMin=35.5
     * 菜id发起人id-skuMoneyOut菜的原价-amount菜的差价
     *
     * 注意:该userId是当前用户userId要从网关中获取
     */
    @RequestMapping(value = "/initiator", method = RequestMethod.POST)
    public BaseResp<BargainLogOut> initiator(GoldLogInVo inVo) throws ParseException {
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<BargainLogOut>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        if (inVo==null||inVo.getRefId()==null||inVo.getUserId()==null||inVo.getShopId()==null||inVo.getAmount()==null||inVo.getSkuMoneyOut()==null||inVo.getSkuMoneyMin()==null){
            return new BaseResp<BargainLogOut>(ResultStatus.error_param_empty);
        }
        BargainLogOut result = goldLogService.initiator(inVo);
        if (result==null){
            return new BaseResp<BargainLogOut>(ResultStatus.error_sku_is_notpay);
        }
        return new BaseResp<BargainLogOut>(ResultStatus.SUCCESS, result);
    }

    /**
     * 用户发起领金币(发起人)(抢购)
     * @param inVo
     * @return
     * gold/createQg?refId=218&userId=1400&shopId=35&amount=24.50&skuMoneyOut=60&skuMoneyMin=35.5
     * 菜id发起人id-skuMoneyOut菜的原价-amount菜的差价
     */
    @RequestMapping(value = "/createQg", method = RequestMethod.POST)
    public BaseResp<BargainLogOut> createQg(GoldLogInVo inVo) {
        if (inVo==null||inVo.getRefId()==null||inVo.getUserId()==null||inVo.getShopId()==null||inVo.getAmount()==null||inVo.getSkuMoneyOut()==null||inVo.getSkuMoneyMin()==null){
            return new BaseResp<BargainLogOut>(ResultStatus.error_param_empty);
        }
        BargainLogOut result = goldLogService.createQg(inVo);
        if (result==null){
            return new BaseResp<BargainLogOut>(ResultStatus.error_sku_is_notpay);
        }
        return new BaseResp<BargainLogOut>(ResultStatus.SUCCESS, result);
    }

    /**
     * 是否帮忙砍过菜或者人数已满(砍菜)
     * @param inVo
     * @return
     * localhost:8080/gold/getshior?refId=218&userId=238&groupId=4&parentId=1400
     *
     * 注意:该userId是当前用户userId要从网关中获取
     */
    @RequestMapping(value = "/getshior", method = RequestMethod.GET)
    public BaseResp<Integer> getshior(GoldLogInVo inVo) {
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        if (inVo==null||inVo.getRefId()==null||inVo.getUserId()==null||inVo.getGroupId()==null||inVo.getParentId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        Integer result = goldLogService.getshior(inVo);
        if (result ==1){
            return new BaseResp<Integer>(ResultStatus.SUCCESS, result);//1可以领取
        }else if (result ==2){
            return new BaseResp<Integer>(ResultStatus.error_sku_hava_get);//2已经砍过菜领过金币了
        }else if(result ==3){
            return new BaseResp<Integer>(ResultStatus.error_sku_people_);//3领金币人数已满
        }else if(result ==4){
            return new BaseResp<Integer>(ResultStatus.error_gold_is_out);//4砍菜时间已失效
        }else {
            return new BaseResp<Integer>(ResultStatus.error_sku_is_ent);//5用户购买成功，缓存删除，砍菜已结束
        }
    }

    /**
     * 是否帮忙砍过菜或者人数已满(抢购)
     * @param inVo
     * @return
     * localhost:8080/gold/getshiorQg?refId=218&userId=238&groupId=4&parentId=1400
     *
     * 注意:该userId是当前用户userId要从网关中获取
     */
    @RequestMapping(value = "/getshiorQg", method = RequestMethod.GET)
    public BaseResp<Integer> getshiorQg(GoldLogInVo inVo) {
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        if (inVo==null||inVo.getRefId()==null||inVo.getUserId()==null||inVo.getGroupId()==null||inVo.getParentId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        Integer result = goldLogService.getshiorQg(inVo);
        if (result ==1){
            return new BaseResp<Integer>(ResultStatus.SUCCESS, result);//1可以领取
        }else if (result ==2){
            return new BaseResp<Integer>(ResultStatus.error_sku_hava_get);//2已经砍过菜领过金币了
        }else if(result ==3){
            return new BaseResp<Integer>(ResultStatus.error_sku_people_);//3领金币人数已满
        }else if(result ==4){
            return new BaseResp<Integer>(ResultStatus.error_gold_is_out);//4砍菜时间已失效
        }else {
            return new BaseResp<Integer>(ResultStatus.error_sku_is_ent);//5用户购买成功，缓存删除，砍菜已结束
        }
    }



}
