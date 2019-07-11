package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.User;
import com.xq.live.service.BargainLogService;
import com.xq.live.vo.in.BargainLogInVo;
import com.xq.live.vo.out.BargainLogOut;
import com.xq.live.web.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户砍菜记录
 * Created by ss on 2018/8/13.
 */
@RestController
@RequestMapping(value = "/bargain")
public class BargainLogController {

    @Autowired
    private BargainLogService bargainLogService;

    /**
     * 查询砍菜人列表和缓存的信息
     * @param inVo
     * @return
     * localhost:8080/bargain/skuGroup?shopId=35&skuId=218&parentId=1400&groupId=12
     */
    @RequestMapping(value = "/skuGroup",method = RequestMethod.GET)
    public BaseResp<List<BargainLogOut>> skuGroupList(BargainLogInVo inVo){
        if (inVo==null||inVo.getSkuId()==null||inVo.getParentId()==null||inVo.getShopId()==null||inVo.getGroupId()==null){
            return new BaseResp<List<BargainLogOut>>(ResultStatus.error_param_empty);
        }
        List<BargainLogOut> list=bargainLogService.skuGrouplist(inVo);
        if (list==null){
            return new BaseResp<List<BargainLogOut>>(ResultStatus.error_gold_select_null);//没有查询到信息
        }
        return new BaseResp<List<BargainLogOut>>(ResultStatus.SUCCESS,list);
    }


    /**
     * 查询本人砍菜缓存信息
     * @param inVo
     * @return
     * localhost:8080/bargain/userRedis?userId=
     *
     * 注意:该userId是当前用户userId要从网关中获取
     */
    @RequestMapping(value = "/userRedis",method = RequestMethod.GET)
    public BaseResp<List<BargainLogOut>> userGroupRedis(BargainLogInVo inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<List<BargainLogOut>>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        if (inVo==null||inVo.getUserId()==null){
            return new BaseResp<List<BargainLogOut>>(ResultStatus.error_param_empty);
        }
        List<BargainLogOut> list=bargainLogService.skuGroupRedis(inVo);
        if (list==null){
            return new BaseResp<List<BargainLogOut>>(ResultStatus.error_gold_select_null);//没有查询到信息
        }
        return new BaseResp<List<BargainLogOut>>(ResultStatus.SUCCESS,list);
    }

    /**
     * 查询本人砍菜对当个菜的缓存信息
     * @param inVo
     * @return
     * localhost:8080/bargain/skuRedis?userId=&skuId=
     *
     * 注意:该userId是当前用户userId要从网关中获取
     */
    @RequestMapping(value = "/skuRedis",method = RequestMethod.GET)
    public BaseResp<List<BargainLogOut>> skuGroupRedis(BargainLogInVo inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<List<BargainLogOut>>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        if (inVo==null||inVo.getUserId()==null||inVo.getSkuId()==null){
            return new BaseResp<List<BargainLogOut>>(ResultStatus.error_param_empty);
        }
        List<BargainLogOut> list=bargainLogService.skuGroupRedis(inVo);
        if (list==null){
            return new BaseResp<List<BargainLogOut>>(ResultStatus.error_gold_select_null);//没有查询到信息
        }
        return new BaseResp<List<BargainLogOut>>(ResultStatus.SUCCESS,list);
    }
}
