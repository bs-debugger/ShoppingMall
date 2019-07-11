package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.CommentMapper;
import com.xq.live.dao.UserMapper;
import com.xq.live.dao.ZanMapper;
import com.xq.live.model.Comment;
import com.xq.live.model.User;
import com.xq.live.service.CommentService;
import com.xq.live.vo.in.CommentInVo;
import com.xq.live.vo.in.ZanInVo;
import com.xq.live.vo.out.CommentOut;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-08 19:56
 * @copyright:hbxq
 **/
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ZanMapper zanMapper;

    @Override
    public Long add(Comment comment) {
        int res = commentMapper.insert(comment);
        if(res < 1){
            return null;
        }
        return comment.getId();
    }

    @Override
    public Integer update(Comment comment) {
        return null;
    }

    @Override
    public Integer deleteByInVo(CommentInVo inVo) {
        return commentMapper.deleteComment(inVo);
    }

    @Override
    public Integer delete(Long id) {
        return commentMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer get(Long id) {
        return null;
    }

    @Override
    public Pager<CommentOut> list(CommentInVo inVo) {
        Pager<CommentOut> result = new Pager<CommentOut>();
        int total = commentMapper.listTotal(inVo);
        if(total > 0){
            List<CommentOut> list = commentMapper.list(inVo);
            List<CommentOut> listForOut = new ArrayList<CommentOut>();
            for (CommentOut comment : list) {
                User user = userMapper.selectByPrimaryKey(comment.getUserId());
                int total1ForZan;
                if(comment.getId()!=null&&comment.getCmtType()!=null){
                    ZanInVo zanInVo = new ZanInVo();
                    zanInVo.setRefId(comment.getId());
                    zanInVo.setType(4);
                    total1ForZan = zanMapper.total(zanInVo);
                }else{
                    total1ForZan = 0;
                }
                CommentOut commentOut = new CommentOut();
                BeanUtils.copyProperties(comment,commentOut);
                if(user!=null){
                    commentOut.setUserPic(user.getIconUrl());
                }
                commentOut.setZan(total1ForZan);
                listForOut.add(commentOut);
            }
            Collections.sort(listForOut);
            result.setList(listForOut);
        }
        result.setTotal(total);
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public List<CommentOut> top(CommentInVo inVo) {
        return commentMapper.list(inVo);
    }
}
