package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.Zhibo;
import com.xq.live.vo.in.ZhiboInVo;

import java.util.List;

/**
 *  直播service服务定义
 *
 * @author zhangpeng32
 * @date 2018-02-07 18:07
 * @copyright:hbxq
 **/
public interface ZhiboService {
    /**
     * 查一条记录
     * @param id
     * @return
     */
    public Zhibo selectOne(Long id);

    /**
     * 新增
     * @param zhibo
     * @return
     */
    public Long add(Zhibo zhibo);

    /**
     * 更新
     * @param zhibo
     * @return
     */
    public int update(Zhibo zhibo);

    /**
     * 删除一条记录
     * @param id
     * @return
     */
    public int delete(Long id);

    /**
     * 分页查询记录列表
     * @param inVo
     * @return
     */
    public Pager<Zhibo> list(ZhiboInVo inVo);

    /**
     * 查询热门直播
     * @param inVo
     * @return
     */
    public List<Zhibo> top(ZhiboInVo inVo);

}
