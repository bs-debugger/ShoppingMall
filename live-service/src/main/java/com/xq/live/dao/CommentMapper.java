package com.xq.live.dao;

import com.xq.live.model.Comment;
import com.xq.live.vo.in.CommentInVo;
import com.xq.live.vo.out.CommentOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    /**
     * 删除评论
     * @param inVo
     * @return
     */
    int deleteComment(CommentInVo inVo);

    /**
     * 查询列表
     * @param inVo
     * @return
     */
    List<CommentOut> list(CommentInVo inVo);

    /**
     * 记录总数
     * @param inVo
     * @return
     */
    int listTotal(CommentInVo inVo);
}