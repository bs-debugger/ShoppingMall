package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.vo.in.ActSkuInVo;
import com.xq.live.vo.out.ActSkuOut;

import java.util.List;

/**
 * 推荐菜活动Service
 * Created by lipeng on 2018/6/13.
 */
public interface ActSkuService {
    /**
     * 分页查询参与推荐菜列表信息(针对的是新活动，带有开始时间和截止时间，可以多次投票)
     * @param inVo
     * @return
     */
    Pager<ActSkuOut> listForNewAct(ActSkuInVo inVo);

    /**
     * 查询单个详情页的活动推荐菜信息
     * @param inVo
     * @return
     */
    ActSkuOut findByInVo(ActSkuInVo inVo);

    /**
     * 根据id来修改信息
     * @param inVo
     * @return
     */
    Integer update(ActSkuInVo inVo);

    /**
     * 新增一个活动推荐菜信息
     * @param record
     * @return
     */
    Long insert(ActSkuInVo record);

    /**
     * 批量修改活动推荐菜信息（落选）
     * @param record
     * @return
     */
    int updateLuoXuan(List<ActSkuOut> record);

    /**
     * 按票数查询活动推荐菜信息
     * @param record
     * @return
     */
    List<ActSkuOut> listActSkuOut(ActSkuInVo record);
}
