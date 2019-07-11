package com.xq.live.service;/**
 * 评论service
 *
 * @author zhangpeng32
 * @create 2018-02-05 18:55
 */

import com.xq.live.common.Pager;
import com.xq.live.model.Comment;
import com.xq.live.vo.in.CommentInVo;
import com.xq.live.vo.out.CommentOut;

import java.util.List;

/**
 * 评论service
 * @author zhangpeng32
 * @create 2018-02-05 18:55
 **/
public interface CommentService {
    /**
     * 新增
     * @param comment
     * @return
     */
    Long add(Comment comment);

    /**
     * 更新
     * @param comment
     * @return
     */
    Integer update(Comment comment);

    /**
     * 删除
     * @param id
     * @return
     */
    Integer delete(Long id);

    /**
     * 根据条件删除
     * @param inVo
     * @return
     */
    Integer deleteByInVo(CommentInVo inVo);

    /**
     * 查询一条记录
     * @param id
     * @return
     */
    Integer get(Long id);

    /**
     * 分页查询列表
     * @param inVo
     * @return
     */
    Pager<CommentOut> list(CommentInVo inVo);

    /**
     * 热门评论
     * @param inVo
     * @return
     */
    List<CommentOut> top(CommentInVo inVo);
}
