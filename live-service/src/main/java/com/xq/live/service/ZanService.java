package com.xq.live.service;

import com.xq.live.model.Zan;
import com.xq.live.vo.in.ZanInVo;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-08 19:16
 * @copyright:hbxq
 **/
public interface ZanService {
    /**
     * 新增
     * @param zan
     * @return
     */
    public Long add(Zan zan);

    /**
     * 取消赞
     * @param zan
     * @return
     */
    public int deleteByZan(Zan zan);

    /**
     * 取消赞
     * @param id
     * @return
     */
    public int delete(Long id);

    /**
     * 根据refid和type查询点赞总数
     * @param inVo
     * @return
     */
    public int total(ZanInVo inVo);

}
