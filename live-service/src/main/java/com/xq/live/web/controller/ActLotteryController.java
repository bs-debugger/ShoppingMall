package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.ActLottery;
import com.xq.live.model.User;
import com.xq.live.service.ActLotteryService;
import com.xq.live.vo.in.ActLotteryInVo;
import com.xq.live.web.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by admin on 2019/1/8.
 */
@RestController
@RequestMapping(value = "/actLottery")
public class ActLotteryController {

    @Autowired
    private ActLotteryService actLotteryService;

    /**
     * 抽奖次数查询
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public BaseResp<ActLottery> get(ActLotteryInVo inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<ActLottery>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        ActLottery result= actLotteryService.selectUserLottery(inVo);
        return new BaseResp<ActLottery>(ResultStatus.SUCCESS,result);
    }

    /**
     * 增加抽奖次数
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public BaseResp<Integer> add(ActLotteryInVo inVo){
        inVo.setUserId(inVo.getUserId());
        inVo.setActId(new Long(46));
        inVo.setTotalNumber(1);
        //Integer result= actLotteryService.add(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS,0);
    }
}
