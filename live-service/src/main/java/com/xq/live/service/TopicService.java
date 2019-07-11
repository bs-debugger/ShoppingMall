package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.Topic;
import com.xq.live.vo.in.TopicInVo;
import com.xq.live.vo.out.TopicForZanOut;
import com.xq.live.vo.out.TopicOut;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-08 13:58
 * @copyright:hbxq
 **/
public interface TopicService {
    /**
     * 查一条记录
     * @param id
     * @return
     */
    public TopicForZanOut selectOne(Long id);

    public TopicForZanOut selectByZan(TopicInVo inVo);

    /**
     * 新增
     * @param zhibo
     * @return
     */
    public Long add(Topic zhibo);

    /**
     * 更新
     * @param zhibo
     * @return
     */
    public int update(Topic zhibo);

    /**
     * 逻辑删除一条记录
     * @param id
     * @return
     */
    public int delete(Long id);

    /**
     * 分页查询列表
     * @param inVo
     * @return
     */
    Pager<TopicForZanOut> list(TopicInVo inVo);

    /**
     * 不分页查询列表
     * @param inVo
     * @return
     */
    List<TopicForZanOut> listHome(TopicInVo inVo);

    Pager<TopicForZanOut> myList(TopicInVo inVo);

    /**
     * 查询最热
     * @param inVo
     * @return
     */
    List<TopicForZanOut> top(TopicInVo inVo);
}
