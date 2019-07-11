package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.UserOpinionMapper;
import com.xq.live.model.UserOpinion;
import com.xq.live.service.UserOpinionService;
import com.xq.live.vo.in.UserOpinionInVo;
import com.xq.live.vo.out.UserOpinionOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ss on 2018/10/24.
 */
@Service
public class UserOpinionServiceImpl implements UserOpinionService {
    @Autowired
    private UserOpinionMapper userOpinionMapper;


    /**
     * 查看
     * @param id
     * @return
     */
    @Override
    public UserOpinion selectByPrimaryKey(Long id) {
        UserOpinion userOpinion=userOpinionMapper.selectByPrimaryKey(id);
        return userOpinion;
    }

    /**
     * 新增
     * @param inVo
     * @return
     */
    @Override
    public Long add(UserOpinionInVo inVo) {
        Integer integer=userOpinionMapper.insertForOpinion(inVo);
        if (integer>0){
            return inVo.getId();
        }
        return null;
    }

    /**
     * 删除
     * @param inVo
     * @return
     */
    @Override
    public Integer delete(UserOpinionInVo inVo) {
        Integer integer= userOpinionMapper.updateByUser(inVo);
        return integer;
    }

    /**
     * 查看
     * @param inVo
     * @return
     */
    @Override
    public Pager<UserOpinionOut> listForUser(UserOpinionInVo inVo) {
        Pager<UserOpinionOut> result =  new Pager<UserOpinionOut>();
        int total = userOpinionMapper.listTotal(inVo);
        if(total > 0){
            List<UserOpinionOut> list = userOpinionMapper.selectByUserId(inVo);
            result.setList(list);
        }
        result.setTotal(total);
        result.setPage(inVo.getPage());
        return result;
    }
}
