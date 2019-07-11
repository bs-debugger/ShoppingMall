package com.xq.live.service.impl;/**
 * 直播实现类
 *
 * @author zhangpeng32
 * @date 2018-02-07 18:12
 * @copyright:hbxq
 **/

import com.xq.live.common.Pager;
import com.xq.live.dao.ZhiboMapper;
import com.xq.live.model.Zhibo;
import com.xq.live.service.ZhiboService;
import com.xq.live.vo.in.ZhiboInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *直播实现类
 *@author zhangpeng32
 *@create 2018-02-07 18:12
 **/
@Service
public class ZhiboServiceImpl implements ZhiboService {

    @Autowired
    private ZhiboMapper zhiboMapper;

    @Override
    public Zhibo selectOne(Long id) {
        return zhiboMapper.selectByPrimaryKey(id);
    }

    @Override
    public Long add(Zhibo zhibo) {
        return zhiboMapper.insert(zhibo);
    }

    @Override
    public int update(Zhibo zhibo) {
        return zhiboMapper.updateByPrimaryKey(zhibo);
    }

    @Override
    public int delete(Long id) {
        return zhiboMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Pager<Zhibo> list(ZhiboInVo inVo) {
        Pager<Zhibo> result = new Pager<Zhibo>();
        int listTotal = zhiboMapper.listTotal(inVo);
        if(listTotal > 0){
            List<Zhibo> list = zhiboMapper.list(inVo);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        result.setTotal(listTotal);
        return result;
    }

    @Override
    public List<Zhibo> top(ZhiboInVo inVo) {
        return zhiboMapper.list(inVo);
    }
}
