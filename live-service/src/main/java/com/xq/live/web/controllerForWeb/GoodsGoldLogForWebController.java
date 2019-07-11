package com.xq.live.web.controllerForWeb;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.User;
import com.xq.live.service.GoodsGoldLogService;
import com.xq.live.vo.in.GoodsGoldLogInVo;
import com.xq.live.vo.out.GoodsBargainLogOut;
import com.xq.live.web.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Map;

/**
 * Created by ss on 2018/11/3.
 */
@RestController
@RequestMapping("/website/goodsGold")
public class GoodsGoldLogForWebController {

    @Autowired
    private GoodsGoldLogService goodsGoldLogService;

    /**
     * 是否帮忙砍过价或者人数已满
     * @param inVo
     * @return
     * localhost:8080/goodsGold/getshior?refId=218&userId=238&groupId=4&parentId=1400&actId
     *
     * 注意:该userId是当前用户userId要从网关中获取
     */
    @RequestMapping(value = "/getshior", method = RequestMethod.GET)
    public BaseResp<Integer> getshior(GoodsGoldLogInVo inVo) {
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        if (user.getId().toString().equals(inVo.getParentId().toString())){
            return new BaseResp<Integer>(ResultStatus.FAIL);
        }
        inVo.setUserId(user.getId());
        if (inVo==null||inVo.getRefId()==null||inVo.getUserId()==null||inVo.getParentId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        Integer result = goodsGoldLogService.getShior(inVo);
        if (result ==null){
            return new BaseResp<Integer>(ResultStatus.error_sku_is_ent);//5用户购买成功，缓存删除，砍菜已结束
        }else if (result ==1){
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
     * 用户发起领金币(发起人)
     * @param inVo
     * @return
     * goodsGold/initiator?refId=218&shopId=35&actId&
     * 菜id发起人id-skuMoneyOut菜的原价-amount菜的差价
     *
     * 注意:该userId是当前用户userId要从网关中获取
     */
    @RequestMapping(value = "/initiator", method = RequestMethod.POST)
    public BaseResp<GoodsBargainLogOut> initiator(GoodsGoldLogInVo inVo) throws ParseException {
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<GoodsBargainLogOut>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        if (inVo==null||inVo.getRefId()==null||inVo.getUserId()==null||inVo.getShopId()==null){
            return new BaseResp<GoodsBargainLogOut>(ResultStatus.error_param_empty);
        }
        GoodsBargainLogOut result = goodsGoldLogService.initiator(inVo);
        if (result==null){
            return new BaseResp<GoodsBargainLogOut>(ResultStatus.error_sku_is_notpay);
        }
        return new BaseResp<GoodsBargainLogOut>(ResultStatus.SUCCESS, result);
    }

    /**
     * 帮忙砍菜人领金币和砍菜(好友)
     * @param inVo
     * @return
     * localhost:8080/goodsGold/helpfriend?shopId=35&refId=218&parentId=1400&groupId=12&actId
     * 商家id-用户id-菜id-发起人id-小组id
     *
     * 注意:该userId是当前用户userId要从网关中获取
     */
    @RequestMapping(value = "/helpfriend",method = RequestMethod.POST)
    public BaseResp<Map<String,Object>> helpfriend(GoodsGoldLogInVo inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Map<String,Object>>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        Map<String,Object> map = goodsGoldLogService.helpFriend(inVo);
        if (map==null){
            return new BaseResp<Map<String,Object>>(ResultStatus.error_gold_select_null);
        }
        return new BaseResp<Map<String,Object>>(ResultStatus.SUCCESS,map);
    }

    /**
     * 更新用户金币
     * @param inVo
     * @return
     * goodsGold/update?refId=218&groupId=3&actId
     * 菜id发起人id小组id
     *
     * 注意:该parentId是当前用户userID,要从网关中获取
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BaseResp<Integer> update(GoodsGoldLogInVo inVo) {
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        inVo.setParentId(user.getId());
        if (inVo==null||inVo.getGroupId()==null||inVo.getRefId()==null||inVo.getParentId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        int result = goodsGoldLogService.changeState(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }

}
