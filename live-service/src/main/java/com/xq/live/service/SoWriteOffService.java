package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.SoWriteOff;
import com.xq.live.vo.in.SoInVo;
import com.xq.live.vo.in.SoWriteOffInVo;
import com.xq.live.vo.out.SoOut;
import com.xq.live.vo.out.SoWriteOffOut;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-21 18:32
 * @copyright:hbxq
 **/
public interface SoWriteOffService {
    /***
     * 分页查询
     * @param inVo
     * @return
     */
    Pager<SoWriteOffOut> list(SoWriteOffInVo inVo);

    /**
     * 根据商家ID查询指定年份内的所有账单记录
     * @param inVo
     * @return
     */
    List<SoWriteOffOut> listAmount(SoWriteOffInVo inVo);

    /**
     * 排序查询记录列表
     * @param inVo
     * @return
     */
    List<SoWriteOffOut> top(SoWriteOffInVo inVo);

    /**
     * 查询一段时间内核销的券类别
     * @param inVo
     * @return
     */
    List<SoWriteOffOut> sotype(SoWriteOffInVo inVo);

    /**
     * 核销
     * @param soWriteOff
     * @return
     */
    Long add(SoWriteOff soWriteOff);

    /**
     * 查询一条记录
     * @param id
     * @return
     */
    SoWriteOff get(Long id);

    /**
     * 输入参数校验
     * @param soWriteOff
     * @return
     */
    int validInput(SoWriteOff soWriteOff);


    /**
     * 判断是否能够领取折扣券
     * @param inVo
     * @return
     */
    Integer canGetAgio(SoInVo inVo);

    /**
     * 查询一段时间内商家核销票数
     * @param inVo
     * @return
     */
    int listTotal(SoWriteOffInVo inVo);


    /**
     * 批量修改符合条件订单的结算状态(适用版，通过shopid和时间段批量修改)
     * @param soWriteOffInVo
     * @return
     */
    int updateByShopId(SoWriteOffInVo soWriteOffInVo);
}
