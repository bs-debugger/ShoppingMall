package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.ActUser;
import com.xq.live.vo.in.ActUserInVo;
import com.xq.live.vo.out.ActUserOut;

import java.util.List;

/**
 * 选手活动Service
 * Created by lipeng on 2018/4/27.
 */
public interface ActUserService {

    /**
     * 查询选手是否报名
     * @param inVo
     * @return
     */
    ActUserOut findByInVo(ActUserInVo inVo);

    Long add(ActUserInVo inVo);

    /**
     * 分页查询参与商家列表信息(针对的是新活动，带有开始时间和截止时间，可以多次投票)
     * @param inVo
     * @return
     */
    Pager<ActUserOut> listForNewAct(ActUserInVo inVo);

    /**
     * 不分页查询参与此活动的用户
     * @param inVo
     * @return
     */
    List<ActUserOut> listActForId(ActUserInVo inVo);

    //批量更新活动落选名单
    int udateByLuoXuan(List<ActUserOut> userOuts);

    //批量更新活动落选名单
    int udateByLuoXuanTwo(List<ActUserOut> userOuts);
}
