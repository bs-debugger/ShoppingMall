package com.xq.live.service.impl;

import com.xq.live.dao.SoDao;
import com.xq.live.model.So;
import com.xq.live.service.SoService;
import com.xq.live.vo.BaseVo;
import com.xq.live.vo.out.SoOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-22 17:56
 * @copyright:hbxq
 **/
@Service
public class SoServiceImpl implements SoService {

    @Autowired
    private SoDao soDao;

    @Override
    public List<SoOut> queryWithPg(BaseVo inVo) throws Exception {
        Integer rowCount = queryWithCount(inVo);
        inVo.getPager().setRowCount(rowCount);
        return soDao.queryWithPg(inVo);
    }

    @Override
    public int queryWithCount(BaseVo inVo) throws Exception {
        return soDao.queryWithCount(inVo);
    }

    @Override
    public SoOut queryById(Long id) throws Exception {
        return soDao.findById(id);
    }
}
