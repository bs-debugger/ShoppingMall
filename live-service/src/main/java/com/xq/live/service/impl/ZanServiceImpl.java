package com.xq.live.service.impl;

import com.xq.live.dao.ZanMapper;
import com.xq.live.model.Zan;
import com.xq.live.service.ZanService;
import com.xq.live.vo.in.ZanInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-08 19:17
 * @copyright:hbxq
 **/
@Service
public class ZanServiceImpl implements ZanService {

    @Autowired
    private ZanMapper zanMapper;

    @Override
    public Long add(Zan zan) {
        int r = zanMapper.insert(zan);
        if (r < 1) {
            return null;
        }

        return zan.getId();
    }

    @Override
    public int deleteByZan(Zan zan) {
        return zanMapper.deleteZan(zan);
    }

    @Override
    public int delete(Long id) {
        return zanMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int total(ZanInVo inVo) {
        return zanMapper.total(inVo);
    }
}
