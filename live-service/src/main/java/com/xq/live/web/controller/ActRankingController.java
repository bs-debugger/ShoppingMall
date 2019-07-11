package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.service.ActRankingService;
import com.xq.live.vo.in.ActRankingInVo;
import com.xq.live.vo.out.ActRankingOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by ss on 2019/3/1.
 * 查询获奖商品列表
 */
@RestController
@RequestMapping(value = "/actRanking")
public class ActRankingController {

    @Autowired
    private ActRankingService actRankingService;


    /**
     * 查询获奖商品列表
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResp<List<ActRankingOut>> list(ActRankingInVo inVo){
        List<ActRankingOut> list = actRankingService.selectActInfoList(inVo);
        if (list!=null){
            return new BaseResp<List<ActRankingOut>>(ResultStatus.SUCCESS,list);
        }
        return new BaseResp<List<ActRankingOut>>(ResultStatus.FAIL);
    }
}
