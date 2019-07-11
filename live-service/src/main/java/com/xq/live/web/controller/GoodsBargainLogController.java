package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.User;
import com.xq.live.service.GoodsBargainLogService;
import com.xq.live.vo.in.GoodsBargainLogInVo;
import com.xq.live.vo.out.GoodsBargainLogOut;
import com.xq.live.web.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by ss on 2018/11/3.
 * 商城砍价拼团
 */
@RestController
@RequestMapping("/goodsBar")
public class GoodsBargainLogController {

    @Autowired
    private GoodsBargainLogService goodsBargainLogService;

    /**
     * 查询砍菜人列表和缓存的信息
     * @param inVo
     * @return
     * localhost:8080/goodsBar/skuGroup?shopId=35&skuId=218&parentId=1400&groupId=12&actId
     */
    @RequestMapping(value = "/skuGroup",method = RequestMethod.GET)
    public BaseResp<List<GoodsBargainLogOut>> skuGroupList(GoodsBargainLogInVo inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<List<GoodsBargainLogOut>>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        if (inVo==null||inVo.getSkuId()==null||inVo.getParentId()==null||inVo.getShopId()==null||inVo.getGroupId()==null){
            return new BaseResp<List<GoodsBargainLogOut>>(ResultStatus.error_param_empty);
        }
        List<GoodsBargainLogOut> list=goodsBargainLogService.skuGrouplist(inVo);
        if (list==null){
            return new BaseResp<List<GoodsBargainLogOut>>(ResultStatus.error_gold_select_null);//没有查询到信息
        }
        return new BaseResp<List<GoodsBargainLogOut>>(ResultStatus.SUCCESS,list);
    }


    /**
     * 查询本人砍菜缓存信息
     * @param inVo
     * @return
     * localhost:8080/goodsBar/userRedis?actId
     *
     * 注意:该userId是当前用户userId要从网关中获取
     */
    @RequestMapping(value = "/userRedis",method = RequestMethod.GET)
    public BaseResp<List<GoodsBargainLogOut>> userGroupRedis(GoodsBargainLogInVo inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<List<GoodsBargainLogOut>>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        List<GoodsBargainLogOut> list=goodsBargainLogService.skuGroupRedis(inVo);
        if (list==null){
            return new BaseResp<List<GoodsBargainLogOut>>(ResultStatus.error_gold_select_null);//没有查询到信息
        }
        return new BaseResp<List<GoodsBargainLogOut>>(ResultStatus.SUCCESS,list);
    }

    /**
     * 查询本人砍菜对当个菜的缓存信息
     * @param inVo
     * @return
     * localhost:8080/goodsBar/skuRedis?skuId=&actId
     *
     * 注意:该userId是当前用户userId要从网关中获取
     */
    @RequestMapping(value = "/skuRedis",method = RequestMethod.GET)
    public BaseResp<List<GoodsBargainLogOut>> skuGroupRedis(GoodsBargainLogInVo inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<List<GoodsBargainLogOut>>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        if (inVo==null||inVo.getUserId()==null||inVo.getSkuId()==null){
            return new BaseResp<List<GoodsBargainLogOut>>(ResultStatus.error_param_empty);
        }
        List<GoodsBargainLogOut> list=goodsBargainLogService.skuGroupRedis(inVo);
        if (list==null){
            return new BaseResp<List<GoodsBargainLogOut>>(ResultStatus.error_gold_select_null);//没有查询到信息
        }
        return new BaseResp<List<GoodsBargainLogOut>>(ResultStatus.SUCCESS,list);
    }


}
